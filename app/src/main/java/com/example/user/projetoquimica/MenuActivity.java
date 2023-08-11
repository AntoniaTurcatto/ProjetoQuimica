package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.user.banco.InformacoesApp;

public class MenuActivity extends AppCompatActivity {
    ImageView ivMenuLogo;
    Button bMenuOrganica, bMenuInorganica;
    InformacoesApp informacoesApp;
    int tipoConteudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ivMenuLogo = (ImageView)findViewById(R.id.ivMenuLogo);
        bMenuOrganica = findViewById(R.id.bMenuOrganica);
        bMenuInorganica = findViewById(R.id.bMenuInorganica);
        bMenuOrganica.setOnClickListener(trataEventoClique);
        bMenuInorganica.setOnClickListener(trataEventoClique);
        informacoesApp = (InformacoesApp)getApplicationContext();

    }
    // A INORGANICA TBM CHAMARÁ O MENUPRINCIAL???????????????????????????????
    // Por enquanto, sim!!!
    View.OnClickListener trataEventoClique = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == bMenuOrganica.getId()) {
                Intent itMenuPrincipal = new Intent(MenuActivity.this, MenuPrincipalActivity.class);
                startActivity(itMenuPrincipal);
                tipoConteudo = 1;
                informacoesApp.setTipoConteudo(tipoConteudo);
            } else if (v.getId() == bMenuInorganica.getId()) {
                Intent itMenuPrincipal = new Intent(MenuActivity.this, MenuPrincipalActivity.class);
                startActivity(itMenuPrincipal);
                tipoConteudo = 2;
                informacoesApp.setTipoConteudo(tipoConteudo);
            }
        }
    };
}