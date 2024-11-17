package ucne.edu.proyectofinalaplicada2.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse


import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import ucne.edu.proyectofinalaplicada2.R
import kotlin.coroutines.cancellation.CancellationException


class GoogleAuthClient(
    private val context: Context,
) {
    private val tag = "GoogleAuthClient: "

    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun isSignedIn():Boolean{
        return firebaseAuth.currentUser != null
    }
    suspend fun signIn(): Boolean{
        if(isSignedIn()){
            return true
        }

        try {
            val result = buildCredentialReqquest()

            return handleSignIn(result)
        }catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            return false
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Boolean {
        val credential = result.credential

        if(
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ){
            try {

                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                println(tag + "Name: ${tokenCredential.displayName}")
                println(tag + "email: ${tokenCredential.id}")
                println(tag + "image: ${tokenCredential.profilePictureUri}")
                val authCredential = GoogleAuthProvider.getCredential(
                    tokenCredential.idToken, null
                )

                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                return authResult.user != null

            }catch (e: GoogleIdTokenParsingException){
                e.printStackTrace()
                return false
            }
        }else{
            return false
        }
    }

    private suspend fun buildCredentialReqquest(): GetCredentialResponse{
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(
                       "138527275305-glcjm6vrapkqeqc83fpd1qdvdmjv6lb6.apps.googleusercontent.com"
                    )
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()
        return credentialManager.getCredential(
            request = request, context = context
        )
    }

    suspend fun signOut(){
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        firebaseAuth.signOut()
    }
}

