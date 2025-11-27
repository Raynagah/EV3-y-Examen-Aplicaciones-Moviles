package com.example.vidasalud.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.vidasalud.ui.theme.BotonOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponenteTextField(
    valor: String, // Valor actual del campo de texto
    enValorCambiado: (String) -> Unit, // Callback cuando cambia el texto
    etiqueta: String, // Texto de etiqueta del campo
    esContrasena: Boolean = false, // Si true, oculta el texto (password)
    isError: Boolean = false, // Indica si hay error en el campo
    errorTexto: String? = null, // Mensaje de error opcional
    enabled: Boolean = true // Habilita o deshabilita el campo
) {
    OutlinedTextField(
        value = valor, // Texto mostrado
        onValueChange = enValorCambiado, // Ejecuta al cambiar el texto
        label = { Text(etiqueta) }, // Etiqueta visible
        modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
        shape = RoundedCornerShape(12.dp), // Bordes redondeados
        singleLine = true, // Un solo renglón
        enabled = enabled, // Habilitar/Deshabilitar campo
        isError = isError, // Mostrar estilo de error si aplica
        supportingText = {
            if (isError && errorTexto != null) {
                Text(
                    text = errorTexto, // Mensaje de error bajo el campo
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = BotonOscuro, // Borde cuando está seleccionado
            focusedLabelColor = BotonOscuro, // Color etiqueta seleccionada
            cursorColor = BotonOscuro, // Color del cursor
            containerColor = Color.White // Fondo blanco del campo
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esContrasena) KeyboardType.Password else KeyboardType.Email // Tipo de teclado
        ),
        visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None // Ocultar texto si es contraseña
    )
}
