package com.example.dailynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.balysv.materialripple.MaterialRippleLayout;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    TextInputLayout txt_email,txt_pass,email_address;
    MaterialRippleLayout btn_sign_In;
    String email,pass,emailAddress;
    TextView txt_forget;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    Button btn_google,btn_face;
    private CallbackManager mCallbackManager;
    private long backPressedTime;
    private Toast back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        setID();
        mAuth = FirebaseAuth.getInstance();
        btn_sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=txt_email.getEditText().getText().toString().trim();
                pass=txt_pass.getEditText().getText().toString().trim();
                if(!isVaildEmail() | !isVaildPass()){
                    return;
                }else{
                    signInWithEmailAndPassword();
                    txt_email.getEditText().setText("");
                    txt_pass.getEditText().setText("");
                }
            }
        });

        txt_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder= new AlertDialog.Builder(Login.this);
                View view= LayoutInflater.from(Login.this).inflate(R.layout.forget_password_layout,null,false);
                MaterialRippleLayout reset=view.findViewById(R.id.btn_get_pass);
                email_address=view.findViewById(R.id.edit_email_forget);
                alertBuilder.setView(view);
                alertBuilder.setTitle("Enter your email to reset password");
                alertBuilder.setIcon(R.drawable.ic_baseline_error_outline_24);
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();

                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        emailAddress =email_address.getEditText().getText().toString().trim();
                        if(!isVaildEmailForget()){
                            return;
                        }else {
                            mAuth.sendPasswordResetEmail(emailAddress)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),"Check your email for reset",Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),"There is error...!",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }

                    }
                });
            }
        });

        //Code of google start from here
        createRequest();
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInwithgoogle();
            }
        });

        //Code of facebook start from here
        mCallbackManager = CallbackManager.Factory.create();
        btn_face=findViewById(R.id.btn_face);
        btn_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(),"facebook:onCancel",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("error", "onError: "+error.toString());
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    private void setID() {
        txt_email=findViewById(R.id.edit_email);
        txt_pass=findViewById(R.id.edit_pass);
        btn_sign_In=findViewById(R.id.btn_login);
        txt_forget=findViewById(R.id.txt_forget);
        btn_google=findViewById(R.id.btn_google);
    }

    boolean isVaildEmail(){
        if(email.isEmpty()){
            txt_email.getEditText().setError("Email can’t be empty..!");
            txt_email.getEditText().requestFocus();
            return false;
        }
        if(email.length()>50){
            txt_email.getEditText().setError("Email is too long..!");
            return false;
        }
        return true;
    }
    boolean isVaildEmailForget(){
        if(emailAddress.isEmpty()){
            email_address.getEditText().setError("Email can’t be empty..!");
            email_address.getEditText().requestFocus();
            return false;
        }
        if(emailAddress.length()>50){
            email_address.getEditText().setError("Email is too long..!");
            return false;
        }
        return true;
    }
    boolean isVaildPass(){
        if(pass.isEmpty()){
            txt_pass.getEditText().setError("Password can’t be empty..!");
            return false;
        }
        if(pass.length()<6){
            txt_pass.getEditText().setError("Password is too small..!");
            return false;
        }
        return true;
    }

    void signInWithEmailAndPassword(){
        mAuth.signInWithEmailAndPassword(email,pass).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                         if(mAuth.getCurrentUser().isEmailVerified()){
                              Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                              startActivity(intent);
                          }
                          else{
                             Toast.makeText(getApplicationContext()," please verity..! ",Toast.LENGTH_LONG).show();
                          }

                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signUp(View view) {
        Intent intent=new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000>System.currentTimeMillis()){
            back.cancel();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            super.onBackPressed();
            return;
        }else {
            back= Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_LONG);
            back.show();
        }
        backPressedTime=System.currentTimeMillis();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Toast.makeText(getApplicationContext(), "Login success.", Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Log.e("error gmail",e.getMessage());
                Toast.makeText(getApplicationContext(), "Login failed."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //This function used to define account in google

    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInwithgoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Authentication success.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //This function used to define account in facebook


    private void handleFacebookAccessToken(AccessToken token) {
//        Toast.makeText(getApplicationContext(),"handleFacebookAccessToken:" + token,Toast.LENGTH_LONG).show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(),"signInWithCredential:success",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {

//                            Toast.makeText(getApplicationContext(),"signInWithCredential:failure",Toast.LENGTH_LONG).show();
//                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            Intent intent=new Intent(Login.this,MainActivity.class);
//            startActivity(intent);
//        }
//    }
}