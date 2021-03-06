package com.example.jowoan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jowoan.R
import com.example.jowoan.R.layout.item_practice
import com.example.jowoan.anim.Expandable
import com.example.jowoan.models.Practice
import com.example.jowoan.models.Subpractice
import kotlinx.android.synthetic.main.item_practice.view.*

class PracticeAdapter(private val practices: MutableList<Practice>, private val action: Action) :
    RecyclerView.Adapter<PracticeAdapter.PracticeViewHolder>() {

    val TAG = "PracticeAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PracticeViewHolder {
        return PracticeViewHolder(
            LayoutInflater.from(parent.context).inflate(item_practice, parent, false)
        )
    }

    override fun getItemCount() = practices.size

    override fun onBindViewHolder(holder: PracticeViewHolder, position: Int) {
        holder.bind(practices[position], action)
    }

    class PracticeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.image
        private val name = view.name
        private val totalSubpractices = view.totalSubpractices
        private val toggleSubpractices = view.toggleSubpractices
        private val recyclerView = view.recyclerView

        fun bind(practice: Practice, action: Action) {
            val subpractice = mutableListOf<Subpractice>()
            subpractice.addAll(practice.subpractices)
            name.text = practice.name
            totalSubpractices.text = "${practice.subpractices.size} Pelajaran"
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SubpracticeAdapter(subpractice, action)
            }
            toggleSubpractices.setOnClickListener {
                expandView(recyclerView)
            }
            Glide.with(view.context).load("http://${practice.image}").centerCrop()
                .placeholder(R.drawable.gambar_pemula1).into(image)
        }

        private fun expandView(view: View) {
            if (view.visibility == View.GONE)
                Expandable.expand(view)
            else
                Expandable.collapse(view)
        }
    }

    interface Action {
        fun subpracticeClicked(subpractice: Subpractice)
    }

}