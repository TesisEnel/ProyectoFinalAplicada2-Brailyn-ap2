package ucne.edu.proyectofinalaplicada2.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import kotlinx.coroutines.tasks.await
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.presentation.authentication.AuthState
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource // Inyectamos el repositorio de cliente
) {

    // Login con Flow
    fun login(email: String, password: String): Flow<Resource<AuthState>> = flow {
        if (email.isBlank() || password.isBlank()) {
            emit(Resource.Error("Email or password can't be empty"))
            return@flow
        }

        emit(Resource.Loading())

        try {
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()

            val user = authResult.user
            if (user != null) {
                emit(Resource.Success(AuthState.Authenticated))
            } else {
                emit(Resource.Error("User is null after successful authentication"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Invalid email or password"))
        }
    }

    // Signup con Flow
    fun signup(email: String, password: String): Flow<Resource<AuthState>> = flow {
        if (email.isBlank() || password.isBlank()) {
            emit(Resource.Error("Email or password can't be empty"))
            return@flow
        }

        emit(Resource.Loading())

        try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            val user = authResult.user
            if (user != null) {

                emit(Resource.Success(AuthState.Authenticated))
            } else {
                emit(Resource.Error("User is null after successful registration"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Signup failed"))
        }
    }

    // Signout
    fun signout(): Flow<Resource<AuthState>> = flow {
        emit(Resource.Loading())
        try {
            FirebaseAuth.getInstance().signOut()
            emit(Resource.Success(AuthState.Unauthenticated))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Signout failed"))
        }
    }
}