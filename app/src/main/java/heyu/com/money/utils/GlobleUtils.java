package heyu.com.money.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import heyu.com.money.R;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class GlobleUtils {

    private static Toast sToast;
    private static Toast sToastNotify;
    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainThreadID;
    private static Handler mMainThreadHandler;


    public static void updateToast(String res, int duration) {
        if (sToast == null) {
            //LayoutInflater inflate = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).getView();
            sToast = new Toast(getContext());
            sToast.setView(v);
        }
        sToast.setText(res);
        sToast.setDuration(duration);
        sToast.show();
    }

    public static void setContext(Context context, Thread thread, long id, Handler handler) {
        mContext = context;
        mMainThread = thread;
        mMainThreadID = id;
        mMainThreadHandler = handler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadID;
    }

    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public static void showToast(final String message) {

        if (isRunInMainThread()) {
            updateToast(message, Toast.LENGTH_SHORT);
        } else {
            runInMainThread(new Runnable() {
                @Override
                public void run() {
                    updateToast(message, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    /**
     * dp转px px = dip * density / 160
     *
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px转换dip
     *
     * @param px
     * @return
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取主线程的handler
     *
     * @return
     */
    public static Handler getHandler() {
        return mMainThreadHandler;
    }

    /**
     * 延时在主线程执行runnable
     *
     * @param runnable
     * @param delayMillis
     * @return
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     *
     * @param runnable
     * @return
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 从主线程looper里面移除runnable
     *
     * @param runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 获取资源
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     *
     * @param resId
     * @return
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     *
     * @param resId
     * @return
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     *
     * @param resId
     * @return
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     *
     * @param resId
     * @return
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     *
     * @param resId
     * @return
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    /**
     * 所有页面收到通知消息时弹Toast,int
     *
     * @param resId
     */
    public static void showNotify(int resId) {
        showNotify(getContext().getString(resId));
    }

    /**
     * 所有页面收到通知消息时弹Toast
     *
     * @param str
     */
    public static void showNotify(final String str) {

        if (isRunInMainThread()) {
            showNotifyInMainThread(str);
        } else {
            runInMainThread(new Runnable() {
                @Override
                public void run() {
                    showNotifyInMainThread(str);
                }
            });
        }
    }

    private static void showNotifyInMainThread(String resId) {

        if (sToastNotify == null) {

            sToastNotify = new Toast(getContext());
        }
        sToastNotify.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.custom_toast, null);
        TextView sToastNotifyTextView = (TextView) v.findViewById(R.id.tv_custom_toast);
        sToastNotifyTextView.setText(resId);
        sToastNotifyTextView.getBackground().mutate().setAlpha(50);
        sToastNotify.setView(v);
        sToastNotify.setDuration(Toast.LENGTH_LONG);
        sToastNotifyTextView.setText(resId);
        sToastNotify.setDuration(Toast.LENGTH_LONG);
        sToastNotify.show();
    }

    public static AlertDialog getDialog(Context context) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        final AlertDialog Dialog = adb.create();
        return Dialog;
    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static boolean getLanguage() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("en")) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 弹出软键盘
     * @param editText
     */
    public static void showSoftInput(EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }


    public static void openUrl(Uri url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(url);
        context.startActivity(intent);
    }

    /**
     * 跳转到activity
     *
     * @param activity
     * @param bundle
     * @param clas
     * @param isFinish
     * @param isBack
     */
    public static void gotoActivity(Activity activity, Bundle bundle, Class<?> clas, boolean isFinish, boolean isBack) {
        Intent intent = new Intent();
        intent.setClass(activity, clas);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isFinish)
            activity.finish();
        if (isBack) {
            activity.overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);//左进右出
        } else {
            activity.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);//右进左出
        }
    }

    public static void gotoBundleActivity(Activity activity, String clas, Bundle bundle, boolean isFinish, boolean isBack) {
        Intent intent = new Intent();
        intent.setClassName(activity, clas);
        if (bundle != null)
            intent.putExtras(bundle);
        if (isFinish)
            activity.finish();
        if (isBack)
            activity.overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        else
            activity.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

}
