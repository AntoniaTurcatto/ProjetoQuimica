package com.example.user.classesDominio;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.Toast;

import com.example.user.componente.NivelConteudoEnum;
import com.example.user.projetoquimica.R;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.user.componente.NivelConteudoEnum.BRONZE;

public class NivelConteudo implements Serializable {
    private int idNivelConteudo;
    private NivelConteudoEnum nivel;
    private NivelConteudoEnum nivelAnterior; //0=desceu; 1=subiu; -1=primeiro ranking
    private Date dataUltimoTeste; //dd/MM/yyyy HH:mm:ss OU -1(caso não exista)
    private Date dataAtualizacaoNivel; //dd/MM/yyyy HH:mm:ss OU -1(caso não exista)
    private DesempenhoConteudo ultimoDesempenhoConteudo;
    private Usuario usuario;
    private Conteudo conteudo;
    private Image imagem;
    private int tentativas; //PEDRO ADICIONEI NUMERO DE TENTATIVAS
    private int vidas; //PEDRO - adicionei número de vidas restantes, por enquanto é um int, podemos mudar para float no futuro

    //construtor 1 - sem id
    public NivelConteudo(NivelConteudoEnum nivel, int subiuOuDesceuNivel, Date dataUltimoTeste, Date dataAtualizacaoNivel, DesempenhoConteudo ultimoDesempenhoConteudo, Usuario usuario, Conteudo conteudo, int tentativas, int vidas) {
        this.idNivelConteudo = -1;
        this.nivel = nivel;
        this.usuario = usuario;
        this.conteudo = conteudo;
        this.tentativas = tentativas;
        this.vidas = vidas;
        //definindo o nivel anterior
        if (subiuOuDesceuNivel == 1){ //subiu de nivel
            switch (nivel.getValor()){
                case 2: this.nivelAnterior = NivelConteudoEnum.COBRE; //cobre =1 (anterior pois subiu de nivel)
                case 3: this.nivelAnterior = NivelConteudoEnum.BRONZE;//bronze =2 (anterior pois subiu de nivel)
                case 4: this.nivelAnterior = NivelConteudoEnum.PRATA; //prata =3 (anterior pois subiu de nivel)
                case 5: this.nivelAnterior = NivelConteudoEnum.OURO;//ouro =4 (anterior pois subiu de nivel)
                default: this.nivelAnterior = NivelConteudoEnum.COBRE;
            }
        } else if (subiuOuDesceuNivel == -1){ //desceu de nível
            switch (nivel.getValor()){
                case 1: this.nivelAnterior = NivelConteudoEnum.BRONZE; //bronze =2 (anterior pois caiu de nivel)
                case 2: this.nivelAnterior = NivelConteudoEnum.PRATA;//prata =3 (anterior pois caiu de nivel)
                case 3: this.nivelAnterior = NivelConteudoEnum.OURO; //ouro =4 (anterior pois caiu de nivel)
                case 4: this.nivelAnterior = NivelConteudoEnum.DIAMANTE;//diamante =5 (anterior pois caiu de nivel)
                default: this.nivelAnterior = NivelConteudoEnum.COBRE;
            }
        } else { //Não tem nível anterior (subiuOuDesceuNivel == 0)
            this.nivelAnterior = nivel;
        }
        this.dataAtualizacaoNivel = dataAtualizacaoNivel;
        this.dataUltimoTeste = dataUltimoTeste;
        this.ultimoDesempenhoConteudo = ultimoDesempenhoConteudo;
    }

