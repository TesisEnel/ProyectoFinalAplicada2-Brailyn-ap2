package ucne.edu.proyectofinalaplicada2.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ucne.edu.proyectofinalaplicada2.data.local.database.RentCarDb
import ucne.edu.proyectofinalaplicada2.data.remote.api.RentCarApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    private const val BASE_URL = "https://rentcarapi.azurewebsites.net/"

    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesRentCarApi(moshi: Moshi): RentCarApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RentCarApi::class.java)
    }
    @Provides
    @Singleton
    fun providesRentCarDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            RentCarDb::class.java,
            "RentCarDb"
        ).fallbackToDestructiveMigration().build()



    @Provides
    @Singleton
    fun providesVehiculoDao(rentCarDb: RentCarDb) = rentCarDb.vehiculoDao()
    @Provides
    @Singleton
    fun providesMarcaDao(rentCarDb: RentCarDb) = rentCarDb.marcaDao()
    @Provides
    @Singleton
    fun providesModeloDao(rentCarDb: RentCarDb) = rentCarDb.modeloDao()

    @Provides
    @Singleton
    fun providesProveedorDao(rentCarDb: RentCarDb) = rentCarDb.proveedorDao()

    @Provides
    @Singleton
    fun providesRentaDao(rentCarDb: RentCarDb) = rentCarDb.rentaDao()
    @Provides
    @Singleton
    fun providesTipoCombustibleDao(rentCarDb: RentCarDb) = rentCarDb.tipoCombustibleDao()

    @Provides
    @Singleton
    fun providesTipoVehiculoDao(rentCarDb: RentCarDb) = rentCarDb.tipoVehiculoDao()

    @Provides
    @Singleton
    fun providesClienteDao(rentCarDb: RentCarDb) = rentCarDb.clienteDao()




}