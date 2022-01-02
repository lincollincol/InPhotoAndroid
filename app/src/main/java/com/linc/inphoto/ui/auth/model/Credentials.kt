package com.linc.inphoto.ui.auth.model

sealed class Credentials(
    val email: String,
    val password: String,
) {

    class SignIn(
        email: String,
        password: String
    ) : Credentials(email, password)

    class SignUp(
        email: String,
        val username: String,
        password: String,
        val repeatPassword: String
    ) : Credentials(email, password)

    class Reset(
        email: String,
        password: String,
        val repeatPassword: String
    ) : Credentials(email, password)

}