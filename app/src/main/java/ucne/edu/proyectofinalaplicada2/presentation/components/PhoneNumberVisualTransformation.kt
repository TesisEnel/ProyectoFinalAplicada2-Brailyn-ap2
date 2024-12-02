package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = formatPhoneNumber(text.text)
        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length
            override fun transformedToOriginal(offset: Int): Int = text.text.length
        }
        return TransformedText(AnnotatedString(formatted), offsetMap)
    }
}

fun formatPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() } // Filtrar solo los dígitos
    val builder = StringBuilder()

    for (i in digits.indices) {
        builder.append(digits[i])
        if (i == 2 || i == 5) { // Agregar guiones después del 3er y 6to dígito
            builder.append('-')
        }
    }

    return builder.toString()
}