package com.example.amanthakur.sbsstc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SplashActivity extends AppCompatActivity {

    RelativeLayout relativeLayout2;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("loggedIn",MODE_PRIVATE);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loggedIn");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    Boolean ServerLoggedIn = Boolean.parseBoolean(dataSnapshot.getValue().toString().trim());
                    sharedPreferences = getSharedPreferences("loggedIn",MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                    preferencesEditor.putBoolean("ServerLoggedIn",ServerLoggedIn);
                    preferencesEditor.apply();
                    if(!ServerLoggedIn){
                        preferencesEditor.putBoolean("isLoggedIn",false);
                        preferencesEditor.apply();
                    }

                }

                catch (Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SharedPreferences sharedPreferences1;
        sharedPreferences1 = getSharedPreferences("loggedIn",MODE_PRIVATE);
        Boolean isLoggedIn = sharedPreferences1.getBoolean("isLoggedIn",false);
        Boolean ServerLoggedIn = sharedPreferences1.getBoolean("ServerLoggedIn",false);


        if (isLoggedIn && ServerLoggedIn){
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_splash);



    }



    public void loginActivity(View view){

       Intent intent = new Intent(this, LoginActivity.class);
       startActivity(intent);
       finish();
    }
}
