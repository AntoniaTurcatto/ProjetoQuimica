package com.example.user.projetoquimica;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.componente.NivelConteudoEnum;

import java.util.ArrayList;

public class TelaTeste extends AppCompatActivity {
    Button bSalvarConteudoComNivel;
    Spinner spConteudosComNivel, spNiveisConteudos;
    ConteudoDB conteudoDB;
    InformacoesApp informacoesApp;
    NivelConteudoDB nivelConteudoDB;

    ArrayList<Conteudo> listaConteudos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_teste);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spConteudosComNivel = findViewById(R.id.spConteudosComNivel);
        spNiveisConteudos = findViewById(R.id.spNiveisConteudos);
        bSalvarConteudoComNivel = findViewById(R.id.bSalvarConteudoComNivel);
        listaConteudos = new ArrayList<>();
        conteudoDB = new ConteudoDB(getApplicationContext());
        informacoesApp = (InformacoesApp)getApplicationContext();
        nivelConteudoDB = new NivelConteudoDB(getApplicationContext());
        listaConteudos = conteudoDB.buscaConteudos(informacoesApp.getTipoConteudo());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] nomesConteudos = new String[(listaConteudos.size()) + 1];
        nomesConteudos[0] = "<<Selecionar>>";
        for (int x = 0; x < listaConteudos.size(); x++) {
            Conteudo umConteudo = listaConteudos.get(x);
            nomesConteudos[x + 1] = umConteudo.getNomeConteudo();
        }

        spConteudosComNivel.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomesConteudos));

        bSalvarConteudoComNivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spConteudosComNivel.getSelectedItemPosition() != 0) {
                    if (spNiveisConteudos.getSelectedItemPosition() != 0) {
                        int indice = spConteudosComNivel.getSelectedItemPosition();
                        Conteudo meuConteudo = listaConteudos.get(indice - 1);
                        int nivel = spNiveisConteudos.getSelectedItemPosition();
                        NivelConteudoEnum nivel2 = null;
                        String retornoNivel;
                        NivelConteudo meuNivelConteudo;

                        meuNivelConteudo = nivelConteudoDB.buscaConteudoComNivel(meuConteudo, informacoesApp.getMeuUsuario());
                        Log.d("Teste", "Até aqui foi 1");
                        if (nivel == 1) {
                            nivel2 = NivelConteudoEnum.COBRE;
                        } else if (nivel == 2) {
                            nivel2 = NivelConteudoEnum.BRONZE;
                        } else if (nivel == 3) {
                            nivel2 = NivelConteudoEnum.PRATA;
                        } else if (nivel == 4) {
                            nivel2 = NivelConteudoEnum.OURO;
                        } else if (nivel == 5) {
                            nivel2 = NivelConteudoEnum.DIAMANTE;
                        }
                        Log.d("Teste", "Até aqui foi 2");

                        if (meuNivelConteudo.getNivel() == NivelConteudoEnum.COBRE) {
                            if (nivel2 != NivelConteudoEnum.COBRE) {
                                meuNivelConteudo.setNivel(nivel2);
                                retornoNivel = nivelConteudoDB.insereNivel(meuNivelConteudo);
                            } else {
                                retornoNivel = nivelConteudoDB.deletaNivelConteudo(meuNivelConteudo.getIdNivelConteudo());  //PEDRO - Substituir por altera nível passando cobre para não dar erro ao carregar as listas
                                Toast.makeText(TelaTeste.this, "O conteúdo já está em cobre", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (nivel2 == NivelConteudoEnum.COBRE) {
                                retornoNivel = nivelConteudoDB.deletaNivelConteudo(meuNivelConteudo.getIdNivelConteudo()); //PEDRO - Substituir por altera nível passando cobre para não dar erro ao carregar as listas
                            } else {
                                meuNivelConteudo.setNivel(nivel2);
                                retornoNivel = nivelConteudoDB.alteraNivel(meuNivelConteudo);
                            }
                        }


                        Toast.makeText(TelaTeste.this, retornoNivel, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TelaTeste.this, "Informe o nível!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TelaTeste.this, "Informe o conteúdo!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}