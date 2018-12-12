package com.example.android.clase10

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.lang.Exception

//se debe hacer una clase CUSTOMSQL
class CustomSQL (val miContexto: Context,
                 val nombre:String,
                 val factory: SQLiteDatabase.CursorFactory?,
                 var version:Int):SQLiteOpenHelper(miContexto,nombre, factory, version){

    override fun onCreate(db: SQLiteDatabase?) {
        //se crea la tabla y se le indica las columnas ID (tipo int) y MENSAJE (tipo txt)
        val query = "CREATE TABLE lista(id INTEGER PRIMARY KEY AUTOINCREMENT,mensaje TEXT)"
        //como podria ser q sea nulo, se pone signo de pregunta para evitarlo
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertar (mensaje:String) {
        try{
            val db = this.writableDatabase
            //forma de crear elementos con clave de valor
            var cv = ContentValues()
            //como se llama la columna y que voy a insertar
            //si el valor es nulo no hace nada
            //la clave q se le dara a la columna debe llamarse igual que la columna
            cv.put("mensaje",mensaje)
            val resultado = db.insert("lista",null,cv)
            db.close()

            //Corroboramos que se haya agregado una nueva fila
            if(resultado != -1L) //si resultado equivale a 1 Long (se resume 1L)
                {
                    Toast.makeText(miContexto,"Agregado",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(miContexto,"No agregado",Toast.LENGTH_SHORT).show()
                }

        }
        catch (e:SQLException) {
            Toast.makeText(miContexto,"${e.message}",Toast.LENGTH_SHORT).show()
            Log.e("sqlInsertar",e.message)
        }
    }


    /*ARRAYLIST STRING
    //se lista array de solamente los mensajes por eso es strinh
    fun listar():ArrayList<String> {

        //se crea var lista que es un arraylist
        var lista = ArrayList<String>()
        try {
            val db = this.writableDatabase
            //se crea var cursor
            var cursor : Cursor? = null
            //se recorre los registros de la tabla con el cursor
            cursor = db.rawQuery("select * from lista", null)
            if (cursor.moveToFirst()) {
                do {
                    val msg = cursor.getString(1)
                    lista.add(msg)
                } while (cursor.moveToNext())
            }
            //se debe siempre cerrar la base
            db.close()
            return lista
        }
        catch (e:SQLException) {
            Toast.makeText(miContexto,"${e.message}",Toast.LENGTH_SHORT).show()
        }

        return lista
    }*/

   // ARRAYLIST REGISTRO
    //se lista array de solamente los mensajes por eso es strinh
    fun listar():ArrayList<Registro> {

        //se crea var lista que es un arraylist
        var lista = ArrayList<Registro>()
        try {
            val db = this.writableDatabase
            //se crea var cursor
            var cursor : Cursor? = null
            //se recorre los registros de la tabla con el cursor
            cursor = db.rawQuery("select * from lista", null)
            if (cursor.moveToFirst()) {
                do {
                    //el cursor recorre las columnas
                    val id = cursor.getInt(0)
                    val msg = cursor.getString(1)
                    val reg = Registro(id,msg)
                    //a la lista se añaden los reg q son las filas de los registros
                    lista.add(reg)
                } while (cursor.moveToNext())
            }
            //se debe siempre cerrar la base
            db.close()
            return lista
        }
        catch (e:SQLException) {
            Toast.makeText(miContexto,"${e.message}",Toast.LENGTH_SHORT).show()
        }

        return lista
    }

    fun eliminar(id:Int){
        try {
            val db = this.writableDatabase
            val args = arrayOf(id.toString())
            val resultado = db.delete("lista", "id=?", args)
            if (resultado == 0) {
                Toast.makeText(miContexto,"mensaje no actualizado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(miContexto,"mensaje eliminado", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:SQLException) {
            Toast.makeText(miContexto,"Error al eliminar ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("sqlEliminar",e.message)
        }
    }

    fun actualizar(registro: Registro) {
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            val args = arrayOf(registro.id.toString())
            cv.put("mensaje",registro.mensaje)
            val resultado = db.update("lista", cv, "id = ?", args)

            db.close()
            if (resultado == 0) {
                Toast.makeText(miContexto,"mensaje no actualizado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(miContexto,"mensaje actualizado", Toast.LENGTH_SHORT).show()
            }
        }

        catch (e:Exception) {
            Toast.makeText(miContexto,"Error al actualizar ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("sqlActualizar",e.message)
        }
    }

}