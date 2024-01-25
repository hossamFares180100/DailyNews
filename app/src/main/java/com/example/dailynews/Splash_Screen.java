package com.example.dailynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import pl.droidsonroids.gif.GifImageView;

public class Splash_Screen extends AppCompatActivity {


    CoordinatorLayout image;
    CardView imageView;
    TextView text1;
    TextView text2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash__screen);
        image = findViewById(R.id.fm_image);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startEnterAnimation();

            }
        },1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startExitAnimation();
            }
        },6000);





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mAuth.getCurrentUser()!=null){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                }
                finish();

            }
        },7000);



    }

    private void startEnterAnimation() {

        image.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_in));
        text1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_in));
        text2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_in));
        image.setVisibility(View.VISIBLE);
        text1.setVisibility(View.VISIBLE);
        text2.setVisibility(View.VISIBLE);


    }

    private void startExitAnimation() {

        image.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_out));
        text1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_out));
        text2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_out));
        image.setVisibility(View.INVISIBLE);
        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}