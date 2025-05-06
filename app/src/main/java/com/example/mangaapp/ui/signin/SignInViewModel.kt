package com.example.mangaapp.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.domain.usecase.GetCurrentUserUseCase
import com.example.mangaapp.domain.usecase.LoginUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _shouldSkipSignIn = MutableStateFlow(false)
    val shouldSkipSignIn: StateFlow<Boolean> = _shouldSkipSignIn.asStateFlow()

    fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                if (user != null && user.isLoggedIn) {
                    _signInState.value = SignInState.Success("User already logged in")
                }
            }
        }
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun signIn() {
        viewModelScope.launch {
            _signInState.value = SignInState.Loading

            loginUseCase(_email.value, _password.value)
                .onSuccess { user ->
                    _signInState.value = SignInState.Success("Sign in successful")
                }
                .onFailure { exception ->
                    _signInState.value = SignInState.Error(exception.message ?: "Unknown error")
                }
        }
    }

    // Function to skip sign in (when user presses back/close)
    fun skipSignIn() {
        _shouldSkipSignIn.value = true
    }

    // Reset skip state (after navigating to home)
    fun resetSkipState() {
        _shouldSkipSignIn.value = false
    }
}