package com.example.user.projetoquimica;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.PerguntaDB;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.Pergunta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EntradaDePerguntasActivity extends AppCompatActivity {
    ConteudoDB conteudoDB;
    PerguntaDB perguntaDB;
    Button bSalvar, bCancelar, bImagem;
    EditText etEnunciado, etOpcaoA,etOpcaoB,etOpcaoC,etOpcaoD,etOpcaoE;
    Spinner spConteudos, spTestePrevio, spRespostaCerta, spNivelDificuldade, spQuestaoVestibular;
    ArrayList<Conteudo> listaConteudos;
    ImageView imageView;
    InformacoesApp informacoesApp;

    final int request_code_gallery = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada_de_perguntas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bSalvar = findViewById(R.id.bSalvar);
        bCancelar = findViewById(R.id.bCancelar);
        bImagem = findViewById(R.id.bImagem);


        etEnunciado = findViewById(R.id.etEnunciado);
        etOpcaoA = findViewById(R.id.etOpcaoA);
        etOpcaoB = findViewById(R.id.etOpcaoB);
        etOpcaoC = findViewById(R.id.etOpcaoC);
        etOpcaoD = findViewById(R.id.etOpcaoD);
        etOpcaoE = findViewById(R.id.etOpcaoE);
        imageView = findViewById(R.id.imageView);


        spConteudos = findViewById(R.id.spConteudos);
        spTestePrevio = findViewById(R.id.spTestePevio);
        spRespostaCerta = findViewById(R.id.spRespostaCerta);
        spNivelDificuldade = findViewById(R.id.spNivelDificuldade);
        spQuestaoVestibular = findViewById(R.id.spQuestaoVestibular);

        informacoesApp = (InformacoesApp) getApplicationContext();

        listaConteudos = new ArrayList<>();

        conteudoDB = new ConteudoDB(getApplicationContext());
        perguntaDB = new PerguntaDB(getApplicationContext());

        listaConteudos = conteudoDB.buscaConteudos(informacoesApp.getTipoConteudo());



        String[] nomesConteudos = new String[(listaConteudos.size())+1];
        nomesConteudos[0]="<<Selecionar>>";
        for (int x = 0; x < listaConteudos.size(); x++) {
            Conteudo umConteudo = listaConteudos.get(x);
            nomesConteudos[x+1] = umConteudo.getNomeConteudo();
        }

        //VOLTAR A TELA ANTERIOR
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        spConteudos.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,nomesConteudos));


        bImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        EntradaDePerguntasActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        request_code_gallery
                );
            }
        });


        bSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!etEnunciado.getText().toString().equals("")){
                if(!etOpcaoA.getText().toString().equals("")){
                    if (!etOpcaoB.getText().toString().equals("")){
                        if(!etOpcaoC.getText().toString().equals("")){
                            if (!etOpcaoD.getText().toString().equals("")){
                                if(!etOpcaoE.getText().toString().equals("")){
                                    if(spConteudos.getSelectedItemPosition()!=0){
                                        if(spTestePrevio.getSelectedItemPosition()!=0){
                                            if(spRespostaCerta.getSelectedItemPosition()!=0){
                                                if (spNivelDificuldade.getSelectedItemPosition()!=0){
                                                    if (spQuestaoVestibular.getSelectedItemPosition()!=0){

                                                        String enunciado = etEnunciado.getText().toString();
                                                        String opcaoA = etOpcaoA.getText().toString();
                                                        String opcaoB = etOpcaoB.getText().toString();
                                                        String opcaoC = etOpcaoC.getText().toString();
                                                        String opcaoD = etOpcaoD.getText().toString();
                                                        String opcaoE = etOpcaoE.getText().toString();
                                                        int indice = spConteudos.getSelectedItemPosition();
                                                        Conteudo meuConteudo = listaConteudos.get(indice-1);
                                                        int testePrevioSpinnerPosition = spTestePrevio.getSelectedItemPosition();
                                                        int testePrevio = 0;
                                                        if (testePrevioSpinnerPosition != 0){ // não
                                                            if (testePrevioSpinnerPosition == 1){
                                                                testePrevio = 1; //SIM
                                                            }
                                                        }

                                                        byte[] imagem = null;
                                                        //if (imageView != null) {
                                                        //    imagem = imageViewToByte(imageView);
                                                        //}
                                                        String respostaCorreta = (String) spRespostaCerta.getSelectedItem();
                                                        char respostaCerta = respostaCorreta.charAt(0);
                                                        int nivelDificuldade = spNivelDificuldade.getSelectedItemPosition();
                                                        int questaoVestibular = spQuestaoVestibular.getSelectedItemPosition();

                                                        Pergunta umaPergunta = new Pergunta(enunciado, opcaoA, opcaoB, opcaoC, opcaoD, opcaoE, imagem, testePrevio, meuConteudo, respostaCerta, nivelDificuldade, questaoVestibular);
                                                        String retornoPergunta = perguntaDB.inserePergunta(umaPergunta);
                                                        //limpaCampos();

                                                        Toast.makeText(EntradaDePerguntasActivity.this, retornoPergunta, Toast.LENGTH_SHORT).show();

                                                    } else {
                                                        Toast.makeText(EntradaDePerguntasActivity.this, "Informe se a questão é de vestibular ou não!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Toast.makeText(EntradaDePerguntasActivity.this, "Informe o nível de dificuldade da questão!", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(EntradaDePerguntasActivity.this, "Informe a alternativa correta!", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(EntradaDePerguntasActivity.this, "Informe se a pergunta pertence a teste prévio ou não!", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(EntradaDePerguntasActivity.this, "Informe o conteúdo o qual a pergunta pertence!", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    etOpcaoE.setError("Informe a alternativa E da questão!");
                                    etOpcaoE.requestFocus();
                                }
                            }else {
                                etOpcaoD.setError("Informe a alternativa D da questão!");
                                etOpcaoD.requestFocus();
                            }
                        }else {
                            etOpcaoC.setError("Informe a alternativa C da questão!");
                            etOpcaoC.requestFocus();
                        }
                    }else {
                        etOpcaoB.setError("Informe a alternativa B da questão!");
                        etOpcaoB.requestFocus();
                    }
                }else {
                    etOpcaoA.setError("Informe a alternativa A da questão!");
                    etOpcaoA.requestFocus();
                }
            }else {
                etEnunciado.setError("Informe o enunciado da questão!!");
                etEnunciado.requestFocus();
            }
            }
        });

        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }
    ///////////////////  MÉTODOS DE CONVERSÃO DE IMAGEMMM  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[]byteArray = stream.toByteArray();
        return byteArray;
    }

    public Bitmap ByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == request_code_gallery){
            if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent it = new Intent(Intent.ACTION_PICK);
                it.setType("image/*");
                startActivityForResult(it, request_code_gallery);
            }
            else{
                Toast.makeText(this, "Você não tem permissão pra acessar a localização deste arquivo!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == request_code_gallery && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void limpaCampos(){
        etEnunciado.setText("");
        etOpcaoA.setText("");
        etOpcaoB.setText("");
        etOpcaoC.setText("");
        etOpcaoD.setText("");
        etOpcaoE.setText("");
        spConteudos.setSelection(0);
        spTestePrevio.setSelection(0);
        spNivelDificuldade.setSelection(0);
        spRespostaCerta.setSelection(0);
        imageView.setImageResource(R.mipmap.ic_sem_imagem);
        spQuestaoVestibular.setSelection(0);
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
