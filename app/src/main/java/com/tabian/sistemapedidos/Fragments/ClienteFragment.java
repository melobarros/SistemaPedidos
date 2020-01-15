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

public class ClienteFragment extends Fragment {
    private static final String TAG = "ClienteFragment";

    private Button btnSave;
    private Button btnSelect;
    private Button btnDelete;
    private List<Cliente> clientes = new ArrayList<>();
    private TextView textView;
    private EditText nome_editText;
    private EditText sobrenome_editText;
    private EditText cpf_editText;
    private EditText id_editText;
    Retrofit retrofit;
    alphaws alphaws;




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
        View view = inflater.inflate(R.layout.cliente_fragment,container,false);
        textView = (TextView) view.findViewById(R.id.clientes_textView);
        nome_editText = (EditText) view.findViewById(R.id.nome_editText);
        sobrenome_editText = (EditText) view.findViewById(R.id.sobrenome_editText);
        cpf_editText = (EditText) view.findViewById(R.id.cpf_editText);
        id_editText = (EditText) view.findViewById(R.id.id_editText);
        btnSave = (Button) view.findViewById(R.id.btnSalvarCliente);
        btnSelect = (Button) view.findViewById(R.id.btnSelectCliente);
        btnDelete = (Button) view.findViewById(R.id.btnDeleteCliente);


        initClientes();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING CLIENTE CLICK",Toast.LENGTH_SHORT).show();
                if(id_editText.getText().toString().equals("")){ //ID NAO PREENCHIDO
                    saveCliente();
                } else{ // ID PREENCHIDO
                    updateCliente();
                }
                initClientes();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!id_editText.getText().toString().equals("")){ //ID PREENCHIDO
                    deleteCliente();
                    initClientes();
                }
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!id_editText.getText().toString().equals("")){ //ID PREENCHIDO
                    for(Cliente c : clientes){
                        if(c.getId() == Integer.parseInt(id_editText.getText().toString())){
                            nome_editText.setText(c.getNome());
                            sobrenome_editText.setText(c.getSobrenome());
                            cpf_editText.setText(c.getCpf());
                        }
                    }
                }
            }
        });


        return view;
    }

    private void initClientes(){

        nome_editText.setText("");
        sobrenome_editText.setText("");
        cpf_editText.setText("");
        id_editText.setText("");
        textView.setText("");
        Log.d(TAG, "initClientes: fetching cliente list");

        Call<List<Cliente>> requestClientes = alphaws.getClientes();

        requestClientes.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    return;
                } else{
                    clientes = response.body();
                    for(Cliente c : clientes){
                        String content = "";
                        content += "ID: " + c.getId() + "\n";
                        content += "Nome: " + c.getNome() + "\n";
                        content += "Sobrenome: " + c.getSobrenome() + "\n";
                        content += "CPF: " + c.getCpf() + "\n\n";

                        textView.append(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Log.e(TAG, "Erro: " + t.getMessage());
            }
        });
    }

    private void saveCliente(){

        Cliente c = new Cliente(nome_editText.getText().toString(),
                                sobrenome_editText.getText().toString(),
                                cpf_editText.getText().toString());
        final Call<String> requestInsert = alphaws.insertCliente(c);
        requestInsert.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    return;
                } else{
                    if (response.body().equals("OK")) {
                        Toast.makeText(getActivity(), "CLIENTE INSERIDO COM SUCESSO",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Cliente inserido. " + response.raw());
                        initClientes();
                    } else{
                        Toast.makeText(getActivity(), "FALHA AO INSERIR CLIENTE",Toast.LENGTH_SHORT).show();
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

    private void updateCliente(){
        Cliente c = new Cliente(nome_editText.getText().toString(),
                sobrenome_editText.getText().toString(),
                cpf_editText.getText().toString());

        c.setId(Integer.parseInt(id_editText.getText().toString()));

        Call<String> requestUpdate = alphaws.updateCliente(c);
        requestUpdate.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    return;
                } else{
                    if (response.body().equals("OK")) {
                        Toast.makeText(getActivity(), "CLIENTE ATUALIZADO COM SUCESSO",Toast.LENGTH_SHORT).show();
                        initClientes();
                    } else{
                        Toast.makeText(getActivity(), "FALHA AO ATUALIZAR CLIENTE",Toast.LENGTH_SHORT).show();
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

    private void deleteCliente(){

        int id = Integer.parseInt(id_editText.getText().toString());

        Call<String> requestDelete = alphaws.deleteCliente(id);
        requestDelete.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Erro: " + response.code());
                    return;
                } else{
                    if (response.body().equals("OK")) {
                        Toast.makeText(getActivity(), "CLIENTE DELETADO COM SUCESSO",Toast.LENGTH_SHORT).show();
                        initClientes();
                    } else{
                        Toast.makeText(getActivity(), "FALHA AO DELETAR CLIENTE",Toast.LENGTH_SHORT).show();
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
