package com.example.avaliacaofinalp4;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText mEditEmail;
    private EditText mEditUser;
    private Button mBtnEnter;

    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        mEditEmail = findViewById(R.id.edit_email);
        mEditUser = findViewById(R.id.edit_user_name);
        mBtnEnter = findViewById(R.id.btn_enter);

        service = RetrofitController.getInstance().create(Service.class);

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEnter();
            }
        });

        ImageView imageView = findViewById(R.id.login_voltar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void clickEnter(){
        String email = mEditEmail.getText().toString();
        String user = mEditUser.getText().toString();

        Log.i("Tentando Logar com", email);

        if (email == null || email.isEmpty() || user == null || user.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Usuário e email devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        service.carregarContatos().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();

                User lgn = null;

                for (User u : users) {
                    System.out.println(u);

                    if(u.getEmail().equals(email) && u.getName().equals(user)){
                        lgn = u;
                    }

                }

                if(lgn != null){
                    System.out.println("EMAIL ENCONTRADO !! logando...");

                    SharedPreferences preferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("USER_ID", lgn.getId());
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this.getApplicationContext(), ChatActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(LoginActivity.this, "Email e Nome não coincidem", Toast.LENGTH_LONG).show();
                }
       }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });


        //Log.i("Teste", task.getResult().getUser().getUid());

        //Intent intent = new Intent(LoginActivity.this, MessagesActivity.class);

        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        //startActivity(intent);
    }
}
