package com.example.dailynews.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.example.dailynews.Login;
import com.example.dailynews.MainActivity;
import com.example.dailynews.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment {
    FirebaseUser user;
    FirebaseAuth mAuth;
    TextInputLayout userName , email;
    CircleImageView circleImageView;
    MaterialRippleLayout materialRippleLayout;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct=null;
    public UserFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        acct = GoogleSignIn.getLastSignedInAccount(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        setId(view);
        getUserData();
        materialRippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteAccount();
            }
        });
        return view;
    }
    private void setId(View view){
        email = view.findViewById(R.id.email_view);
        userName = view.findViewById(R.id.username_view);
        circleImageView = view.findViewById(R.id.user_image_view);
        materialRippleLayout = view.findViewById(R.id.btn_delete_user);
    }
    private void getUserData(){
        if(user.getDisplayName()==null){
            userName.getEditText().setText("None");
        }
        else{
            userName.getEditText().setText(user.getDisplayName());
        }
       if(user.getPhotoUrl() == null){
            circleImageView.setImageResource(R.drawable.ic_avatar_man);
        }
        else {
           Glide.with(getActivity()).load(user.getPhotoUrl()).into(circleImageView);
       }
        email.getEditText().setText(user.getEmail());
    }
    private void setDeleteAccount(){

       user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   Toast.makeText(getActivity(), "AccountDeleted", Toast.LENGTH_SHORT).show();
                   getActivity().finish();
                   getActivity().getSupportFragmentManager().popBackStack();
                   if (acct != null) {
                       mGoogleSignInClient.signOut()
                               .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                   }
                               });
                   }else{
                   FirebaseAuth.getInstance().signOut();
                   LoginManager.getInstance().logOut();}
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }
}