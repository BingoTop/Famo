package com.softsquared.template.kotlin.src.auth.signup.models

data class PostRequestSignUp (
    private val loginID:String,
    private val password:String,
    private val nickname:String,
    private val phoneNumber:String
        )