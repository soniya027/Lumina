package com.quoteapp

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class QuoteManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("quote_prefs", Context.MODE_PRIVATE)

    private val quotes = listOf(
        Quote(1, "The only way to do great work is to love what you do.", "Steve Jobs", "Passion"),
        Quote(2, "In the middle of every difficulty lies opportunity.", "Albert Einstein", "Resilience"),
        Quote(3, "It does not matter how slowly you go as long as you do not stop.", "Confucius", "Perseverance"),
        Quote(4, "Life is what happens when you're busy making other plans.", "John Lennon", "Life"),
        Quote(5, "The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt", "Dreams"),
        Quote(6, "Spread love everywhere you go. Let no one ever come to you without leaving happier.", "Mother Teresa", "Kindness"),
        Quote(7, "When you reach the end of your rope, tie a knot in it and hang on.", "Franklin D. Roosevelt", "Strength"),
        Quote(8, "You miss 100% of the shots you don't take.", "Wayne Gretzky", "Action"),
        Quote(9, "Whether you think you can or you think you can't, you're right.", "Henry Ford", "Mindset"),
        Quote(10, "Nothing is impossible, the word itself says I'm possible.", "Audrey Hepburn", "Inspiration"),
        Quote(11, "The secret of getting ahead is getting started.", "Mark Twain", "Action"),
        Quote(12, "It always seems impossible until it's done.", "Nelson Mandela", "Perseverance"),
        Quote(13, "Do not go where the path may lead; go instead where there is no path and leave a trail.", "Ralph Waldo Emerson", "Leadership"),
        Quote(14, "You will face many defeats in life, but never let yourself be defeated.", "Maya Angelou", "Resilience"),
        Quote(15, "Life is either a daring adventure or nothing at all.", "Helen Keller", "Adventure"),
        Quote(16, "If you look at what you have in life, you'll always have more.", "Oprah Winfrey", "Gratitude"),
        Quote(17, "If you want to live a happy life, tie it to a goal, not to people or things.", "Albert Einstein", "Happiness"),
        Quote(18, "Your time is limited, so don't waste it living someone else's life.", "Steve Jobs", "Authenticity"),
        Quote(19, "The greatest glory in living lies not in never falling, but in rising every time we fall.", "Nelson Mandela", "Strength"),
        Quote(20, "In the end, it's not the years in your life that count. It's the life in your years.", "Abraham Lincoln", "Life")
    )

    fun getQuoteOfTheDay(): Quote {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val savedDate = prefs.getString("daily_date", "")
        val savedId = prefs.getInt("daily_quote_id", -1)

        return if (today == savedDate && savedId >= 0) {
            quotes.firstOrNull { it.id == savedId } ?: pickAndSaveDailyQuote(today)
        } else {
            pickAndSaveDailyQuote(today)
        }
    }

    private fun pickAndSaveDailyQuote(today: String): Quote {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val quote = quotes[dayOfYear % quotes.size]
        prefs.edit()
            .putString("daily_date", today)
            .putInt("daily_quote_id", quote.id)
            .apply()
        return quote
    }

    fun getRandomQuote(): Quote {
        val current = prefs.getInt("last_random_id", -1)
        val pool = quotes.filter { it.id != current }
        val quote = pool.random()
        prefs.edit().putInt("last_random_id", quote.id).apply()
        return quote
    }
}