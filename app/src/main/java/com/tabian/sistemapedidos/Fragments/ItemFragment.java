package com.tabian.sistemapedidos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tabian.sistemapedidos.Model.Cliente;
import com.tabian.sistemapedidos.Model.ItemDoPedido;
import com.tabian.sistemapedidos.Model.Pedido;
import com.tabian.sistemapedidos.Model.Produto;
import com.tabian.sistemapedidos.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 2/28/2017.
 */

public class ItemFragment extends Fragment {
    private static final String TAG = "ItemFragment";

    private Button btnIncluir;
    private List<Produto> produtos = new ArrayList<>();
    private TextView listaProdutos_items_textView;
    private EditText cpf_items_editText;
    private EditText id_produto_items_editText;
    Retrofit retrofit;
    com.tabian.sistemapedidos.Services.alphaws alphaws;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(alphaws.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        alphaws = retrofit.create(com.tabian.sistemapedidos.Services.alphaws.class);

        Log.d(TAG, "onCreateView: started.");
        View view = inflater.inflate(R.layout.item_fragment,container,false);
        listaProdutos_items_textView = (TextView) view.findViewById(R.id.listaProdutos_items_textView);
        cpf_items_editText = (EditText) view.findViewById(R.id.cpf_item_editText);
        id_produto_items_editText = (EditText) view.findViewById(R.id.id_produto_item_editText);
        btnIncluir = (Button) view.findViewById(R.id.incluirItem_button);

        initProdutos();

        btnIncluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING ITEM CLICK",Toast.LENGTH_SHORT).show();
                incluirItem();
            }
        });

        return view;
    }

    private void initProdutos(){
        cpf_items_editText.setText("");
        id_produto_items_editText.setText("");
        listaProdutos_items_textView.setText("");

        Call<List<Produto>> requestProdutos = alphaws.getProdutos();

        requestProdutos.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    return;
                } else{
                    produtos = response.body();
                    for(Produto p : produtos){
                        String content = "";
                        content += "ID: " + p.getId() + "\n";
                        content += "Descricao: " + p.getDescricao() + "\n\n";

                        listaProdutos_items_textView.append(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                Log.e(TAG, "Erro: " + t.getMessage());
            }
        });
    }

    private void incluirItem(){

        Produto p = new Produto("Geladeira");
        Cliente c = new Cliente("Amauri", "Silva", "1111");
        Pedido pe = new Pedido("Data", c, null);
        ItemDoPedido ip = new ItemDoPedido(p, pe, 12);

        final Call<String> requestInsert = alphaws.insertItem(ip);
        requestInsert.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    Toast.makeText(getActivity(), "NAO DEU BOA. " + response.toString(),Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    if (response.body().equals("OK")) {
                        Toast.makeText(getActivity(), "ITEM INSERIDO COM SUCESSO",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Item inserido. " + response.raw());
                        initProdutos();
                    } else{
                        Toast.makeText(getActivity(), "ERRO AO INSERIR ITEM",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro: " + t.toString(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: Erro: " + t.toString() + "Call: " + call.toString() + " - T: " + t.getLocalizedMessage() + "Request: " + requestInsert.toString());
            }
        });
    }
}
