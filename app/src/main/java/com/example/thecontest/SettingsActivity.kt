package com.example.thecontest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun saveLanguage(view: View) {
        val radioGroup = findViewById<RadioGroup>(R.id.language_radio_group)
        val language = when (radioGroup.checkedRadioButtonId) {
            R.id.language_english -> "en"
            R.id.language_spanish -> "es"
            else -> "en"
        }

        sharedPreferences.edit {
            putString("language", language)
        }
        setLocale(language)
        recreate()
    }

    private fun setLocale(language: String) {
        val resources = resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(Locale(language))
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun openMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
