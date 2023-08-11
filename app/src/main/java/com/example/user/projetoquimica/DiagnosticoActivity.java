package com.example.user.projetoquimica;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.banco.DesempenhoQuestionarioDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.DesempenhoConteudo;
import com.example.user.classesDominio.DesempenhoQuestionario;
import com.example.user.classesDominio.Feedback;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Pergunta;
import com.example.user.classesDominio.Usuario;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class DiagnosticoActivity extends AppCompatActivity {
    TextView tvPerguntaDiagnostico;
    TextView tvPosicaoDiagnostico, tvConteudoDiagnostico;
    RadioButton rbOpcaoADiagnostico, rbOpcaoBDiagnostico ,rbOpcaoCDiagnostico, rbOpcaoDDiagnostico,
            rbOpcaoEDiagnostico;
    Button bProximoDiagnostico, bAnteriorDiagnostico, bFinalizarDiagnostico;
    ArrayList<Pergunta> listaPerguntas;
    ArrayList<Conteudo> listaConteudos;
    ArrayList<NivelConteudo> listaNivelConteudos;
    int quantidade; // quantidade de perguntas por conteúdo -> informação que vem da tela anterior
    NivelConteudoDB nivelConteudoDB;
    RadioGroup rgAlternativaDiagnostico;
    ImageView ivImagemPerguntaDiagnostico;
    DesempenhoQuestionarioDB desempenhoQuestionarioDB;
    InformacoesApp informacoesApp;
    ClasseIntermediaria classeIntermediaria;
    ArrayList<Feedback> listaFeedbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent it = getIntent();

    }
}
