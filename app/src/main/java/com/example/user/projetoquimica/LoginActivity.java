package com.example.user.projetoquimica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.banco.InformacoesApp;
import com.example.user.banco.UsuarioDB;
import com.example.user.classesDominio.Usuario;

public class LoginActivity extends AppCompatActivity{
    EditText etUsuario, etSenha;
    Button bLogin, bCadastrar, bCancelar;
    UsuarioDB usuarioDB;
    Usuario meuUsuario;
    InformacoesApp informacoesApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etUsuario = findViewById(R.id.etUsuario);
        etSenha = findViewById(R.id.etSenha);
        bLogin = findViewById(R.id.bLogin);
        bCadastrar = findViewById(R.id.bCadastrar);
        bCancelar = findViewById(R.id.bCancelar);
        informacoesApp = (InformacoesApp) getApplicationContext();
        usuarioDB = new UsuarioDB(getApplicationContext());


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etUsuario.getText().toString().equals("")){
                    if(!etSenha.getText().toString().equals("")){

                        String nomeUsuario = etUsuario.getText().toString();
                        String senha = etSenha.getText().toString();

                        //cria o objeto usuario com nomeUsuario e senha
                        meuUsuario = new Usuario(nomeUsuario, senha);
                        //chama o metodo de usuarioDB, passando o objeto criado
                        meuUsuario = usuarioDB.logaUsuario(meuUsuario);
                        //verificando se login está correto
                        if (meuUsuario != null) {
                            //joga o usuario para informacoesApp
                            informacoesApp.setMeuUsuario(meuUsuario);
                            Intent it = new Intent(LoginActivity.this,MenuActivity.class);
                            startActivity(it);
                        } else {
                            Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
                            limpaCampos();
                        }
                    }else{
                        etSenha.setError("Senha do usuário precisa ser preenchido!");
                        etSenha.requestFocus();
                    }
                }else{
                    etUsuario.setError("Usuário precisa ser preenchido!");
                    etUsuario.requestFocus();
                }

            }
        });

        bCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this,CadastroActivity.class);
                startActivity(it);
            }
        });

        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }

    public void limpaCampos(){
        etUsuario.setText("");
        etSenha.setText("");
    }

}
