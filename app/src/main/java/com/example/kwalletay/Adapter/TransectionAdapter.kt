package com.example.kwalletay.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kwalletay.Model.Transection
import com.example.kwalletay.R
import com.example.kwalletay.databinding.TransectionViewholderBinding

class TransectionAdapter(private val items: List<Transection>) :
    RecyclerView.Adapter<TransectionAdapter.ViewHolder>() {

    class ViewHolder(val binding: TransectionViewholderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransectionViewholderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            TitleTxt.text = item.title
            dateTxt.text = item.date
            priceTxt.text = if (item.price >= 0) "₹${item.price}" else "-₹${Math.abs(item.price)}"

            if (item.price >= 0) {
                img.setImageResource(R.drawable.arrow_green)
                img.setBackgroundResource(R.drawable.light_green_bg)
                priceTxt.setTextColor(ContextCompat.getColor(root.context, R.color.darkGreen))
            } else {
                img.setImageResource(R.drawable.arrow_red)
                img.setBackgroundResource(R.drawable.light_red_bg)
                priceTxt.setTextColor(ContextCompat.getColor(root.context, R.color.red))
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