    //construtor 2 - com id
    public NivelConteudo(int idNivelConteudo, NivelConteudoEnum nivel, int subiuOuDesceuNivel, Date dataUltimoTeste, Date dataAtualizacaoNivel, DesempenhoConteudo ultimoDesempenhoConteudo, Usuario usuario, Conteudo conteudo, int tentativas, int vidas) {
        this.idNivelConteudo = idNivelConteudo;
        this.nivel = nivel;
        this.usuario = usuario;
        this.conteudo = conteudo;
        this.tentativas = tentativas;
        this.vidas = vidas;
        this.ultimoDesempenhoConteudo = ultimoDesempenhoConteudo;

        if (subiuOuDesceuNivel == 1){ //subiu de nivel
            switch (nivel.getValor()){
                case 2: this.nivelAnterior = NivelConteudoEnum.COBRE; //cobre =1 (anterior pois subiu de nivel)
                case 3: this.nivelAnterior = NivelConteudoEnum.BRONZE;//bronze =2 (anterior pois subiu de nivel)
                case 4: this.nivelAnterior = NivelConteudoEnum.PRATA; //prata =3 (anterior pois subiu de nivel)
                case 5: this.nivelAnterior = NivelConteudoEnum.OURO;//ouro =4 (anterior pois subiu de nivel)
                default: this.nivelAnterior = NivelConteudoEnum.COBRE;
            }
        } else if (subiuOuDesceuNivel == -1){ //desceu de nível
            switch (nivel.getValor()){
                case 1: this.nivelAnterior = NivelConteudoEnum.BRONZE; //bronze =2 (anterior pois caiu de nivel)
                case 2: this.nivelAnterior = NivelConteudoEnum.PRATA;//prata =3 (anterior pois caiu de nivel)
                case 3: this.nivelAnterior = NivelConteudoEnum.OURO; //ouro =4 (anterior pois caiu de nivel)
                case 4: this.nivelAnterior = NivelConteudoEnum.DIAMANTE;//diamante =5 (anterior pois caiu de nivel)
                default: this.nivelAnterior = NivelConteudoEnum.COBRE;
            }
        } else { //Não tem nível anterior (subiuOuDesceuNivel == 0)
            this.nivelAnterior = nivel;
        }
        this.dataAtualizacaoNivel = dataAtualizacaoNivel;
        this.dataUltimoTeste = dataUltimoTeste;
    }

    public NivelConteudo(NivelConteudoEnum nivel, Conteudo conteudo, Image imagem) {
        this.nivel = nivel;
        this.conteudo = conteudo;
        this.imagem = imagem;
    }

    // método para incrementar UM nível
    // retorno: 0- erro ao atualizar, 1- atualizado e 2- não atualizou por estar no último nível
    // método retorna quantidade de níveis incrementados
    public int incrementaUmNivel() {
        int retorno = -1;
        NivelConteudoEnum novoNivel = obtemIncrementoUmNivel();
        if (this.nivel != NivelConteudoEnum.DIAMANTE){
            this.nivelAnterior= nivel;
            this.nivel = novoNivel;
            retorno = 1;
        } else {
            retorno = 0;
        }
        return retorno;
    }
    //PEDRO DECAINIVEL
    public int decaiUmNivel(){
        int retorno =-1;
        NivelConteudoEnum novoNivel = obtemDecaiUmNivel();
        if (this.nivel != NivelConteudoEnum.COBRE){
            this.nivelAnterior= nivel;
            this.nivel = novoNivel;
            retorno = 1;
        } else {
            retorno = 0;
        }
        return retorno;
    }

    // obtem incrementaUmNivel
    public NivelConteudoEnum obtemIncrementoUmNivel() {
        NivelConteudoEnum retorno = null;
        if (this.nivel == NivelConteudoEnum.COBRE) {
            retorno = NivelConteudoEnum.BRONZE;
        } else if (this.nivel == BRONZE) {
            retorno = NivelConteudoEnum.PRATA;
        } else if (this.nivel == NivelConteudoEnum.PRATA) {
            retorno = NivelConteudoEnum.OURO;
        } else if (this.nivel == NivelConteudoEnum.OURO) {
            retorno = NivelConteudoEnum.DIAMANTE;
        } else if (this.nivel == NivelConteudoEnum.DIAMANTE) {
            retorno = null;
        }
        return retorno;
    }

    // método para o diagnóstico (incremento)
    // lembrar que pela regra do diagnostico nem sempre irá incrementar dois níveis (ex: quando estiver
    // no nível ouro)
    // retorno: 0- erro ao atualizar, 1- incrementou dois níveis, 2- incrementou um nível
    // método retorna quantidade de níveis incrementados
    public int incrementaDoisNiveis() {
        int retorno = 0;
        if (this.nivel == NivelConteudoEnum.COBRE || this.nivel == BRONZE || this.nivel == NivelConteudoEnum.PRATA){
            NivelConteudoEnum novoNivel = obtemIncrementoDoisNiveis();
            this.nivel = novoNivel;
            retorno = 2;
        } else if (this.nivel == NivelConteudoEnum.OURO) {
            NivelConteudoEnum novoNivel = obtemIncrementoUmNivel();
            this.nivel = novoNivel;
            retorno = 1;
        } else {
            retorno = 0;
        }
        return retorno;
    }

