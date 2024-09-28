package com.jzapata.todosum

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class Session(
    val email: String = "",
    var password: String = "",
    val name: String = ""
)

object AuthManager {
    private val database = Firebase.database
    private val usersRef = database.getReference("users")

    private var currentSession: Session? = null
    private var sharedPreferences: SharedPreferences? = null

    suspend fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
            val savedEmail = sharedPreferences?.getString("USER_EMAIL", null)
            if (savedEmail != null) {
                val userKey = savedEmail.replace(".", ",")
                val snapshot = usersRef.child(userKey).get().await()
                currentSession = snapshot.getValue(Session::class.java)
            }
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        if (sharedPreferences == null) {
            throw IllegalStateException("AuthManager not initialized. Call init() first.")
        }
        val userKey = email.replace(".", ",")
        val snapshot = usersRef.child(userKey).get().await()
        val session = snapshot.getValue(Session::class.java)

        if (session != null && session.password == password) {
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

    suspend fun resetPassword(email: String): Boolean {
        val userKey = email.replace(".", ",")
        val snapshot = usersRef.child(userKey).get().await()
        val session = snapshot.getValue(Session::class.java)

        if (session != null) {
            val newPassword = "123456"
            usersRef.child(userKey).child("password").setValue(newPassword).await()
            return true
        }
        return false
    }

    suspend fun createAccount(email: String, password: String, name: String): Boolean {
        val userKey = email.replace(".", ",")
        val snapshot = usersRef.child(userKey).get().await()

        if (snapshot.exists()) {
            return false // El email ya está en uso
        }

        val newUser = Session(email, password, name)
        usersRef.child(userKey).setValue(newUser).await()
        return true
    }

    suspend fun initializeTestUsers() {
        val testUsers = listOf(
            Session("jzapata@crell.cl", "123456", "Jaime Zapata"),
            Session("test@test.cl", "123456", "Usuario Test"),
            Session("jmoil@gmail.com", "654321", "Jessica Moil"),
            Session("usuario1@gmail.com", "password1", "Usuario 1"),
            Session("usuario2@gmail.com", "password2", "Usuario 2"),
            Session("usuario3@gmail.com", "password3", "Usuario 3")
        )

        testUsers.forEach { user ->
            val userKey = user.email.replace(".", ",")
            usersRef.child(userKey).setValue(user).await()
        }
    }
}