package com.quoteapp

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class FavoritesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    private fun getRawJson(): JSONArray {
        val raw = prefs.getString("favorites", "[]") ?: "[]"
        return try { JSONArray(raw) } catch (e: Exception) { JSONArray() }
    }

    fun getFavorites(): List<Quote> {
        val arr = getRawJson()
        val list = mutableListOf<Quote>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            list.add(
                Quote(
                    id = obj.getInt("id"),
                    text = obj.getString("text"),
                    author = obj.getString("author"),
                    category = obj.getString("category")
                )
            )
        }
        return list
    }

    fun isFavorite(id: Int): Boolean = getFavorites().any { it.id == id }

    fun addFavorite(quote: Quote) {
        if (isFavorite(quote.id)) return
        val arr = getRawJson()
        val obj = JSONObject().apply {
            put("id", quote.id)
            put("text", quote.text)
            put("author", quote.author)
            put("category", quote.category)
        }
        arr.put(obj)
        prefs.edit().putString("favorites", arr.toString()).apply()
    }

    fun removeFavorite(id: Int) {
        val arr = getRawJson()
        val newArr = JSONArray()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            if (obj.getInt("id") != id) newArr.put(obj)
        }
        prefs.edit().putString("favorites", newArr.toString()).apply()
    }
}