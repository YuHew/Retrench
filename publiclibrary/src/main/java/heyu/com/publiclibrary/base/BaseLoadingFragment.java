package heyu.com.publiclibrary.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import heyu.com.publiclibrary.utils.GlobleUtils;

/**
 * @author heyu
 *         文件名：需要联网加载的fragment的基类
 *         描 述：
 *         时 间：15/08/15
 */
public abstract class BaseLoadingFragment extends Fragment {

    protected Activity mActivity;
    private  LoadingPage mLoadingPage;

    public BaseLoadingFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLoadingPage = new LoadingPage(GlobleUtils.getContext()) {

            @Override
            public void load() {
                BaseLoadingFragment.this.load();
            }

            @Override
            public View createLoadedView() {
                View view = BaseLoadingFragment.this.createLoadedView();
                return view;
            }
        };
        return mLoadingPage;
    }

    /**
     * 加载(子线程)
     *
     * @return
     */
    protected abstract void load();

    /**
     * 根据加载的结果来创建布局(主线程)
     *
     * @return
     */
    protected abstract View createLoadedView();

    public void show() {
        if (mLoadingPage != null) {
            mLoadingPage.show();
        }
    }

    /**
     * 设置请求网络结果
     *
     * @param result
     */
    public  void setState(LoadingPage.LoadResult result) {
        mLoadingPage.setState(result);
    }
}
