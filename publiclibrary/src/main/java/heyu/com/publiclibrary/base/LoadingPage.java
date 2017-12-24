package heyu.com.publiclibrary.base;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import heyu.com.publiclibrary.R;
import heyu.com.publiclibrary.utils.GlobleUtils;

/**
 * @author heyu
 *         文件名：加载页面
 *         描 述：
 *         时 间：15/08/18
 */
public abstract class LoadingPage extends FrameLayout {

    private static final int STATE_UNLOADED = 0;// 加载默认的状态
    private static final int STATE_LOADING = 1;// 加载的状态
    private static final int STATE_ERROR = 3;// / 加载失败的状态
    private static final int STATE_EMPTY = 4;// 加载成功了但是没有数据状态
    private static final int STATE_SUCCEED = 5;// 加载成功的状态

    private Button mTryButton;// 重试按钮
    private View mLoadingView;// 加载时显示的View
    private View mErrorView;// 加载出错显示的View
    private View mEmptyView;// 加载没有数据显示的View
    private View mSucceedView;// 加载成功显示的View

    private int mCurrentState;// 当前的状态，显示需要根据该状态判断

    public LoadingPage(Context context) {
        super(context);
        init();
    }

    private void init() {
        // 第一次进来先给一个默认的状态
        mCurrentState = STATE_UNLOADED;

        // 创建一个加载的页面
        mLoadingView = createLoadingView();
        if (null != mLoadingView) {
            addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }

        // 创建一个错误的页面
        mErrorView = createErrorView();
        if (null != mErrorView) {
            addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            mTryButton = (Button) mErrorView.findViewById(R.id.but_try_again);
            mTryButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    show();
                }
            });
        }

        // 创建一个空的页面
        mEmptyView = createEmptyView();
        if (null != mEmptyView) {
            addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
        showSafePage();
    }

    protected View createLoadingView() {
        return GlobleUtils.inflate(R.layout.loading_page_loading);
    }

    protected View createEmptyView() {
        return GlobleUtils.inflate(R.layout.loading_page_empty);
    }

    protected View createErrorView() {
        return GlobleUtils.inflate(R.layout.loading_page_error);
    }

    /**
     * 根据结果设置联网加载后的状态 显示对应的页面(子线程)
     *
     * @param result
     */
    public void setState(LoadResult result) {
        mCurrentState = result.getValue();
        showSafePage();
    }

    public void show() {
        if (mCurrentState == STATE_ERROR || mCurrentState == STATE_EMPTY) {
            mCurrentState = STATE_UNLOADED;
        }
        //STATE_UNLOADED 还有一个作用就是防止同时多次调用show方法导致开了多个线程
        if (mCurrentState == STATE_UNLOADED) {
            mCurrentState = STATE_LOADING;
            TaskRunnable task = new TaskRunnable();
            ThreadManager.getLongPool().execute(task);
        }
        showSafePage();
    }

    private void showSafePage() {
        GlobleUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        });
    }

    /**
     * 根据状态来展示具体的某一个界面
     */
    private void showPage() {
        if (null != mLoadingView) {
            mLoadingView.setVisibility(mCurrentState == STATE_UNLOADED
                    || mCurrentState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(mCurrentState == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(mCurrentState == STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }

        if (mCurrentState == STATE_SUCCEED && mSucceedView == null) {
            mSucceedView = createLoadedView();
            addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }

        if (null != mSucceedView) {
            mSucceedView.setVisibility(mCurrentState == STATE_SUCCEED ? View.VISIBLE
                    : View.INVISIBLE);
        }
    }

    class TaskRunnable implements Runnable {
        @Override
        public void run() {
            load();
        }
    }

    public enum LoadResult {
        ERROR(3), EMPTY(4), SUCCEED(5);
        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 联网加载(子线程)
     *
     * @return
     */
    public abstract void load();

    /**
     * 根据加载的结果来创建布局(主线程)
     *
     * @return
     */
    public abstract View createLoadedView();
}
