package com.jzapata.todosum

import android.content.Context
import android.content.SharedPreferences

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

    private var currentSession: Session? = null
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
            val savedEmail = sharedPreferences?.getString("USER_EMAIL", null)
            if (savedEmail != null) {
                currentSession = sessions.find { it.email == savedEmail }
            }
        }
    }

    fun login(email: String, password: String): Boolean {
        if (sharedPreferences == null) {
            throw IllegalStateException("AuthManager not initialized. Call init() first.")
        }
        val session = sessions.find { it.email == email && it.password == password }
        if (session != null) {
            currentSession = session
            sharedPreferences?.edit()?.putString("USER_EMAIL", email)?.apply()
            return true
        }
        return false
    }

    fun logout() {
        if (sharedPreferences == null) {
            throw IllegalStateException("AuthManager not initialized. Call init() first.")
        }
        currentSession = null
        sharedPreferences?.edit()?.remove("USER_EMAIL")?.apply()
    }

    fun getCurrentUserName(): String? {
        return currentSession?.name
    }

    fun isLoggedIn(): Boolean {
        return currentSession != null
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