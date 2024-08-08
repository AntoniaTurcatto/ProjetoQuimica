package com.example.user.projetoquimica;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.banco.UsuarioDB;
import com.example.user.classesDominio.Usuario;

public class CadastroActivity extends AppCompatActivity{
    EditText etNomeCompleto, etCpf, etEmail, etNomeUsuario, etSenha;
    Button bCadastrarUsuario, bLimpar;
    UsuarioDB usuarioDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etNomeCompleto = findViewById(R.id.etNomeCompleto);
        etCpf = findViewById(R.id.etCpf);
        etEmail = findViewById(R.id.etEmail);
        etNomeUsuario = findViewById(R.id.etNomeUsuario);
        etSenha = findViewById(R.id.etSenha);
        bCadastrarUsuario = findViewById(R.id.bCadastarUsuario);
        bLimpar = findViewById(R.id.bLimpar);

        usuarioDB = new UsuarioDB(getApplicationContext());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etNomeCompleto.getText().toString().equals("")){
                    if (!etCpf.getText().toString().equals("")){
                        if (!etEmail.getText().toString().equals("")){
                            if (!etNomeUsuario.getText().toString().equals("")){
                                if (!etSenha.getText().toString().equals("")){
                                    //cadastro bem sucedido
                                    String nomeCompleto = etNomeCompleto.getText().toString();
                                    long cpf = Long.parseLong(etCpf.getText().toString());
                                    String email = etEmail.getText().toString();
                                    String nomeUsuario = etNomeUsuario.getText().toString();
                                    String senha = etSenha.getText().toString();


                                    Usuario meuUsuario = new Usuario(nomeCompleto, nomeUsuario, senha, email, cpf);
                                    String retornoUsuario = usuarioDB.insereUsuario(meuUsuario);
                                    limpaCampos();

                                    Toast.makeText(CadastroActivity.this, retornoUsuario, Toast.LENGTH_SHORT).show();

                                } else {
                                    etSenha.setError("Senha do usuário precisa ser preenchido!");
                                    etSenha.requestFocus();
                                }
                            } else {
                                etNomeUsuario.setError("Usuário precisa ser preenchido!");
                                etNomeUsuario.requestFocus();
                            }
                        } else {
                            etNomeCompleto.setError("Nome completo precisa ser preenchido!");
                            etNomeCompleto.requestFocus();
                        }
                    } else {
                        etSenha.setError("Email do usuário precisa ser preenchido!");
                        etSenha.requestFocus();
                    }
                } else {
                    etSenha.setError("CPF do usuário precisa ser preenchido!");
                    etSenha.requestFocus();
                }

            }
        });

        bLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpaCampos();
            }
        });
    }

    public void limpaCampos(){
        etNomeCompleto.setText("");
        etNomeUsuario.setText("");
        etSenha.setText("");
        etCpf.setText("");
        etEmail.setText("");
    }

}
