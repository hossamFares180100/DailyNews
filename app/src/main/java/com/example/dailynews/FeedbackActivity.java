package com.example.dailynews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hsalf.smileyrating.SmileyRating;

public class FeedbackActivity extends AppCompatActivity {
    SmileyRating smiley;
    TextInputLayout feed;
    MaterialRippleLayout send;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        smiley = (SmileyRating) findViewById(R.id.smile_rating);
        feed = findViewById(R.id.feed);
        send = findViewById(R.id.send_feedback);
        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid() + "";
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!feed.getEditText().getText().toString().equals("")){
                   SmileyRating.Type type = smiley.getSelectedSmiley();
                   Intent emailIntent = new Intent(Intent.ACTION_SENDTO , Uri.parse("mailto:" + "dailynews20202020@gmail.com"));
                   emailIntent.setType("text/plain");
                   emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
                   emailIntent.putExtra(Intent.EXTRA_TEXT, "User Id: "+id+"\n \n" +"feedback rating = "+type+"\n\n\n"+"user feedback : \n"+feed.getEditText().getText().toString());
                   startActivity(emailIntent);
                   finish();
               }
        }
    });



}
}
