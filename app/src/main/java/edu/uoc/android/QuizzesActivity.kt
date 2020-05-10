package edu.uoc.android

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import edu.uoc.android.rest.QuestionQuizzes
import edu.uoc.android.rest.QuizzesAdapter
import kotlinx.android.synthetic.main.activity_quizzes.*

// Firestore database of google cloud services. It is important to define the Cloud Firestore API

class QuizzesActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)

        Log.d("Control","control activity creada")
        val recyclerView = rv_quizzes

        // Safe call to the database stored in the google console
        db.collection("Quizzes")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // stops the clock bar in the layout
                    indeterminateBarQuizzes.setVisibility(View.GONE)
                    recyclerView.setHasFixedSize(true)
                    recyclerView.layoutManager = LinearLayoutManager(this@QuizzesActivity)
                    var questionList: MutableList<QuestionQuizzes> = mutableListOf()
                    for (document in task.result!!) {
                        // Each document of Quizzes database cointains the information of one museum

                        var image = document.data["image"].toString()
                        var choice1 = document.data["choice1"].toString()
                        var choice2 = document.data["choice2"].toString()
                        var rightChoice = document.data["rightChoice"].toString()
                        var title = document.data["title"].toString()
                        var q = QuestionQuizzes(image,rightChoice,title,choice1,choice2)

                        questionList.add(q)

                        Log.d("Control","control document created " + document.data + " " )
                    }

                    recyclerView.adapter =  QuizzesAdapter(questionList, this@QuizzesActivity)

                } else {
                    Log.w("Error", "Error getting Firebase", task.exception)
                }


            }

    }
}