    // obtem incrementaDoisNiveis
    public NivelConteudoEnum obtemIncrementoDoisNiveis() {
        NivelConteudoEnum retorno = null;
        if (this.nivel == NivelConteudoEnum.COBRE) {
            retorno = NivelConteudoEnum.PRATA;
        } else if (this.nivel == BRONZE) {
            retorno = NivelConteudoEnum.OURO;
        } else if (this.nivel == NivelConteudoEnum.PRATA) {
            retorno = NivelConteudoEnum.DIAMANTE;
        } else if (this.nivel == NivelConteudoEnum.OURO) {
            retorno = NivelConteudoEnum.DIAMANTE;
        } else if (this.nivel == NivelConteudoEnum.DIAMANTE) {
            retorno = null;
        }
        return retorno;
    }
    //PEDRO OBTEMDECAINIVEL
    public NivelConteudoEnum obtemDecaiUmNivel(){
        NivelConteudoEnum retorno = null;
        if (this.nivel == NivelConteudoEnum.DIAMANTE){
            retorno = NivelConteudoEnum.OURO;
        } else if (this.nivel == NivelConteudoEnum.OURO){
            retorno = NivelConteudoEnum.PRATA;
        } else if (this.nivel == NivelConteudoEnum.PRATA){
            retorno = NivelConteudoEnum.BRONZE;
        } else if (this.nivel == NivelConteudoEnum.BRONZE){
            retorno = NivelConteudoEnum.COBRE;
        } else {
            retorno = null;
        }
        return retorno;
    }

