package com.example.dailynews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dailynews.fragments.Favorites;
import com.example.dailynews.fragments.HomeFragment;
import com.example.dailynews.fragments.SearchFragment;
import com.example.dailynews.fragments.UserFragment;
import com.example.dailynews.fragments.ViewNews;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.Gravity.START;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , SearchFragmentListener,HomeFragmentListener ,FavoriteFragmentListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public static BottomNavigationView bottomNavigationView;
    MaterialSearchBar materialSearchBar;
    View view_nave;
    CircleImageView circleImageView;
    TextView txt_name,txt_email;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct=null;
    private long backPressedTime;
    private Toast back;
    String frag_check="";
    DatabaseReference databaseReference;
    DatabaseReference mDatabase;
    StorageReference ref;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setId();

        setDrawerLayout();
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        materialSearchBar=findViewById(R.id.searchBar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);


        //Search view
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(frag_check.equals("fav") && haveNetwork()){
                    Favorites.filter(s.toString());
                }
                else if(!haveNetwork()){
                    if (s.toString().length()>0) {
                        HomeFragment.filterOff(s.toString());
                    }
                }
                else {
                    HomeFragment.filter(s.toString());
                }
            }
        });


        //To get information and display it in navigation
        navigationView.setNavigationItemSelectedListener(this);
        view_nave=navigationView.getHeaderView(0);
        mAuth = FirebaseAuth.getInstance();
        circleImageView=view_nave.findViewById(R.id.imageView_nav);
        txt_email=view_nave.findViewById(R.id.txt_email_nave);
        txt_name=view_nave.findViewById(R.id.txt_user_nav);
        if(mAuth.getCurrentUser()!=null){
            String name=mAuth.getCurrentUser().getDisplayName();
            String email=mAuth.getCurrentUser().getEmail();
            Uri uri=mAuth.getCurrentUser().getPhotoUrl();
            txt_name.setText(name);
            txt_email.setText(email);
            Toast.makeText(this, mAuth.getCurrentUser().getPhotoUrl()+"", Toast.LENGTH_SHORT).show();
            if(uri==null){
                circleImageView.setImageResource(R.drawable.user_defult);
            }
            else {
                Glide.with(this).load(uri).into(circleImageView);
            }
        }

        //To check connect internet
       if (!haveNetwork()) {
            Snackbar.make(drawerLayout,"No Internet Connection",Snackbar.LENGTH_LONG).show();
        }
       bottomNavigationView.setSelectedItemId(R.id.navigationHome);
    }



    private  void setId(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.navigation_bottom);
    }
    private void setDrawerLayout(){
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(navigationView.getCheckedItem()!=null){
            navigationView.getCheckedItem().setChecked(false);
        }
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            bottomNavigationView.setSelectedItemId(R.id.navigationHome);
        }else if(frag_check.equals("view")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
            materialSearchBar.setVisibility(View.VISIBLE);
            bottomNavigationView.setSelectedItemId(R.id.navigationHome);
            frag_check="";
        }else if(frag_check.equals("favView")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new Favorites()).commit();
            materialSearchBar.setVisibility(View.VISIBLE);
            frag_check="";
        }
        else if(frag_check.equals("fav")){
            frag_check="";
        }
        else {
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
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        switch (id){
            case R.id.nav_logout:
                signOut();
                break;
            case R.id.nav_about:
                Intent n=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(n);
                break;
            case R.id.nav_contact_us:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.contact_us_layout, null);
                MaterialRippleLayout face = view.findViewById(R.id.facebook);
                MaterialRippleLayout gog = view.findViewById(R.id.tele);
                builder.setView(view).setTitle("Contact").setIcon(R.drawable.ic_phone_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();
                face.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent browse=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Daily-News-109948707528539/"));
                        startActivity(browse);
                        dialog.dismiss();

                    }
                });

                gog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO , Uri.parse("mailto:" + "dailynews20202020@gmail.com"));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Contact Owner");
                        startActivity(emailIntent);
                        dialog.dismiss();
                    }
                });


                break;
            case R.id.nav_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new Favorites()).commit();
                frag_check = "fav";
                break;
            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_countries:
                Toast.makeText(this, "Views News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Intent settingIntent=new Intent();
                settingIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",getPackageName(),null);
                settingIntent.setData(uri);
                startActivity(settingIntent);

                break;
            case R.id.nav_share_app:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Intent intent = new Intent(MainActivity.this,FeedbackActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(START);
        return true;
    }

    private void signOut() {
        if (acct != null) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(MainActivity.this,Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }else{
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent intent=new Intent(getApplicationContext(),Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(),"User account sign out.",Toast.LENGTH_SHORT).show();

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navigationHome:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).addToBackStack(null).commit();
                    materialSearchBar.setVisibility(View.VISIBLE);
                    frag_check="";
                    break;
                case R.id.navigationSearch:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new SearchFragment()).commit();
                    materialSearchBar.setVisibility(View.GONE);
                    break;
                case R.id.navigationMenu:
                    drawerLayout.openDrawer(GravityCompat.START);
                    break;
                case R.id.navigationMyProfile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new UserFragment()).commit();
                    materialSearchBar.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
    };


    @Override
    public void onDataSent(String data) {
        HomeFragment.upadteData(data);
    }

    @Override
    public void sendUrl(String url) {
        ViewNews.instance(url);
        materialSearchBar.setVisibility(View.GONE);
        frag_check="view";
    }

    @Override
    public void sendDescription(String description) {
        ViewNews.getDescription(description);
        materialSearchBar.setVisibility(View.GONE);
        frag_check="view";
    }

    @Override
    public void onClick(News news) {
        ViewNews.instance(news.getNewsUrl());
        materialSearchBar.setVisibility(View.GONE);
        frag_check="favView";
    }
    public boolean haveNetwork(){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }
        return have_WIFI||have_MobileData;
    }

}
