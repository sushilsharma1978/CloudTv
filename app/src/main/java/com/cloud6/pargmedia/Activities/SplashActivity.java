package com.cloud6.pargmedia.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cloud6.pargmedia.R;


public class SplashActivity extends AppCompatActivity {
    SharedPreferences sp_signup;
    SharedPreferences.Editor ed_signup;
    private Context mContext = this;
    String[] perm = {"android.permission.READ_PHONE_STATE"};
    int permstats = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_splash);

        sp_signup=getSharedPreferences("SIGNUP",MODE_PRIVATE);
        ed_signup=sp_signup.edit();



        AccessPremissions();


    }

    private void AccessPremissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perm, permstats);
        } else {

             ProceedAfterPermisson();
        }

    }

    private void ProceedAfterPermisson() {

        /*VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +
                R.raw.wathanam_new));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {

                if(sp_signup.getString("email","").equals("")){
                    startActivity(new Intent(mContext, SignUpActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }
                // Do whatever u need to do here

            }
        });*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sp_signup.getString("email","").equals("")){
                    startActivity(new Intent(mContext, SignUpActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }
            }
        },3000);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permstats) {
            boolean acess = false;
            for (int i = 0; i < grantResults.length; i++) {
                acess = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                if (!acess) {
                    AccessPremissions();
                    break;
                }
            }
            if (acess) {
                ProceedAfterPermisson();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
