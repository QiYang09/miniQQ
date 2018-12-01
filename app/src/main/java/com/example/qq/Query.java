package com.example.qq;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Query extends AppCompatActivity implements View.OnClickListener{

    private List<User> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Button button1;
    private EditText editText;
    private SharedPreferences pref;
    int userId;
    TextView responseText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_layout);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new MyAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        editText=findViewById(R.id.edit_id);
        responseText = (TextView) findViewById(R.id.response_text4);
        pref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        userId = pref.getInt("id",0);

        button1 = findViewById(R.id.btn_query);
        button1.setOnClickListener(this);

    }


    private void loadList() {

        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url("http://202.194.15.144:12234/user/query?key=" + editText.getText().toString())
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String respStr = response.body().string();
                        try {
                            JSONArray array = new JSONArray(new JSONObject(respStr).getString("obj"));
                            dataList.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject userObj = array.getJSONObject(i);
                                User user = new User();
                                user.setId(userObj.getInt("id"));
                                user.setNickname(userObj.getString("nickname"));
                                dataList.add(user);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_query) {
            loadList();
        }
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<User> dataList;

        public MyAdapter(List<User> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.user_item, viewGroup, false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            final User user = dataList.get(i);
            viewHolder.tvNickname.setText(user.getNickname());
            viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.btn_add_friend) {
                              addFriend(userId,user.getId());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }


        private void addFriend(int myId, final int friendsId) {
            try {
                new Thread(){
                    @Override
                    public void run(){
                        try{
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("userId", userId + "")
                                    .add("friendId",friendsId+"" )
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://202.194.15.144:12234/user/addFriend")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            int code = jsonObject.getInt("code");
                            Looper.prepare();
                            if (code==0)
                                Toast.makeText(Query.this,"添加成功",
                                        Toast.LENGTH_SHORT).show();
                            else if (code==1)
                                Toast.makeText(Query.this,"已经是好友",
                                        Toast.LENGTH_SHORT).show();
                            else if (code==-1)
                                Toast.makeText(Query.this,"添加失败",
                                        Toast.LENGTH_SHORT).show();
                            showResponse3(responseData);
                            Looper.loop();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                }

                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void showResponse3(final String response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    responseText.setText(response);
                }
            });
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvNickname;
            Button btnAdd;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNickname = itemView.findViewById(R.id.tv_nickname);
                btnAdd = itemView.findViewById(R.id.btn_add_friend);
            }
        }
    }
}