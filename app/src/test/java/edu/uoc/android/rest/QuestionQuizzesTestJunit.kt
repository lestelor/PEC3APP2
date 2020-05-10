package edu.uoc.android.rest


import org.junit.Assert.assertEquals
import org.junit.Test


class QuestionQuizzesTestJunit {

    @Test
    fun getRightChoice() {

        val questionList: MutableList<QuestionQuizzes> = mutableListOf()
        val q = QuestionQuizzes("Título","0","Título", "chioce1","choice2")
        questionList.add(q)
        assertEquals(questionList[0].getRightCchoice(), (questionList[0].rightChoice))
    }
}

