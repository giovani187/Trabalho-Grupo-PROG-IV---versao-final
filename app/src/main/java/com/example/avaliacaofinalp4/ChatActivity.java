package com.example.avaliacaofinalp4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;


    private int USER_ID_API = 85;

    private EditText editChat;

    private Service service;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat);


        getSupportActionBar().setTitle("Chat");

        rv = findViewById(R.id.recycler_chat);
        editChat = findViewById(R.id.edit_chat);
        Button btnChat = findViewById(R.id.btn_chat);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


        service = RetrofitController.getInstance().create(Service.class);

    }

    public void onResume(){
        super.onResume();
        fetchMessages();
    }

    private void fetchMessages() {

            System.out.println("RECARREGANDO MSG");
            adapter.clear();

            Call<List<Message>> result = service.carregarMensagens();

            result.enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    List<Message> msgs = response.body();


                    for (Message m: msgs) {
                        System.out.println(m);
                        adapter.add(new MessageItem(m));
                    }
                    rv.smoothScrollToPosition(rv.getAdapter().getItemCount()-1);

                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {

                }
            });

    }

    private void sendMessage() {
        String text = editChat.getText().toString();

        editChat.setText(null);

        final MensagemEnviar message = new MensagemEnviar();
        message.setId(USER_ID_API);
        message.setMessage(text);

        service.enviarMensagem(message).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("MENSAGEM ENVIADA! " + response.code() );

                fetchMessages();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.fillInStackTrace();
            }
        });

    }

    private class MessageItem extends Item<ViewHolder> {

        private final Message message;

        private MessageItem(Message message) {
            this.message = message;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.txt_msg);
            TextView txtUser = viewHolder.itemView.findViewById(R.id.img_message_user);

            txtMsg.setText(message.getMessage());
            txtUser.setText(message.getName() + ": ");
        }

        @Override
        public int getLayout() {
            return message.getUsers_id() == USER_ID_API
                    ? R.layout.item_from_message
                    : R.layout.item_to_message;
        }
    }

}
