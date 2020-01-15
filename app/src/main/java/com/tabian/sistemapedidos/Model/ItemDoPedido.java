package com.tabian.sistemapedidos.Model;

public class ItemDoPedido {
    Produto produto;
    Pedido pedido;
    int quantidade;

    public ItemDoPedido(Produto produto, Pedido pedido, int quantidade){
        this.produto = produto;
        this.pedido = pedido;
        this.quantidade = quantidade;
    }

    public Produto getProduto(){
        return produto;
    }

    public void setProduto(Produto produto){
        this.produto = produto;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }

    public Pedido getPedido(){
        return pedido;
    }

    public void setPedido(Pedido p){
        this.pedido = p;
    }
}

