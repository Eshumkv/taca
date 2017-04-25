package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.ContactType;

/**
 * Created by Eshum on 11/04/2017.
 */

public class PagingAdapter extends MyBaseListAdapter<String> {

    public PagingAdapter(Context ctx, List<String> values) {
        super(ctx, values, R.layout.item_paging);
    }

    static class ViewHolder {
        private TextView page;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, null);

            viewHolder = new ViewHolder();
            viewHolder.page = (TextView) convertView.findViewById(R.id.page);

            convertView.setTag(viewHolder);
        }

        final String value = this.values.get(position);
        viewHolder.page.setText(value);

        return convertView;
    }
}

