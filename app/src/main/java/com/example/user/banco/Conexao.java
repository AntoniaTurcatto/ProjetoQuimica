package com.example.user.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String NOME_BANCO = "DadosQuimica.db";
    private static final int VERSAO = 18;

    public Conexao(Context context) {
        super(context,NOME_BANCO,null,VERSAO);
    }

    //TABELA RESUMO
    private static final String TABELA_RESUMO = "Resumo";
    private static final String _ID_RESUMO = "_idResumo";
    private static final String TITULO_RESUMO = "titulo";
    private static final String TEXTO_RESUMO = "texto";
    private static final String SUBTITULO_RESUMO = "subtitulo";
    private static final String FORMULA_RESUMO = "formula";
    private static final String FK_CONTEUDO_RESUMO  = "fk_idConteudo";

    //TABELA PERGUNTA
    private static final String TABELA_PERGUNTA = "Pergunta";
    private static final String _ID_PERGUNTA = "_idPergunta";
    private static final String ENUNCIADO_PERGUNTA = "enunciado";
    private static final String OPCAO_A = "opcaoA";
    private static final String OPCAO_B = "opcaoB";
    private static final String OPCAO_C = "opcaoC";
    private static final String OPCAO_D = "opcaoD";
    private static final String OPCAO_E = "opcaoE";
    private static final String IMAGEM_PERGUNTA = "imagem";
    private static final String TESTE_PREVIO = "testePrevio";
    private static final String FK_CONTEUDO_PERGUNTA = "fk_idConteudo";
    private static final String ALTERNATIVA_CORRETA = "alternativaCorreta";
    private static final String NIVEL_DIFICULDADE = "nivelDificuldade";
    private static final String QUESTAO_VESTIBULAR = "questaoVestibular";

    //TABELA CONTEUDO
    private static final String TABELA_CONTEUDO = "Conteudo";
    private static final String _ID_CONTEUDO = "_idConteudo";
    private static final String NOME_CONTEUDO = "nome";
    private static final String TIPO_CONTEUDO = "tipo";
    private static final String QUANTIDADE = "quantidade";
    private static final String PRECISAO = "precisao";

    //TABELA DESEMPENHO CONTEÚDO
    private static final String TABELA_DESEMPENHO_CONTEUDO = "DesempenhoConteudo";
    private static final String _ID_DESEMPENHO_CONTEUDO = "_idDesempenhoConteudo";
    private static final String FK_CONTEUDO_DESEMPENHO_CONTEUDO = "fk_idDesempenhoConteudo";
    private static final String QUANTIDADE_PERGUNTAS = "quantidadePerguntas";
    private static final String QUANTIDADE_ACERTOS = "quantidadeAcertos";
    private static final String QUANTIDADE_ERROS = "quantidadeErros";
    private static final String PONTUACAO_CONTEUDO = "pontuacao";
    private static final String MEDIA_ACERTOS_ULTIMOS_QUESTIONARIOS= "mediaAcertosUltimosQuestionarios";
    private static final String FK_DESEMPENHO_QUESTIONARIO = "fk_idDesempenhoQuestionario";

    //TABELA DESEMPENHO QUESTIONÁRIO
    private static final String TABELA_DESEMPENHO_QUESTIONARIO = "DesempenhoQuestionario";
    private static final String _ID_DESEMPENHO_QUESTIONARIO = "_idDesempenhoQuestionario";
    private static final String DATA_QUESTIONARIO = "data";
    private static final String PONTUACAO_QUESTIONARIO = "pontuacao";
    private static final String TIPO_DESEMPENHO = "tipoDesempenho";
    private static final String FK_USUARIO_QUESTIONARIO = "fk_idUsuario";

    //TABELA HIDROCARBONETOS
    private static final String TABELA_HIDROCARBONETO = "Hidrocarboneto";
    private static final String _ID_HIDROCARBONETO = "_idHidrocarboneto";
    private static final String NOME_HIDROCARBONETO = "nome";
    private static final String DESCRICAO_HIDROCARBONETO = "descricao";

    //TABELA MOLECULAS
    private static final String TABELA_MOLECULA = "Molecula";
    private static final String _ID_MOLECULA = "_idMolecula";
    private static final String NOME_MOLECULA = "nome";
    private static final String IMAGEM_MOLECULA = "imagem";
    private static final String FK_HIDROCARBONETO = "fk_idHidrocarboneto";

    //TABELA ELEMENTODETALHADO
    private static final String TABELA_ELEMENTO_DETALHADO = "ElementoDetalhado";
    private static final String _ID_ELEMENTO_DETALHADO = "_idElementoDetalhado";
    private static final String IMAGEM_ELEMENTO = "imagem";
    private static final String NUMERO_ATOMICO = "numeroAtomico";
    private static final String MASSA_ATOMICA = "massaAtomica";
    private static final String VOLUME_MOLAR = "volumeMolar";
    private static final String APARENCIA_ELEMENTO = "aparencia";
    private static final String NOME_ELEMENTO = "nome";
    private static final String NOME_CIENTIFICO = "nomeCientifico";
    private static final String ORIGEM_NOME = "origemNome";
    private static final String SERIE_QUIMICA = "serieQuimica";
    private static final String GRUPO_ELEMENTO = "grupo";
    private static final String CONFIGURACAO_ELETRONICA ="configuracaoEletronica";
    private static final String ELETRONS = "eletrons";
    private static final String ESTADO_OXIDACAO = "estadoOxidacao";
    private static final String ESTADO_MATERIA = "estadoMateria";
    private static final String ESTADO_FUSAO_ELEMENTO = "estadoFusao";
    private static final String ESTADO_EBULICAO_ELEMENTO = "estadoEbulicao";

    //TABELA ELEMENTODETALHADO_INFORMACOESCOMPOSTO
    private static final String TABELA_ELEMENTO_INFORMACOES = "ElementoInformacoes";
    private static final String FK_ELEMENTO_DETALHADO = "fk_idElementoDetalhado";
    private static final String FK_INFORMACOES_COMPOSTO = "fk_idInformacoesComposto";

    //INFORMAÇÕES COMPOSTO
    private static final String TABELA_INFORMACOES_COMPOSTO = "InformacoesComposto";
    private static final String _ID_INFORMACOES_COMPOSTO = "_idInformacoesComposto";
    private static final String NOME_COMPOSTO = "nome";
    private static final String DENSIDADE_RELATIVA = "densidadeRelativa";
    private static final String ESTADO_FUSAO_COMPOSTO = "estadoFusao";
    private static final String ESTADO_EBULICAO_COMPOSTO = "estadoEbulicao";
    private static final String MASSA_MOLAR = "massaMolar";
    private static final String SOLUBILIDADE = "solubilidade";
    private static final String APARENCIA_COMPOSTO = "aparencia";
    private static final String ESTRUTURA = "estrutura";
    private static final String PONTO_FULGOR = "pontoFulgor";

    //TABELA USUARIO
    private static final String TABELA_USUARIO = "Usuario";
    private static final String _ID_USUARIO = "_idUsuario";
    private static final String NOME_USUARIO = "nome";
    private static final String NOME_COMPLETO_USUARIO = "nomeCompleto";
    private static final String SENHA_USUARIO = "senha";
    private static final String EMAIL = "email";
    private static final String CPF_USUARIO = "cpf";

    //TABELA NIVEL DO CONTEUDO
    private static final String TABELA_NIVEL_CONTEUDO = "NivelConteudo";
    private static final String _ID_NIVEL_CONTEUDO = "_idConteudo";
    private static final String FK_USUARIO_NIVEL = "fk_idUsuario";
    private static final String FK_CONTEUDO_NIVEL = "fk_idConteudo";
    private static final String NIVEL = "nivel";
    private static final String SUBIU_OU_DESCEU_NIVEL = "subiuOuDesceuNivel";//-1=desceu 1=subiu 0=começou agora
    private static final String DATA_ATUALIZACAO_NIVEL = "dataAtualizacaoNivel";
    private static final String DATA_ULTIMO_TESTE = "dataUltimoTeste";
    private static final String TENTATIVAS = "tentativas";
    private static final String VIDAS = "vidas";
    private static final String IMAGEM_NIVEL = "imagem";
    private static final String FK_ULTIMO_DESEMPENHO_CONTEUDO = "fk_ultimoDesempenhoConteudo"; //vai estar la tambem a taxa de acerto dos ultimos questionarios

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIO);
        String sql = "CREATE TABLE "+TABELA_USUARIO+
                "("+_ID_USUARIO+" integer primary key autoincrement," +
                NOME_COMPLETO_USUARIO+" VARCHAR(100),"+
                NOME_USUARIO+" VARCHAR(100),"+
                SENHA_USUARIO+" VARCHAR(100)," +
                EMAIL+" VARCHAR(100)," +
                CPF_USUARIO+" INTEGER)";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_CONTEUDO);
        sql = "CREATE TABLE "+TABELA_CONTEUDO+
                "("+_ID_CONTEUDO+" integer primary key autoincrement," +
                NOME_CONTEUDO+" text," +
                TIPO_CONTEUDO+" integer," +
                QUANTIDADE+" integer,"+
                PRECISAO+" float)";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_RESUMO);
        sql = "CREATE TABLE "+TABELA_RESUMO+
                "("+_ID_RESUMO+" integer primary key autoincrement," +
                TITULO_RESUMO+" VARCHAR(50),"+
                TEXTO_RESUMO+" VARCHAR(200)," +
                SUBTITULO_RESUMO+" VARCHAR(50)," +
                FORMULA_RESUMO+" VARCHAR(25),"+
                FK_CONTEUDO_RESUMO+" INTEGER,"+
                " FOREIGN KEY ("+FK_CONTEUDO_RESUMO+") REFERENCES " + TABELA_CONTEUDO + "("+_ID_CONTEUDO + "))";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_PERGUNTA );
        sql = "CREATE TABLE "+TABELA_PERGUNTA+
                "("+_ID_PERGUNTA+" integer primary key autoincrement," +
                ENUNCIADO_PERGUNTA+" VARCHAR(45),"+
                OPCAO_A+" VARCHAR(45)," +
                OPCAO_B+" VARCHAR(45)," +
                OPCAO_C+" VARCHAR(45)," +
                OPCAO_D+" VARCHAR(45)," +
                OPCAO_E+" VARCHAR(45)," +
                IMAGEM_PERGUNTA+" VARCHAR(45)," +
                TESTE_PREVIO+" TINYINT(1),"+
                ALTERNATIVA_CORRETA+" VARCHAR(1),"+
                NIVEL_DIFICULDADE+" VARCHAR(1),"+
                QUESTAO_VESTIBULAR+" INTEGER(1),"+
                FK_CONTEUDO_PERGUNTA+" INTEGER,"+
                " FOREIGN KEY ("+FK_CONTEUDO_PERGUNTA+") REFERENCES " + TABELA_CONTEUDO + "("+_ID_CONTEUDO + "))";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_DESEMPENHO_QUESTIONARIO);
        sql = "CREATE TABLE "+TABELA_DESEMPENHO_QUESTIONARIO+
                "("+_ID_DESEMPENHO_QUESTIONARIO+" integer primary key autoincrement," +
                DATA_QUESTIONARIO+" VARCHAR(30),"+
                PONTUACAO_QUESTIONARIO+" FLOAT,"+
                FK_USUARIO_QUESTIONARIO+" INTEGER,"+
                TIPO_DESEMPENHO+" INTEGER,"+
                " FOREIGN KEY ("+FK_USUARIO_QUESTIONARIO+") REFERENCES " + TABELA_USUARIO + "("+_ID_USUARIO+"))";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_DESEMPENHO_CONTEUDO);
        sql = "CREATE TABLE "+TABELA_DESEMPENHO_CONTEUDO+
                "("+_ID_DESEMPENHO_CONTEUDO+" integer primary key autoincrement," +
                QUANTIDADE_PERGUNTAS+" INTEGER,"+
                QUANTIDADE_ACERTOS+" INTEGER,"+
                QUANTIDADE_ERROS+" INTEGER,"+
                PONTUACAO_CONTEUDO+" FLOAT,"+
                MEDIA_ACERTOS_ULTIMOS_QUESTIONARIOS+" FLOAT,"+//PARA DESCOBRIR FAZER SELECT WHERE FK_CONTEUDO = CONTEUDO E PEGAR OS ULTIMOS REALIZADOS, LIMITE 5
                FK_CONTEUDO_DESEMPENHO_CONTEUDO+" INTEGER,"+
                FK_DESEMPENHO_QUESTIONARIO+" INTEGER,"+
                " FOREIGN KEY ("+FK_CONTEUDO_DESEMPENHO_CONTEUDO+") REFERENCES " + TABELA_CONTEUDO + "("+_ID_CONTEUDO + "), "+
                " FOREIGN KEY ("+FK_DESEMPENHO_QUESTIONARIO+") REFERENCES " + TABELA_DESEMPENHO_QUESTIONARIO + "("+_ID_DESEMPENHO_QUESTIONARIO +"))";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_NIVEL_CONTEUDO);
        sql = "CREATE TABLE "+TABELA_NIVEL_CONTEUDO+
                "("+_ID_NIVEL_CONTEUDO+" integer primary key autoincrement," +
                NIVEL+" INTEGER,"+
                DATA_ATUALIZACAO_NIVEL +" VARCHAR(30),"+
                DATA_ULTIMO_TESTE +" VARCHAR(30),"+
                SUBIU_OU_DESCEU_NIVEL +" TINYINT,"+
                TENTATIVAS+ " INTEGER,"+
                VIDAS+ " INTEGER,"+
                IMAGEM_NIVEL+" VARCHAR(45),"+
                FK_USUARIO_NIVEL+" INTEGER,"+
                FK_CONTEUDO_NIVEL+" INTEGER,"+
                FK_ULTIMO_DESEMPENHO_CONTEUDO +" INTEGER,"+
                " FOREIGN KEY ("+FK_USUARIO_NIVEL+") REFERENCES " + TABELA_USUARIO + "("+_ID_USUARIO +"), "+
                " FOREIGN KEY ("+FK_CONTEUDO_NIVEL+") REFERENCES " + TABELA_CONTEUDO + "("+_ID_CONTEUDO +"), "+
                " FOREIGN KEY ("+ FK_ULTIMO_DESEMPENHO_CONTEUDO +") REFERENCES "+ TABELA_DESEMPENHO_CONTEUDO + "("+_ID_DESEMPENHO_CONTEUDO+"))";
                db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_HIDROCARBONETO);
        sql = "CREATE TABLE "+TABELA_HIDROCARBONETO+
                "("+_ID_HIDROCARBONETO+" integer primary key autoincrement," +
                NOME_HIDROCARBONETO+" VARCHAR(45),"+
                DESCRICAO_HIDROCARBONETO+" VARCHAR(100))";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_MOLECULA);
        sql = "CREATE TABLE "+TABELA_MOLECULA+
                "("+_ID_MOLECULA+" integer primary key autoincrement," +
                NOME_MOLECULA+" VARCHAR(30),"+
                IMAGEM_MOLECULA+" VARCHAR(30),"+
                FK_HIDROCARBONETO+" INTEGER," +
                "FOREIGN KEY ("+FK_HIDROCARBONETO+") REFERENCES " + TABELA_HIDROCARBONETO +"("+_ID_HIDROCARBONETO +"))";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ELEMENTO_DETALHADO);
        sql = "CREATE TABLE "+TABELA_ELEMENTO_DETALHADO+
                "("+_ID_ELEMENTO_DETALHADO+" integer primary key autoincrement," +
                IMAGEM_ELEMENTO+" VARCHAR(30),"+
                NUMERO_ATOMICO+" TINYINT(3),"+
                MASSA_ATOMICA+" INT(7)," +
                VOLUME_MOLAR+" VARCHAR(20),"+
                APARENCIA_ELEMENTO+" VARCHAR(150),"+
                NOME_ELEMENTO + " VARCHAR(20),"+
                NOME_CIENTIFICO+" VARCHAR(30),"+
                ORIGEM_NOME+" VARCHAR(25),"+
                SERIE_QUIMICA+" VARCHAR(30),"+
                GRUPO_ELEMENTO+" VARCHAR(25),"+
                CONFIGURACAO_ELETRONICA+" VARCHAR(25),"+
                ELETRONS+" TINYINT(3),"+
                ESTADO_OXIDACAO+" TINYINT (2),"+
                ESTADO_MATERIA+" VARCHAR(20),"+
                ESTADO_FUSAO_ELEMENTO+" VARCHAR(20),"+
                ESTADO_EBULICAO_ELEMENTO+" VARCHAR(20)"+")";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_INFORMACOES_COMPOSTO);
        sql = "CREATE TABLE "+TABELA_INFORMACOES_COMPOSTO+
                "("+_ID_INFORMACOES_COMPOSTO+" integer primary key autoincrement," +
                NOME_COMPOSTO+" VARCHAR(40),"+
                DENSIDADE_RELATIVA+" VARCHAR(15),"+
                ESTADO_FUSAO_COMPOSTO+" VARCHAR(20),"+
                ESTADO_EBULICAO_COMPOSTO+" VARCHAR(20),"+
                MASSA_MOLAR+" VARCHAR(30),"+
                SOLUBILIDADE+" VARCHAR(60),"+
                APARENCIA_COMPOSTO+" VARCHAR(40),"+
                ESTRUTURA+" VARCHAR(20),"+
                PONTO_FULGOR+" VARCHAR(25))";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ELEMENTO_INFORMACOES);
        sql = "CREATE TABLE "+TABELA_ELEMENTO_INFORMACOES+
                "("+ FK_ELEMENTO_DETALHADO+" INT(5),"+
                FK_INFORMACOES_COMPOSTO+" INT(5),"+
                "FOREIGN KEY ("+FK_ELEMENTO_DETALHADO+") REFERENCES " + TABELA_ELEMENTO_DETALHADO +"("+_ID_ELEMENTO_DETALHADO + "), "+
                "FOREIGN KEY ("+FK_INFORMACOES_COMPOSTO+") REFERENCES " + TABELA_INFORMACOES_COMPOSTO +"("+ _ID_INFORMACOES_COMPOSTO+"))";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIO);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_RESUMO);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PERGUNTA);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_CONTEUDO);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_HIDROCARBONETO);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_MOLECULA);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ELEMENTO_DETALHADO);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_INFORMACOES_COMPOSTO);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ELEMENTO_INFORMACOES);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_DESEMPENHO_CONTEUDO);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_DESEMPENHO_QUESTIONARIO);
        //onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABELA_NIVEL_CONTEUDO);
        onCreate(db);
    }


    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public static String getNomeBanco() {
        return NOME_BANCO;
    }

    public static int getVERSAO() { return VERSAO; }

    public static String getTabelaResumo() { return TABELA_RESUMO; }

    public static String getIdResumo() { return _ID_RESUMO; }

    public static String getTituloResumo() { return TITULO_RESUMO; }

    public static String getTextoResumo() { return TEXTO_RESUMO; }

    public static String getSubtituloResumo() { return SUBTITULO_RESUMO; }

    public static String getFormulaResumo() { return FORMULA_RESUMO; }

    public static String getFkConteudoResumo() { return FK_CONTEUDO_RESUMO; }

    public static String getTabelaPergunta() { return TABELA_PERGUNTA; }

    public static String getIdPergunta() { return _ID_PERGUNTA; }

    public static String getEnunciadoPergunta() { return ENUNCIADO_PERGUNTA; }

    public static String getOpcaoA() { return OPCAO_A; }

    public static String getOpcaoB() { return OPCAO_B; }

    public static String getOpcaoC() { return OPCAO_C; }

    public static String getOpcaoD() { return OPCAO_D; }

    public static String getOpcaoE() { return OPCAO_E; }

    public static String getImagemPergunta() { return IMAGEM_PERGUNTA; }

    public static String getTestePrevio() { return TESTE_PREVIO; }

    public static String getFkConteudoPergunta() { return FK_CONTEUDO_PERGUNTA; }

    public static String getAlternativaCorreta() { return ALTERNATIVA_CORRETA; }

    public static String getNivelDificuldade() { return NIVEL_DIFICULDADE; }

    public static String getTabelaConteudo() { return TABELA_CONTEUDO; }

    public static String getIdConteudo() { return _ID_CONTEUDO; }

    public static String getNomeConteudo() { return NOME_CONTEUDO; }

    public static String getTipoConteudo() { return TIPO_CONTEUDO; }

    public static String getTabelaDesempenhoConteudo() { return TABELA_DESEMPENHO_CONTEUDO; }

    public static String getIdDesempenhoConteudo() { return _ID_DESEMPENHO_CONTEUDO; }

    public static String getFkConteudoDesempenhoConteudo() { return FK_CONTEUDO_DESEMPENHO_CONTEUDO; }

    public static String getQuantidadePerguntas() { return QUANTIDADE_PERGUNTAS; }

    public static String getQuantidadeAcertos() { return QUANTIDADE_ACERTOS; }

    public static String getQuantidadeErros() { return QUANTIDADE_ERROS; }

    public static String getPontuacaoConteudo() { return PONTUACAO_CONTEUDO; }

    public static String getFkDesempenhoQuestionario() { return FK_DESEMPENHO_QUESTIONARIO; }

    public static String getTabelaDesempenhoQuestionario() { return TABELA_DESEMPENHO_QUESTIONARIO; }

    public static String getIdDesempenhoQuestionario() { return _ID_DESEMPENHO_QUESTIONARIO; }

    public static String getDataQuestionario() { return DATA_QUESTIONARIO; }

    public static String getPontuacaoQuestionario() { return PONTUACAO_QUESTIONARIO; }

    public static String getTabelaHidrocarboneto() { return TABELA_HIDROCARBONETO; }

    public static String getIdHidrocarboneto() { return _ID_HIDROCARBONETO; }

    public static String getNomeHidrocarboneto() { return NOME_HIDROCARBONETO; }

    public static String getDescricaoHidrocarboneto() { return DESCRICAO_HIDROCARBONETO; }

    public static String getTabelaMolecula() { return TABELA_MOLECULA; }

    public static String getIdMolecula() { return _ID_MOLECULA; }

    public static String getNomeMolecula() { return NOME_MOLECULA; }

    public static String getImagemMolecula() { return IMAGEM_MOLECULA; }

    public static String getFkHidrocarboneto() { return FK_HIDROCARBONETO; }

    public static String getTabelaElementoDetalhado() { return TABELA_ELEMENTO_DETALHADO; }

    public static String getIdElementoDetalhado() { return _ID_ELEMENTO_DETALHADO; }

    public static String getImagemElemento() { return IMAGEM_ELEMENTO; }

    public static String getNumeroAtomico() { return NUMERO_ATOMICO; }

    public static String getMassaAtomica() { return MASSA_ATOMICA; }

    public static String getVolumeMolar() { return VOLUME_MOLAR; }

    public static String getAparenciaElemento() { return APARENCIA_ELEMENTO; }

    public static String getNomeElemento() { return NOME_ELEMENTO; }

    public static String getNomeCientifico() { return NOME_CIENTIFICO; }

    public static String getOrigemNome() { return ORIGEM_NOME; }

    public static String getSerieQuimica() { return SERIE_QUIMICA; }

    public static String getGrupoElemento() { return GRUPO_ELEMENTO; }

    public static String getConfiguracaoEletronica() { return CONFIGURACAO_ELETRONICA; }

    public static String getELETRONS() { return ELETRONS; }

    public static String getEstadoOxidacao() { return ESTADO_OXIDACAO; }

    public static String getEstadoMateria() { return ESTADO_MATERIA; }

    public static String getEstadoFusaoElemento() { return ESTADO_FUSAO_ELEMENTO; }

    public static String getEstadoEbulicaoElemento() { return ESTADO_EBULICAO_ELEMENTO; }

    public static String getTabelaElementoInformacoes() { return TABELA_ELEMENTO_INFORMACOES; }

    public static String getFkElementoDetalhado() { return FK_ELEMENTO_DETALHADO; }

    public static String getFkInformacoesComposto() { return FK_INFORMACOES_COMPOSTO; }

    public static String getTabelaInformacoesComposto() { return TABELA_INFORMACOES_COMPOSTO; }

    public static String getIdInformacoesComposto() { return _ID_INFORMACOES_COMPOSTO; }

    public static String getNomeComposto() { return NOME_COMPOSTO; }

    public static String getDensidadeRelativa() { return DENSIDADE_RELATIVA; }

    public static String getEstadoFusaoComposto() { return ESTADO_FUSAO_COMPOSTO; }

    public static String getEstadoEbulicaoComposto() { return ESTADO_EBULICAO_COMPOSTO; }

    public static String getMassaMolar() { return MASSA_MOLAR; }

    public static String getSOLUBILIDADE() { return SOLUBILIDADE; }

    public static String getAparenciaComposto() { return APARENCIA_COMPOSTO; }

    public static String getESTRUTURA() { return ESTRUTURA; }

    public static String getPontoFulgor() { return PONTO_FULGOR; }

    public static String getTabelaUsuario() { return TABELA_USUARIO; }

    public static String getIdUsuario() { return _ID_USUARIO; }

    public static String getNomeUsuario() { return NOME_USUARIO; }

    public static String getNomeCompletoUsuario() { return NOME_COMPLETO_USUARIO; }

    public static String getSenhaUsuario() { return SENHA_USUARIO; }

    public static String getEMAIL() { return EMAIL; }

    public static String getCpfUsuario() { return CPF_USUARIO; }

    public static String getFkUsuarioQuestionario() { return FK_USUARIO_QUESTIONARIO; }

    public static String getTabelaNivelConteudo() { return TABELA_NIVEL_CONTEUDO; }

    public static String getIdNivelConteudo() { return _ID_NIVEL_CONTEUDO; }

    public static String getFkUsuarioNivel() { return FK_USUARIO_NIVEL; }

    public static String getFkConteudoNivel() { return FK_CONTEUDO_NIVEL; }

    public static String getNIVEL() { return NIVEL; }

    public static String getQuestaoVestibular() { return QUESTAO_VESTIBULAR; }

    public static String getTipoDesempenho() {
        return TIPO_DESEMPENHO;
    }

    public static String getQUANTIDADE() {
        return QUANTIDADE;
    }

    public static String getPRECISAO() {
        return PRECISAO;
    }

    public static String getImagemNivel() {
        return IMAGEM_NIVEL;
    }

    public static String getTENTATIVAS() {
        return TENTATIVAS;
    }

    public static String getVIDAS() {
        return VIDAS;
    }

    public static String getSubiuOuDesceuNivel() {
        return SUBIU_OU_DESCEU_NIVEL;
    }

    public static String getDataAtualizacaoNivel() {
        return DATA_ATUALIZACAO_NIVEL;
    }

    public static String getDataUltimoTeste() {
        return DATA_ULTIMO_TESTE;
    }

    public static String getMediaAcertosUltimosQuestionarios() {
        return MEDIA_ACERTOS_ULTIMOS_QUESTIONARIOS;
    }

    public static String getFkUltimoDesempenhoConteudo() {
        return FK_ULTIMO_DESEMPENHO_CONTEUDO;
    }
}
