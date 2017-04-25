package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
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

public class EditSpinnerAdapter extends MyBaseAdapter<Pair<String, Integer>> {

    public EditSpinnerAdapter(Context ctx, List<Pair<String, Integer>> values) {
        super(ctx, values);
    }

    static class ViewHolder {
        private TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, null);

            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        }

        final Pair<String, Integer> value = this.values.get(position);
        viewHolder.text.setText(value.first);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);

            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        }

        final Pair<String, Integer> value = this.values.get(position);
        viewHolder.text.setText(value.first);

        return convertView;
    }
}

