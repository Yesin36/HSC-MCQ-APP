package com.gogote.hscmcq

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogote.hscmcq.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    // Declare binding for activity layout
    lateinit var binding: ActivityMainBinding
    // List to hold quiz models
    lateinit var quizModelList: MutableList<QuizModel>
    // Adapter for RecyclerView
    lateinit var adapter: QuizListAdpater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display
        enableEdgeToEdge()
        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets for fullscreen experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the quiz list
        quizModelList = mutableListOf()
        // Fetch data from Firebase
        getDataFromFirebase()
    }

    // Set up RecyclerView after loading data
    private fun setupRecyclerView() {
        // Hide progress bar once data is loaded
        binding.progressBar.visibility = View.GONE
        // Initialize the adapter with the quiz list
        adapter = QuizListAdpater(quizModelList)
        // Set the layout manager for vertical scrolling
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        // Attach the adapter to RecyclerView
        binding.recyclerView.adapter = adapter
    }

    // Fetch data from Firebase database
    private fun getDataFromFirebase() {

        // dummy data

//        val listQuizModel = mutableListOf<QuestionsModel>()
//        listQuizModel.add(QuestionsModel("What is Android?", mutableListOf("OS", "App", "Both", "None"), "Both "))
//       listQuizModel.add(QuestionsModel("Who owns Android ?", mutableListOf("Google", "Samsung", "Apple", "None"), "Google"))
//        listQuizModel.add(QuestionsModel("Which assistant android uses?", mutableListOf("Siri","Cortana","Google Assistant","Alexa"), "Google Assistant"))
//
//
//
//        quizModelList.add(QuizModel("1", "Programming", "All the basic programing ", "10", listQuizModel))
//        quizModelList.add(QuizModel("2", "Mathematics", "All the basic Mathematics ", "15", listQuizModel))
//        quizModelList.add(QuizModel("3", "Science", "All the basic Science ", "20", listQuizModel))


        // Show progress bar while loading
        binding.progressBar.visibility = View.VISIBLE

        // Access Firebase database reference
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot ->
                // Check if the data exists
                if (dataSnapshot.exists()) {
                    // Loop through each child snapshot
                    for (snapshot in dataSnapshot.children) {
                        // Convert snapshot to QuizModel object
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        // Add the quiz model to the list if it's not null
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                }
                // Set up RecyclerView with the loaded data
                setupRecyclerView()
            }
    }
}
