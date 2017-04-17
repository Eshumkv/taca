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
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;

public class NavigationAdapter extends MyBaseAdapter<NavigationItems> {

    static class ViewHolder {
        private TextView title;
        private ImageView image;
    }

    public NavigationAdapter(Context context, List<NavigationItems> values) {
        super(context, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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