package edu.uoc.android.fauliclaudi.rest

open class QuestionQuizzes(
     var image: String,
     var rightChoice: String,
     var title: String,
     var choice1: String,
     var choice2: String
) {

fun getRightCchoice(): String {
     return rightChoice
}
}