package com.quoteapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritesManager = FavoritesManager(this)

        recyclerView = findViewById(R.id.recycler_favorites)
        tvEmpty = findViewById(R.id.tv_empty)

        val backBtn = findViewById<View>(R.id.btn_back)
        backBtn.setOnClickListener { finish() }

        setupRecyclerView()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(
            onShare = { quote -> shareQuote(quote) },
            onRemove = { quote ->
                favoritesManager.removeFavorite(quote.id)
                loadFavorites()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadFavorites() {
        val favorites = favoritesManager.getFavorites()
        if (favorites.isEmpty()) {
            recyclerView.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            adapter.submitList(favorites)
        }
    }

    private fun shareQuote(quote: Quote) {
        val shareText = "\u201C${quote.text}\u201D\n\n— ${quote.author}\n\nShared via Lumina"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share quote via..."))
    }
}