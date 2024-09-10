package com.gogote.hscmcq

data class QuizModel(
    val id : String,
    val title : String,
    val subtitle : String,
    val time : String,
    val questionsList : List<QuestionsModel>
) {
    constructor () : this("","","" ,"",emptyList())
}

data class QuestionsModel(
    val question : String,
    val options : List<String>,
    val correct: String,
){
    constructor () : this("", emptyList(), "")
}
