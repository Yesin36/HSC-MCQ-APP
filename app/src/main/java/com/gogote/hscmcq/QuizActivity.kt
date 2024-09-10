package com.gogote.hscmcq

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gogote.hscmcq.databinding.ActivityQuizBinding
import com.gogote.hscmcq.databinding.ScoreDailogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        // Global variables for quiz data and user progress
        var questionsModelList: List<QuestionsModel> = listOf()
        var time: String = ""
        var selectedAnswer: String = ""
        var score = 0
    }

    // Track current question index
    var currentquestionIndex = 0

    // View binding for accessing UI elements
    lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Inflate layout using ViewBinding
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets for fullscreen display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load questions and start quiz timer
        loadQuestions()
        startTimer()

        // Set click listeners for answer options and next button
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
    }

    // Start countdown timer for the quiz
    private fun startTimer() {
        val totalTimeInMilliseconds = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMilliseconds, 1000L) {
            // Called every second (1000ms) to update timer display
            override fun onTick(milisUntilFinished: Long) {
                val seconds = milisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerindigatortext.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            // Called when the timer finishes
            override fun onFinish() {
                // Actions to perform when time runs out
            }
        }.start()
    }

    // Load the current question and update the UI
    private fun loadQuestions() {
        binding.apply {
            // Reset score if loading the first question
            if (currentquestionIndex == 0) {
                score = 0
            }

            // Clear selected answer
            selectedAnswer = ""

            // Check if all questions are answered
            if (currentquestionIndex == questionsModelList.size) {
                finishQuiz() // Show quiz results
                return
            }

            // Update UI elements with the current question and progress
            questionindigatortext.text = "Question ${currentquestionIndex + 1}/${questionsModelList.size}"
            progressindicator.progress = (currentquestionIndex.toFloat() / questionsModelList.size.toFloat() * 100).toInt()
            questiontextview.text = questionsModelList[currentquestionIndex].question

            // Set options for the current question
            btn0.text = questionsModelList[currentquestionIndex].options[0]
            btn1.text = questionsModelList[currentquestionIndex].options[1]
            btn2.text = questionsModelList[currentquestionIndex].options[2]
            btn3.text = questionsModelList[currentquestionIndex].options[3]
        }
    }

    // Handle clicks on the answer options and next button
    override fun onClick(view: View?) {
        // Reset the background color of all option buttons
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }

        val clickedButton = view as Button
        // If "Next" button is clicked, evaluate the answer
        if (clickedButton.id == R.id.next_btn) {
            if (selectedAnswer.isEmpty()) {
                Toast.makeText(this, "Please select an answer to continue", Toast.LENGTH_SHORT).show()
            }

            // Check if the selected answer is correct
            if (selectedAnswer == questionsModelList[currentquestionIndex].correct.trim()) {
                score++ // Increment score for correct answer
                Log.i("score", score.toString()) // Log the current score
            }

            // Move to the next question
            currentquestionIndex++
            loadQuestions() // Load the next question

        } else {
            // If an option button is clicked, highlight the selected option
            clickedButton.setBackgroundColor(getColor(R.color.orange))
            selectedAnswer = clickedButton.text.toString().trim() // Store selected answer
        }
    }

    // Finish the quiz and show the results
    private fun finishQuiz() {
        val totalQuestion = questionsModelList.size
        val percentage = ((score.toFloat() / totalQuestion.toFloat()) * 100).toInt()

        // Inflate custom dialog for showing the score
        val dailogBinding = ScoreDailogBinding.inflate(layoutInflater)
        dailogBinding.apply {
            scoreProgressText.text = "$percentage %"
            scoreProgressIndicator.progress = percentage

            // Display pass/fail message based on score
            if (percentage > 60) {
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(getColor(R.color.blue))
                scoreProgressIndicator.setIndicatorColor(getColor(R.color.orange))
            } else {
                scoreTitle.text = "Sorry! You have failed"
                scoreTitle.setTextColor(getColor(R.color.red))
                scoreProgressIndicator.setIndicatorColor(getColor(R.color.orange))
            }

            // Display total correct answers
            scoreSubtitle.text = "$score out of $totalQuestion questions are correct"

            // Button to finish the quiz
            finishbtn.setOnClickListener {
                finish() // Close the activity
            }
        }

        // Show the score dialog
        AlertDialog.Builder(this)
            .setView(dailogBinding.root)
            .setCancelable(false)
            .show()
    }
}
