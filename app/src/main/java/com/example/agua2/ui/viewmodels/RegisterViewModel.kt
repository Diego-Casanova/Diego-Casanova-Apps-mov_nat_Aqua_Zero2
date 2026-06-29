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
 * Estado de la UI para la pantalla de Registro.
 */
data class RegisterUiState(
    val cedula: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isRegistrationSuccessful: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onCedulaChanged(cedula: String) {
        _uiState.update { it.copy(cedula = cedula, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = null) }
    }

    /**
     * Lógica de registro con validaciones y corrutinas.
     */
    fun register() {
        val currentState = _uiState.value
        
        // Validaciones básicas de negocio
        if (currentState.cedula.isBlank() || currentState.password.isBlank() || currentState.confirmPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Todos los campos son obligatorios") }
            return
        }

        if (currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val success = authRepository.register(currentState.cedula, currentState.password)
            
            if (success) {
                _uiState.update { it.copy(isLoading = false, isRegistrationSuccessful = true) }
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = "Error en el registro (La cédula debe tener 10 dígitos)" 
                    ) 
                }
            }
        }
    }
}
