package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;

/**
 * Created by Eshum on 11/04/2017.
 */

public class HomeScreenAdapter extends MyBaseAdapter<NavigationItems> {

    public HomeScreenAdapter(Context ctx, List<NavigationItems> values) {
        super(ctx, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_homescreen, null);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.text);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        }

        final NavigationItems value = this.values.get(position);

        // Check if it's a dummy item
        if (value.getTitleId() != -1) {
            viewHolder.title.setText(value.getTitleId());
            viewHolder.image.setImageResource(value.getIconId());
        } else {
            viewHolder.title.setText("");
            viewHolder.image.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView title;
        private ImageView image;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // Make the second-to-last item non-clickable.
        return position != (values.size() - 2);
    }
}

