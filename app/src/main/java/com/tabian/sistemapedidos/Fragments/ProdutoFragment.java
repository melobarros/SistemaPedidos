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
import com.tabian.sistemapedidos.Model.Produto;
import com.tabian.sistemapedidos.R;
import com.tabian.sistemapedidos.Services.alphaws;

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

public class ProdutoFragment extends Fragment {
    private static final String TAG = "ProdutoFragment";

    private Button btnSave;
    private Button btnSelect;
    private Button btnDelete;
    private List<Produto> produtos = new ArrayList<>();
    private TextView listaProdutos_textView;
    private EditText descricao_editText;
    private EditText id_produto_editText;
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
        View view = inflater.inflate(R.layout.produto_fragment,container,false);
        listaProdutos_textView = (TextView) view.findViewById(R.id.produtosList_textView);
        descricao_editText = (EditText) view.findViewById(R.id.descricao_produto_editText);
        id_produto_editText = (EditText) view.findViewById(R.id.id_Produto_editText);
        btnSave = (Button) view.findViewById(R.id.btnSalvarProduto);
        btnSelect = (Button) view.findViewById(R.id.btnSelectProduto);
        btnDelete = (Button) view.findViewById(R.id.btnDeleteProduto);

        initProdutos();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING PRODUTO CLICK",Toast.LENGTH_SHORT).show();
                if(id_produto_editText.getText().toString().equals("")){ //ID NAO PREENCHIDO
                    saveProduto();
                } else{ // ID PREENCHIDO
                    updateProduto();
                }
                initProdutos();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!id_produto_editText.getText().toString().equals("")){ //ID PREENCHIDO
                    deleteProduto();
                    initProdutos();
                }
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!id_produto_editText.getText().toString().equals("")){ //ID PREENCHIDO
                    for(Produto p : produtos){
                        if(p.getId() == Integer.parseInt(id_produto_editText.getText().toString())){
                            descricao_editText.setText(p.getDescricao());
                        }
                    }
                }
            }
        });

        return view;
    }

    private void initProdutos(){
        descricao_editText.setText("");
        id_produto_editText.setText("");
        listaProdutos_textView.setText("");

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

                        listaProdutos_textView.append(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                Log.e(TAG, "Erro: " + t.getMessage());
            }
        });
    }

    private void saveProduto(){

        Produto p = new Produto(descricao_editText.getText().toString());

        final Call<String> requestInsert = alphaws.insertProduto(p);
        requestInsert.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    Toast.makeText(getActivity(), "NAO DEU BOA. " + response.toString(),Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    if (response.body().equals("OK")) {
                        Toast.makeText(getActivity(), "PRODUTO INSERIDO COM SUCESSO",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Cliente inserido. " + response.raw());
                        initProdutos();
                    } else{
                        Toast.makeText(getActivity(), "PRODUTO AO INSERIR CLIENTE",Toast.LENGTH_SHORT).show();
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

    private void updateProduto(){
        Produto p = new Produto(descricao_editText.getText().toString());

        p.setId(Integer.parseInt(id_produto_editText.getText().toString()));

        Call<String> requestUpdate = alphaws.updateProduto(p);
        requestUpdate.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    return;
                } else{
                    if (response.body().equals("OK")) {
                        Toast.makeText(getActivity(), "PRODUTO ATUALIZADO COM SUCESSO",Toast.LENGTH_SHORT).show();
                        initProdutos();
                    } else{
                        Toast.makeText(getActivity(), "FALHA AO ATUALIZAR PRODUTO",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "UPDATE - FALHA NA COMUNICACAO COM SERVIDOR",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: Erro: " + t.getMessage());
            }
        });
    }

    private void deleteProduto(){

        int id = Integer.parseInt(id_produto_editText.getText().toString());

        Call<String> requestDelete = alphaws.deleteProduto(id);
        requestDelete.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    return;
                } else{
                    if (response.body().equals("OK")) {
                        Toast.makeText(getActivity(), "PRODUTO DELETADO COM SUCESSO",Toast.LENGTH_SHORT).show();
                        initProdutos();
                    } else{
                        Toast.makeText(getActivity(), "FALHA AO DELETAR PRODUTO",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "DELETE - FALHA NA COMUNICACAO COM SERVIDOR",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: Erro: " + t.getMessage());
            }
        });
    }
}
