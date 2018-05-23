package com.example.amanthakur.sbsstc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    ProgressDialog progressDialog3;
    TextView phoneDetails_tv;
    TextView timer_tv;
    EditText OTP_et;
    private Button verifyOTP_btn;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String phoneNumber;
    String username;
    String password;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    String code;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    //public static SecurePreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        phoneNumber = extras.getString("PHONENUMBER");
        username = extras.getString("USERNAME");
        password = extras.getString("PASSWORD");

        final ProgressDialog progressDialog = new ProgressDialog(OTPActivity.this);
        progressDialog.setMessage("Sending OTP to +91******"+phoneNumber.substring(9));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        phoneDetails_tv = (TextView) findViewById(R.id.phoneDetails_tv);
        phoneDetails_tv.setVisibility(View.INVISIBLE);
        timer_tv = (TextView) findViewById(R.id.timer_tv);
        timer_tv.setVisibility(View.INVISIBLE);
        OTP_et = (EditText) findViewById(R.id.OTP_pt);
        verifyOTP_btn = (Button) findViewById(R.id.verifyOTP_btn);





        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(OTPActivity.this, "onVerificationCompleted", Toast.LENGTH_LONG).show();
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:" + credential);
                OTP_et.setText(code);
                OTP_et.setEnabled(false);
                verifyOTP_btn.setEnabled(false);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(OTPActivity.this, "onVerificationFailed", Toast.LENGTH_LONG).show();
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                // Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                progressDialog.dismiss();
                Toast.makeText(OTPActivity.this, "onCodeSent", Toast.LENGTH_LONG).show();
                phoneDetails_tv.setText("The OTP has been sent to +91******" + phoneNumber.substring(9));
                phoneDetails_tv.setVisibility(View.VISIBLE);
                timer_tv.setVisibility(View.VISIBLE);

                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }


        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks);


    }

    public void onclick(View view){
        code = OTP_et.getText().toString();
        progressDialog3 = new ProgressDialog(OTPActivity.this);
        progressDialog3.setCancelable(false);
        progressDialog3.setCanceledOnTouchOutside(false);
        progressDialog3.setMessage("Validating OTP");
        progressDialog3.show();
        verifyPhoneNumberWithCode(mVerificationId, code);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(OTPActivity.this, "OTP VERIFIED", Toast.LENGTH_LONG).show();

                            sharedPreferences = getSharedPreferences("loggedIn",MODE_PRIVATE);
                            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                            preferencesEditor.putBoolean("isLoggedIn",true);
                            preferencesEditor.apply();
                            SecurePreferences preferences = new SecurePreferences(OTPActivity.this, "my-preferences", "SometopSecretKey1235", true);
                            preferences.put("userId", username);
                            Intent intent = new Intent(OTPActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                            //FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            // updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progressDialog3.dismiss();
                                Toast.makeText(OTPActivity.this, "The verification code entered was invalid", Toast.LENGTH_LONG).show();

                                // [START_EXCLUDE silent]
                                //mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
}
