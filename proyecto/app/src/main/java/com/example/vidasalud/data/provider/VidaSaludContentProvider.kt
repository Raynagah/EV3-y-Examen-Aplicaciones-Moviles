package com.example.vidasalud.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

class VidaSaludContentProvider : ContentProvider() {

    // Identificador único para nuestro proveedor
    companion object {
        const val AUTHORITY = "com.example.vidasalud.provider"
        // Simulamos una tabla llamada "salud_publica"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/salud_publica")
    }

    override fun onCreate(): Boolean {
        return true
    }

    // Cuando alguien pide datos, esto responde
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val columnas = arrayOf("_id", "usuario", "pasos", "calorias")
        val cursor = MatrixCursor(columnas)

        cursor.addRow(arrayOf(1, "Usuario Actual", 5500, 1200))
        cursor.addRow(arrayOf(2, "Datos Ayer", 7200, 2100))

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/vnd.com.example.vidasalud.provider.salud_publica"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}