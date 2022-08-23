package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.OTAUpgradeListener
import cc.fastcv.ble.sdk.log.Logger
import kotlinx.coroutines.*
import java.io.*

/**
 * 针对特定芯片的蓝牙低功耗设备
 */
class RingOTAUpgradeTool {

    companion object {
        /**
         * 指令
         */
        private const val OTA_CMD_NVDS_TYPE = 0
        private const val OTA_CMD_GET_STR_BASE = 1
        private const val OTA_CMD_PAGE_ERASE = 3
        private const val OTA_CMD_WRITE_DATA = 5
        private const val OTA_CMD_WRITE_MEM = 7
        private const val OTA_CMD_REBOOT = 9

        /**
         * 设备类型
         */
        private const val DEVICE_8010: Int = 0
        private const val DEVICE_8010H = 1

        /**
         * 默认地址值
         */
        private const val SECOND_ADDRESS: Int = 0x14000
        private const val FIRST_ADDRESS = 0

        private const val OTA_STATE_INIT = 0
        private const val OTA_STATE_SUCCESS = 1
        private const val OTA_STATE_FAIL = 2
    }

    /**
     * CRC 字节余式表
     */
    private val crcTa8 = intArrayOf(
        0x00000000, 0x77073096, -0x11f19ed4, -0x66f6ae46,
        0x076dc419, 0x706af48f, -0x169c5acb, -0x619b6a5d, 0x0edb8832,
        0x79dcb8a4, -0x1f2a16e2, -0x682d2678, 0x09b64c2b, 0x7eb17cbd,
        -0x1847d2f9, -0x6f40e26f, 0x1db71064, 0x6ab020f2, -0xc468eb8,
        -0x7b41be22, 0x1adad47d, 0x6ddde4eb, -0xb2b4aaf, -0x7c2c7a39,
        0x136c9856, 0x646ba8c0, -0x29d0686, -0x759a3614, 0x14015c4f,
        0x63066cd9, -0x5f0c29d, -0x72f7f20b, 0x3b6e20c8, 0x4c69105e,
        -0x2a9fbe1c, -0x5d988e8e, 0x3c03e4d1, 0x4b04d447, -0x2df27a03,
        -0x5af54a95, 0x35b5a8fa, 0x42b2986c, -0x2444362a, -0x534306c0,
        0x32d86ce3, 0x45df5c75, -0x2329f231, -0x542ec2a7, 0x26d930ac,
        0x51de003a, -0x3728ae80, -0x402f9eea, 0x21b4f4b5, 0x56b3c423,
        -0x30456a67, -0x47425af1, 0x2802b89e, 0x5f058808, -0x39f3264e,
        -0x4ef416dc, 0x2f6f7c87, 0x58684c11, -0x3e9ee255, -0x4999d2c3,
        0x76dc4190, 0x01db7106, -0x672ddf44, -0x102aefd6, 0x71b18589,
        0x06b6b51f, -0x60401b5b, -0x17472bcd, 0x7807c9a2, 0x0f00f934,
        -0x69f65772, -0x1ef167e8, 0x7f6a0dbb, 0x086d3d2d, -0x6e9b9369,
        -0x199ca3ff, 0x6b6b51f4, 0x1c6c6162, -0x7a9acf28, -0xd9dffb2,
        0x6c0695ed, 0x1b01a57b, -0x7df70b3f, -0xaf03ba9, 0x65b0d9c6,
        0x12b7e950, -0x74414716, -0x3467784, 0x62dd1ddf, 0x15da2d49,
        -0x732c830d, -0x42bb39b, 0x4db26158, 0x3ab551ce, -0x5c43ff8c,
        -0x2b44cf1e, 0x4adfa541, 0x3dd895d7, -0x5b2e3b93, -0x2c290b05,
        0x4369e96a, 0x346ed9fc, -0x529877ba, -0x259f4730, 0x44042d73,
        0x33031de5, -0x55f5b3a1, -0x22f28337, 0x5005713c, 0x270241aa,
        -0x41f4eff0, -0x36f3df7a, 0x5768b525, 0x206f85b3, -0x46992bf7,
        -0x319e1b61, 0x5edef90e, 0x29d9c998, -0x4f2f67de, -0x3828574c,
        0x59b33d17, 0x2eb40d81, -0x4842a3c5, -0x3f459353, -0x12477ce0,
        -0x65404c4a, 0x03b6e20c, 0x74b1d29a, -0x152ab8c7, -0x622d8851,
        0x04db2615, 0x73dc1683, -0x1c9cf4ee, -0x6b9bc47c, 0x0d6d6a3e,
        0x7a6a5aa8, -0x1bf130f5, -0x6cf60063, 0x0a00ae27, 0x7d079eb1,
        -0xff06cbc, -0x78f75c2e, 0x1e01f268, 0x6906c2fe, -0x89da8a3,
        -0x7f9a9835, 0x196c3671, 0x6e6b06e7, -0x12be48a, -0x762cd420,
        0x10da7a5a, 0x67dd4acc, -0x6462091, -0x71411007, 0x17b7be43,
        0x60b08ed5, -0x29295c18, -0x5e2e6c82, 0x38d8c2c4, 0x4fdff252,
        -0x2e44980f, -0x5943a899, 0x3fb506dd, 0x48b2364b, -0x27f2d426,
        -0x50f5e4b4, 0x36034af6, 0x41047a60, -0x209f103d, -0x579820ab,
        0x316e8eef, 0x4669be79, -0x349e4c74, -0x43997ce6, 0x256fd2a0,
        0x5268e236, -0x33f3886b, -0x44f4b8fd, 0x220216b9, 0x5505262f,
        -0x3a45c442, -0x4d42f4d8, 0x2bb45a92, 0x5cb36a04, -0x3d280059,
        -0x4a2f30cf, 0x2cd99e8b, 0x5bdeae1d, -0x649b3d50, -0x139c0dda,
        0x756aa39c, 0x026d930a, -0x63f6f957, -0x14f1c9c1, 0x72076785,
        0x05005713, -0x6a40b57e, -0x1d4785ec, 0x7bb12bae, 0x0cb61b38,
        -0x6d2d7165, -0x1a2a41f3, 0x7cdcefb7, 0x0bdbdf21, -0x792c2d2c,
        -0xe2b1dbe, 0x68ddb3f8, 0x1fda836e, -0x7e41e933, -0x946d9a5,
        0x6fb077e1, 0x18b74777, -0x77f7a51a, -0xf09590, 0x66063bca,
        0x11010b5c, -0x709a6101, -0x79d5197, 0x616bffd3, 0x166ccf45,
        -0x5ff51d88, -0x28f22d12, 0x4e048354, 0x3903b3c2, -0x5898d99f,
        -0x2f9fe909, 0x4969474d, 0x3e6e77db, -0x512e95b6, -0x2629a524,
        0x40df0b66, 0x37d83bf0, -0x564351ad, -0x2144613b, 0x47b2cf7f,
        0x30b5ffe9, -0x42420de4, -0x35453d76, 0x53b39330, 0x24b4a3a6,
        -0x452fc9fb, -0x3228f96d, 0x54de5729, 0x23d967bf, -0x4c9985d2,
        -0x3b9eb548, 0x5d681b02, 0x2a6f2b94, -0x4bf441c9, -0x3cf3715f,
        0x5a05df1b, 0x2d02ef8d
    )

