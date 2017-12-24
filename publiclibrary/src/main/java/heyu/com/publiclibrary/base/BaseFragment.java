package heyu.com.publiclibrary.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author heyu
 *         文件名：不需要联网加载的Fragment的基类
 *         描 述：
 *         时 间：15/08/15
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    public BaseFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract View initView();

    public void initData() {

    }
    public boolean isUIRunning(){
        boolean isRunning= isAdded() && (! getActivity().isDestroyed()) &&(!getActivity().isFinishing());
        Log.d("BaseFragment", "isUIRunning:"+isRunning);
        return isRunning;
    }
}
