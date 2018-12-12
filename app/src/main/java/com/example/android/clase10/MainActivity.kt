package com.example.android.clase10
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_lista.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //se crea la variable con la DB
        val myDB = CustomSQL(this,"miDB",null,1)

        btnAdd.setOnClickListener {
        // se usa la funcion y se determina los parametros q se grabarane n la db)
           myDB.insertar(txtNombre.text.toString())
        }

        /*btnUpdate.setOnClickListener {
            val lista = myDB.listar()
            //se crea el adaptador. es simple list porque es solo una fila de datos
            //si fueran muchos mas datos se hace el custom adapter
            val adaptador=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista)
            lvLista.adapter=adaptador
        }*/


        //var item = findViewById(R.id.item_lista)

        btnUpdate.setOnClickListener {
            val lista = myDB.listar()
            //se crea el adaptador. es simple list porque es solo una fila de datos
            //si fueran muchos mas datos se hace el custom adapter
            val adaptador=ArrayAdapter<Registro>(this,android.R.layout.simple_list_item_1,lista)
           lvLista.adapter=adaptador
        }

        btnUpdate.setOnClickListener {
            val lista = myDB.listar()
            lvLista.adapter = CustomAdapter(this,R.layout.item_lista,lista)
        }


    }
    //este custom adapter es para
    class CustomAdapter(val miContexto:Context,
                        val recurso:Int,
                        val lista:ArrayList<Registro>) : ArrayAdapter<Registro>(miContexto,recurso,lista)
    {
        override fun getView(position:Int, convertView: View?,parent: ViewGroup):View
        {
            var v:View = LayoutInflater.from(miContexto).inflate(recurso, null)
            var mensaje = v.findViewById<TextView>(R.id.txtDelete)
            var boton = v.findViewById<Button>(R.id.btnDelete)
            var registro = lista[position]
            mensaje.text = registro.mensaje
            boton.setOnClickListener {
                var alerta = AlertDialog.Builder(miContexto)
                alerta.setTitle("Eliminar")
                alerta.setMessage("Quieres eliminar el registro?")

                alerta.setPositiveButton("si",  {dialog, which ->
                    val db = CustomSQL(miContexto,"myDB",null,1)
                    db.eliminar(registro.id)
                    this.remove(registro)
                })

                alerta.setNegativeButton("no", { dialog, which ->
                    dialog.cancel()
                })
                alerta.show()
            }
            return v
        }
    }
}