    /**
     * OTA升级当前状态
     */
    private var otaState = OTA_STATE_INIT

    /**
     * 用来判断mtu是否设置成功
     */
    private var mtuChange = false

    /**
     * 固件bin文件
     */
    private lateinit var binFile: File

    /**
     * 从蓝牙设备接收到的数据值 这个值用来判断当前指令是否操作成功
     */
    private var isReceiveData = false

    /**
     * 戒指OTA升级接口
     */
    private lateinit var ringOTAProtocol: IRingOTAProtocol

    /**
     * 用于在使用之前判断是否初始化
     */
    private var isInit = false

    /**
     * 设备支持的mtu的值
     */
    private var mtuSize = 247

    /**
     * 发出指令后设备返回的值
     */
    private var receiveValue: ByteArray? = null

    /**
     * 指令写入状态
     */
    private var writeStatus: Boolean = false

    /**
     * 蓝牙断开连接的标识
     */
    private var bleDisConnect = false

    /**
     * OTA升级监听接口
     */
    private var listener: OTAUpgradeListener? = null

    /**
     * 初始化
     */
    fun init(file: File, ringOTAProtocol: IRingOTAProtocol, listener: OTAUpgradeListener) {
        Logger.log("始化OTA工具  file = ${file.absolutePath}")
        binFile = file

        this.ringOTAProtocol = ringOTAProtocol
        ringOTAProtocol.setBleOTANotify()
        this.listener = listener
        isInit = true
    }

