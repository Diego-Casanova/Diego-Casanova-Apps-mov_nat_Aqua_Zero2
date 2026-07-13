package com.example.agua2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agua2.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Representa el estado de la UI para la pantalla de Login.
 * Los lineamientos MAD exigen el manejo de estados reactivos.
 */
data class LoginUiState(
    val cedula: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow(LoginUiState())
    // Estado expuesto como solo lectura (StateFlow) para la UI
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Actualiza la cédula en el estado.
     * Validación: Solo números y máximo 10 dígitos.
     */
    fun onCedulaChanged(cedula: String) {
        if (cedula.all { it.isDigit() } && cedula.length <= 10) {
            _uiState.update { it.copy(cedula = cedula, errorMessage = null) }
        }
    }

    /**
     * Actualiza la contraseña en el estado.
     */
    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    /**
     * Ejecuta la lógica de inicio de sesión usando corrutinas.
     */
    fun login() {
        val currentState = _uiState.value
        
        if (currentState.cedula.length != 10) {
            _uiState.update { it.copy(errorMessage = "La cédula debe tener 10 dígitos") }
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingrese su contraseña") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val success = authRepository.login(currentState.cedula, currentState.password)
            
            if (success) {
                _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = "Cédula o contraseña incorrectas"
                    ) 
                }
            }
        }
    }
}
