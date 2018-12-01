package com.example.qq;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Boundar extends AppCompatActivity implements View.OnClickListener {

    private FriendFragment friendFragment;
    private ZoneFragment zoneFragment;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boundar_layout);
        id = getIntent().getIntExtra("id", 0);

        friendFragment = new FriendFragment();
        zoneFragment = new ZoneFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.friend_fragment,friendFragment);
        fragmentTransaction.add(R.id.friend_fragment,zoneFragment);
        fragmentTransaction.commit();
        Button button1 = findViewById(R.id.btn_add);
        button1.setOnClickListener(this);
        Button button2 = findViewById(R.id.btn_cancel);
        button2.setOnClickListener(this);
        Button button3 = findViewById(R.id.btn_friend);
        button3.setOnClickListener(this);
        Button button4 = findViewById(R.id.btn_zone);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            Intent intent = new Intent(Boundar.this, Query.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_friend) {
            replaceFragment(friendFragment);

        }
        if (v.getId() == R.id.btn_zone) {
            replaceFragment(zoneFragment);
        }
        if (v.getId() == R.id.btn_cancel) {
            getSharedPreferences("userinfo", MODE_PRIVATE)
                    .edit()
                    .remove("username")
                    .remove("password")
                    .apply();
            startActivity(new Intent(Boundar.this, Login.class));
            finish();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(friendFragment);
       fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }
}