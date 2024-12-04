package ucne.edu.proyectofinalaplicada2.presentation.navigation

data class MainUiState(
    val currentTitle: String = "BravquezRentcar",
    val userDisplayName: String? = null,
    val userPhotoUrl: String? = null,
    val showBackButton: Boolean = true
)