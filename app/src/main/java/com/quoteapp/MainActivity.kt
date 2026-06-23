package com.quoteapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuote: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvDayLabel: TextView
    private lateinit var btnFavorite: ImageButton
    private lateinit var btnShare: ImageButton
    private lateinit var btnRefresh: ImageButton
    private lateinit var btnViewFavorites: ImageButton
    private lateinit var cardQuote: CardView

    private lateinit var quoteManager: QuoteManager
    private lateinit var favoritesManager: FavoritesManager
    private var currentQuote: Quote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quoteManager = QuoteManager(this)
        favoritesManager = FavoritesManager(this)

        initViews()
        setupClickListeners()
        loadQuoteOfTheDay()
    }

    private fun initViews() {
        tvQuote = findViewById(R.id.tv_quote)
        tvAuthor = findViewById(R.id.tv_author)
        tvCategory = findViewById(R.id.tv_category)
        tvDayLabel = findViewById(R.id.tv_day_label)
        btnFavorite = findViewById(R.id.btn_favorite)
        btnShare = findViewById(R.id.btn_share)
        btnRefresh = findViewById(R.id.btn_refresh)
        btnViewFavorites = findViewById(R.id.btn_view_favorites)
        cardQuote = findViewById(R.id.card_quote)
    }

    private fun setupClickListeners() {
        btnFavorite.setOnClickListener { toggleFavorite() }
        btnShare.setOnClickListener { shareQuote() }
        btnRefresh.setOnClickListener { loadRandomQuote() }
        btnViewFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    private fun loadQuoteOfTheDay() {
        val quote = quoteManager.getQuoteOfTheDay()
        displayQuote(quote, isDaily = true)
    }

    private fun loadRandomQuote() {
        val quote = quoteManager.getRandomQuote()
        displayQuote(quote, isDaily = false)
    }

    private fun displayQuote(quote: Quote, isDaily: Boolean) {
        currentQuote = quote
        tvQuote.text = "\u201C${quote.text}\u201D"
        tvAuthor.text = "— ${quote.author}"
        tvCategory.text = quote.category.uppercase()
        tvDayLabel.text = if (isDaily) "QUOTE OF THE DAY" else "RANDOM QUOTE"
        updateFavoriteButton()
    }

    private fun toggleFavorite() {
        currentQuote?.let { quote ->
            if (favoritesManager.isFavorite(quote.id)) {
                favoritesManager.removeFavorite(quote.id)
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
            } else {
                favoritesManager.addFavorite(quote)
                Toast.makeText(this, "Saved to favorites ❤️", Toast.LENGTH_SHORT).show()
            }
            updateFavoriteButton()
        }
    }

    private fun updateFavoriteButton() {
        currentQuote?.let { quote ->
            if (favoritesManager.isFavorite(quote.id)) {
                btnFavorite.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                btnFavorite.setImageResource(android.R.drawable.btn_star_big_off)
            }
        }
    }

    private fun shareQuote() {
        currentQuote?.let { quote ->
            val shareText = "\u201C${quote.text}\u201D\n\n— ${quote.author}\n\nShared via Lumina"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_SUBJECT, "Inspiring Quote")
            }
            startActivity(Intent.createChooser(intent, "Share this quote via..."))
        }
    }

    override fun onResume() {
        super.onResume()
        updateFavoriteButton()
    }
}