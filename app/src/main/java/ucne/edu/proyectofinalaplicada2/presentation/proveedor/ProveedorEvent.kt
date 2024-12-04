package ucne.edu.proyectofinalaplicada2.presentation.proveedor

sealed interface ProveedorEvent {
    data class OnChangeProveedorID(val proveedorId: Int) : ProveedorEvent
    data class OnChangeNombre(val nombre: String) : ProveedorEvent
    data class OnChangeTelefono(val telefono: String) : ProveedorEvent
    data object OnSave : ProveedorEvent
    data object OnClearSuccess : ProveedorEvent


}