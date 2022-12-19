package com.example.thecontest

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

private const val TEAM_A_SCORE_KEY = "team_a_score"
private const val TEAM_B_SCORE_KEY = "team_b_score"

class MainActivity : AppCompatActivity() {
    class ScoreViewModel : ViewModel() {
        var teamAScore = 0
        var teamBScore = 0
    }

    private lateinit var scoreViewModel: ScoreViewModel
    private lateinit var teamAScoreButton: Button
    private lateinit var teamBScoreButton: Button
    private lateinit var resetScoreButton: Button
    private lateinit var teamAScoreTextView: TextView
    private lateinit var teamBScoreTextView: TextView

    private var teamAScore = 0
    private var teamBScore = 0

    private lateinit var scoreSound: MediaPlayer
    private lateinit var winSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        scoreViewModel = ViewModelProvider(this)[ScoreViewModel::class.java]

        // Restore scores from savedInstanceState if available
        if (savedInstanceState != null) {
            teamAScore = savedInstanceState.getInt(TEAM_A_SCORE_KEY)
            teamBScore = savedInstanceState.getInt(TEAM_B_SCORE_KEY)
        } else {
            teamAScore = scoreViewModel.teamAScore
            teamBScore = scoreViewModel.teamBScore
        }

        // Initialize views
        teamAScoreButton = findViewById(R.id.team_a_score_button)
        teamBScoreButton = findViewById(R.id.team_b_score_button)
        resetScoreButton = findViewById(R.id.reset_score_button)
        teamAScoreTextView = findViewById(R.id.team_a_score_text_view)
        teamBScoreTextView = findViewById(R.id.team_b_score_text_view)

        // Set up button listeners
        teamAScoreButton.setOnClickListener {
            teamAScore++
            playButtonSound()
            updateScore()
        }
        teamBScoreButton.setOnClickListener {
            teamBScore++
            playButtonSound()
            updateScore()
        }
        resetScoreButton.setOnClickListener {
            resetScores()
        }

        // Update scores on start
        updateScore()

        // Load sounds
        scoreSound = MediaPlayer.create(this, R.raw.score)
        winSound = MediaPlayer.create(this, R.raw.win)
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
        teamAScoreTextView.text = teamAScore.toString()
        teamBScoreTextView.text = teamBScore.toString()

        // Check if a team has won
        if (teamAScore == 10) {
            // Team A has won
            playWinSound()
            showWinDialog(R.string.team_a_win)
        } else if (teamBScore == 10) {
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


}

   
