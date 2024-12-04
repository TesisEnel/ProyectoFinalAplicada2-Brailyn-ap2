package ucne.edu.proyectofinalaplicada2.presentation.navigation

import android.os.Bundle
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()
    @Serializable
    data class VehiculoRegistroScreen(val id: Int? = 0): Screen()
    @Serializable
    data object AuthScreen: Screen()
    @Serializable
    data class RentaScreen(val vehiculoId: Int, val rentaId: Int): Screen()
    @Serializable
    data class TipoVehiculoListScreen(val id: Int): Screen()
    @Serializable
    data object RegistroClienteScreen: Screen()
    @Serializable
    data object RentaListScreen: Screen()
    @Serializable
    data object FiltraVehiculo: Screen()
    @Serializable
    data object Settings: Screen()
    @Serializable
    data object ProveedorRegistroScreen: Screen()
    @Serializable

    companion object {
        fun fromRoute(route: String, args: Bundle?): Screen? {
            val subclass = Screen::class.sealedSubclasses.firstOrNull {
                route.contains(it.qualifiedName.toString())
            }
            return subclass?.let { createInstance(it, args) }
        }

        private fun <T : Any> createInstance(kClass: KClass<T>, bundle: Bundle?): T? {
            val constructor = kClass.primaryConstructor
            return constructor?.let {
                val args = it.parameters.associateWith { param ->
                    bundle?.get(param.name)
                }
                it.callBy(args)
            } ?: kClass.objectInstance
        }
    }

}