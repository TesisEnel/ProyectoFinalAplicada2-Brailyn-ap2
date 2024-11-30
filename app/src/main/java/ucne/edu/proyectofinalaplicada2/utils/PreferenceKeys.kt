package ucne.edu.proyectofinalaplicada2.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val currentUser = stringPreferencesKey("current_user")
    val role = booleanPreferencesKey("role")
}