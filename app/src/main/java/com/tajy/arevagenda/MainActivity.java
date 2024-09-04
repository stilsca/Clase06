package com.tajy.arevagenda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> agenda;
    private ArrayAdapter<String> adaptador;
    private ListView lvTelefono;
    private EditText etNombre,etTelefono;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        agenda = new ArrayList<String>();
        recuperar();
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, agenda);

        lvTelefono = (ListView) findViewById(R.id.lvAgenda);
        lvTelefono.setAdapter(adaptador);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etTelefono = (EditText) findViewById(R.id.etTelefono);

        lvTelefono.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int posicion = i;
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                dialogo1.setTitle("Atencion");
                dialogo1.setMessage("Eliminar Registro?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = agenda.get(posicion);
                        StringTokenizer tokenizer = new StringTokenizer(s, ":");
                        String nombre = tokenizer.nextToken().trim();

                        SharedPreferences.Editor elemento = sharedPreferences.edit();
                        elemento.remove(nombre);
                        elemento.apply();

                        agenda.remove(posicion);
                        adaptador.notifyDataSetChanged();
                    }
                });
                dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //NO CODE
                            }
                    });
                dialogo1.show();
                return false;
            }
        });
    }
    public void recuperar(){
        sharedPreferences = getSharedPreferences("AgendaTelefonica", Context.MODE_PRIVATE);
        Map<String, ?> claves = sharedPreferences.getAll();
        for (Map.Entry<String, ?> clave : claves.entrySet()) {
            agenda.add(clave.getKey()+":" + clave.getValue().toString());
        }
    }

    public void agregar(View view){
        if(etNombre.getText().toString().isBlank() || etTelefono.getText().toString().isBlank()){
            Toast.makeText(getApplicationContext(), "No se ingresaron Datos", Toast.LENGTH_LONG).show();
        }else {
            agenda.add(etNombre.getText().toString() + ":"+ etTelefono.getText().toString());
            adaptador.notifyDataSetChanged();

            SharedPreferences.Editor elementos = sharedPreferences.edit();
            elementos.putString(etNombre.getText().toString(), etTelefono.getText().toString());
            elementos.apply();

            limpiar();
        }
    }
    public void limpiar(){
        etNombre.setText(null);
        etTelefono.setText(null);
        etNombre.requestFocus();
    }
}