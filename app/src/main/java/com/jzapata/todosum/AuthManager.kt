package com.jzapata.todosum

data class Session(
    val email: String,
    var password: String,
    val name: String
)

object AuthManager {
    private val sessions = mutableListOf(
        Session("jzapata@crell.cl", "123456", "Jaime Zapata"),
        Session("jmoil@gmail.com", "654321", "Jessica Moil"),
        Session("usuario1@gmail.com", "password1", "Usuario 1"),
        Session("usuario2@gmail.com", "password2", "Usuario 2"),
        Session("usuario3@gmail.com", "password3", "Usuario 3")
    )

    fun login(email: String, password: String): Boolean {
        return sessions.any { it.email == email && it.password == password }
    }

    fun resetPassword(email: String): Boolean {
        val session = sessions.find { it.email == email }
        if (session != null) {
            // Futuramente implementar envio de correo
            session.password = "newpassword"
            return true
        }
        return false
    }

    fun createAccount(email: String, password: String, name: String): Boolean {
        if (sessions.any { it.email == email }) {
            return false // El email ya está en uso
        }
        sessions.add(Session(email, password, name))
        return true
    }
}