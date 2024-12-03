package ucne.edu.proyectofinalaplicada2.presentation.renta

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.RentaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.repository.ClienteRepository
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.repository.ModeloRepository
import ucne.edu.proyectofinalaplicada2.repository.RentaRepository
import ucne.edu.proyectofinalaplicada2.repository.TipoCombustibleRepository
import ucne.edu.proyectofinalaplicada2.repository.TipoVehiculoRepository
import ucne.edu.proyectofinalaplicada2.repository.VehiculoRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource


class RentaViewModelTest{
    @get:Rule
    val mainDispatcherRule  = MainDispatcherRule()

    private val rentaRepository = mockk<RentaRepository>()
    private val vehiculoRepository = mockk<VehiculoRepository>()
    private val clienteRepository = mockk<ClienteRepository>()
    private val marcaRepository = mockk<MarcaRepository>()
    private val modeloRepository = mockk<ModeloRepository>()
    private val tipoCombustibleRepository = mockk<TipoCombustibleRepository>()
    private val tipoVehiculoRepository = mockk<TipoVehiculoRepository>()

    private lateinit var viewModel: RentaViewModel

    @Before
    fun setUp() {
        coEvery { rentaRepository.getRentas() } returns flowOf(Resource.Success(emptyList()))

        coEvery { vehiculoRepository.getVehiculoById(any()) } returns Resource.Success(null)
        coEvery { clienteRepository.getClienteByEmail(any()) } returns Resource.Success(null)
        coEvery { marcaRepository.getMarcaById(any()) } returns Resource.Success(null)
        coEvery { modeloRepository.getModelosById(any()) } returns Resource.Success(null)
        coEvery { tipoCombustibleRepository.getTipoCombustibleById(any()) } returns Resource.Success(null)
        coEvery { tipoVehiculoRepository.getTipoVehiculoById(any()) } returns Resource.Success(null)



          viewModel = RentaViewModel(
            rentaRepository,
            vehiculoRepository,
            clienteRepository,
            marcaRepository,
            modeloRepository,
            tipoCombustibleRepository,
            tipoVehiculoRepository
        )
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Should Retorn Success When Save a Renta`() = runTest {
        val renta = RentaDto(1, 1, 1, "12/2/2024", "12/2/2024", 2000.0)
        coEvery { rentaRepository.addRenta(renta) } returns flowOf(Resource.Success(renta))

        viewModel.save(renta)

        advanceUntilIdle()
        val uiState = viewModel.uistate.value
        assertEquals("Renta agregada", uiState.success)
        assertNull(uiState.error)
        assertFalse(uiState.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Should return a list of rentas`() = runTest {
        val rentas = listOf(
            RentaEntity(1, 1, 1, "12/2/2024", "12/2/2024", 2000.0),
            RentaEntity(2, 2, 2, "12/2/2024", "12/2/2024", 2000.0),
            RentaEntity(3, 3, 3, "12/2/2024", "12/2/2024", 2000.0)
        )

        coEvery { rentaRepository.getRentas() } returns flowOf(Resource.Success(rentas))
        viewModel.getRentas()
        advanceUntilIdle()
        val result = viewModel.uistate.value.rentas

        assertNotNull(result)
        assertEquals(rentas,result)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Should return a vehicle entity by id`() = runTest {
        val vehiculo = VehiculoEntity( 1, 1, 1, 1, 1, 200, "Hol", 2012, emptyList(), 1, false )

        coEvery { vehiculoRepository.getVehiculoById(1) } returns Resource.Success(vehiculo)
        val result =viewModel.getVehiculoById(1)
        advanceUntilIdle()
        assertEquals(vehiculo,result )

    }

    @Test
    fun `Should return a client entity by email`() = runTest {
        val cliente = ClienteDto(1, "12122", "Enmanuel", "Vasquez", "calle mencia", "12121", "braylin@gmail.com", true )
        coEvery { clienteRepository.getClienteByEmail("braylin@gmail.com") } returns Resource.Success(cliente)

        val result =viewModel.getClienteByEmail("braylin@gmail.com")
        assertEquals(cliente, result)
    }

    @Test
    fun `Should return a marca entity by id`()= runTest {
        val marca = MarcaEntity(1,"Toyota")

        coEvery { marcaRepository.getMarcaById(1) } returns Resource.Success(marca)
        val result = viewModel.getMarcaById(1)
        assertEquals(marca, result)
    }

    @Test
    fun `Should return a modelo entity by id`()= runTest {
        val modelo = ModeloEntity(1,1,"Camry")
        coEvery { modeloRepository.getModelosById(1) } returns Resource.Success(modelo)
        val result = viewModel.getModeloById(1)
        assertEquals(modelo, result)

    }

    @Test
    fun `Should return a tipo combustible entity by id`()= runTest {
        val tipoCombustible = TipoCombustibleEntity(1,"Gasolina")
        coEvery { tipoCombustibleRepository.getTipoCombustibleById(1) } returns Resource.Success(tipoCombustible)
        val result = viewModel.getCombustibleById(1)
        assertEquals(tipoCombustible, result)
    }
    @Test
    fun `Should return a tipo vehiculo entity by id`()= runTest {
        val tipoVehiculo = TipoVehiculoEntity(1,"Carro")
        coEvery { tipoVehiculoRepository.getTipoVehiculoById(1) } returns Resource.Success(tipoVehiculo)
        val result = viewModel.getTipoVehiculoById(1)
        assertEquals(tipoVehiculo, result)


    }

}