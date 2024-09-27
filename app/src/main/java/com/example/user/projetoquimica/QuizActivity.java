package com.example.user.projetoquimica;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.banco.ConteudoDB;
import com.example.user.banco.DesempenhoConteudoDB;
import com.example.user.banco.DesempenhoQuestionarioDB;
import com.example.user.banco.InformacoesApp;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.banco.PerguntaDB;
import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.DesempenhoConteudo;
import com.example.user.classesDominio.DesempenhoQuestionario;
import com.example.user.classesDominio.Feedback;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Pergunta;
import com.example.user.classesDominio.Usuario;
import com.example.user.componente.DesempenhoFeedbackAdapter;
import com.example.user.componente.NivelConteudoAdapter;
import com.example.user.componente.NivelConteudoEnum;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuizActivity extends AppCompatActivity {
    DesempenhoFeedbackAdapter adapter;
    RecyclerView rvDesempenhoConteudo;
    InformacoesApp informacoesApp;
    ArrayList<Feedback> listaFeedbacks;
    ArrayList<NivelConteudo> listaNivelConteudo;
    ArrayList<DesempenhoConteudo> listaDesempenhoConteudos;
    ArrayList<Pergunta> listaPerguntas;
    Context context;
    TextView tvDesempenhoData, tvDesempenhoPontuacaoFinal, tvTituloDesempenho;
    Button bQuizRelatorio, bQuizProgresso, bQuizFeedbackPosQuiz;
    int acertos = 0, erros = 0;
    float pontuacaoMedia = 0;
    float pontuacaoGeral = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvDesempenhoConteudo = (RecyclerView) findViewById(R.id.rvDesempenhoConteudo);
        tvTituloDesempenho = findViewById(R.id.tvTituloDesempenho);
        tvDesempenhoData = findViewById(R.id.tvDesempenhoData);
        tvDesempenhoPontuacaoFinal = findViewById(R.id.tvDesempenhoPontuacaoFinal);
        bQuizRelatorio = findViewById(R.id.bQuizRelatorio);
        bQuizProgresso = findViewById(R.id.bQuizProgresso);
        bQuizFeedbackPosQuiz = findViewById(R.id.bQuizFeedbackPosQuiz);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        informacoesApp = (InformacoesApp) getApplicationContext();
        Intent it = getIntent();
        context = getApplicationContext();


        if (it != null) {
            DesempenhoQuestionario desempenhoQuestionario = (DesempenhoQuestionario) it.getSerializableExtra("desempenho");
            listaDesempenhoConteudos = desempenhoQuestionario.getListaDesempenhoConteudos();
            listaFeedbacks = (ArrayList<Feedback>)it.getSerializableExtra("listaFeedback");
            listaPerguntas = (ArrayList<Pergunta>)it.getSerializableExtra("listaPerguntas");
            listaNivelConteudo = (ArrayList<NivelConteudo>)it.getSerializableExtra("listaNivelConteudos");
            tvTituloDesempenho.setText(informacoesApp.getMeuUsuario().getNomeUsuario().substring(0,1).toUpperCase() + informacoesApp.getMeuUsuario().getNomeUsuario().substring(1).toLowerCase() + ", confira o seu desempenho:");
            tvDesempenhoPontuacaoFinal.setText(String.valueOf(desempenhoQuestionario.getPontuacaoFinal()));
            Log.d("NovoTeste", "Desempenho: " + listaDesempenhoConteudos.size() + ", nivel: " + listaNivelConteudo.size() + ", feedback: " + listaFeedbacks.size());
            Date data = desempenhoQuestionario.getData();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dataFormatada = dateFormat.format(data);
            tvDesempenhoData.setText(dataFormatada);


            // apenas para teste
            String[] desempenho = new String[desempenhoQuestionario.getListaDesempenhoConteudos().size()];

            for (int x = 0; x < desempenhoQuestionario.getListaDesempenhoConteudos().size(); x++) {
                DesempenhoConteudo desempenhoConteudo = desempenhoQuestionario.getListaDesempenhoConteudos().get(x);
                desempenho[x] = desempenhoConteudo.getPontuacaoConteudo() + " - " + desempenhoConteudo.getQuantidadeAcertos();
                acertos += desempenhoConteudo.getQuantidadeAcertos();
                erros +=desempenhoConteudo.getQuantidadeErros();
                pontuacaoGeral +=desempenhoConteudo.getPontuacaoConteudo();

                /*
                if (desempenhoConteudo.getPontuacaoConteudo() >= 65) {
                    //os parâmetros para criar o objeto
                    NivelConteudo meuNivel = listaNivelConteudo.get(x);
                    Intent i = new Intent(QuizActivity.this, VisualizaUpgradeActivity.class);
                    i.putExtra("nivel", meuNivel);
                    startActivity(i);
                }*/
            }

            pontuacaoMedia = pontuacaoGeral/desempenhoQuestionario.getListaDesempenhoConteudos().size();
            //ALERT DIALOG
            exibirAlertDialogResultados();

            adapter = new DesempenhoFeedbackAdapter(listaDesempenhoConteudos, listaNivelConteudo, listaFeedbacks, trataCliqueItem, context); //NivelConteudoOnClickListener
            rvDesempenhoConteudo.setLayoutManager(new LinearLayoutManager(QuizActivity.this));
            rvDesempenhoConteudo.setItemAnimator(new DefaultItemAnimator());
            rvDesempenhoConteudo.setAdapter(adapter);
        }

        bQuizRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent it = new Intent(QuizActivity.this, RelatorioActivity.class );
            it.putExtra("listaPerguntas", listaPerguntas);
            startActivity(it);
            }
        });
        bQuizProgresso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent it = new Intent(QuizActivity.this, VisualizaProgressoActivity.class);
            it.putExtra("listaConteudo", listaNivelConteudo);
            startActivity(it);
            }
        });

        bQuizFeedbackPosQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent it = new Intent(QuizActivity.this, TelaFeedbackActivity.class);
            ArrayList<Conteudo> listaConteudo = new ArrayList<>();
            for (NivelConteudo nivel: listaNivelConteudo){
                listaConteudo.add(nivel.getConteudo());
            }
            it.putExtra("listaConteudos", listaConteudo);
            startActivity(it);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    DesempenhoFeedbackAdapter.DesempenhoFeedbackOnClickListener trataCliqueItem = new DesempenhoFeedbackAdapter.DesempenhoFeedbackOnClickListener() {
        @Override
        public void onClickDesempenhoFeedback(View view, int position) {
            NivelConteudo nivelConteudo = listaNivelConteudo.get(position);
            Feedback feedback = listaFeedbacks.get(position);
            Intent it = new Intent(QuizActivity.this, VisualizaUpgradeActivity.class);
            it.putExtra("nivel", nivelConteudo);
            it.putExtra("feedback", feedback);
            startActivity(it);
        }
    };




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

    public void exibirAlertDialogResultados() {
        // Create an alert builder
        AlertDialog.Builder alertaBuilder = new AlertDialog.Builder(QuizActivity.this, R.style.Theme_DialogCustomizada);
        alertaBuilder.setTitle("Resultados");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_dialog_pos_quiz, null);
        //forma reduzida
        alertaBuilder.setView(customLayout);

        // add components
        Button btnContinuarDialog;
        TextView tvAcertosDialog, tvErrosDialog, tvPontuacaoDialog;

        btnContinuarDialog = customLayout.findViewById(R.id.btnContinuarDialog);

        tvAcertosDialog = customLayout.findViewById(R.id.tvAcertosDialog);
        tvErrosDialog = customLayout.findViewById(R.id.tvErrosDialog);
        tvPontuacaoDialog = customLayout.findViewById(R.id.tvPontuacaoDialog);

        //definindo caracteristicas dos componentes visuais do xml
        tvAcertosDialog.setText(String.valueOf(acertos));
        tvErrosDialog.setText(String.valueOf(erros));
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        tvPontuacaoDialog.setText(String.valueOf(decimalFormat.format(pontuacaoMedia)));

        // create and show the alert dialog
        final AlertDialog dialog = alertaBuilder.create();
        dialog.show();

        btnContinuarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}