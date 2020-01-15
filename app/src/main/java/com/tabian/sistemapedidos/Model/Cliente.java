package com.tabian.sistemapedidos.Model;

public class Cliente {
    int id = -1;
    String cpf;
    String nome;
    String sobrenome;

    public Cliente(String nome, String sobrenome, String cpf){
        this.cpf = cpf;
        this.nome = nome;
        this.sobrenome = sobrenome;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCpf(){
        return cpf;
    }

    public void setCpf(String cpf){
        this.cpf = cpf;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getSobrenome(){
        return sobrenome;
    }

    public void setSobrenome(String sobrenome){
        this.sobrenome = sobrenome;
    }
}
