package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MyBaseListAdapter<T> extends ArrayAdapter<T> {

    final protected int layoutId;
    final protected Context context;
    final protected List<T> values;

    public MyBaseListAdapter(Context ctx, List<T> values, int resId) {
        super(ctx, resId, values);
        this.values = new ArrayList<>(values);
        this.context = ctx;
        this.layoutId = resId;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}

