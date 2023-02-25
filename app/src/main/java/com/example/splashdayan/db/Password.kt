package com.example.splashdayan.db

data class Password (
    var id: Int?,
    var userId: Int,
    var site: String,
    var password: String
    )