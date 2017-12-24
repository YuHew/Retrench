package heyu.com.publiclibrary.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author heyu
 *         文件名：适配器基类
 *         描 述：
 *         时 间：16/06/21
 */
public class CommonListAdapter<T> extends BaseAdapter {

    protected List<T> dataSource;

    public CommonListAdapter(Context context) {
        dataSource = new ArrayList<T>();
    }


    @Override
    public int getCount() {
        return dataSource==null?0:dataSource.size();
    }

    @Override
    public T getItem(int position) {
        return dataSource==null?null:dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
    public void addItem(T item) {
        dataSource.add(item);
    }

    public void addFirstItem(T item) {
        dataSource.add(0, item);
    }

    public void removeItem(int position) {
        dataSource.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(int position, T item) {
        dataSource.add(position, item);
        notifyDataSetChanged();
    }

    public void addItem(T[] items) {
        dataSource.addAll(Arrays.asList(items));
    }

    public void addAll(List<T> items) {
        if(items!=null){
            dataSource.addAll(items);
        }
    }

    public void clear() {

        if (dataSource.size() > 0) {
            dataSource.clear();
        }

    }
}
