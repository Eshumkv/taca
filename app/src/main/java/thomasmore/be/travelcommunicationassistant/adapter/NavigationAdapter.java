package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.misc.NavigationItems;

public class NavigationAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<NavigationItems> values;

    static class ViewHolder {
        private TextView title;
        private ImageView image;
    }

    public NavigationAdapter(Context context, List<NavigationItems> values) {
        this.context = context;
        this.values = values;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return values.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else {
            convertView = inflater.inflate(R.layout.item_navigation_drawer, null);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.text);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        }

        final NavigationItems value = this.values.get(position);
        viewHolder.title.setText(value.getTitleId());
        viewHolder.image.setImageResource(value.getIconId());

        return convertView;
    }
}