    /**
     * 开始更新
     */
    fun startUpgrade() {
        Logger.log("开始OTA升级")
        otaState = OTA_STATE_INIT
        //开始OTA升级操作 携程中进行
        runBlocking {
            bleDisConnect = false
            upgradeInner()
        }
    }

    /**
     * 设置接收值
     */
    fun setReceiveValue(data: ByteArray?) {
        if (isInit) {
            receiveValue = data
            isReceiveData = true
        }
    }

    /**
     * 修改Mtu值
     */
    fun setMtuChangeSateAndValue(mtuChangeState: Boolean, value: Int) {
        if (isInit) {
            mtuChange = mtuChangeState
            mtuSize = value
        }
    }

    /**
     * 修改写入状态
     */
    fun setWriteStatus(status: Int) {
        if (isInit) {
            if (status == 0) {
                writeStatus = true
            }
        }
    }

    /**
     * 正式开始更新
     */
    private suspend fun upgradeInner() {
        //CRC检验文件
        val fileCRCCode = try {
            getCRC32Code(binFile)
        } catch (e: Exception) {
            0
        }
        //通知设备准备升级
        notifyDeviceToStartPreparingUpgrade()
        //检查蓝牙连接状态 如果断开连接了 则中断任务
        if (interruptTaskIfNeeded()) return
        //根据返回值得到设备类型（处理器类型）
        val deviceType = getDeviceProcessorType()
        //检查蓝牙连接状态 如果断开连接了 则中断任务
        if (interruptTaskIfNeeded()) return
        //根据设备类型请求蓝牙设备设置Mtu值 并 获取实际的Mtu值
        val packageSize = requestMtu(deviceType)
        delay(2000)
        //检查蓝牙连接状态 如果断开连接了 则中断任务
        if (interruptTaskIfNeeded()) return
        //获取address 操作的起始地址(设备内存地址)
        val address = getStartAddress(deviceType)
        //检查蓝牙连接状态 如果断开连接了 则中断任务
        if (interruptTaskIfNeeded()) return
        if (address == -1) {
            return
        }
        //擦除数据
        pageErase(address, binFile.length())
        //检查蓝牙连接状态 如果断开连接了 则中断任务
        if (interruptTaskIfNeeded()) return
        //写入数据
        writeToDevice(binFile, packageSize, address)
        //检查蓝牙连接状态 如果断开连接了 则中断任务
        if (interruptTaskIfNeeded()) return
        //通知设备重启
        notifyDeviceRoot(fileCRCCode, binFile)
    }

    /**
     * 检查蓝牙连接状态 如果断开连接了 则中断任务
     */
    private suspend fun interruptTaskIfNeeded() : Boolean {
         if (bleDisConnect) {
             withContext(Dispatchers.Main) {
                 if (listener != null) {
                     listener!!.upgradeFail()
                     listener = null
                 }
             }
             listener = null
         }
        return bleDisConnect
    }

