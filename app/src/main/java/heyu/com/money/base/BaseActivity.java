package heyu.com.money.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import heyu.com.money.R;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public abstract class BaseActivity extends FragmentActivity {

    protected BaseActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity = this;
        initView(savedInstanceState);
        initData();
    }

    public abstract void initView(Bundle savedInstanceState);

    public void initData() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    public void gotoActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null)
            intent.putExtras(encryptData(bundle));
        startActivity(intent);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

    public void gotoActivityAndFinishMe(Class<?> cls, Bundle bundle, boolean isBack) {
        Intent intent = new Intent(this, cls);
        if (bundle != null)
            intent.putExtras(encryptData(bundle));
        startActivity(intent);
        finish();
        if (isBack) {
            overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        } else {
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        }
    }

    private Bundle encryptData(Bundle bundle) {
        return bundle;
    }



    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /***
     * 隐藏软键盘
     */
    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isUIRunning(  ){
        boolean isRunning= (!isDestroyed()) &&(!isFinishing());
        Log.d("UIUtility", "isUIRunning:"+isRunning + this);
        return isRunning;
    }


}
