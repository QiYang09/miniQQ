package com.example.qq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Start extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    int id;
    String username;
    String pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        id = pref.getInt("id",0);
        username = pref.getString("username","");
         pwd = pref.getString("pwd","");

        if ((id == 0)||(username.equals(""))||(pwd.equals(""))){
            Intent intent = new Intent(Start.this,Login.class);
            startActivity(intent);
        }
        else{
            sendRequestWithOkHttp2();
        }
    }
    private void sendRequestWithOkHttp2(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("nickname",username)
                            .add("password",pwd)
                            .build();
                    Request request = new  Request.Builder()
                            .url("http://202.194.15.144:12234/user/login")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();

                    parseJSONWithGSON(responseDate);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithGSON(String jsonData){
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        if(jsonObject.getInteger("code")==0){
            JSONObject jsonObj = JSONObject.parseObject(jsonObject.getString("obj"));
            int userid = jsonObj.getInteger("id");
            String nickName = jsonObj.getString("nickname");

            SharedPreferences.Editor editor = getSharedPreferences("userinfo",
                    MODE_PRIVATE).edit();
            editor.putInt("id",userid);
            editor.putString("username",nickName);
            editor.putString("pwd",pwd);
            editor.apply();

            Intent intent = new Intent(Start.this,Boundar.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(Start.this,Login.class);
            startActivity(intent);
        }
    }
}



