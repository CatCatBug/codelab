package cc.fastcv.bluetoothdemo.bt.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.bluetoothdemo.R
import cc.fastcv.bluetoothdemo.bt.BleDeviceProxy

class DeviceAdapter : RecyclerView.Adapter<DeviceAdapter.VH>() {

    private val deviceList = arrayListOf<BleDeviceProxy>()

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    fun updateList(list: List<BleDeviceProxy>) {
        deviceList.clear()
        deviceList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvName.text = deviceList[position].getName()
        holder.tvAddress.text = deviceList[position].getAddress()
        holder.itemView.setOnClickListener {
            mListener?.onItemClick(deviceList[position])
        }
    }

    override fun getItemCount(): Int = deviceList.size

    interface OnItemClickListener {
        fun onItemClick(dev: BleDeviceProxy)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.name)
        val tvAddress = itemView.findViewById<TextView>(R.id.address)
    }
}