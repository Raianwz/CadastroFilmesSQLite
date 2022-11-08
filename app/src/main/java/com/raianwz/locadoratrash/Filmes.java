package com.raianwz.locadoratrash;

public class Filmes {
    private int id;
    private String filme, genero, ano;

    public Filmes() {
    }

    public Filmes(int id, String filme, String genero, String ano) {
        this.id = id;
        this.filme = filme;
        this.genero = genero;
        this.ano = ano;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilme() {
        return filme;
    }

    public void setFilme(String filme) {
        this.filme = filme;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
}
