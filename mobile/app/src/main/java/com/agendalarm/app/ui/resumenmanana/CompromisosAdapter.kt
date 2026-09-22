package com.agendalarm.app.ui.resumenmanana

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agendalarm.app.databinding.ItemCompromisoBinding

class CompromisosAdapter : ListAdapter<CompromisoUi, CompromisosAdapter.CompromisoHolder>(Comparador) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompromisoHolder {
        val binding = ItemCompromisoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompromisoHolder(binding)
    }

    override fun onBindViewHolder(holder: CompromisoHolder, position: Int) {
        holder.enlazar(getItem(position))
    }

    private object Comparador : DiffUtil.ItemCallback<CompromisoUi>() {
        override fun areItemsTheSame(anterior: CompromisoUi, nuevo: CompromisoUi) = anterior.id == nuevo.id
        override fun areContentsTheSame(anterior: CompromisoUi, nuevo: CompromisoUi) = anterior == nuevo
    }

    class CompromisoHolder(private val binding: ItemCompromisoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun enlazar(compromiso: CompromisoUi) {
            binding.compromiso = compromiso
            binding.executePendingBindings()
        }
    }
}
