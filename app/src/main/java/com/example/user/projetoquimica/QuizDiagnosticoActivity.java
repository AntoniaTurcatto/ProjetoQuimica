package com.example.user.projetoquimica;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class QuizDiagnosticoActivity extends AppCompatActivity {
    TextView tvPergunta;
    TextView tvPosicao, tvConteudo;
    RadioButton rbOpcaoA,rbOpcaoB,rbOpcaoC,rbOpcaoD,rbOpcaoE;
    Button bProximo, bAnterior, bFinalizar;
    ArrayList<Pergunta> listaPerguntas;
    ArrayList<Conteudo> listaConteudos;
    ArrayList<NivelConteudo> listaNivelConteudos;
    int quantidade; // quantidade de perguntas por conteúdo -> informação que vem da tela anterior
    NivelConteudoDB nivelConteudoDB;
    RadioGroup rgAlternativa;
    ImageView ivImagemPergunta;
    DesempenhoQuestionarioDB desempenhoQuestionarioDB;
    InformacoesApp informacoesApp;
    ClasseIntermediaria classeIntermediaria;
    ArrayList<Feedback> listaFeedbacks;
    int tipo = 0;

    // matriz que conterá as quantidades totais, e de cada nivel, para cada um dos conteúdos
    // matriz quantidadePerguntasPorConteudo terá nas linhas cada um dos conteúdos e para esses, na coluna 0 a quantidade total de perguntas, na coluna 1 a quantidde de fáceis, na coluna 2 a quantidde de médias e na coluna 3 a quantidade de difícies
    int[][] quantidadePerguntasPorConteudo;

    int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_diagnostico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvPergunta = findViewById(R.id.tvPergunta);
        rbOpcaoA = findViewById(R.id.rbOpcaoA);
        rbOpcaoB = findViewById(R.id.rbOpcaoB);
        rbOpcaoC = findViewById(R.id.rbOpcaoC);
        rbOpcaoD = findViewById(R.id.rbOpcaoD);
        rbOpcaoE = findViewById(R.id.rbOpcaoE);
        bProximo = findViewById(R.id.bProximo);
        bAnterior = findViewById(R.id.bAnterior);
        bFinalizar = findViewById(R.id.bFinalizar);
        rgAlternativa = findViewById(R.id.rgAlternativa);
        tvPosicao = findViewById(R.id.tvPosicao);
        tvConteudo = findViewById(R.id.tvConteudo);
        ivImagemPergunta = findViewById(R.id.ivImagemPergunta);
        informacoesApp = (InformacoesApp) getApplicationContext();

        Intent it = getIntent();

        int indice = 0;

        quantidade = 0;
        if (it != null){
            listaConteudos = (ArrayList<Conteudo>)it.getSerializableExtra("listaConteudos");
            tipo = it.getIntExtra("tipo",0);
            quantidade = it.getIntExtra("quantidade",0);
        }

        nivelConteudoDB = new NivelConteudoDB(getApplicationContext());

        // chamada da nivel conteudo
        listaNivelConteudos = nivelConteudoDB.buscaConteudosComNivel(listaConteudos, informacoesApp.getMeuUsuario());
        String mensagem = "";
        for (int x = 0; x < listaNivelConteudos.size(); x++){
            mensagem = mensagem + "\n Conteudo: " + listaNivelConteudos.get(x).getConteudo().getNomeConteudo() + ", nivel: " + listaNivelConteudos.get(x).getNivel();
        }
        Toast.makeText(informacoesApp, mensagem, Toast.LENGTH_SHORT).show();
        // chamada da classe intermediaria
        // matriz quantidadePerguntasPorConteudo terá nas linhas cada um dos conteúdos e para esses, na coluna 0 a quantidade total de perguntas, na coluna 1 a quantidde de fáceis, na coluna 2 a quantidde de médias e na coluna 3 a quantidade de difícies
        quantidadePerguntasPorConteudo = new int[listaNivelConteudos.size()][4];
        // por enquanto estamos padronizando a mesma quantidade de perguntas para todos os conteudos, futuramente poderiamos permitir informar a quantidade para cada conteúdo
        // para o padrão vamos montar o for abaixo...
        for (int x = 0; x < listaNivelConteudos.size(); x++){
            quantidadePerguntasPorConteudo[x][0] = quantidade;
        }
        classeIntermediaria = new ClasseIntermediaria(getApplicationContext());
        if (tipo == 1) {
            listaPerguntas = classeIntermediaria.carregaQuantPerguntasPorConteudo(listaNivelConteudos, quantidadePerguntasPorConteudo);
        } else if (tipo == 2) {
            listaPerguntas = classeIntermediaria.carregaQuantPerguntasPorConteudoDiagnostico(listaNivelConteudos, quantidadePerguntasPorConteudo);
        }


        if (listaPerguntas.size() > 0) {
            posicao = 0;
            carregaPergunta();
        }

        bProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posicao != (listaPerguntas.size() - 1)){
                    salvaResposta();
                    posicao++;
                    carregaPergunta();
                }else{
                    Toast.makeText(QuizDiagnosticoActivity.this, "Não existem mais perguntas!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posicao != 0){
                    salvaResposta();
                    posicao--;
                    carregaPergunta();
                }else{
                    Toast.makeText(QuizDiagnosticoActivity.this, "Você está na primeira pergunta!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaResposta();
                boolean respondeuTodas = verificaResposta();
                if (respondeuTodas == true) {
                    DesempenhoQuestionario desempenhoQuestionario = null;
                    listaFeedbacks = new ArrayList<>();
                    //carregar a lista de feedbacks (algo parecido com o que fazemos no diagnóstico q precisa consolidar, fazer aqui no quiz para dar um feedback

                    // chamada da classe intermediária
                    if (tipo == 1) { //quiz
                        desempenhoQuestionario = classeIntermediaria.calculaDesempenhoQuestionario(listaNivelConteudos, quantidadePerguntasPorConteudo, listaPerguntas, informacoesApp.getMeuUsuario(), listaFeedbacks);
                        /*Intent it = new Intent(QuizDiagnosticoActivity.this, QuizActivity.class);
                        it.putExtra("listaNivelConteudos", listaNivelConteudos);
                        it.putExtra("listaFeedbacks", listaFeedbacks);
                        startActivity(it);
                        for (int x = 0; x < listaFeedbacks.size(); x++){
                            Log.d("Teste", "QUIZ-DIAG Lista Feedbacks = Conteudo: " + listaFeedbacks.get(x).getConteudo().getNomeConteudo() + ", nivel anterior: " + listaFeedbacks.get(x).getNivelAnterior() + ", nivel atual: " + listaFeedbacks.get(x).getNivelAtual());
                        }*/
                    } else if (tipo == 2) { //diagnóstico (Será que precisa fazer tudo isso que está no IF ? Fábio (01/07/22))
                        desempenhoQuestionario = classeIntermediaria.calculaDesempenhoDiagnostico(listaNivelConteudos, quantidadePerguntasPorConteudo, listaPerguntas, informacoesApp.getMeuUsuario(), listaFeedbacks);
                        // de fato, acho que não precisamos dessa parte!
                        /*
                        int tipoDesempenho = desempenhoQuestionario.getTipoDesempenho();
                        Intent it = new Intent(QuizDiagnosticoActivity.this, FeedbackActivity.class);
                        it.putExtra("listaNivelConteudos", listaNivelConteudos);
                        it.putExtra("listaFeedbacks", listaFeedbacks);
                        it.putExtra("tipoDesempenho", tipoDesempenho);
                        startActivity(it);
                        for (int x = 0; x < listaFeedbacks.size(); x++){
                            Log.d("Teste", "QUIZ-DIAG Lista Feedbacks = Conteudo: " + listaFeedbacks.get(x).getConteudo().getNomeConteudo() + ", nivel anterior: " + listaFeedbacks.get(x).getNivelAnterior() + ", nivel atual: " + listaFeedbacks.get(x).getNivelAtual());
                        }
                        */

                    }

                    // como ele respondeu todos, temos condições de fazer pontuacao final
                    Usuario meuUsuario = informacoesApp.getMeuUsuario();
                    desempenhoQuestionario.setMeuUsuario(meuUsuario);
                    desempenhoQuestionario.calculaPontuacaoFinal();
                    desempenhoQuestionarioDB = new DesempenhoQuestionarioDB(getApplicationContext());
                    desempenhoQuestionarioDB.insereDesempenhoQuestionario(desempenhoQuestionario);
                    //após ter consolidado no banco e estiver no quiz (tipo = 1) chamar tela passando desempenho (e talvez a lista de feedback) e mostrar as eficiencias dos conteúdos
                    //fazer por conteúdo as notas e colocar a nota final do quiz
                    Toast.makeText(QuizDiagnosticoActivity.this, "DESEMPENHO NO QUESTIONARIO\nData: " + desempenhoQuestionario.getData() + ", pontuação final: " + desempenhoQuestionario.getPontuacaoFinal(), Toast.LENGTH_LONG).show();
                    Toast.makeText(QuizDiagnosticoActivity.this, "DESEMPENHO POR CONTEÚDO: ", Toast.LENGTH_SHORT).show();
                    //colocar na activity essas informacoes
                    if(desempenhoQuestionario.getTipoDesempenho() == 1){
                        Intent it = new Intent(QuizDiagnosticoActivity.this, QuizActivity.class);
                        it.putExtra("desempenho", desempenhoQuestionario);
                        it.putExtra("listaFeedback", listaFeedbacks);
                        it.putExtra("listaNivelConteudos", listaNivelConteudos);
                        it.putExtra("listaPerguntas", listaPerguntas);
                        startActivity(it);
                    } else if(desempenhoQuestionario.getTipoDesempenho() == 2){
                        int tipoDesempenho = desempenhoQuestionario.getTipoDesempenho();
                        Intent it = new Intent(QuizDiagnosticoActivity.this, FeedbackActivity.class);
                        it.putExtra("listaNivelConteudos", listaNivelConteudos);
                        it.putExtra("tipoDesempenho", tipoDesempenho);
                        it.putExtra("listaFeedbacks", listaFeedbacks);
                        it.putExtra("listaPerguntas", listaPerguntas);
                        startActivity(it);
                    }

                }
            }
        });

    }

    public Bitmap ByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    public void carregaPergunta() {
        Pergunta minhaPergunta = listaPerguntas.get(posicao);
        tvPergunta.setText(minhaPergunta.getEnunciado());
        rgAlternativa.clearCheck();
        rbOpcaoA.setText(minhaPergunta.getOpcaoA());
        rbOpcaoB.setText(minhaPergunta.getOpcaoB());
        rbOpcaoC.setText(minhaPergunta.getOpcaoC());
        rbOpcaoD.setText(minhaPergunta.getOpcaoD());
        rbOpcaoE.setText(minhaPergunta.getOpcaoE());
        tvPosicao.setText("Questão: " + (posicao+1) + "/ " + listaPerguntas.size());
        tvConteudo.setText("Conteúdo: " + minhaPergunta.getConteudo().getNomeConteudo());

        Bitmap imagem;

        if (minhaPergunta.getImagem() != null){
            ivImagemPergunta.setImageBitmap(null);
            imagem = ByteArrayToBitmap(minhaPergunta.getImagem());
            ivImagemPergunta.setImageBitmap(imagem);
        }else{
            ivImagemPergunta.setImageBitmap(null);
        }

        if(minhaPergunta.getOpcaoEscolhida() != ' '){
            if(minhaPergunta.getOpcaoEscolhida() == 'A'){
                rbOpcaoA.setChecked(true);
            }else if(minhaPergunta.getOpcaoEscolhida() == 'B'){
                rbOpcaoB.setChecked(true);
            } else if (minhaPergunta.getOpcaoEscolhida() == 'C') {
                rbOpcaoC.setChecked(true);
            } else if (minhaPergunta.getOpcaoEscolhida() == 'D') {
                rbOpcaoD.setChecked(true);
            } else if (minhaPergunta.getOpcaoEscolhida() == 'E') {
                rbOpcaoE.setChecked(true);
            }
        }
    }

    public void carregaConteudo() {
        Conteudo meuConteudos = listaConteudos.get(posicao);
        tvPergunta.setText(meuConteudos.getIdConteudo());

        ///tirar????????
        tvPosicao.setText("Questão: " + (posicao + 1) + "/ " + listaPerguntas.size());
        Bitmap imagem;
    }

    public void salvaResposta(){
        char opcaoEscolhida = ' ';
        if(rbOpcaoA.isChecked()){
            opcaoEscolhida = 'A';
        }else if(rbOpcaoB.isChecked()){
            opcaoEscolhida = 'B';
        }else if(rbOpcaoC.isChecked()){
            opcaoEscolhida = 'C';
        }else if(rbOpcaoD.isChecked()){
            opcaoEscolhida = 'D';
        }else if(rbOpcaoE.isChecked()){
            opcaoEscolhida = 'E';
        }

        if(opcaoEscolhida != ' '){
            Pergunta minhaPergunta = listaPerguntas.get(posicao);
            minhaPergunta.setOpcaoEscolhida(opcaoEscolhida);
        }
    }

    public void limpaDados(){
        tvPergunta.setText("");
        rbOpcaoA.setText("");
        rbOpcaoB.setText("");
        rbOpcaoC.setText("");
        rbOpcaoD.setText("");
        rbOpcaoE.setText("");
    }

    public boolean verificaResposta(){
        int NaoRespondida = 0;
        int Respondida = 0;

        boolean respondeuTodas = false;

        ArrayList<Integer> listaQuestoesNaoRespondidas = new ArrayList<>();

        for (int x = 0; x <listaPerguntas.size(); x++){
            Pergunta minhPergunta = listaPerguntas.get(x);

            if(minhPergunta.getOpcaoEscolhida() == ' '){
                NaoRespondida++;
                listaQuestoesNaoRespondidas.add(x + 1);
            }else {
                Respondida++;
            }
        }

        if(listaQuestoesNaoRespondidas.size() > 0){
            String msg = "";
            for(int x = 0; x < listaQuestoesNaoRespondidas.size(); x++){
                msg = msg + (x + 1) + " ";
            }
            Toast.makeText(this, "As seguintes questões não foram respondidas: " + msg, Toast.LENGTH_LONG).show();

            respondeuTodas = false;
        }else{
            respondeuTodas = true;
        }
        return respondeuTodas;
    }
}
