package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.repository.ModeloRepository
import ucne.edu.proyectofinalaplicada2.repository.ProveedorRepository
import ucne.edu.proyectofinalaplicada2.repository.TipoCombustibleRepository
import ucne.edu.proyectofinalaplicada2.repository.TipoVehiculoRepository
import ucne.edu.proyectofinalaplicada2.repository.VehiculoRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VehiculoViewModel @Inject constructor(
    private val vehiculoRepository: VehiculoRepository,
    private val modeloRepository: ModeloRepository,
    private val marcaRepository: MarcaRepository,
    private val tipoCombustibleRepository: TipoCombustibleRepository,
    private val tipoVehiculoRepository: TipoVehiculoRepository,
    private val proveedorRepository: ProveedorRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(VehiculoUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getMarcas()
        getTipoCombustible()
        getProveedores()
        getTipoVehiculos()
        getVehiculos()
    }

    private fun getVehiculos() {
        viewModelScope.launch {
            vehiculoRepository.getVehiculos().collect { result ->

                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error",
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            val vehiculos = result.data ?: emptyList()
                            val vehiculoConMarcasYModelos =
                                transformarVehiculosConMarcasYModelos(vehiculos)
                            it.copy(
                                isLoading = false,
                                vehiculos = vehiculos,
                                vehiculoConMarcas = vehiculoConMarcasYModelos,
                                filteredVehiculoConMarcas = vehiculoConMarcasYModelos
                            )
                        }
                    }
                }
            }
        }
    }


    private suspend fun transformarVehiculosConMarcasYModelos(vehiculos: List<VehiculoEntity>): List<VehiculoConMarca> {
        val vehiculoConMarcas = vehiculos.map { vehiculo ->
            val marca = marcaRepository.getMarcaById(vehiculo.marcaId ?: 0).data
            val modelo = modeloRepository.getModelosById(vehiculo.modeloId ?: 0).data
            VehiculoConMarca(
                nombreMarca = marca?.nombreMarca,
                nombreModelo = modelo?.modeloVehiculo,
                vehiculo = vehiculo,
                estaRentado = vehiculo.estaRentado
            )
        }
        return vehiculoConMarcas

    }

    private fun filterVehiculos(query: String) {
        _uistate.update { state ->

            val filteredList = state.filteredVehiculoConMarcas.filter { vehiculo ->
                val marca = vehiculo.nombreMarca?.contains(query, ignoreCase = true) ?: false
                val modelo = vehiculo.nombreModelo?.contains(query, ignoreCase = true) ?: false
                marca || modelo
            }
            if (filteredList.isEmpty()) {
                state.copy(
                    filteredListIsEmpty = true,
                    searchQuery = query,
                )
            } else {
                state.copy(
                    filteredListIsEmpty = false,
                    searchQuery = query,
                    vehiculoConMarcas = filteredList
                )
            }

        }
    }

    private fun getMarcas() {
        viewModelScope.launch {
            val result = marcaRepository.getMarcas().data
            _uistate.update {
                it.copy(
                    marcas = result ?: emptyList(),
                )
            }
        }
    }

    private fun getTipoCombustible() {
        viewModelScope.launch {
            val result = tipoCombustibleRepository.getTiposCombustibles().data
            _uistate.update {
                it.copy(
                    tipoCombustibles = result ?: emptyList(),
                )
            }
        }
    }

    private fun getProveedores() {
        viewModelScope.launch {
            val result = proveedorRepository.getProveedores().data
            _uistate.update {
                it.copy(
                    proveedores = result ?: emptyList(),
                )
            }
        }
    }

    private fun getTipoVehiculos() {
        viewModelScope.launch {
            val result = tipoVehiculoRepository.getTiposVehiculos().data
            _uistate.update {
                it.copy(
                    tipoVehiculos = result ?: emptyList(),
                )
            }
        }
    }

    private fun getModelosByMarcaId(id: Int) {
        viewModelScope.launch {
            val modelos = modeloRepository.getListModelosByMarcaId(id).data
            _uistate.update {
                it.copy(
                    modelos = modelos ?: emptyList()
                )
            }
        }
    }

    private fun save() {
        if(!validarVehiculo()){
            return
        }
        viewModelScope.launch {

            val vehiculo = vehiculoRepository.addVehiculo(
                uistate.value.toEntity()
            )

            vehiculo.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error",
                                isLoadingData = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoadingData = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                success = "Vehiculo agregado",
                                isLoadingData = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun validarVehiculo(): Boolean {
        var isValid = true

        if (_uistate.value.tipoVehiculoId == null) {
            isValid = false
            _uistate.update {
                it.copy(
                    tipoVehiculoError = "Seleccione un tipo de vehículo"
                )
            }
        } else {
            _uistate.update { it.copy(tipoVehiculoError = "") }
        }

        if (_uistate.value.marcaId == 0) {
            isValid = false
            _uistate.update {
                it.copy(
                    marcaError = "Seleccione una marca"
                )
            }
        } else {
            _uistate.update { it.copy(marcaError = "") }
        }

        if (_uistate.value.modeloId == 0) {
            isValid = false
            _uistate.update {
                it.copy(
                    modeloError = "Seleccione un modelo"
                )
            }
        } else {
            _uistate.update { it.copy(modeloError = "") }
        }

        if (_uistate.value.precio == null || _uistate.value.precio!! < 1000 || _uistate.value.precio!! > 100000) {
            isValid = false
            _uistate.update {
                it.copy(
                    precioError = "Ingrese un precio válido (>= 1000)"
                )
            }
        } else {
            _uistate.update { it.copy(precioError = "") }
        }

        if (_uistate.value.descripcion.isEmpty()) {
            isValid = false
            _uistate.update {
                it.copy(
                    descripcionError = "Ingrese una descripción"
                )
            }
        } else {
            _uistate.update { it.copy(descripcionError = "") }
        }

        if (_uistate.value.anio == null || _uistate.value.anio!! < 2010) {
            isValid = false
            _uistate.update {
                it.copy(
                    anioError = "Ingrese un año válido (>= 2010)"
                )
            }
        } else {
            _uistate.update { it.copy(anioError = "") }
        }

        if (_uistate.value.imagePath.isEmpty()) {
            isValid = false
            _uistate.update {
                it.copy(
                    imageError = "Seleccione una imagen"
                )
            }
        } else {
            _uistate.update { it.copy(imageError = "") }
        }

        if (_uistate.value.tipoCombustibleId == null) {
            isValid = false
            _uistate.update {
                it.copy(
                    tipoCombustibleError = "Seleccione un tipo de combustible"
                )
            }
        } else {
            _uistate.update { it.copy(tipoCombustibleError = "") }
        }
        if (_uistate.value.proveedorId == null) {
            isValid = false
            _uistate.update {
                it.copy(
                    proveedorError = "Seleccione un proveedor"
                )
            }
        } else {
            _uistate.update { it.copy(tipoCombustibleError = "") }
        }

        return isValid
    }

    private fun updateVehiculo() {
        viewModelScope.launch {
            val vehiculo = vehiculoRepository.updateVehiculo(
                uistate.value.toEntity()
            )
            vehiculo.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error"
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                success = "Vehiculo actualizado",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteVehiculo(id: Int){
        viewModelScope.launch {
            vehiculoRepository.deleteVehiculo(id).collect{ result->
                when(result){
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error"
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                success = "Vehiculo eliminado",
                                isLoading = false
                            )
                        }
                    }

                }
            }
        }
    }
    private fun onImagesSelected(uris: List<Uri>, context: Context) {
        viewModelScope.launch {
            val imageFiles = uris.mapNotNull { uri ->
                uriToFile(uri, context)
            }

            _uistate.update {
                it.copy(imagePath = imageFiles.map { file -> file.absolutePath })
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("selected_image", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun getVehiculoConMarcas() {
        val vehiculoConMarcas = _uistate.value.vehiculos.map { vehiculo ->
            val marca = marcaRepository.getMarcaById(vehiculo.marcaId ?: 0).data
            val modelo = modeloRepository.getModelosById(vehiculo.modeloId ?: 0).data
            VehiculoConMarca(
                nombreModelo = modelo?.modeloVehiculo,
                nombreMarca = marca?.nombreMarca,
                vehiculo = vehiculo,
                estaRentado = vehiculo.estaRentado
            )
        }
        _uistate.update {
            it.copy(
                vehiculoConMarcas = vehiculoConMarcas
            )
        }
    }

    private fun selectedVehiculo(vehiculoId: Int) {
        _uistate.update {
            it.copy(
                isLoadingData = true
            )
        }
        viewModelScope.launch {
            val vehiculo = vehiculoRepository.getVehiculoById(vehiculoId).data
            val marcas = marcaRepository.getMarcas().data
            val tipoCombustibles = tipoCombustibleRepository.getTiposCombustibles().data
            val tipoVehiculos = tipoVehiculoRepository.getTiposVehiculos().data
            val modelos = modeloRepository.getModelos().data
            if (vehiculo != null) {
                _uistate.update {
                    it.copy(
                        vehiculoId = vehiculo.vehiculoId,
                        marcaId = vehiculo.marcaId?:0,
                        precio = vehiculo.precio,
                        tipoVehiculoId = vehiculo.tipoVehiculoId,
                        descripcion = vehiculo.descripcion ?: "",
                        tipoCombustibleId = vehiculo.tipoCombustibleId,
                        modeloId = vehiculo.modeloId ?: 0,
                        proveedorId = vehiculo.proveedorId,
                        anio = vehiculo.anio,
                        imagePath = vehiculo.imagePath?: emptyList(),

                        marcas = marcas ?: emptyList(),
                        tipoCombustibles = tipoCombustibles,
                        tipoVehiculos = tipoVehiculos,
                        modelos = modelos ?: emptyList(),
                        isLoadingData = false
                    )
                }

            }
        }
    }

    private fun onChangePrecio(precio: Int) {
        _uistate.update {
            it.copy(
                precio = precio,
                precioError = ""
            )
        }
    }

    private fun onChangeMarcaId(marcaId: Int) {
        getModelosByMarcaId(marcaId)
        _uistate.update {
            it.copy(
                marcaId = marcaId,
                marcaError = ""
            )
        }
    }

    private fun onChangeDescripcion(descripcion: String) {
        _uistate.update {
            it.copy(
                descripcion = descripcion,
                descripcionError = ""
            )
        }
    }

    private fun onChangeTipoCombustibleId(tipoCombustibleId: Int) {
        _uistate.update {
            it.copy(
                tipoCombustibleId = tipoCombustibleId,
                tipoCombustibleError = ""
            )
        }

    }

    private fun onChangeTipoVehiculoId(tipoVehiculoId: Int) {
        _uistate.update {
            it.copy(
                tipoVehiculoId = tipoVehiculoId,
                tipoVehiculoError = ""
            )
        }
    }

    private fun onChangeModeloId(modeloId: Int) {
        _uistate.update {
            it.copy(
                modeloId = modeloId,
                modeloError = ""
            )
        }
    }

    private fun onChangeProveedorId(proveedorId: Int) {
        _uistate.update {
            it.copy(
                proveedorId = proveedorId,
                proveedorError = ""
            )
        }
    }

    private fun onChangeImagePath(imagesPath: List<File?>) {
        val updatedImage = imagesPath.map { image ->
            image?.absolutePath
        }
        _uistate.update {
            it.copy(
                imagePath = updatedImage,
                imageError = ""
            )
        }
    }

    private fun onChangeAnio(anio: Int) {
        _uistate.update {
            it.copy(
                anio = anio,
                anioError = ""
            )
        }
    }

    private fun clearImageError() {
        _uistate.value = _uistate.value.copy(imageError = "")
    }
    private fun clearError() {
        _uistate.update {
            it.copy(error = "")
        }
    }
    private fun clearSuccess() {
        _uistate.update {
            it.copy(success = "")
        }
    }
    fun onEvent(event: VehiculoEvent) {
        when (event) {
            is VehiculoEvent.OnChangeDescripcion -> onChangeDescripcion(event.descripcion)
            is VehiculoEvent.OnChangePrecio -> onChangePrecio(event.precio)
            is VehiculoEvent.OnChangeTipoCombustibleId -> onChangeTipoCombustibleId(event.tipoCombustibleId)
            is VehiculoEvent.OnChangeTipoVehiculoId -> onChangeTipoVehiculoId(event.tipoVehiculoId)
            is VehiculoEvent.OnChangeModeloId -> onChangeModeloId(event.modeloId)
            is VehiculoEvent.OnChangeImagePath -> onChangeImagePath(event.imagePath)
            is VehiculoEvent.OnChangeProveedorId -> onChangeProveedorId(event.proveedorId)
            VehiculoEvent.Save -> save()
            VehiculoEvent.GetVehiculos -> getVehiculos()
            VehiculoEvent.UpdateVehiculo -> updateVehiculo()
            is VehiculoEvent.OnChangeAnio -> onChangeAnio(event.anio)
            is VehiculoEvent.OnChangeMarcaId -> onChangeMarcaId(event.marcaId)
            is VehiculoEvent.OnFilterVehiculos -> filterVehiculos(event.query)
            is VehiculoEvent.SelectedVehiculo -> selectedVehiculo(event.vehiculoId)
            is VehiculoEvent.OnImagesSelected -> onImagesSelected(event.uris, event.context)
            is VehiculoEvent.DeleteVehiculo -> deleteVehiculo(event.id)
            VehiculoEvent.ClearImageError -> clearImageError()
            VehiculoEvent.ClearError -> clearError()
            VehiculoEvent.ClearSuccess -> clearSuccess()
        }
    }
}