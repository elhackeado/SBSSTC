package com.example.amanthakur.sbsstc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    EditText userId_et;
    EditText password_et;
    Button login_btn;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

        // onCreate
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        userId_et = (EditText)findViewById(R.id.userId_pt);
        password_et = (EditText)findViewById(R.id.password_pw);
        login_btn = (Button)findViewById(R.id.login_btn);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Logging You In...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                final String userId = userId_et.getText().toString().trim();
                final String password = password_et.getText().toString().trim();
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+userId);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {
                            if (password.equals(dataSnapshot.child("password").getValue().toString().trim())) {
                                progressDialog.dismiss();
                                final ProgressDialog progressDialog1 = new ProgressDialog(LoginActivity.this);
                                progressDialog1.setMessage("User Authenticated !");
                                progressDialog1.setCancelable(false);
                                progressDialog1.setCanceledOnTouchOutside(false);
                                progressDialog1.show();
                                Toast.makeText(LoginActivity.this, "Authenticated user", Toast.LENGTH_LONG).show();
                                Intent intent;
                                intent = new Intent(LoginActivity.this,OTPActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("USERNAME",userId);
                                extras.putString("PASSWORD",password);
                                extras.putString("PHONENUMBER",dataSnapshot.child("phone").getValue().toString());
                                intent.putExtras(extras);
                                startActivity(intent);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Wrong UserId/Password", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "User doesn't exists", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this,"onCancelled",Toast.LENGTH_LONG).show();
                    }
                });

                Toast.makeText(LoginActivity.this,userId + "  " + password,Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();;

    }

    @Override
    protected void onPause(){
        super.onPause();

    }
}