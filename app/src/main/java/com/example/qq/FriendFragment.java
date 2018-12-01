package com.example.qq;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendFragment extends Fragment {

    private List<User> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private SharedPreferences pref;
    private static int id  ;
    public FriendFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendfragment_layout, container, false);
        super.onCreate(savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MyAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        pref=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        id = pref.getInt("id",0);
        loadList();
        return view;
    }

    private void loadList() {

        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url("http://202.194.15.144:12234/user/friendList?id=" + id)
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
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject userObj = array.getJSONObject(i);
                                User user = new User();
                                user.setId(userObj.getInt("id"));
                                user.setNickname(userObj.getString("nickname"));
                                dataList.add(user);
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                    .inflate(R.layout.friend_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            User user = dataList.get(i);
            viewHolder.tvNickname.setText(user.getNickname());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvNickname;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNickname = itemView.findViewById(R.id.tv_nickname);
            }
        }
    }
}