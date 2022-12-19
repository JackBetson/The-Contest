package com.example.thecontest

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TEAM_A_SCORE_KEY = "team_a_score"
private const val TEAM_B_SCORE_KEY = "team_b_score"

class MainActivity : AppCompatActivity() {

    private lateinit var teamAScoreButton: Button
    private lateinit var teamBScoreButton: Button
    private lateinit var resetScoreButton: Button
    private lateinit var teamAScoreTextView: TextView
    private lateinit var teamBScoreTextView: TextView

    private var teamAScore = 0
    private var teamBScore = 0

    private lateinit var scoreSound: MediaPlayer
    private lateinit var winSound: MediaPlayer

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                openSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(TEAM_A_SCORE_KEY, teamAScore)
        savedInstanceState.putInt(TEAM_B_SCORE_KEY, teamBScore)
        Log.d("MainActivity", "Saving team A score: $teamAScore")
        Log.d("MainActivity", "Saving team B score: $teamBScore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        restoreScores(savedInstanceState)
        initViews()
        setUpButtonListeners()
        updateScore()
        loadSounds()
    }

    private fun restoreScores(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            Log.d("MainActivity", "Restoring team A score: ${savedInstanceState.getInt(TEAM_A_SCORE_KEY)}")
            Log.d("MainActivity", "Restoring team B score: ${savedInstanceState.getInt(TEAM_B_SCORE_KEY)}")
            teamAScore = savedInstanceState.getInt(TEAM_A_SCORE_KEY)
            teamBScore = savedInstanceState.getInt(TEAM_B_SCORE_KEY)
        }
    }

    private fun initViews() {
        teamAScoreButton = findViewById(R.id.team_a_score_button)
        teamBScoreButton = findViewById(R.id.team_b_score_button)
        resetScoreButton = findViewById(R.id.reset_score_button)
        teamAScoreTextView = findViewById(R.id.team_a_score_text_view)
        teamBScoreTextView = findViewById(R.id.team_b_score_text_view)
    }

    private fun setUpButtonListeners() {
        teamAScoreButton.setOnClickListener {
            teamAScore++
            Log.d("MainActivity", "Team A score: $teamAScore")
            playButtonSound()
            updateScore()
        }
        teamBScoreButton.setOnClickListener {
            teamBScore++
            Log.d("MainActivity", "Team B score: $teamBScore")
            playButtonSound()
            updateScore()
        }
        resetScoreButton.setOnClickListener {
            Log.d("MainActivity", "Resetting scores")
            resetScores()
        }
    }

    private fun playButtonSound() {
        scoreSound.start()
    }

    private fun playWinSound() {
        winSound.start()
    }

    private fun resetScores() {
        teamAScore = 0
        teamBScore = 0
        updateScore()
    }

    private fun updateScore() {
        updateScoreTextViews()
        checkIfTeamWon()
    }

    private fun loadSounds() {
        scoreSound = MediaPlayer.create(this, R.raw.score)
        winSound = MediaPlayer.create(this, R.raw.win)
    }

    private fun updateScoreTextViews() {
        teamAScoreTextView.text = teamAScore.toString()
        teamBScoreTextView.text = teamBScore.toString()
    }

    private fun checkIfTeamWon() {
        if (teamAScore == 15) {
            // Team A has won
            playWinSound()
            showWinDialog(R.string.team_a_win)
        } else if (teamBScore == 15) {
            // Team B has won
            playWinSound()
            showWinDialog(R.string.team_b_win)
        }
    }

    private fun showWinDialog(winMessage: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(winMessage)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                resetScores()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}


