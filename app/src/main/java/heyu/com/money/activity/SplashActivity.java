package heyu.com.money.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import heyu.com.money.R;
import heyu.com.publiclibrary.utils.GlobleUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        GlobleUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlobleUtils.gotoActivity(SplashActivity.this, null, MainActivity.class, true, false);
            }
        }, 500);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

}
