package com.example.user.classesDominio;

import android.content.Context;
import android.icu.util.DateInterval;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.user.banco.Conexao;
import com.example.user.banco.ConteudoDB;
import com.example.user.banco.DesempenhoConteudoDB;
import com.example.user.banco.NivelConteudoDB;
import com.example.user.banco.PerguntaDB;
import com.example.user.componente.NivelConteudoEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClasseIntermediaria {
    Context context;
    NivelConteudoDB nivelConteudoDB;

    public ClasseIntermediaria(Context context) {
        this.context = context;
    }

    public ArrayList<Pergunta> carregaQuantPerguntasPorConteudo(ArrayList<NivelConteudo> listaNivelConteudos, int[][] quantidadePerguntasPorConteudo, int testePrevio) {
        ArrayList<Pergunta> listaPerguntas;

        for (int x = 0; x < listaNivelConteudos.size(); x++) {
            NivelConteudo meuNivelConteudo = listaNivelConteudos.get(x);

            int quantidadeFaceis = 0;
            int quantidadeMedias = 0;
            int quantidadeDificeis = 0;

            Log.d("Teste", "Nivel: " + meuNivelConteudo.getNivel());
            if (meuNivelConteudo.getNivel() == NivelConteudoEnum.COBRE) {
                quantidadeFaceis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.7f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - quantidadeFaceis;
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.BRONZE) {
                quantidadeFaceis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.5f);
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.1f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - (quantidadeFaceis + quantidadeDificeis);
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.PRATA) {
                quantidadeFaceis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.3f);
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.3f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - (quantidadeFaceis + quantidadeDificeis);
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.OURO) {
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.5f);
                quantidadeFaceis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.1f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - (quantidadeDificeis + quantidadeFaceis);
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.DIAMANTE) {
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.7f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - quantidadeDificeis;
            }
            quantidadePerguntasPorConteudo[x][1] = quantidadeFaceis;
            quantidadePerguntasPorConteudo[x][2] = quantidadeMedias;
            quantidadePerguntasPorConteudo[x][3] = quantidadeDificeis;
            Log.d("Teste", "Conteudo: " + x + "-" + listaNivelConteudos.get(x).getConteudo().getNomeConteudo() + ": Total " + quantidadePerguntasPorConteudo[x][0] + ", faceis " + quantidadePerguntasPorConteudo[x][1] + ", médias " + quantidadePerguntasPorConteudo[x][2] + ", dificeis " + quantidadePerguntasPorConteudo[x][3]);
        }
        PerguntaDB perguntaDB = new PerguntaDB(this.context);
        //listaPerguntas = perguntaDB.buscaPerguntasPorConteudosUnionAll(listaNivelConteudos, quantidadePerguntasPorConteudo);

        if (testePrevio == 0){// sem teste previo
            listaPerguntas = perguntaDB.buscaPerguntasPorConteudosUnionAll(listaNivelConteudos, quantidadePerguntasPorConteudo);
        } else { // com teste previo
            listaPerguntas = perguntaDB.buscaPerguntasPorConteudosUnionAllComTestePrevio(listaNivelConteudos, quantidadePerguntasPorConteudo);
        }

        return listaPerguntas;
    }

    /*public DesempenhoQuestionario ClassificaQuantitativoConteudo (Conteudo meuConteudo, DesempenhoQuestionario meuDesempenhoQuestionario){
        if(meuConteudo.getTipoConteudo() < 4 ){
            calculaDesempenhoQuestionario(meuConteudo);
        }
    }*/

    public DesempenhoQuestionario calculaDesempenhoQuestionario(ArrayList<NivelConteudo> listaNivelConteudos, int[][] quantidadePerguntasPorConteudo, ArrayList<Pergunta> listaPerguntas, Usuario meuUsuario, ArrayList<Feedback> listaFeedbacks) {
        int acertos = 0;
        int erros = 0;
        nivelConteudoDB = new NivelConteudoDB(context);
        ArrayList<NivelConteudo> listaNivelConteudoParaAtualizar = new ArrayList<>();
        ArrayList<NivelConteudo> listaNivelConteudoParaDecair = new ArrayList<>();

        // criando o objeto desempenho questionario para adicionar os desempenhos por conteudo
        // testar se o metodo "System...." volta uma data ou data com hora
        Date dataAtual = new Date(System.currentTimeMillis());
        Log.d("Teste", "data = " + dataAtual);
        DesempenhoQuestionario desempenhoQuestionario = new DesempenhoQuestionario
                (dataAtual, 1, meuUsuario); //recebe 1 no tipo desempenho pois é um quiz

        int inicio = 0;

        // navegando na lista de conteúdos para saber as quantidades e também os níveis de cada um
        for (int indice = 0; indice < listaNivelConteudos.size(); indice++) {
            // obtendo o nivel conteudo
            NivelConteudo meuNivelConteudo = listaNivelConteudos.get(indice);
            //definindo a data do último quiz/teste realizado (esse)
            meuNivelConteudo.setDataUltimoTeste(desempenhoQuestionario.getData());
            // definindo os valores de faceis, medias e dificeis de acordo com o nível que o usuário está
            float valorFaceis = 0;
            float valorMedias = 0;
            float valorDificeis = 0;
            //TESTE PRA NÂO DIVIDIR POR ZERO - PEDRO
            if (quantidadePerguntasPorConteudo[indice][1] == 0){
                quantidadePerguntasPorConteudo[indice][1] = 1;
            } else if ( quantidadePerguntasPorConteudo[indice][2] == 0){
                quantidadePerguntasPorConteudo[indice][2] = 1;
           } else if (quantidadePerguntasPorConteudo[indice][3] == 0){
               quantidadePerguntasPorConteudo[indice][3] = 1;
           }
            /*float percentualAuxiliar;*/
            if (meuNivelConteudo.getNivel() == NivelConteudoEnum.COBRE) { //PEDRO - o problema com as notas acontece pois estamos dividindo int por int
                valorFaceis = 68f / quantidadePerguntasPorConteudo[indice][1];
                valorMedias = 32f / quantidadePerguntasPorConteudo[indice][2];
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.BRONZE) {
                valorFaceis = 45f / quantidadePerguntasPorConteudo[indice][1];
                valorMedias = 35f / quantidadePerguntasPorConteudo[indice][2];
                valorDificeis = 20f / quantidadePerguntasPorConteudo[indice][3];
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.PRATA) {
                valorFaceis = 30f / quantidadePerguntasPorConteudo[indice][1];
                valorMedias = 40f / quantidadePerguntasPorConteudo[indice][2];
                valorDificeis = 30f / quantidadePerguntasPorConteudo[indice][3];
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.OURO) {
                valorFaceis = 20f / quantidadePerguntasPorConteudo[indice][1];
                valorMedias = 35f / quantidadePerguntasPorConteudo[indice][2];
                valorDificeis = 45f / quantidadePerguntasPorConteudo[indice][3];
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.DIAMANTE) {
                valorMedias =  32f  / quantidadePerguntasPorConteudo[indice][2];
                valorDificeis = 68f / quantidadePerguntasPorConteudo[indice][3];
            }
            Log.d("Teste", "Percentual valor fáceis: " + valorFaceis + ", percentual médias: " + valorMedias + ", percentual díficeis: " + valorDificeis);
            float pontuacaoConteudo = 0; //PEDRO - Talvez mudar para double

            // fazendo o controle para saber até onde ele deve ir nas perguntas para esse determinado conteúdo
            int fim = inicio + quantidadePerguntasPorConteudo[indice][0];
            // navegando nas perguntas do conteúdo em questão
            for (int x = inicio; x < fim ; x++) {
                // obtendo a pergunta do conteúdo
                Pergunta minhaPergunta = listaPerguntas.get(indice);

                // verificando se acertou ou não
                if (minhaPergunta.getOpcaoEscolhida() == minhaPergunta.getAlternativaCorreta()) {
                    // acertou
                    acertos++;
                    // verificando a dificuldade para saber o valor a ser acrescido (pesos diferentes)
                    if (minhaPergunta.getNivelDificuldade() == 1) {
                        pontuacaoConteudo = pontuacaoConteudo + valorFaceis;
                    } else if (minhaPergunta.getNivelDificuldade() == 2) {
                        pontuacaoConteudo = pontuacaoConteudo + valorMedias;
                    } else if (minhaPergunta.getNivelDificuldade() == 3) {
                        pontuacaoConteudo = pontuacaoConteudo + valorDificeis;
                    }
                } else {
                    // errou
                    erros++;
                }

                float media = 0;
                /*TESTE*/
                /*if(meuConteudo.getQuantidade() == 0){
                    meuConteudo.setQuantidade(1);
                } else if(meuConteudo.getQuantidade() <= 5){
                    int quantidade = 1;
                    meuConteudo.calculaPrecisao(meuConteudo, pontuacaoConteudo);
                    quantidade++;
                    meuConteudo.setQuantidade(quantidade);
                } else if(meuConteudo.getQuantidade() > 5){
                    if(meuConteudo.getPorcentagemNivel() >=0 || meuConteudo.getPorcentagemNivel() <=100){
                        float soma = meuConteudo.getPrecisao();
                        if(pontuacaoConteudo < 65){
                            soma = - soma;
                        }
                        if((meuConteudo.getPorcentagemNivel() + soma) >= 0 && (meuConteudo.getPorcentagemNivel() + soma) < 100){
                            meuConteudo.setPorcentagemNivel((int) (meuConteudo.getPorcentagemNivel() + soma));
                        } else if((meuConteudo.getPorcentagemNivel() + soma) > 100){
                            float somaFinal = soma - meuConteudo.getPorcentagemNivel();
                            // passou para o proximo nivel com essa soma final de pontos no proximo nivel
                        } else if ((meuConteudo.getPorcentagemNivel() + soma) < 0){
                            if(meuConteudo.getCaiuNivel() == 0){
                                meuConteudo.setCaiuNivel(1);
                                meuConteudo.setPorcentagemNivel(0);
                            } else {
                                if(meuNivelConteudo.getNivel() == NivelConteudoEnum.COBRE){
                                    // fica no cobre com 0 pontos
                                } else {
                                    float somaFinal = 100 + soma;
                                    // caiu nivel e ficou com essa soma final de pontos no nivel anterior
                                }
                            }
                        }
                    }
                }*/
            }

            Log.d("Teste", "Pontuação conteúdo: " + pontuacaoConteudo);
            // atualizando o novo início
            inicio = fim;

            DesempenhoConteudoDB desempenhoConteudoDB = new DesempenhoConteudoDB(context);

            //divide a soma de todos os acertos em um conteúdo, nos últimos 3 questionários,
            // e divide pela quantidade de perguntas que esse conteúdo teve nesses questionários.
            ArrayList<DesempenhoConteudo> listaUltimos3DesempenhosConteudos = desempenhoConteudoDB.buscaUltimos3DesempenhosConteudosComConteudo(meuNivelConteudo.getConteudo().getIdConteudo());
            float mediaAcertosUltimos3 = 0.0f;
            if (listaUltimos3DesempenhosConteudos.size()>=3){
                mediaAcertosUltimos3 = (float)acertos + (float)listaUltimos3DesempenhosConteudos.get(0).getQuantidadeAcertos() + (float)listaUltimos3DesempenhosConteudos.get(1).getQuantidadeAcertos()
                        /(quantidadePerguntasPorConteudo[indice][0] + listaUltimos3DesempenhosConteudos.get(0).getQuantidadePerguntas() + listaUltimos3DesempenhosConteudos.get(1).getQuantidadePerguntas());

            }

            DesempenhoConteudo desempenhoConteudo = new DesempenhoConteudo(meuNivelConteudo.getConteudo(), quantidadePerguntasPorConteudo[indice][0], acertos, erros, pontuacaoConteudo,mediaAcertosUltimos3);
            desempenhoConteudoDB.insereDesempenhoConteudo(desempenhoConteudo);
            meuNivelConteudo.setUltimoDesempenhoConteudo(desempenhoConteudo);
            //nivelConteudoDB.atualizaDesempenhoConteudo(meuNivelConteudo, meuUsuario);
            // adicionando no desempenho do questionário
            desempenhoQuestionario.getListaDesempenhoConteudos().add(desempenhoConteudo);

            // reiniciando os acertos e erros
            acertos = 0;
            erros = 0;
            //mediaAcertosUltimos3 = 0f;

            // verificando se pontuação conteúdo fez com que devesse pular de nível
            Log.d("Teste", "Nível antigo: " + meuNivelConteudo.getNivel());
            //montar o feedback (parecido com o caso de desempenho entre 40 e 70 do diagnostico)
            if (pontuacaoConteudo >= 65) {
                if (meuNivelConteudo.getTentativas() >= 2){ //PEDRO - Apto a passar de nível
                    Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel());
                    NivelConteudoEnum novoNivel = meuNivelConteudo.obtemIncrementoUmNivel();
                    meuFeedback.setNivelAtual(novoNivel);
                    int retorno = 0;
                    if (novoNivel != null) { //sinal de que pulou um nível
                        retorno = 1;
                    }
                    meuFeedback.setNiveisAvancados(retorno);
                    listaFeedbacks.add(meuFeedback);
                    retorno = meuNivelConteudo.incrementaUmNivel();
                    if (retorno == -1) {
                        Toast.makeText(context, "Erro ao atualizar nível!", Toast.LENGTH_SHORT).show();
                    } else if (retorno == 1) {
                        meuNivelConteudo.setDataAtualizacaoNivel(desempenhoQuestionario.getData());
                        listaNivelConteudoParaAtualizar.add(meuNivelConteudo);
                        Toast.makeText(context, "Parabéns!!! Você pulou no conteúdo: " + meuNivelConteudo.getConteudo().getNomeConteudo() + " para o nível: " + meuNivelConteudo.getNivel(), Toast.LENGTH_SHORT).show();
                        meuNivelConteudo.setTentativas(0);
                        meuNivelConteudo.setVidas(5);
                        Log.d("Tentativas","Tentativas Intermediarias: " + meuNivelConteudo.getTentativas());
                    } else if (retorno == 0) {
                        Toast.makeText(context, "Parabéns!!! Você ja completou todos os níveis para o conteúdo: " + meuNivelConteudo.getConteudo().getNomeConteudo() + ". Agora você pode continuar praticando! \n A sua nota foi " + pontuacaoConteudo + ". Será que você consegue se superar?", Toast.LENGTH_SHORT).show();
                        meuNivelConteudo.setTentativas(0);
                        meuNivelConteudo.setVidas(5);
                        Log.d("TesteVidas", "Completou todos os niveis Vidas: " + meuNivelConteudo.getVidas());
                    }

                } else{ //Pedro - Não obteve três notas acima da média, então ainda não está apto a passar de nível
                    meuNivelConteudo.setTentativas(meuNivelConteudo.getTentativas()+1);
                    Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel(), meuNivelConteudo.getNivel(), 0 );
                    listaFeedbacks.add(meuFeedback);
                    Toast.makeText(context, "Parabéns!!! Você teve um desempenho acima da média, caso consiga realizar isso mais "+(3- meuNivelConteudo.getTentativas())+" Você passará de nível no conteúdo", Toast.LENGTH_SHORT).show();

                }
            } else if (pontuacaoConteudo <65){ //Pedro - TRATAMENTO PARA NOTA ABAIXO DA MÉDIA
                if (meuNivelConteudo.getVidas() > 1){ //Pedro - Ainda não zerou todas as vidas então não decai de nível
                    if (pontuacaoConteudo<=10){ //PEDRO - Número de vidas é decrementado em dois por conta do desempenho menor ou igual a 10
                        meuNivelConteudo.setVidas(meuNivelConteudo.getVidas()-2);
                        Log.d("TesteVidas", "-2 Vidas: " + meuNivelConteudo.getVidas());
                        System.out.println("Vidas: "+meuNivelConteudo.getVidas());
                        Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel(), meuNivelConteudo.getNivel(), 0);
                        listaFeedbacks.add(meuFeedback);
                        Toast.makeText(context, "Sua nota foi abaixo ou igual a 10, você perdeu duas vidas, você tem mais "+(meuNivelConteudo.getVidas())+" vidas", Toast.LENGTH_SHORT).show();
                    } else{ //Pedro - Número de vidas é decrementado em 1
                        meuNivelConteudo.setVidas(meuNivelConteudo.getVidas()-1);
                        System.out.println("Vidas: "+meuNivelConteudo.getVidas());
                        Log.d("TesteVidas", "-1 Vidas: " + meuNivelConteudo.getVidas());
                        Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel(), meuNivelConteudo.getNivel(), 0);
                        listaFeedbacks.add(meuFeedback);
                        Toast.makeText(context, "Você tirou abaixo de 65, você perdeu uma vida, você tem mais "+ (meuNivelConteudo.getVidas())+" vidas", Toast.LENGTH_SHORT).show();
                    }

                } else { //PEDRO - Decai um nível direto pois já zerou ou irá zerar o número de vidas
                    meuNivelConteudo.setTentativas(0); //PEDRO - Zera as tentativas pois desceu de nível
                    meuNivelConteudo.setVidas(5); //PEDRO - Restaura o número base vidas (4) pois desceu de nível
                    Log.d("TesteVidas", "Decai nivel Vidas: " + meuNivelConteudo.getVidas());
                    Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel());
                    NivelConteudoEnum novoNivel = meuNivelConteudo.obtemDecaiUmNivel();
                    meuFeedback.setNivelAtual(novoNivel);
                    int retorno = 0;
                    if (novoNivel != null){ //PEDRO - Sinal de que decaiu um nivel
                        retorno = -1;
                    }
                    meuFeedback.setNiveisAvancados(retorno);
                    listaFeedbacks.add(meuFeedback);
                    retorno = meuNivelConteudo.decaiUmNivel();
                    if (retorno == -1){
                        Toast.makeText(context, "Erro ao atualizar o nível!", Toast.LENGTH_SHORT).show();
                    } else if ( retorno == 1){
                        meuNivelConteudo.setDataAtualizacaoNivel(desempenhoQuestionario.getData());

                        listaNivelConteudoParaDecair.add(meuNivelConteudo);
                        Toast.makeText(context, "Que pena, infelizmente você decaiu no conteúdo: " + meuNivelConteudo.getConteudo().getNomeConteudo() + " para o nível: " + meuNivelConteudo.getNivel()+" seu número de vidas foi restaurado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Parece que você está com dificuldades, gostaria de ler o material de apoio?", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            else { // precisa indicar no FeedBack que não teve avanço
                Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel(), meuNivelConteudo.getNivel(), 0);
                listaFeedbacks.add(meuFeedback);

            }
            //PEDRO - Atualizando o número de tentativas no banco
            nivelConteudoDB.atualizaTentativas(meuNivelConteudo, meuUsuario);
            //PEDRO - Atualizando o número de vidas no banco
            nivelConteudoDB.atualizaVidas(meuNivelConteudo, meuUsuario);


            Log.d("Teste", "Nível novo: " + meuNivelConteudo.getNivel());

            Log.d("Tentativas","Tentativas Atualiza: " + meuNivelConteudo.getTentativas());

            Log.d("TesteVidas", "Atualiza Vidas: " + meuNivelConteudo.getVidas());

        }

        //atualizando datas de todos os nivelConteudo
        NivelConteudoDB nivelConteudoDB = new NivelConteudoDB(context);
        nivelConteudoDB.atualizaDatas(listaNivelConteudos, meuUsuario);

        // verificando se existem conteúdos a serem atualizados os níveis no banco
        if (listaNivelConteudoParaAtualizar.size() > 0) {

            nivelConteudoDB.incrementaNivel(listaNivelConteudoParaAtualizar, meuUsuario);
        }
        if (listaNivelConteudoParaDecair.size() > 0){

            nivelConteudoDB.decaiNivel(listaNivelConteudoParaDecair, meuUsuario);
        }

        nivelConteudoDB.atualizaDesempenhoConteudo(listaNivelConteudos, desempenhoQuestionario);
        return desempenhoQuestionario;
    }

    public ArrayList<Pergunta> carregaQuantPerguntasPorConteudoDiagnostico(ArrayList<NivelConteudo> listaNivelConteudos, int[][] quantidadePerguntasPorConteudo, int testePrevio) {
        ArrayList<Pergunta> listaPerguntas;

        for (int x = 0; x < listaNivelConteudos.size(); x++) {
            NivelConteudo meuNivelConteudo = listaNivelConteudos.get(x);

            int quantidadeFaceis = 0;
            int quantidadeMedias = 0;
            int quantidadeDificeis = 0;

            Log.d("Teste", "Nivel: " + meuNivelConteudo.getNivel());

            if (meuNivelConteudo.getNivel() == NivelConteudoEnum.COBRE) {
                // nesse caso aplica-se as regras do nível prata
                quantidadeFaceis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.3f);
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.3f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - (quantidadeFaceis + quantidadeDificeis);
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.BRONZE) {
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.5f);
                quantidadeFaceis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.1f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - (quantidadeDificeis + quantidadeFaceis);
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.PRATA) {
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.7f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - quantidadeDificeis;
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.OURO) {
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.7f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - quantidadeDificeis;
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.DIAMANTE) {
                quantidadeDificeis = Math.round(quantidadePerguntasPorConteudo[x][0] * 0.7f);
                quantidadeMedias = quantidadePerguntasPorConteudo[x][0] - quantidadeDificeis;
            }
            quantidadePerguntasPorConteudo[x][1] = quantidadeFaceis;
            quantidadePerguntasPorConteudo[x][2] = quantidadeMedias;
            quantidadePerguntasPorConteudo[x][3] = quantidadeDificeis;
            Log.d("Teste", "Conteudo: " + x + "-" + listaNivelConteudos.get(x).getConteudo().getNomeConteudo() + ": Total " + quantidadePerguntasPorConteudo[x][0] + ", faceis " + quantidadePerguntasPorConteudo[x][1] + ", médias " + quantidadePerguntasPorConteudo[x][2] + ", dificeis " + quantidadePerguntasPorConteudo[x][3]);
        }
        PerguntaDB perguntaDB = new PerguntaDB(this.context);
        //listaPerguntas = perguntaDB.buscaPerguntasPorConteudosUnionAll(listaNivelConteudos, quantidadePerguntasPorConteudo);

        if (testePrevio == 0){// sem teste previo
            listaPerguntas = perguntaDB.buscaPerguntasPorConteudosUnionAll(listaNivelConteudos, quantidadePerguntasPorConteudo);
        } else { // com teste previo
            listaPerguntas = perguntaDB.buscaPerguntasPorConteudosUnionAllComTestePrevio(listaNivelConteudos, quantidadePerguntasPorConteudo);
        }

        return listaPerguntas;
    }


    public DesempenhoQuestionario calculaDesempenhoDiagnostico(ArrayList<NivelConteudo> listaNivelConteudos, int[][] quantidadePerguntasPorConteudo, ArrayList<Pergunta> listaPerguntas, Usuario meuUsuario, ArrayList<Feedback> listaFeedbacks) {
        int acertos = 0;
        int erros = 0;

        // criando o objeto desempenho questionario para adicionar os desempenhos por conteudo
        // testar se o metodo "System...." volta uma data ou data com hora
        Date dataAtual = new Date(System.currentTimeMillis());
        Log.d("Teste", "data = " + dataAtual);
        DesempenhoQuestionario desempenhoQuestionario = new DesempenhoQuestionario
                (dataAtual, 2, meuUsuario); // recebe 2 no tipo desempenho pois é um diagnóstico

        int inicio = 0;

        // navegando na lista de conteúdos para saber as quantidades e também os níveis de cada um
        for (int indice = 0; indice < listaNivelConteudos.size(); indice++) {
            // obtendo o nivel conteudo
            NivelConteudo meuNivelConteudo = listaNivelConteudos.get(indice);

            // definindo os valores de faceis, medias e dificeis de acordo com o nível que o usuário está
            float valorFaceis = 0;
            float valorMedias = 0;
            float valorDificeis = 0;

            if (meuNivelConteudo.getNivel() == NivelConteudoEnum.COBRE) {
                // nesse caso aplica-se a pontuação conforme o nível Prata
                valorFaceis = 30f / quantidadePerguntasPorConteudo[indice][1];
                valorMedias = 40f / quantidadePerguntasPorConteudo[indice][2];
                valorDificeis = 30f / quantidadePerguntasPorConteudo[indice][3];
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.BRONZE) {
                valorFaceis = 20f / quantidadePerguntasPorConteudo[indice][1];
                valorMedias = 35f / quantidadePerguntasPorConteudo[indice][2];
                valorDificeis = 45f / quantidadePerguntasPorConteudo[indice][3];
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.PRATA) {
                valorMedias = 32f / quantidadePerguntasPorConteudo[indice][2];
                valorDificeis = 68f / quantidadePerguntasPorConteudo[indice][3];
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.OURO) {
                valorMedias = 32f / quantidadePerguntasPorConteudo[indice][2]; //Pedro - alterei pra tentar resolver as notas
                valorDificeis = 68f / quantidadePerguntasPorConteudo[indice][3]; //Pedro - alterei pra tentar resolver as notas
            } else if (meuNivelConteudo.getNivel() == NivelConteudoEnum.DIAMANTE) {
                valorMedias = 32f / quantidadePerguntasPorConteudo[indice][2]; //Pedro - alterei pra tentar resolver as notas
                valorDificeis = 68f / quantidadePerguntasPorConteudo[indice][3]; //Pedro - alterei pra tentar resolver as notas
            }
            Log.d("Teste", "Percentual valor fáceis: " + valorFaceis + ", percentual médias: " + valorMedias + ", percentual díficeis: " + valorDificeis);

            float pontuacaoConteudo = 0;

            // fazendo o controle para saber até onde ele deve ir nas perguntas para esse determinado conteúdo
            int fim = inicio + quantidadePerguntasPorConteudo[indice][0];
            // navegando nas perguntas do conteúdo em questão
            for (int x = inicio; x < fim; x++) {
                // obtendo a pergunta do conteúdo
                Pergunta minhaPergunta = listaPerguntas.get(x);

                // verificando se acertou ou não
                if (minhaPergunta.getOpcaoEscolhida() == minhaPergunta.getAlternativaCorreta()) {
                    // acertou
                    acertos++;
                    // verificando a dificuldade para saber o valor a ser acrescido (pesos diferentes)
                    if (minhaPergunta.getNivelDificuldade() == 1) {
                        pontuacaoConteudo = pontuacaoConteudo + valorFaceis;
                    } else if (minhaPergunta.getNivelDificuldade() == 2) {
                        pontuacaoConteudo = pontuacaoConteudo + valorMedias;
                    } else if (minhaPergunta.getNivelDificuldade() == 3) {
                        pontuacaoConteudo = pontuacaoConteudo + valorDificeis;
                    }
                } else {
                    // errou
                    erros++;
                }
            }
            pontuacaoConteudo = Math.round(pontuacaoConteudo);
            Log.d("Teste", "Pontuação conteúdo: " + pontuacaoConteudo);
            // atualizando o novo início
            inicio = fim;

            DesempenhoConteudoDB desempenhoConteudoDB = new DesempenhoConteudoDB(context);

            //divide a soma de todos os acertos em um conteúdo, nos últimos 3 questionários,
            // e divide pela quantidade de perguntas que esse conteúdo teve nesses questionários.
            ArrayList<DesempenhoConteudo> listaUltimos3DesempenhosConteudos = desempenhoConteudoDB.buscaUltimos3DesempenhosConteudosComConteudo(meuNivelConteudo.getConteudo().getIdConteudo());
            float mediaAcertosUltimos3 = 0.0f;
            if (listaUltimos3DesempenhosConteudos.size()>=3){
                mediaAcertosUltimos3 = (float)acertos + (float)listaUltimos3DesempenhosConteudos.get(0).getQuantidadeAcertos() + (float)listaUltimos3DesempenhosConteudos.get(1).getQuantidadeAcertos()
                        /(quantidadePerguntasPorConteudo[indice][0] + listaUltimos3DesempenhosConteudos.get(0).getQuantidadePerguntas() + listaUltimos3DesempenhosConteudos.get(1).getQuantidadePerguntas());

            }
            DesempenhoConteudo desempenhoConteudo = new DesempenhoConteudo(meuNivelConteudo.getConteudo(), quantidadePerguntasPorConteudo[indice][0], acertos, erros, pontuacaoConteudo,mediaAcertosUltimos3);

            // adicionando no desempenho do questionário
            meuNivelConteudo.setUltimoDesempenhoConteudo(desempenhoConteudo);
            desempenhoQuestionario.getListaDesempenhoConteudos().add(desempenhoConteudo);

            // reiniciando os acertos e erros
            acertos = 0;
            erros = 0;

            // verificando se pontuação conteúdo fez com que devesse pular de nível
            Log.d("Teste", "Nível antigo: " + meuNivelConteudo.getNivel());
            if (pontuacaoConteudo >= 70) {
                // preparando o feedback
                Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel());
                NivelConteudoEnum novoNivel = meuNivelConteudo.obtemIncrementoDoisNiveis();
                meuFeedback.setNivelAtual(novoNivel);
                int retorno = 0;
                if (novoNivel != null) { // verificando se pulou um nível
                    if (meuNivelConteudo.getNivel() == NivelConteudoEnum.OURO) { // pulou e está no penúltimo nível, logo, avançou um
                        retorno = 1;
                    } else {
                        retorno = 2;
                    }
                }
                meuFeedback.setNiveisAvancados(retorno);
                listaFeedbacks.add(meuFeedback);
            } else if (pontuacaoConteudo >= 40 && pontuacaoConteudo < 70) {
                // preparando o feedback
                Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel());
                NivelConteudoEnum novoNivel = meuNivelConteudo.obtemIncrementoUmNivel();
                meuFeedback.setNivelAtual(novoNivel);
                int retorno = 0;
                if (novoNivel != null) { //sinal de que pulou um nível
                    retorno = 1;
                }
                meuFeedback.setNiveisAvancados(retorno);
                listaFeedbacks.add(meuFeedback);
            } else if (pontuacaoConteudo < 40) {
                // preparando o feedback
                Feedback meuFeedback = new Feedback(meuNivelConteudo.getConteudo(), meuNivelConteudo.getNivel(), meuNivelConteudo.getNivel(), 0);
                listaFeedbacks.add(meuFeedback);
            }
            for (int x = 0; x < listaFeedbacks.size(); x++) {
                Log.d("Teste", "INTERMEDIARIA Lista Feedbacks = Conteudo: " + listaFeedbacks.get(x).getConteudo().getNomeConteudo() + ", nivel anterior: " + listaFeedbacks.get(x).getNivelAnterior() + ", nivel atual: " + listaFeedbacks.get(x).getNivelAtual());
            }
            Log.d("Teste", "Nível novo: " + meuNivelConteudo.getNivel());
        }

        //atualizando datas de todos os nivelConteudo
        NivelConteudoDB nivelConteudoDB = new NivelConteudoDB(context);
        nivelConteudoDB.atualizaDatas(listaNivelConteudos, meuUsuario);
        nivelConteudoDB.atualizaDesempenhoConteudo(listaNivelConteudos, desempenhoQuestionario);

        return desempenhoQuestionario;
    }

    public ArrayList<String> criaVariasExplicacoesFeedback(Usuario usuario, ArrayList<NivelConteudo> listaNivelConteudo){
        //LOGICA DA EXPLICAÇÃO:======================================================================================
        //coisas necessárias:
        /*
        ranking atual .................................................................................(NivelConteudoEnum)
        ranking anterior (detectar progresso ou regresso)..............................................(NivelConteudoEnum)
        taxa de acerto dos últimos 3 questionários (média) (ou menos caso não tenham suficientem)......(float (0.0 a 1.0))
        taxa de acerto último questionário.............................................................(float (0.0 a 1.0))
        ultimoDesempenhoConteudo.......................................................................(DesempenhoConteudo)
        data de ultimo teste/questionario..............................................................(Date)
        data ultima atualizacao de nível...............................................................(Date)
        dataAtual......................................................................................(Date)
        */
        NivelConteudoDB nivelConteudoDB = new NivelConteudoDB(context);
        DesempenhoConteudoDB desempenhoConteudoDB = new DesempenhoConteudoDB(context);
        ArrayList<String> listaExplicacoes = new ArrayList<>();

        Date dataHoje = new Date(System.currentTimeMillis());

        for (int i = 0; i < listaNivelConteudo.size(); i++){
            String explicacaoDesempenho;
            NivelConteudoEnum rankingAtual; //FEITO
            NivelConteudoEnum rankingAnterior; //FEITO
            float taxaDeAcertosUltimosTres; //FEITO
            float taxaDeAcertosUltimo; //FEITO
            Date dataUltimoTeste; //FEITO
            Date dataUltimaAtualizacaoNivel; //FEITO

            DesempenhoConteudo ultimoDesempenhoConteudo = desempenhoConteudoDB.buscaDesempenhoConteudoComConteudo(listaNivelConteudo.get(i).getConteudo().getIdConteudo());

            //calculando o ranking atual e anterior
            rankingAtual = listaNivelConteudo.get(i).getNivel();
            rankingAnterior = listaNivelConteudo.get(i).getNivelAnterior();
            if (ultimoDesempenhoConteudo != null){
                ArrayList<DesempenhoConteudo> ultimos3DesempenhoConteudo = desempenhoConteudoDB.buscaUltimos3DesempenhosConteudosComConteudo(listaNivelConteudo.get(i).getIdNivelConteudo());

                if (ultimos3DesempenhoConteudo.get(0).getQuantidadePerguntas()>0
                    && ultimos3DesempenhoConteudo.get(1).getQuantidadePerguntas()>0
                    && ultimos3DesempenhoConteudo.get(2).getQuantidadePerguntas()>0){

                    taxaDeAcertosUltimo = ultimoDesempenhoConteudo.getQuantidadeAcertos()/ultimoDesempenhoConteudo.getQuantidadePerguntas();

                    taxaDeAcertosUltimosTres = (ultimoDesempenhoConteudo.getQuantidadeAcertos()
                            +ultimos3DesempenhoConteudo.get(1).getQuantidadeAcertos()
                            +ultimos3DesempenhoConteudo.get(2).getQuantidadeAcertos())
                            /(ultimoDesempenhoConteudo.getQuantidadePerguntas()
                            +ultimos3DesempenhoConteudo.get(1).getQuantidadePerguntas()
                            +ultimos3DesempenhoConteudo.get(2).getQuantidadePerguntas());

                } else {
                    taxaDeAcertosUltimo = 0.0f;
                    taxaDeAcertosUltimosTres = 0.0f;
                }

                if (ultimoDesempenhoConteudo.getQuantidadePerguntas()>0){

                } else {

                }

                dataUltimoTeste = listaNivelConteudo.get(i).getDataUltimoTeste();
                dataUltimaAtualizacaoNivel = listaNivelConteudo.get(i).getDataAtualizacaoNivel();
                long diffUltimoTeste;
                long diffUltimaAtualizacaoNivel;
                //tempo entre o último teste e o dia atual
                //com tratamento de possíveis nulos
                if (dataUltimoTeste != null){
                    long diffInMilliesDesdeUltimoTeste = Math.abs(dataUltimoTeste.getTime() - dataHoje.getTime());
                    diffUltimoTeste = TimeUnit.DAYS.convert(diffInMilliesDesdeUltimoTeste, TimeUnit.MILLISECONDS);
                    Log.d("dataUltimoTeste", dataUltimoTeste.toString());
                } else{
                    diffUltimoTeste = 365L;
                    Log.d("dataUltimoTeste", "null");
                }

                if (dataUltimaAtualizacaoNivel != null){
                    long diffInMilliesDesdeUltimaAtualizacaoNivel = Math.abs(dataUltimoTeste.getTime() - dataHoje.getTime());
                    diffUltimaAtualizacaoNivel = TimeUnit.DAYS.convert(diffInMilliesDesdeUltimaAtualizacaoNivel, TimeUnit.MILLISECONDS);

                    Log.d("dataUltimoMudaRank", dataUltimaAtualizacaoNivel.toString());
                } else {
                    diffUltimaAtualizacaoNivel = 365L;

                    Log.d("dataUltimoMudaRank", "null");
                }


                Log.d("dataHoje", dataHoje.toString());


                Log.d("Dias desde o ultimoTest", String.valueOf(diffUltimoTeste));
                Log.d("DiasDesdeUltimaMudaRank", String.valueOf(diffUltimaAtualizacaoNivel));
                if (rankingAtual.getValor() > rankingAnterior.getValor()){ // IF FINALIZADO
                    if (rankingAtual.getValor() - 2 == rankingAnterior.getValor()){//avançou 2 níveis (diagnóstico)
                        if (taxaDeAcertosUltimo >= taxaDeAcertosUltimosTres){
                            if (diffUltimoTeste < 7){
                                if (diffUltimaAtualizacaoNivel<7){
                                    //rank melhor que último, taxa de acertos subindo
                                    //fez questionario nessa semana e subiu de ranking essa semana
                                    explicacaoDesempenho = "Parabéns, Seus esforços estão rendendo!"
                                            +" Seu desempenho no conteúdo "+listaNivelConteudo.get(i).getConteudo().getNomeConteudo()+" apresenta uma significativa evolução e"
                                            +" comprometimento com o aprendizado. Você subiu do ranking "+rankingAnterior.toString()
                                            +" para o "+rankingAtual.toString()
                                            +" essa semana através de diagnóstico, se desafiando em conteúdo mais difíceis!"
                                            +" Para fixar o novo conteúdo estude e continue se exercitando. Continue assim!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                } else {
                                    //rank melhor que último, taxa de acertos subindo
                                    //fez questionario nessa semana e subiu de ranking há mais de uma semana
                                    explicacaoDesempenho = "Seu desempenho em "+listaNivelConteudo.get(i).getConteudo().getNomeConteudo()
                                            +" constata que você apresenta uma constante evolução nas taxas de acerto, mesmo após avançar dois níveis de uma vez,"
                                            +" podendo subir de ranking novamente"
                                            +" a qualquer momento. Continue aprendendo e se exercitando!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                }
                            } else {
                                if (diffUltimaAtualizacaoNivel<7){
                                    //rank melhor que último, taxa de acertos subindo
                                    //fez questionario há mais de uma semana e subiu de ranking nessa semana
                                    explicacaoDesempenho = "A sua evolução no ranking e taxa de acertos indicam que você" +
                                            " apresenta evolução nos conhecimentos, mesmo após a difícil tarefa de subir de ranking em um diagnóstico."+
                                            " Porém, o estudo precisa de prática constante para não se esquecer dos conteúdos e você," +
                                            " mesmo que tenha subido de ranking nessa semana," +
                                            " não realizou nem um teste nela. Isso pode ser bom, pausas são importantes, mas não deixe de praticar" +
                                            " por medo de baixar as sua estatísticas. O importante não é o ranking, mas o conhecimento que você adquiriu."+
                                            " Não deixe de praticar!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                } else {
                                    //rank melhor que último, taxa de acertos subindo
                                    //fez questionario há mais de uma semana e subiu de ranking há mais de uma semana
                                    explicacaoDesempenho = "A sua evolução no ranking e taxa de acertos indicam que você" +
                                            " apresenta evolução nos conhecimentos, mesmo após a difícil tarefa de subir de ranking em um diagnóstico."+
                                            " Porém, o estudo precisa de prática constante para não se esquecer dos conteúdos e você não realizou" +
                                            " nem um teste ou subiu de ranking essa semana. Isso pode ser bom, pausas são importantes, mas não deixe de praticar" +
                                            " por medo de baixar as sua estatísticas. O importante não é o ranking, mas o conhecimento que você adquiriu."+
                                            " Não deixe de praticar!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                }
                            }
                        } else {
                            if (diffUltimoTeste < 7){
                                if (diffUltimaAtualizacaoNivel<7){
                                    //rank melhor que último, taxa de acertos igual ou decaindo
                                    //fez questionario nessa semana e subiu de ranking nessa semana
                                    explicacaoDesempenho = "De acordo com sua taxa de acertos," +
                                            " você encontrou uma certa dificuldade no novo ranking que você conseguiu essa semana, o que pode" +
                                            " acontecer após um diagnóstico. Para continuar nesse nível, estude um pouco mais e continue praticando. Você consegue!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                } else {
                                    //rank melhor que último, taxa de acertos igual ou decaindo
                                    //fez questionario nessa semana e subiu de ranking há mais de uma semana
                                    explicacaoDesempenho = "De acordo com sua taxa de acertos," +
                                            " você encontrou uma certa dificuldade no seu ranking, o que pode" +
                                            " acontecer após um diagnóstico. Para continuar nesse nível, estude um pouco mais e continue praticando. Você consegue!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                }
                            } else {
                                if (diffUltimaAtualizacaoNivel<7){
                                    //rank melhor que último, taxa de acertos igual ou decaindo
                                    //fez questionario há mais de uma semana e subiu de ranking nessa semana
                                    explicacaoDesempenho = "Você subiu de ranking essa semana através de um diagnóstico, porém não pratica há mais de uma e apresenta certa dificuldade no novo ranking."+
                                            " Para o conhecimento se consolidar, é necessário a prática. Volte a praticar para fixar o conteúdo, você consegue!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                } else {
                                    //rank melhor que último, taxa de acertos igual ou decaindo
                                    //fez questionario há mais de uma semana e subiu de ranking há mais de uma semana
                                    explicacaoDesempenho = "Não se desanime! Estudar é uma tarefa difícil e tudo bem" +
                                            " fazer uma pausa, mas não abandone essa atividade. Você apresenta dificuldade no conteúdo," +
                                            " porém isso não quer dizer que você não seja capaz de aprendê-lo. Continue estudando, sem medo de errar,"+
                                            " que alguma hora você passa a entender melhor o conteúdo!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                }
                            }
                        }
                    } else {//avançou 1 níveis (questionário)
                        if (taxaDeAcertosUltimo >= taxaDeAcertosUltimosTres){
                            if (diffUltimoTeste < 7){
                                if (diffUltimaAtualizacaoNivel<7){
                                    //rank melhor que último, taxa de acertos subindo
                                    //fez questionario nessa semana e subiu de ranking essa semana
                                    explicacaoDesempenho = "Parabéns, Seus esforços estão rendendo!"
                                            +" Seu desempenho no conteúdo "+listaNivelConteudo.get(i).getConteudo().getNomeConteudo()+" apresenta uma significativa evolução e"
                                            +" comprometimento com o aprendizado. Você subiu do ranking "+rankingAnterior.toString()
                                            +" para o "+rankingAtual.toString()
                                            +" essa semana, dominando o conteúdo anterior!"
                                            +" Para fixar o novo conteúdo estude e continue se exercitando. Continue assim!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                } else {
                                    //rank melhor que último, taxa de acertos subindo
                                    //fez questionario nessa semana e subiu de ranking há mais de uma semana
                                    explicacaoDesempenho = "Seu desempenho em "+listaNivelConteudo.get(i).getConteudo().getNomeConteudo()
                                            +" constata que você apresenta uma constante evolução nas taxas de acerto,"
                                            +" podendo subir de ranking novamente"
                                            +" a qualquer momento. Continue aprendendo e se exercitando!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                }
                            } else {
                                if (diffUltimaAtualizacaoNivel<7){
                                    //rank melhor que último, taxa de acertos subindo
                                    //fez questionario há mais de uma semana e subiu de ranking nessa semana
                                    explicacaoDesempenho = "A sua evolução no ranking e taxa de acertos indicam que você" +
                                            " apresenta evolução nos conhecimentos."+
                                            " Porém, o estudo precisa de prática constante para não se esquecer dos conteúdos e você," +
                                            " mesmo que tenha subido de ranking nessa semana," +
                                            " não realizou nem um teste nela. Isso pode ser bom, pausas são importantes, mas não deixe de praticar" +
                                            " por medo de baixar as sua estatísticas. O importante não é o ranking, mas o conhecimento que você adquiriu."+
                                            " Não deixe de praticar!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                } else {
                                    //rank melhor que último, taxa de acertos subindo
                                    //fez questionario há mais de uma semana e subiu de ranking há mais de uma semana
                                    explicacaoDesempenho = "A sua evolução no ranking e taxa de acertos indicam que você" +
                                            " apresenta evolução nos conhecimentos."+
                                            " Porém, o estudo precisa de prática constante para não se esquecer dos conteúdos e você não realizou" +
                                            " nem um teste ou subiu de ranking essa semana. Isso pode ser bom, pausas são importantes, mas não deixe de praticar" +
                                            " por medo de baixar as sua estatísticas. O importante não é o ranking, mas o conhecimento que você adquiriu."+
                                            " Não deixe de praticar!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                }
                            }
                        } else {
                            if (diffUltimoTeste < 7){
                                if (diffUltimaAtualizacaoNivel<7){
                                    //rank melhor que último, taxa de acertos igual ou decaindo
                                    //fez questionario nessa semana e subiu de ranking nessa semana
                                    explicacaoDesempenho = "De acordo com sua taxa de acertos," +
                                            " você encontrou uma certa dificuldade no novo ranking que você conseguiu essa semana." +
                                            " Para continuar nesse nível, estude um pouco mais e continue praticando. Você consegue!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                } else {
                                    //rank melhor que último, taxa de acertos igual ou decaindo
                                    //fez questionario nessa semana e subiu de ranking há mais de uma semana
                                    explicacaoDesempenho = "De acordo com sua taxa de acertos," +
                                            " você encontrou uma certa dificuldade no seu ranking." +
                                            " Para continuar nesse nível, estude um pouco mais e continue praticando. Você consegue!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                }
                            } else {
                                if (diffUltimaAtualizacaoNivel<7){
                                    //rank melhor que último, taxa de acertos igual ou decaindo
                                    //fez questionario há mais de uma semana e subiu de ranking nessa semana
                                    explicacaoDesempenho = "Você subiu de ranking essa semana, porém não pratica há mais de uma e apresenta certa dificuldade no novo ranking."+
                                            " Para o conhecimento se consolidar, é necessário a prática. Volte a praticar para fixar o conteúdo, você consegue!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                } else {
                                    //rank melhor que último, taxa de acertos igual ou decaindo
                                    //fez questionario há mais de uma semana e subiu de ranking há mais de uma semana
                                    explicacaoDesempenho = "Não se desanime! Estudar é uma tarefa difícil e tudo bem" +
                                            " fazer uma pausa, mas não abandone essa atividade. Você apresenta dificuldade no conteúdo," +
                                            " porém isso não quer dizer que você não seja capaz de aprendê-lo. Continue estudando, sem medo de errar,"+
                                            " que alguma hora você passa a entender melhor o conteúdo!";
                                    listaExplicacoes.add(explicacaoDesempenho);
                                }
                            }
                        }
                    }

                } else if (rankingAtual.getValor() < rankingAnterior.getValor()){
                    if (taxaDeAcertosUltimo >= taxaDeAcertosUltimosTres){
                        if (diffUltimoTeste < 7){
                            if (diffUltimaAtualizacaoNivel<7){
                                //rank menor que último, taxa de acertos subindo
                                //fez questionario nessa semana e subiu de ranking essa semana
                                explicacaoDesempenho = "Parabéns, Você está evoluindo!"
                                        +" Seu desempenho no conteúdo "+listaNivelConteudo.get(i).getConteudo().getNomeConteudo()+" apresenta uma significativa evolução e"
                                        +" comprometimento com o aprendizado. Mesmo caindo de ranking essa semana, sua taxa de acertos e prática constante lhe farão aprender ainda mais"
                                        +" As vezes é necessário dar um passo para trás, para dar dois para frente. Continue Assim!";
                                listaExplicacoes.add(explicacaoDesempenho);
                            } else {
                                //rank menor que último, taxa de acertos subindo
                                //fez questionario nessa semana e subiu de ranking há mais de uma semana
                                explicacaoDesempenho = "Seu desempenho em "+listaNivelConteudo.get(i).getConteudo().getNomeConteudo()
                                        +" constata que você apresenta uma constante evolução nas taxas de acerto,"
                                        +" podendo subir de ranking novamente!"
                                        +" a qualquer momento. Continue aprendendo e se exercitando!";
                                listaExplicacoes.add(explicacaoDesempenho);
                            }
                        } else {
                            if (diffUltimaAtualizacaoNivel<7){
                                //rank menor que último, taxa de acertos subindo
                                //fez questionario há mais de uma semana e subiu de ranking nessa semana
                                explicacaoDesempenho =  "Mesmo que o seu ranking tenha caído recentemente, a sua taxa de acertos indica que você" +
                                        " apresenta evolução nos conhecimentos."+
                                        " Porém, o estudo precisa de prática constante para não se esquecer dos conteúdos e você não realizou" +
                                        " nem um teste. Isso pode ser bom, pausas são importantes, mas não deixe de praticar" +
                                        " por medo de baixar as sua estatísticas. O importante não é o ranking, mas o conhecimento que você adquiriu."+
                                        " Não deixe de praticar!";
                                listaExplicacoes.add(explicacaoDesempenho);
                            } else {
                                //rank menor que último, taxa de acertos subindo
                                //fez questionario há mais de uma semana e subiu de ranking há mais de uma semana
                                explicacaoDesempenho = "Mesmo que o seu ranking tenha caído, a sua taxa de acertos indica que você" +
                                        " apresenta evolução nos conhecimentos."+
                                        " Porém, o estudo precisa de prática constante para não se esquecer dos conteúdos e você não realizou" +
                                        " nem um teste. Isso pode ser bom, pausas são importantes, mas não deixe de praticar" +
                                        " por medo de baixar as sua estatísticas. O importante não é o ranking, mas o conhecimento que você adquiriu."+
                                        " Não deixe de praticar!";
                                listaExplicacoes.add(explicacaoDesempenho);
                            }
                        }
                    } else {
                        if (diffUltimoTeste < 7){
                            if (diffUltimaAtualizacaoNivel<7){
                                //rank menor que último, taxa de acertos igual ou decaindo
                                //fez questionario nessa semana e subiu de ranking nessa semana
                                explicacaoDesempenho = "De acordo com sua taxa de acertos," +
                                        " você encontrou uma certa dificuldade no ranking após voltar a ele essa semana." +
                                        " Para continuar nesse nível, mantenha a calma, estude um pouco mais e continue praticando. Você consegue!";
                                listaExplicacoes.add(explicacaoDesempenho);
                            } else {
                                //rank menor que último, taxa de acertos igual ou decaindo
                                //fez questionario nessa semana e subiu de ranking há mais de uma semana
                                explicacaoDesempenho = "De acordo com sua taxa de acertos," +
                                        " você encontrou uma certa dificuldade no ranking atual." +
                                        " Para continuar nesse nível, mantenha a calma, estude um pouco mais e continue praticando. Você consegue!";
                                listaExplicacoes.add(explicacaoDesempenho);
                            }
                        } else {
                            if (diffUltimaAtualizacaoNivel<7){
                                //rank menor que último, taxa de acertos igual ou decaindo
                                //fez questionario há mais de uma semana e subiu de ranking nessa semana
                                explicacaoDesempenho = "Você não pratica há mais de uma e apresenta certa dificuldade no ranking cujo você voltou essa semana."+
                                        " Para o conhecimento se consolidar, é necessário a prática. Volte a praticar para fixar o conhecimento, você consegue! Essa é uma oportunidade para aprender melhor esse conteúdo.";
                                listaExplicacoes.add(explicacaoDesempenho);
                            } else {
                                //rank menor que último, taxa de acertos igual ou decaindo
                                //fez questionario há mais de uma semana e subiu de ranking há mais de uma semana
                                explicacaoDesempenho = "Não se desanime! Estudar é uma tarefa difícil e tudo bem" +
                                        " fazer uma pausa, mas não abandone essa atividade. Você apresenta dificuldade no conteúdo," +
                                        " porém isso não quer dizer que você não seja capaz de aprendê-lo. Continue estudando, sem medo de errar,"+
                                        " que alguma hora você passa a entender melhor o conteúdo!";
                                listaExplicacoes.add(explicacaoDesempenho);
                            }
                        }
                    }
                } else {
                    if (taxaDeAcertosUltimo >= taxaDeAcertosUltimosTres){
                        if (diffUltimoTeste < 7){
                            //rank igual ao último, taxa de acertos subindo
                            //fez questionario nessa semana
                            explicacaoDesempenho = "Parabéns, você está indo bem nos quizes!"
                                    +" Seu desempenho no conteúdo "+listaNivelConteudo.get(i).getConteudo().getNomeConteudo()+" apresenta uma significativa evolução e"
                                    +" comprometimento com o aprendizado. "
                                    +" Continue estudando e praticando!";
                            listaExplicacoes.add(explicacaoDesempenho);
                        } else {
                            //rank igual ao último, taxa de acertos subindo
                            //fez questionario há mais de uma semana
                            explicacaoDesempenho = "A sua taxa de acertos indica que você" +
                                    " apresenta evolução nos conhecimentos."+
                                    " Porém, o estudo precisa de prática constante para não se esquecer dos conteúdos e você" +
                                    " não realizou nem um teste essa semana. Isso pode ser bom, pausas são importantes, mas não deixe de praticar, você está indo bem!";
                            listaExplicacoes.add(explicacaoDesempenho);
                        }
                    } else {
                        if (diffUltimoTeste < 7){
                            //rank igual ao último, taxa de acertos igual ou decaindo
                            //fez questionario nessa semana
                            explicacaoDesempenho = "De acordo com sua taxa de acertos," +
                                    " você encontrou uma certa dificuldade no aprendizado." +
                                    " Estude um pouco mais e continue praticando. Você consegue!";
                            listaExplicacoes.add(explicacaoDesempenho);
                        } else {
                            if (taxaDeAcertosUltimosTres == 0.0f){
                                listaExplicacoes.add("Realize mais questionários para descobrir mais sobre seu desempenho!");
                            } else {
                                //rank igual ao último, taxa de acertos igual ou decaindo
                                //fez questionario há mais de uma semana
                                explicacaoDesempenho = "Você não pratica há mais de uma semana e apresenta certa dificuldade no conteúdo."+
                                        " Para o conhecimento se consolidar, é necessário a prática. Volte a praticar para fixar o conteúdo, você consegue!";
                                listaExplicacoes.add(explicacaoDesempenho);
                            }
                        }
                    }
                }
            } else {
                listaExplicacoes.add("Realize mais questionários para descobrir mais sobre seu desempenho!");
            }
        }
        return listaExplicacoes;
    }

    public ArrayList<NivelConteudo> carregaListaDeNivelConteudoComConteudo(ArrayList<Conteudo> listaConteudos, Usuario usuario){
        return new NivelConteudoDB(context).buscaConteudosComNivel(listaConteudos, usuario);
    }

    public DesempenhoConteudo getDesempenhoConteudoComId(int id){
        return new DesempenhoConteudoDB(context).buscaDesempenhoConteudoComId(id);
    }

    public String[] insereConteudoComNivelConteudoInicial(Conteudo conteudo, Usuario usuario, int tipoConteudo){
        String[] retorno = new String[2];

        ConteudoDB conteudoDB = new ConteudoDB(context);
        NivelConteudoDB nivelConteudoDB = new NivelConteudoDB(context);
        String resultadoInsereConteudo = conteudoDB.insereConteudo(conteudo);
        conteudo.setIdConteudo(conteudoDB.buscaUltimoConteudo(tipoConteudo).getIdConteudo());
        String resultadoInsereNivelConteudo;
        Date dataAtualizacaoNivel = null;
        Date dataUltimoTeste = null;
        try {
            dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
            dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
        } catch(ParseException parse) {
            parse.printStackTrace();
        }


        NivelConteudo nivelConteudoNovo = new NivelConteudo(NivelConteudoEnum.COBRE, -1, dataUltimoTeste, dataAtualizacaoNivel, null, usuario, conteudo, 0, 5);
        resultadoInsereNivelConteudo = nivelConteudoDB.insereNivel(nivelConteudoNovo);
        retorno[0] = resultadoInsereConteudo;
        retorno[1] = resultadoInsereNivelConteudo;
        return retorno;

    }

    public void ligaNivelConteudoComDesempenhoConteudo(DesempenhoQuestionario desempenhoQuestionario, ArrayList<NivelConteudo> listaNivelConteudo){
        NivelConteudoDB nivelConteudoDB = new NivelConteudoDB(context);
        nivelConteudoDB.atualizaDesempenhoConteudo(listaNivelConteudo, desempenhoQuestionario);
    }
}