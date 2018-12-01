package com.example.qq;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RecycleView extends RecyclerView.Adapter<RecycleView.ViewHolder> {

    private List<User> dataList;

    public RecycleView(List<User> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = dataList.get(i);
        viewHolder.tvNickname.setText(user.getNickname());
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void addFriend(int myId, int friendId) {

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