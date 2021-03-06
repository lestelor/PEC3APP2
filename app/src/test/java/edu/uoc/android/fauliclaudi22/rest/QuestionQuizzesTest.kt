package edu.uoc.android.fauliclaudi22.rest


import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class QuestionQuizzesTest {
    private var questionQuizzes: QuestionQuizzes? = null
    //Not necessary to pass any parrameter
    //private var righChoice:String = "1"

    //The class QuestionQuizzes is simulated. So no need to fill the data.
    @Mock
    var mMockQuestionQuizzes: QuestionQuizzes? = null

    //The object questionQuizzes is simulated. So no need to fill the data.
    @Before
    fun before() {

        MockitoAnnotations.initMocks(this)
        // Create questionQuizzes to persist.
        questionQuizzes = mMockQuestionQuizzes
    }

    // The list is filled with mocked data. Then we check that the call is correctly done
    @Test
    fun QuestionQuizzes_getrightchoice() {
        //Not necessary to define the actual value.
        //questionQuizzes?.rightChoice=righChoice
        val questionList: MutableList<QuestionQuizzes> = mutableListOf()
        questionList.add(questionQuizzes!!)

        MatcherAssert.assertThat(
            "Checking that getRightChoice method answers the current value",
            questionList[0].getRightCchoice(),
            `is`(CoreMatchers.equalTo(questionList[0].rightChoice))
        )
    }
}






