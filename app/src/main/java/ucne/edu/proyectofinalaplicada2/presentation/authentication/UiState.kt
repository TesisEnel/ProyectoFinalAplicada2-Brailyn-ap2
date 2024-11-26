package ucne.edu.proyectofinalaplicada2.presentation.authentication


data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val email: String = "",
    val password: String = "",
    val errorEmail: String? = null,
    val errorPassword: String? = null
)

