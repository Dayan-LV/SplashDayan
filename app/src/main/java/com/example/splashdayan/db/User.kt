package com.example.splashdayan.db

data class User(
    var userId: Int?,
    var email: String,
    var username: String,
    var password: String,
    var gender: String?,
    var birthDate: String?,
    var weight: Double?
)
