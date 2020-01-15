package com.tabian.sistemapedidos.Services;

import com.tabian.sistemapedidos.Model.Cliente;
import com.tabian.sistemapedidos.Model.ItemDoPedido;
import com.tabian.sistemapedidos.Model.Pedido;
import com.tabian.sistemapedidos.Model.Produto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface alphaws {

    public static final String BASE_URL = "http://192.168.43.211:8080";

    @GET("/clientes")
    Call<List<Cliente>> getClientes();

    @GET("/clientes/{id}")
    Call<Cliente> getClienteById(@Path("id") int id);

    @POST("/clientes")
    Call<String> insertCliente(@Body Cliente cliente);

    @PUT("/clientes")
    Call<String> updateCliente(@Body Cliente cliente);

    @DELETE("/clientes/{id}")
    Call<String> deleteCliente(@Path("id") int id);

    //------------------------------------------------------

    @GET("/produtos")
    Call<List<Produto>> getProdutos();

    @GET("/produtos/{id}")
    Call<Produto> getProdutoById(@Path("id") int id);

    @POST("/produtos")
    Call<String> insertProduto(@Body Produto produto);

    @PUT("/produtos")
    Call<String> updateProduto(@Body Produto produto);

    @DELETE("/produtos/{id}")
    Call<String> deleteProduto(@Path("id") int id);

    // -------------------------------------------------------

    @GET("/pedidos")
    Call<List<Pedido>> getPedidos();

    @GET("/pedidos/{id}")
    Call<Pedido> getPedidoById();

    @GET("/pedidos/cpf/{cpf}")
    Call<List<ItemDoPedido>> getPedidoByCpf(@Path("cpf") String cpf);

    @POST("/pedidos")
    Call<String> insertPedido(@Body Pedido pedido);

    // -------------------------------------------------------

    @POST("/item")
    Call<String> insertItem(@Body ItemDoPedido ip);
}

