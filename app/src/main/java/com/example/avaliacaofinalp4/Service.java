package com.example.avaliacaofinalp4;


import com.google.gson.JsonElement;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {
    @GET("chat/mensagens")
    Call<List<Message>> carregarMensagens();


    @POST("chat/enviar")
    Call<ResponseBody> enviarMensagem(@Body MensagemEnviar msg);


    @GET("chat/contatos")
    Call<List<User>> carregarContatos();
}
