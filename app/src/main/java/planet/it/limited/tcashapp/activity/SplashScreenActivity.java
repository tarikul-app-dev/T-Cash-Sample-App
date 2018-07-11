package planet.it.limited.tcashapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import planet.it.limited.tcashapp.R;
import planet.it.limited.tcashapp.utill.SaveValueSharedPreference;

public class SplashScreenActivity extends AppCompatActivity {
    boolean isLogin = true;
    SaveValueSharedPreference saveValueSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // isLogin = saveValueSharedPreference.getBoleanValueSharedPreferences("islogin",SplashScreenActivity.this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isLogin){
                    waitSomeMin();
                }

            }
        }, 2000);
    }
    public void waitSomeMin(){
        Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
        startActivity(intent);
        ActivityCompat.finishAffinity(SplashScreenActivity.this);
    }
}
