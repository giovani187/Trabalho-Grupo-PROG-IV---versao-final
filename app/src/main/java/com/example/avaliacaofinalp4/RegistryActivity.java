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
        import android.widget.Toast;

        import java.util.List;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;


public class RegistryActivity extends AppCompatActivity {

    private EditText mEditEmail;
    private EditText mEditUser;
    private Button mBtnEnter;

    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_registro);

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


    }


    public void clickEnter(){
        String email = mEditEmail.getText().toString();
        String user = mEditUser.getText().toString();

        Log.i("Tentando Registrar com", email);

        if (email == null || email.isEmpty() || user == null || user.isEmpty()) {
            Toast.makeText(RegistryActivity.this, "Usuário e email devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        service.carregarContatos().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();

                User lgn = null;

                for (User u : users) {
                    System.out.println(u);

                    if(u.getEmail().equals(email)){
                        lgn = u;
                    }

                }

                if(lgn != null){
                    System.out.println("EMAIL ENCONTRADO !! ");
                    Toast.makeText(RegistryActivity.this, "Email já cadastrado, tente um outro email", Toast.LENGTH_LONG).show();


                }else{

                   registrarUsuario(email, user);

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.fillInStackTrace();
            }
        });

    }


    public void registrarUsuario(String email, String userName){

        NewUser newUser = new NewUser();
        newUser.setEmail(email);
        newUser.setNome(userName);

        service.registrarUsuario(newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User u = response.body();

                if(u != null){

                    SharedPreferences preferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("USER_ID", u.getId());
                    editor.apply();

                    Intent intent = new Intent(RegistryActivity.this.getApplicationContext(), ChatActivity.class);
                    startActivity(intent);

                }else{

                    Toast.makeText(RegistryActivity.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.fillInStackTrace();
            }
        });

    }

}



