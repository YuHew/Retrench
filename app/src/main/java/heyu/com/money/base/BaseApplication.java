package heyu.com.money.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.facebook.android.crypto.keychain.AndroidConceal;
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.Entity;
import com.facebook.crypto.keychain.KeyChain;

import heyu.com.publiclibrary.utils.GlobleUtils;
import heyu.com.publiclibrary.utils.security.SecurityTool;


/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class BaseApplication extends Application {
    // 获取到主线程的上下文
    private static BaseApplication mContext;
    // 获取到主线程的handler
    private static Handler mMainThreadHandler;
    // 获取到主线程
    private static Thread mMainThread;
    // 获取到主线程的id
    private static int mMainThreadId;
    private static BaseApplication application;

    public boolean isRedTipStatus() {
        return redTipStatus;
    }

    public void setRedTipStatus(boolean redTipStatus) {
        this.redTipStatus = redTipStatus;
    }

    private boolean redTipStatus = false;
    private static final String OCTOPUS_CONCEAL_KEY = "octopus738" + Build.SERIAL;

    public static BaseApplication getInstance() {
        if (application == null) {
            application = new BaseApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        application = this;
        SecurityTool.setCrypto(getConceal());
        SecurityTool.setEntity(Entity.create(OCTOPUS_CONCEAL_KEY));
        mMainThreadHandler = new Handler();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        GlobleUtils.setContext(this, mMainThread, mMainThreadId, mMainThreadHandler);
    }

    private Crypto getConceal() {
        KeyChain keyChain = new SharedPrefsBackedKeyChain(getApplicationContext(), CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        return crypto;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 对外暴露上下文
     *
     * @return
     */
    public static BaseApplication getApplication() {
        return mContext;
    }

    /**
     * 对外暴露主线程的handler
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 对外暴露主线程
     *
     * @return
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 对外暴露主线程id
     *
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

}
