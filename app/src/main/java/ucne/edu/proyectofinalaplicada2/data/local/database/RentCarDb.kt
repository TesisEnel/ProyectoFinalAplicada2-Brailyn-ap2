package ucne.edu.proyectofinalaplicada2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ucne.edu.proyectofinalaplicada2.Converter
import ucne.edu.proyectofinalaplicada2.data.local.dao.ClienteDao
import ucne.edu.proyectofinalaplicada2.data.local.dao.MarcaDao
import ucne.edu.proyectofinalaplicada2.data.local.dao.ModeloDao
import ucne.edu.proyectofinalaplicada2.data.local.dao.ProveedorDao
import ucne.edu.proyectofinalaplicada2.data.local.dao.RentaDao
import ucne.edu.proyectofinalaplicada2.data.local.dao.TipoCombustibleDao
import ucne.edu.proyectofinalaplicada2.data.local.dao.TipoVehiculoDao
import ucne.edu.proyectofinalaplicada2.data.local.dao.VehiculoDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.ClienteEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ProveedorEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.RentaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity

@Database(
    entities = [
        ClienteEntity::class,
        MarcaEntity::class,
        ModeloEntity::class,
        ProveedorEntity::class,
        RentaEntity::class,
        TipoCombustibleEntity::class,
        TipoVehiculoEntity::class,
        VehiculoEntity::class
    ],
    version = 4,
    exportSchema = false
)

@TypeConverters(Converter::class)
abstract class RentCarDb : RoomDatabase() {
    abstract fun clienteDao(): ClienteDao
    abstract fun marcaDao(): MarcaDao
    abstract fun modeloDao(): ModeloDao
    abstract fun proveedorDao(): ProveedorDao
    abstract fun rentaDao(): RentaDao
    abstract fun tipoCombustibleDao(): TipoCombustibleDao
    abstract fun tipoVehiculoDao(): TipoVehiculoDao
    abstract fun vehiculoDao(): VehiculoDao

}