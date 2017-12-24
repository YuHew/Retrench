package heyu.com.money.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import heyu.com.money.R;
import heyu.com.money.fragment.RetrenchHomePageFragment;
import heyu.com.money.fragment.RetrenchMePageFragment;
import heyu.com.money.fragment.RetrenchPageFragment;
import heyu.com.money.fragment.RetrenchFoundPageFragment;
import heyu.com.publiclibrary.base.BaseActivity;
import heyu.com.publiclibrary.utils.GlobleUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mIvHomePage, mIvRetrench, mIvFound, mIvMe;
    private TextView mTvHomePage, mTvRetrench, mTvFound, mTvMe;
    private RelativeLayout mRlHomePage, mRlRetrench, mRlFound, mRlMe;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private RetrenchHomePageFragment mRetrenchHomePageFragment;
    private RetrenchPageFragment mRetrenchPageFragment;
    private RetrenchFoundPageFragment mRetrenchFoundPageFragment;
    private RetrenchMePageFragment mRetrenchMePageFragment;

    private static final int TAB_RETRENCH_HOME_PAGE = 0;
    private static final int TAB_RETRENCH_PAGE = 1;
    private static final int TAB_RETRENCH_FOUND_PAGE = 2;
    private static final int TAB_RETRENCH_ME_PAGE = 3;

    private static final String TAG_RETRENCH_HOME_PAGE = "TAG_RETRENCH_HOME_PAGE";
    private static final String TAG_RETRENCH_PAGE = "TAG_RETRENCH_PAGE";
    private static final String TAG_RETRENCH_FOUND_PAGE = "TAG_RETRENCH_FOUND_PAGE";
    private static final String TAG_RETRENCH_ME_PAGE = "TAG_RETRENCH_ME_PAGE";
    private int mCurrentSelection = -1;


    @Override
    public void initView(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        mRlHomePage = (RelativeLayout) findViewById(R.id.rl_home_page);
        mRlRetrench = (RelativeLayout) findViewById(R.id.rl_retrench);
        mRlFound = (RelativeLayout) findViewById(R.id.rl_found);
        mRlMe = (RelativeLayout) findViewById(R.id.rl_me);

        mIvHomePage = (ImageView) findViewById(R.id.iv_home_page);
        mIvRetrench = (ImageView) findViewById(R.id.iv_retrench);
        mIvFound = (ImageView) findViewById(R.id.iv_found);
        mIvMe = (ImageView) findViewById(R.id.iv_me);

        mTvHomePage = (TextView) findViewById(R.id.tv_home_page);
        mTvRetrench = (TextView) findViewById(R.id.tv_retrench);
        mTvFound = (TextView) findViewById(R.id.tv_found);
        mTvMe = (TextView) findViewById(R.id.tv_me);

        mRlHomePage.setOnClickListener(this);
        mRlRetrench.setOnClickListener(this);
        mRlFound.setOnClickListener(this);
        mRlMe.setOnClickListener(this);
        if (savedInstanceState != null) {
            mRetrenchHomePageFragment = (RetrenchHomePageFragment) mFragmentManager.findFragmentByTag(TAG_RETRENCH_HOME_PAGE);
            mRetrenchPageFragment = (RetrenchPageFragment) mFragmentManager.findFragmentByTag(TAG_RETRENCH_PAGE);
            mRetrenchFoundPageFragment = (RetrenchFoundPageFragment) mFragmentManager.findFragmentByTag(TAG_RETRENCH_FOUND_PAGE);
            mRetrenchMePageFragment = (RetrenchMePageFragment) mFragmentManager.findFragmentByTag(TAG_RETRENCH_ME_PAGE);
        }
    }

    @Override
    public void loadData() {
        setTabSelection(TAB_RETRENCH_HOME_PAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (mCurrentSelection) {
            case TAB_RETRENCH_HOME_PAGE:
                setTabSelection(TAB_RETRENCH_HOME_PAGE);
                break;
            case TAB_RETRENCH_PAGE:
                setTabSelection(TAB_RETRENCH_PAGE);
                break;
            case TAB_RETRENCH_FOUND_PAGE:
                setTabSelection(TAB_RETRENCH_FOUND_PAGE);
                break;
            case TAB_RETRENCH_ME_PAGE:
                setTabSelection(TAB_RETRENCH_ME_PAGE);
                break;
            default:
                setTabSelection(TAB_RETRENCH_HOME_PAGE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home_page://服务
                setTabSelection(TAB_RETRENCH_HOME_PAGE);
                break;
            case R.id.rl_retrench:// 设备

                setTabSelection(TAB_RETRENCH_PAGE);
                break;
            case R.id.rl_found:// 社区

                setTabSelection(TAB_RETRENCH_FOUND_PAGE);
                break;
            case R.id.rl_me:// 我的
                setTabSelection(TAB_RETRENCH_ME_PAGE);
                break;
        }
    }

    /**
     * 切换对应的Fragment
     *
     * @param iPage
     */
    private void setTabSelection(int iPage) {
        //重复点击
        if (iPage == mCurrentSelection) {
            return;
        }
        mCurrentSelection = iPage;
        mFragmentTransaction = mFragmentManager.beginTransaction();
        clearSelection();
        hideFragments(mFragmentTransaction);
        switch (iPage) {
            case TAB_RETRENCH_HOME_PAGE:
                mIvHomePage.setBackgroundResource(R.drawable.home_page_selected);
                mTvHomePage.setTextColor(android.graphics.Color.parseColor(GlobleUtils.getString(R.color.home_page_tab_selected)));
                if (mRetrenchHomePageFragment == null) {
                    mRetrenchHomePageFragment = new RetrenchHomePageFragment();
                    mFragmentTransaction.add(R.id.fl_content, mRetrenchHomePageFragment);
                } else {
                    mFragmentTransaction.show(mRetrenchHomePageFragment);
                }
                break;
            case TAB_RETRENCH_PAGE:
                mIvRetrench.setBackgroundResource(R.drawable.retrench_selected);
                mTvRetrench.setTextColor(android.graphics.Color.parseColor(GlobleUtils.getString(R.color.home_page_tab_selected)));
                if (mRetrenchPageFragment == null) {
                    mRetrenchPageFragment = new RetrenchPageFragment();
                    mFragmentTransaction.add(R.id.fl_content, mRetrenchPageFragment);
                } else {
                    mFragmentTransaction.show(mRetrenchPageFragment);
                }
                break;
            case TAB_RETRENCH_FOUND_PAGE:
                mIvFound.setBackgroundResource(R.drawable.found_selected);
                mTvFound.setTextColor(android.graphics.Color.parseColor(GlobleUtils.getString(R.color.home_page_tab_selected)));
                if (mRetrenchFoundPageFragment == null) {
                    mRetrenchFoundPageFragment = new RetrenchFoundPageFragment();
                    mFragmentTransaction.add(R.id.fl_content, mRetrenchFoundPageFragment);
                } else {
                    mFragmentTransaction.show(mRetrenchFoundPageFragment);
                }
                break;
            case TAB_RETRENCH_ME_PAGE:// 我的
                mIvMe.setBackgroundResource(R.drawable.me_selected);
                mTvMe.setTextColor(android.graphics.Color.parseColor(GlobleUtils.getString(R.color.home_page_tab_selected)));
                if (mRetrenchMePageFragment == null) {
                    mRetrenchMePageFragment = new RetrenchMePageFragment();
                    mFragmentTransaction.add(R.id.fl_content, mRetrenchMePageFragment);
                } else {
                    mFragmentTransaction.show(mRetrenchMePageFragment);
                }
                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 清除选中态
     */
    private void clearSelection() {
        mIvMe.setBackgroundResource(R.drawable.me_unselected);
        mTvMe.setTextColor(android.graphics.Color.parseColor(GlobleUtils.getString(R.color.home_page_tab_unselected)));
        mIvHomePage.setBackgroundResource(R.drawable.home_page_unselected);
        mTvHomePage.setTextColor(android.graphics.Color.parseColor(GlobleUtils.getString(R.color.home_page_tab_unselected)));
        mIvRetrench.setBackgroundResource(R.drawable.retrench_unselected);
        mTvRetrench.setTextColor(android.graphics.Color.parseColor(GlobleUtils.getString(R.color.home_page_tab_unselected)));
        mIvFound.setBackgroundResource(R.drawable.found_unselected);
        mTvFound.setTextColor(android.graphics.Color.parseColor(GlobleUtils.getString(R.color.home_page_tab_unselected)));
    }

    /**
     * 隐藏fragment
     *
     * @param fragmentTransation
     */
    public void hideFragments(FragmentTransaction fragmentTransation) {
        if (mRetrenchHomePageFragment != null) {
            fragmentTransation.hide(mRetrenchHomePageFragment);
        }
        if (mRetrenchPageFragment != null) {
            fragmentTransation.hide(mRetrenchPageFragment);
        }
        if (mRetrenchFoundPageFragment != null) {
            fragmentTransation.hide(mRetrenchFoundPageFragment);
        }
        if (mRetrenchMePageFragment != null) {
            fragmentTransation.hide(mRetrenchMePageFragment);
        }
    }

}
