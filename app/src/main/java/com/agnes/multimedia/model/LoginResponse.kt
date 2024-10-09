package com.agnes.multimedia.model

data class LoginResponse(
    var id : Int,
    var username : String,
    var email : String,
    var firstname : String,
    var lastname : String,
    var gender : String,
    var image : String,
    var accessToken : String,
    var refreshToken : String
)
