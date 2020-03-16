package com.valoresareceber.valoresareceber.Activity.Activity.Model;

public class Modelo {
    private int ID;
    private String Emissao;
    private String Nome;
    private String Fone;
    private String Valor;
    private String VCTO;
    private String valor_PGTO;



    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }



    public String getEmissao() {
        return Emissao;
    }

    public void setEmissao(String emissao) {
        Emissao = emissao;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getFone() {
        return Fone;
    }

    public void setFone(String fone) {
        Fone = fone;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getVCTO() {
        return VCTO;
    }

    public void setVCTO(String VCTO) {
        this.VCTO = VCTO;
    }

    public String getValor_PGTO() {
        return valor_PGTO;
    }

    public void setValor_PGTO(String valor_PGTO) {
        this.valor_PGTO = valor_PGTO;
    }

    public Modelo(int id, String emissao, String nome, String fone, String valor, String VCTO, String valor_PGTO) {
        ID = id;
        Emissao = emissao;
        Nome = nome;
        Fone = fone;
        Valor = valor;
        this.VCTO = VCTO;
        this.valor_PGTO = valor_PGTO;
    }
}
