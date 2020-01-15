package com.tabian.sistemapedidos.Model;

import java.util.List;

public class Pedido {
    int id;
    String data;
    Cliente cliente;
    List<ItemDoPedido> itens;

    public Pedido(String data, Cliente cliente, List<ItemDoPedido> itens){
        this.data = data;
        this.cliente = cliente;
        this.itens = itens;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Cliente getCliente(){
        return cliente;
    }

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    public String getData(){
        return data;
    }

    public void setData(String data){
        this.data = data;
    }

    public List<ItemDoPedido> getItens(){
        return itens;
    }

    public void setItens(List<ItemDoPedido> itens){
        this.itens = itens;
    }
}
