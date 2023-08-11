package com.example.user.projetoquimica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;

public class ResumosActivity extends AppCompatActivity {
    ListView lvVisualizaTexto;
    LinkedList<String> listaTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvVisualizaTexto = findViewById(R.id.lvVisualizaTexto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //por enquanto apenas instanciando a lista
        listaTexto = new LinkedList<>();

        String[] texto = new String[listaTexto.size()];
        for (int x = 0; x < listaTexto.size(); x++) {
            texto[x] = listaTexto.get(x);
        }

        // definindo o adapter e o conteúdo do ListView
        lvVisualizaTexto.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, texto));

        // programando os eventos de click no ListView
        lvVisualizaTexto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

}
