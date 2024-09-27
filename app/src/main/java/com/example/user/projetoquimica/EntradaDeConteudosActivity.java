package com.example.user.projetoquimica;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.classesDominio.Conteudo;

public class EntradaDeConteudosActivity extends AppCompatActivity{
    EditText etNomeConteudo;
    Button bSalvarConteudo, bCancelarConteudo;
    InformacoesApp informacoesApp;

    //ConteudoDB conteudoDB;
    ClasseIntermediaria classeIntermediaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada_de_conteudos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etNomeConteudo = findViewById(R.id.etNomeConteudo);
        bSalvarConteudo = findViewById(R.id.bSalvarConteudo);
        bCancelarConteudo = findViewById(R.id.bCancelarConteudo);
        informacoesApp = (InformacoesApp)getApplicationContext();

        //conteudoDB = new ConteudoDB(getApplicationContext());
        classeIntermediaria = new ClasseIntermediaria(getApplicationContext());
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bSalvarConteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etNomeConteudo.getText().toString().equals("")){
                    String nomeConteudo = etNomeConteudo.getText().toString();

                    Conteudo meuConteudo = new Conteudo(nomeConteudo, informacoesApp.getTipoConteudo());
                    Log.d("Teste", "Tipo do conteudo: " + informacoesApp.getTipoConteudo());

                    String[] retornoConteudo = classeIntermediaria.insereConteudoComNivelConteudoInicial(meuConteudo, informacoesApp.getMeuUsuario());
                    limpaCampos();
                    Toast.makeText(EntradaDeConteudosActivity.this, retornoConteudo[0], Toast.LENGTH_SHORT).show();
                    Toast.makeText(EntradaDeConteudosActivity.this, retornoConteudo[1], Toast.LENGTH_SHORT).show();
                } else {
                    etNomeConteudo.setError("Informe o nome do conteúdo");
                    etNomeConteudo.requestFocus();
                }

            }
        });
        bCancelarConteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }

    public void limpaCampos(){
        etNomeConteudo.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        //QUIMICA ORGANICA
        if(informacoesApp.getTipoConteudo() == 1){
            menu.findItem(R.id.iv_organica_ou_inorganica).setIcon(R.mipmap.organica);
        } else {
            menu.findItem(R.id.iv_organica_ou_inorganica).setIcon(R.mipmap.inorganica);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.iv_organica_ou_inorganica){
            //tipo de quimica (inorganica ou organica) por escrito
            String tipoQuimica;
            //QUIMICA ORGANICA
            if(informacoesApp.getTipoConteudo() == 1){
                tipoQuimica = "Organica";

            } else {
                //QUIMICA INORGANICA
                tipoQuimica = "Inorganica";
            }
            Toast.makeText(informacoesApp, "Você está no modo Química "+ tipoQuimica + "\nCaso deseja trocar volte ao menu de escolha de modo (organica ou inorganica)", Toast.LENGTH_SHORT).show();
        }

        if(id == R.id.action_informacoes){
            Toast.makeText(informacoesApp, "Clicou no item de settings", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
