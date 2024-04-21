package com.example.utsanmp160421081.model

import java.util.Date

data class News(
    val id:String,
    val title:String,
    val imageURL : String,
    val date : Date,
    val description: String,
    val content : List<String>,
    val author_name : String,
)

data class User(
    val id:String?,
    val username:String?,
    val firstName:String?,
    val lastName:String?,
    val email:String?,
    val password:String,
)
