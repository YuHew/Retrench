package heyu.com.money.fragment;

import android.view.View;

import com.stonesun.newssdk.NewsAgent;

import heyu.com.money.R;
import heyu.com.publiclibrary.base.BaseFragment;
import heyu.com.publiclibrary.utils.GlobleUtils;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class RetrenchFoundPageFragment extends BaseFragment {

    private View mView;

    @Override
    public View initView() {
        mView = GlobleUtils.inflate(R.layout.fragment_retrench_found);
        return mView;
    }

    @Override
    public void initData() {
    }
}
