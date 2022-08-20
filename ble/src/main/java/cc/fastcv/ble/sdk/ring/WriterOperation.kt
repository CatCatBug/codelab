package cc.fastcv.ble.sdk.ring

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGatt
import android.util.Log

@SuppressLint("MissingPermission")
class WriterOperation {
    var context = ByteArray(256)

    //	private ReadWriteActivity rw;
    private fun cmd_write_op(opcode: Int, length: Int, addr: Int, datalenth: Int): ByteArray {
        val cmd: ByteArray
        cmd = if (opcode == OTA_CMD_PAGE_ERASE) {
            ByteArray(7)
        } else {
            ByteArray(9)
        }
        cmd[0] = (opcode and 0xff).toByte()
        cmd[1] = (length and 0xff).toByte()
        cmd[2] = (length and 0xff shr 8).toByte()
        cmd[3] = (addr and 0xff).toByte()
        cmd[4] = (addr and 0xff00 shr 8).toByte()
        cmd[5] = (addr and 0xff0000 shr 16).toByte()
        cmd[6] = (addr and -0x1000000 shr 24).toByte()
        if (opcode != OTA_CMD_PAGE_ERASE) {
            cmd[7] = (datalenth and 0xff).toByte()
            cmd[8] = (datalenth and 0xff00 shr 8).toByte()
        }
        return cmd
    }

    fun cmd_operation(type: Int, lenth: Int, addr: Int): ByteArray? {
        var cmd: ByteArray? = null
        if (type == OTA_CMD_WRITE_MEM || type == OTA_CMD_WRITE_DATA) {
            cmd = cmd_write_op(type, 9, addr, lenth)
        } else if (type == OTA_CMD_GET_STR_BASE || type == OTA_CMD_NVDS_TYPE) {
            cmd = cmd_write_op(type, 3, 0, 0)
        } else if (type == OTA_CMD_PAGE_ERASE) {
            cmd = cmd_write_op(type, 7, addr, 0)
        }
        return cmd
    }

    fun send_data_long(
        type: Int,
        addr: Int,
        buffer: ByteArray?,
        length: Long,
        mgattCharacteristic: BluetoothGattCharacteristic,
        gatt: BluetoothGatt?
    ): Boolean {
        val cmd = ByteArray(11)
        var result_cmd: ByteArray? = null
        if (type == OTA_CMD_REBOOT) {
//			cmd[0] = (byte) (type&0xff);
//			result_cmd = cmd;
            cmd[0] = (type and 0xff).toByte()
            cmd[1] = 0xa
            cmd[2] = 0x00
            cmd[3] = (length and 0xff).toByte()
            cmd[4] = (length and 0xff00 shr 8).toByte()
            cmd[5] = (length and 0xff0000 shr 16).toByte()
            cmd[6] = (length and -0x1000000 shr 24).toByte()
            cmd[7] = (addr and 0xff).toByte()
            cmd[8] = (addr and 0xff00 shr 8).toByte()
            cmd[9] = (addr and 0xff0000 shr 16).toByte()
            cmd[10] = (addr and -0x1000000 shr 24).toByte()
            result_cmd = cmd
        }
        mgattCharacteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
        mgattCharacteristic.value = result_cmd
        return gatt?.writeCharacteristic(mgattCharacteristic)?:false
    }


    fun send_data(
        type: Int,
        addr: Int,
        buffer: ByteArray?,
        length: Int,
        mgattCharacteristic: BluetoothGattCharacteristic,
        gatt: BluetoothGatt?
    ): Boolean {
        var isWriteSuccess = false
        var cmd_write: ByteArray? = null
        var result_cmd: ByteArray? = null
        val cmd = ByteArray(1)
        if (type == OTA_CMD_NULL && mgattCharacteristic != null) {
            val writeData = ByteArray(length)
            System.arraycopy(buffer, 0, writeData, 0, length)
            //无需远程设备响应即可写入特性
            mgattCharacteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            mgattCharacteristic.value = writeData
            return gatt?.writeCharacteristic(mgattCharacteristic)?:false
        }
        cmd_write = cmd_operation(type, length, addr)
        if (type == OTA_CMD_GET_STR_BASE || type == OTA_CMD_PAGE_ERASE || type == OTA_CMD_NVDS_TYPE) {
            result_cmd = cmd_write
        } else if (type == OTA_CMD_REBOOT) {
            cmd[0] = (type and 0xff).toByte()
            result_cmd = cmd
        } else {
            if (cmd_write != null && buffer != null) {
                result_cmd = byteMerger(cmd_write, buffer)
            }
        }
        if (result_cmd != null) {
            mgattCharacteristic!!.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            mgattCharacteristic.value = result_cmd
        }
        if (mgattCharacteristic != null) {
            isWriteSuccess = gatt?.writeCharacteristic(mgattCharacteristic)?:false
        }
        Log.i(TAG, "WriterOperation send_data isWriteSuccess: $isWriteSuccess")
        return isWriteSuccess
    }

    fun bytetoint(data: ByteArray): Int {
        var addr: Int
        addr = data[4].toInt() and 0x000000ff
        addr = addr or (data[5].toInt() and 0x0000ff shl 8)
        addr = addr or (data[6].toInt() and 0x000000ff shl 16)
        addr = addr or (data[7].toInt() and 0x000000ff shl 24)
        return addr
    }

    fun bytetochar(data: ByteArray): Int {
        val value: Int
        value = data[4].toInt() and 0x000000ff
        return value
    }

    companion object {
        private const val TAG = "WriterOperation"
        private const val OTA_CMD_NVDS_TYPE = 0
        private const val OTA_CMD_GET_STR_BASE = 1
        private const val OTA_CMD_PAGE_ERASE = 3
        private const val OTA_CMD_CHIP_ERASE = 4
        private const val OTA_CMD_WRITE_DATA = 5
        private const val OTA_CMD_READ_DATA = 6
        private const val OTA_CMD_WRITE_MEM = 7
        private const val OTA_CMD_READ_MEM = 8
        private const val OTA_CMD_REBOOT = 9
        private const val OTA_CMD_NULL = 10
        fun byteMerger(byte_1: ByteArray, byte_2: ByteArray): ByteArray {
            val byte_3 = ByteArray(byte_1.size + byte_2.size)
            System.arraycopy(byte_1, 0, byte_3, 0, byte_1.size)
            System.arraycopy(byte_2, 0, byte_3, byte_1.size, byte_2.size)
            return byte_3
        }
    }
}