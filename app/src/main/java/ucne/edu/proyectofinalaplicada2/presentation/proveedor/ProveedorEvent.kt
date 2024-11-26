package ucne.edu.proyectofinalaplicada2.presentation.proveedor

sealed interface ProveedorEvent {
    data class OnChangeProveedorID(val proveedorId: Int) : ProveedorEvent
}