package com.quoteapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(
    private val onShare: (Quote) -> Unit,
    private val onRemove: (Quote) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var quotes = listOf<Quote>()

    fun submitList(newList: List<Quote>) {
        quotes = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQuote: TextView = view.findViewById(R.id.tv_fav_quote)
        val tvAuthor: TextView = view.findViewById(R.id.tv_fav_author)
        val tvCategory: TextView = view.findViewById(R.id.tv_fav_category)
        val btnShare: ImageButton = view.findViewById(R.id.btn_fav_share)
        val btnRemove: ImageButton = view.findViewById(R.id.btn_fav_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_quote, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]
        holder.tvQuote.text = "\u201C${quote.text}\u201D"
        holder.tvAuthor.text = "— ${quote.author}"
        holder.tvCategory.text = quote.category.uppercase()
        holder.btnShare.setOnClickListener { onShare(quote) }
        holder.btnRemove.setOnClickListener { onRemove(quote) }
    }

    override fun getItemCount() = quotes.size
}