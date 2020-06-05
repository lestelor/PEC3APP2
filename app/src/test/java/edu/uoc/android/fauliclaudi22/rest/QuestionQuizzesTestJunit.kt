package edu.uoc.android.fauliclaudi22.rest


import org.junit.Assert.assertEquals
import org.junit.Test


class QuestionQuizzesTestJunit {
    // In this test we simply fill the data and check that the call getRightCchoice() is correctly implemented
    @Test
    fun getRightChoice() {

        val questionList: MutableList<QuestionQuizzes> = mutableListOf()
        val q = QuestionQuizzes(
            "Título",
            "0",
            "Título",
            "chioce1",
            "choice2"
        )
        questionList.add(q)
        assertEquals(questionList[0].getRightCchoice(), (questionList[0].rightChoice))
    }
}