    public Drawable getImagemUpgrade(Context context){
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_upgrade);
        return drawable;
    }

    //retorna a imagem conforme o nivel (ícone)
    public Drawable getImagemNivel(Context context){
        Drawable drawable = null;
        if (this.nivel == NivelConteudoEnum.COBRE) {
            drawable = context.getResources().getDrawable(R.drawable.ic_cobre); /*imagem ícone cobre*/
        } else if (this.nivel == BRONZE) {
            drawable = context.getResources().getDrawable(R.drawable.ic_bronze); /*imagem ícone bronze*/
        } else if (this.nivel == NivelConteudoEnum.PRATA) {
            drawable = context.getResources().getDrawable(R.drawable.ic_prata); /*imagem ícone prata*/
        } else if (this.nivel == NivelConteudoEnum.OURO) {
            drawable = context.getResources().getDrawable(R.drawable.ic_ouro); /*imagem ícone ouro*/
        } else if (this.nivel == NivelConteudoEnum.DIAMANTE) {
            drawable = context.getResources().getDrawable(R.drawable.ic_diamante); /*imagem ícone diamante*/
        }
        return drawable;
    }


    // retorna o ícone do nivel conteudo (ENUM) recebido como parâmetro
    public Drawable getImagemNivelParametro(NivelConteudoEnum nivelParametro, Context context) {
        Drawable drawable = null;
        if (nivelParametro == NivelConteudoEnum.COBRE) {
            drawable = context.getResources().getDrawable(R.drawable.ic_cobre); /*imagem ícone cobre*/
        } else if (nivelParametro == BRONZE) {
            drawable = context.getResources().getDrawable(R.drawable.ic_bronze); /*imagem ícone cobre*/
        } else if (nivelParametro == NivelConteudoEnum.PRATA) {
            drawable = context.getResources().getDrawable(R.drawable.ic_prata); /*imagem ícone bronze*/
        } else if (nivelParametro == NivelConteudoEnum.OURO) {
            drawable = context.getResources().getDrawable(R.drawable.ic_ouro); /*imagem ícone ouro*/
        } else if (nivelParametro == NivelConteudoEnum.DIAMANTE) {
            drawable = context.getResources().getDrawable(R.drawable.ic_diamante); /*imagem ícone diamante*/
        }
        return drawable;
    }

    //retorna a imagem conforme o nivel (lateralizado)
    public Drawable getImagemNivelAlternativo(Context context){
        Drawable drawable = null;
        if (this.nivel == NivelConteudoEnum.COBRE) {
            drawable = context.getResources().getDrawable(R.drawable.vis_nivel_cobre); /*imagem ícone cobre*/
        } else if (this.nivel == BRONZE) {
            drawable = context.getResources().getDrawable(R.drawable.vis_nivel_bronze); /*imagem ícone bronze*/
        } else if (this.nivel == NivelConteudoEnum.PRATA) {
            drawable = context.getResources().getDrawable(R.drawable.vis_nivel_prata); /*imagem ícone prata*/
        } else if (this.nivel == NivelConteudoEnum.OURO) {
            drawable = context.getResources().getDrawable(R.drawable.vis_nivel_ouro); /*imagem ícone ouro*/
        } else if (this.nivel == NivelConteudoEnum.DIAMANTE) {
            drawable = context.getResources().getDrawable(R.drawable.vis_nivel_diamante); /*imagem ícone diamante*/
        }
        return drawable;
    }

    //retorna a imagem conforme o nivel (caminho)
    public Drawable getImagemNivelCaminho(Context context){
        Drawable drawable = null;
        if (this.nivel == NivelConteudoEnum.COBRE) {
            drawable = context.getResources().getDrawable(R.drawable.caminho_cobre); /*imagem ícone cobre*/
        } else if (this.nivel == BRONZE) {
            drawable = context.getResources().getDrawable(R.drawable.caminho_bronze); /*imagem ícone bronze*/
        } else if (this.nivel == NivelConteudoEnum.PRATA) {
            drawable = context.getResources().getDrawable(R.drawable.caminho_prata); /*imagem ícone prata*/
        } else if (this.nivel == NivelConteudoEnum.OURO) {
            drawable = context.getResources().getDrawable(R.drawable.caminho_ouro); /*imagem ícone ouro*/
        } else if (this.nivel == NivelConteudoEnum.DIAMANTE) {
            drawable = context.getResources().getDrawable(R.drawable.caminho_diamante); /*imagem ícone diamante*/
        }
        return drawable;
    }

    public Drawable getImagemVidasConteudo(Context context){
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_atomovida5);

        if (this.vidas == 1){
            drawable = context.getResources().getDrawable(R.drawable.ic_atomovida1);
        }else if (this.vidas == 2){
            drawable = context.getResources().getDrawable(R.drawable.ic_atomovida2);
        }else if (this.vidas == 3){
            drawable = context.getResources().getDrawable(R.drawable.ic_atomovida3);
        }else if (this.vidas == 4){
            drawable = context.getResources().getDrawable(R.drawable.ic_atomovida4);
        }
        return drawable;
    }

    public int getIdNivelConteudo() { return idNivelConteudo; }

    public void setIdNivelConteudo(int idNivelConteudo) { this.idNivelConteudo = idNivelConteudo; }

    public NivelConteudoEnum getNivel() { return nivel; }

    public void setNivel(NivelConteudoEnum nivel) { this.nivel = nivel; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Conteudo getConteudo() { return conteudo; }

    public void setConteudo(Conteudo conteudo) { this.conteudo = conteudo; }

    public Image getImagem() {
        return imagem;
    }

    public void setImagem(Image imagem) {
        this.imagem = imagem;
    }

    public int getTentativas() {
        return tentativas;
    }

    public void setTentativas(int tentativas) {
        this.tentativas = tentativas;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public Date getDataUltimoTeste() {
        return dataUltimoTeste;
    }

    public void setDataUltimoTeste(String dataUltimoTeste) {
        //definindo a data conforme a string
        try {
            if (!dataUltimoTeste.equals("")){
                this.dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataUltimoTeste);
            } else {
                this.dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/0001 00:00:00");
            }
        } catch(ParseException parse) {
            parse.printStackTrace();
        }
    }

    public void setDataUltimoTeste(Date dataUltimoTeste) {
        this.dataUltimoTeste = dataUltimoTeste;
    }

    public Date getDataAtualizacaoNivel() {
        return dataAtualizacaoNivel;
    }

    public void setDataAtualizacaoNivel(String dataAtualizacaoNivel) {
        //definindo a data conforme a string
        try {
            if (!dataAtualizacaoNivel.equals("")){
                this.dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataAtualizacaoNivel);
            } else {
                this.dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/0001 00:00:00");
            }
        } catch(ParseException parse) {
            parse.printStackTrace();
        }
    }

    public void setDataAtualizacaoNivel(Date dataAtualizacaoNivel) {
        this.dataAtualizacaoNivel = dataAtualizacaoNivel;
    }

    public NivelConteudoEnum getNivelAnterior() {
        return nivelAnterior;
    }

    public void setNivelAnterior(NivelConteudoEnum nivelAnterior) {
        this.nivelAnterior = nivelAnterior;
    }

    public DesempenhoConteudo getUltimoDesempenhoConteudo() {
        return ultimoDesempenhoConteudo;
    }

    public void setUltimoDesempenhoConteudo(DesempenhoConteudo ultimoDesempenhoConteudo) {
        this.ultimoDesempenhoConteudo = ultimoDesempenhoConteudo;
    }
}


