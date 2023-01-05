package cc.fastcv.bluetoothdemo.ble.client

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.bluetoothdemo.R
import cc.fastcv.bluetoothdemo.ble.BleDev

@SuppressLint("MissingPermission")
class DeviceAdapter : RecyclerView.Adapter<DeviceAdapter.VH>() {

    private val deviceList = arrayListOf<BleDev>()

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    fun updateList(list: List<BleDev>) {
        deviceList.clear()
        deviceList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false))
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        val dev: BleDev = deviceList.get(position)
        val name = dev.dev.name
        val address = dev.dev.address
        holder.tvName.text = String.format("%s, %s, Rssi=%s", name, address, dev.scanResult.rssi)
        holder.tvAddress.text = String.format("广播数据{%s}", dev.scanResult.scanRecord)

        holder.itemView.setOnClickListener {
            mListener?.onItemClick(deviceList[position])
        }
    }

    override fun getItemCount(): Int = deviceList.size

    interface OnItemClickListener {
        fun onItemClick(dev: BleDev)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.name)
        val tvAddress = itemView.findViewById<TextView>(R.id.address)
    }

}