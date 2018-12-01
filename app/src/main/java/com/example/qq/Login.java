package com.example.qq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Login extends AppCompatActivity implements View.OnClickListener {

    TextView responseText;
    private EditText accountEdit;
    private EditText passwordEdit;
    String pwd;
    String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Button button4 = findViewById(R.id.btn_register);
        button4.setOnClickListener(this);
        responseText = (TextView) findViewById(R.id.response_text1);
        Button button1 = (Button) findViewById(R.id.btn_login);
        accountEdit = (EditText) findViewById(R.id.edit_account);
        passwordEdit= (EditText)findViewById(R.id.edit_password);
        button1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        username = accountEdit.getText().toString();
        pwd = passwordEdit.getText().toString();
        if (v.getId() == R.id.btn_register) {
            sendRequestWithOkHttp1();
        }
        if (v.getId() == R.id.btn_login) {
            sendRequestWithOkHttp2();
        }
    }

    private void sendRequestWithOkHttp1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("nickname",username)
                            .add("password",pwd)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://202.194.15.144:12234/user/register")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse1(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void showResponse1(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
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
                    showResponse2(responseDate);
                    parseJSONWithGSON(responseDate);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse2(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
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

            Intent intent = new Intent(Login.this,Boundar.class);
            startActivity(intent);
            finish();
        }
    }
}