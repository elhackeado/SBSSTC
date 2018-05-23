package com.example.amanthakur.sbsstc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    TextView textViewname;
    TextView textViewrollno;
    TextView textViewphoneno;
    TextView textViewemail;
    SharedPreferences sharedPreferences;


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SecurePreferences preferences = new SecurePreferences(getActivity(), "my-preferences", "SometopSecretKey1235", true);
        String userId = preferences.getString("userId");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+userId);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                        textViewname.setText(dataSnapshot.child("name").getValue().toString().trim());
                    textViewrollno.setText(dataSnapshot.child("userid").getValue().toString().trim());
                    textViewphoneno.setText(dataSnapshot.child("phone").getValue().toString().trim());
                    textViewemail.setText(dataSnapshot.child("email").getValue().toString().trim());


                }
                catch (Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // initialise your views
        textViewname = (TextView) getView().findViewById(R.id.textView2);
        textViewrollno = (TextView) getView().findViewById(R.id.textView3);
        textViewphoneno = (TextView) getView().findViewById(R.id.textView4);
        textViewemail = (TextView) getView().findViewById(R.id.textView5);

    }


}
