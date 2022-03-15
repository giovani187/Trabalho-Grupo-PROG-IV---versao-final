package com.example.avaliacaofinalp4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ContatosActivity extends AppCompatActivity {

    TextView contatos;
    ImageView imageView;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contatos);
        contatos = findViewById(R.id.tv_contatos);
        imageView = findViewById(R.id.contatos_voltar);
        contatos.setMovementMethod(new ScrollingMovementMethod());

        service = RetrofitController.getInstance().create(Service.class);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onResume(){
        super.onResume();
        contatos.setText("");

        service.carregarContatos().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                if(users != null){

                    for (User u: users) {
                        contatos.append(u.getName() + " #ID: " + u.getId() + "\n" + u.getEmail() + "\n............................\n");
                    }

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

}