    /**
     * 通知设备重启
     */
    private suspend fun notifyDeviceRoot(fileCRCCode: Int, file: File) {
        Logger.log("通知设备重启---")
        isInit = false
        if (otaState == OTA_STATE_FAIL) return

        val length = file.length()
        ringOTAProtocol.writeLongCommand(
            OTA_CMD_REBOOT,
            fileCRCCode,
            null,
            length
        )
        withContext(Dispatchers.Main) {
            if (listener != null) {
                listener!!.upgradeSuccessful()
                listener = null
            }
        }
        otaState = OTA_STATE_SUCCESS
        listener = null
    }

    /**
     * 写入数据到设备
     */
    private suspend fun writeToDevice(file: File, packageSize: Int, startAddress: Int) {
        Logger.log("写入数据到设备---")
        try {
            var lastReadCount = 0
            var sendDataCount = 0
            var address = startAddress
            var readCount: Int
            val fileIs = FileInputStream(file)
            val input = BufferedInputStream(fileIs)
            val inputBuffer = ByteArray(packageSize)
            var writePercent = 0
            val length = file.length()
            while (input.read(inputBuffer, 0, packageSize).also { readCount = it } != -1) {
                while (!ringOTAProtocol.writeIntCommand(
                        OTA_CMD_WRITE_DATA,
                        address,
                        inputBuffer,
                        readCount
                    )
                ) {
                    delay(50)
                    if (bleDisConnect) {
                        break
                    }
                }
                var delayNumber = 0
                while (!writeStatus) {
                    delayNumber++
                    if (delayNumber % 8000 == 0) {
                        ringOTAProtocol.writeIntCommand(
                            OTA_CMD_WRITE_DATA,
                            address,
                            inputBuffer,
                            readCount
                        )
                    }
                    if (bleDisConnect) {
                        break
                    }
                }
                writeStatus = false
                address += readCount
                lastReadCount = readCount
                sendDataCount += readCount
                if (writePercent != (sendDataCount.toFloat() / length * 100).toInt()) {
                    writePercent = (sendDataCount.toFloat() / length * 100).toInt()
                    withContext(Dispatchers.Main) {
                        if (writePercent == 1) {
                            listener?.upgradeStart()
                        }
                        listener?.upgradeProgress(writePercent)
                    }
                }
                while (!isReceiveData) {
                    delay(100)
                    if (bleDisConnect) {
                        break
                    }
                }
                if (bleDisConnect) {
                    break
                }
                //重置接收值状态
                isReceiveData = false
            }
            while (byteToInt(receiveValue) != (address - lastReadCount)) {
                delay(100)
                if (bleDisConnect) {
                    break
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                listener?.upgradeFail()
                listener = null
            }
            otaState = OTA_STATE_FAIL
            listener = null
        }
    }

    /**
     * 擦除数据
     */
    private suspend fun pageErase(startAddress: Int, length: Long) {
        Logger.log("擦除数据---")
        var address = startAddress
        //重置接收值状态
        isReceiveData = false
        //计算需要擦除的页的数量
        var count: Long = length / 0x1000
        if (length % 0x1000 != 0L) {
            count++
        }
        //循环擦除页数据
        for (i in 0 until count) {
            while (!ringOTAProtocol.writeIntCommand(OTA_CMD_PAGE_ERASE, address, null, 0)) {
                delay(50)
                if (bleDisConnect) {
                    break
                }
            }
            var delayNumber = 0
            while (!writeStatus) {
                delayNumber++
                if (delayNumber % 8000 == 0) {
                    ringOTAProtocol.writeIntCommand(
                        OTA_CMD_PAGE_ERASE,
                        address,
                        null,
                        0
                    )
                }
                if (bleDisConnect) {
                    break
                }
            }
            while (!isReceiveData) {
                delay(100)
                if (bleDisConnect) {
                    break
                }
            }
            //重置接收值状态
            isReceiveData = false
            address += 0x1000
        }
    }

    /**
     * 获取操作的起始地址(设备内存地址)
     */
    private suspend fun getStartAddress(deviceType: Int): Int {
        Logger.log("获取操作的起始地址(设备内存地址)---")
        //重置接收值状态
        isReceiveData = false
        ringOTAProtocol.writeIntCommand(OTA_CMD_GET_STR_BASE, 0, null, 0)
        while (!isReceiveData) {
            delay(50)
            if (bleDisConnect) {
                break
            }
        }
        return if (deviceType == DEVICE_8010) {
            if (byteToInt(receiveValue) == FIRST_ADDRESS) {
                SECOND_ADDRESS
            } else {
                FIRST_ADDRESS
            }
        } else {
            byteToInt(receiveValue)
        }
    }

    /**
     * 根据设备类型设置Mtu
     */
    private suspend fun requestMtu(deviceType: Int): Int {
        Logger.log("根据设备类型设置Mtu---")
        if (deviceType == DEVICE_8010) {
            ringOTAProtocol.requestMtu(247)
        } else {
            ringOTAProtocol.requestMtu(512)
        }
        while (!mtuChange) {
            delay(100)
            if (bleDisConnect) {
                break
            }
        }
        return mtuSize - 3 - 9
    }

    /**
     * 获取设备类型
     */
    private fun getDeviceProcessorType() = if ((simpleByteToInt(receiveValue) and 0x10) == 0) {
        DEVICE_8010
    } else {
        DEVICE_8010H
    }

    /**
     * 数据转换
     */
    private fun simpleByteToInt(data: ByteArray?): Int {
        if (data == null) return 0
        return data[4].toInt() and 0x000000ff
    }

    private fun byteToInt(data: ByteArray?): Int {
        if (data == null) return 0
        var result: Int = data[4].toInt() and 0x000000ff
        result = result or (data[5].toInt() and 0x0000ff shl 8)
        result = result or (data[6].toInt() and 0x000000ff shl 16)
        result = result or (data[7].toInt() and 0x000000ff shl 24)
        return result
    }

    /**
     * 通知设备开始准备更新
     */
    private suspend fun notifyDeviceToStartPreparingUpgrade() {
        Logger.log("通知设备开始准备更新---")
        //重置接收值状态
        isReceiveData = false
        //写入数据指令
        ringOTAProtocol.writeIntCommand(OTA_CMD_NVDS_TYPE, 0, null, 0)
        //等待指令响应值
        while (!isReceiveData) {
            delay(100)
            if (bleDisConnect) {
                break
            }
        }
    }

    /**
     * 获取当前bin文件的CRC检验码
     */
    @Throws(Exception::class)
    private fun getCRC32Code(file: File): Int {
        Logger.log("获取当前bin文件的CRC检验码---")
        val fileIs = FileInputStream(file)
        var readCount: Int
        val input: InputStream = BufferedInputStream(fileIs)
        val inputBuffer = ByteArray(256)
        var crcCode = 0
        var count = 0
        while (input.read(inputBuffer, 0, 256).also { readCount = it } != -1) {
            if (count != 0) {
                //数据加解密
                crcCode = crc32CalByByte(crcCode, inputBuffer, 0, readCount)
            }
            count++
        }
        input.close()
        fileIs.close()
        return crcCode
    }

    /**
     * CRC按32位字节校验
     */
    private fun crc32CalByByte(oldCrcCode: Int, ptr: ByteArray, offset: Int, length: Int): Int {
        var len = length
        var crcCode = oldCrcCode
        var i = offset
        while (len-- != 0) {
            val high = crcCode / 256 //取CRC高8位
            crcCode = crcCode shl 8
            crcCode = crcCode xor crcTa8[high xor ptr[i].toInt() and 0xff]
            crcCode = crcCode and -0x1
            i++
        }
        return crcCode and -0x1
    }

    /**
     * 蓝牙连接断开的回调 或者 蓝牙 关闭
     */
    fun onBleDisConnect() {
        bleDisConnect = true
    }
}

interface IRingOTAProtocol {
    fun writeIntCommand(type: Int, address: Int, buffer: ByteArray?, length: Int) : Boolean
    fun writeLongCommand(type: Int, address: Int, buffer: ByteArray?, length: Long) : Boolean
    fun requestMtu(mtu:Int)
    fun setBleOTANotify()
}