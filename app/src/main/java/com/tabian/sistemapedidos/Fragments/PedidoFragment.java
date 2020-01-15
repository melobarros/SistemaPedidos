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
import com.tabian.sistemapedidos.Model.ItemDoPedido;
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

public class PedidoFragment extends Fragment {
    private static final String TAG = "ProdutoFragment";

    private Button btnListar;
    private List<ItemDoPedido> items = new ArrayList<>();
    private TextView listaItems_textView;
    private EditText cpf_pedido_textView;
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
        View view = inflater.inflate(R.layout.pedido_fragment,container,false);
        listaItems_textView = (TextView) view.findViewById(R.id.listaItems_textView);
        cpf_pedido_textView = (EditText) view.findViewById(R.id.cpf_pedido_editText);
        btnListar = (Button) view.findViewById(R.id.listar_items_button);

        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING PEDIDO CLICK",Toast.LENGTH_SHORT).show();
                if(cpf_pedido_textView.getText().toString().equals("")){ //ID NAO PREENCHIDO
                    Toast.makeText(getActivity(), "INSIRA UM CPF",Toast.LENGTH_SHORT).show();
                } else{ // ID PREENCHIDO
                    getItems();
                }
            }
        });

        return view;
    }

    private void getItems(){

        final Call<List<ItemDoPedido>> requestInsert = alphaws.getPedidoByCpf(cpf_pedido_textView.getText().toString());
        requestInsert.enqueue(new Callback<List<ItemDoPedido>>(){
            @Override
            public void onResponse(Call<List<ItemDoPedido>> call, Response<List<ItemDoPedido>> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    Toast.makeText(getActivity(), "NAO DEU BOA. " + response.toString(),Toast.LENGTH_SHORT).show();
                    return;
                } else{
                        Toast.makeText(getActivity(), "LISTADO COM SUCESSO",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Pedido listado. " + response.raw());
                        items = response.body();
                        listaItems_textView.setText("Produto \t\t Qtde\n--------------------------------------------------\n");
                        for(ItemDoPedido ip : items){
                            String content = "";
                            content += ip.getProduto().getDescricao() + "\t\t\n";
                            content += ip.getQuantidade() + "\n\n";
                            listaItems_textView.append(content);

                        }
                }
            }

            @Override
            public void onFailure(Call<List<ItemDoPedido>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro: " + t.toString(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: Erro: " + t.toString() + "Call: " + call.toString() + " - T: " + t.getLocalizedMessage() + "Request: " + requestInsert.toString());
            }
        });
    }
}
