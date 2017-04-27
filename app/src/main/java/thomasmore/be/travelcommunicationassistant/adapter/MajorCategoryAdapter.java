package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.model.Room;

public class MajorCategoryAdapter extends MyBaseAdapter<MajorCategory> {

    static class ViewHolder {
        private TextView id;
        private TextView name;
    }

    public MajorCategoryAdapter(Context context, List<MajorCategory> values) {
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
            convertView = inflater.inflate(R.layout.item_majorcategory, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);

            convertView.setTag(viewHolder);
        }

        final MajorCategory value = this.values.get(position);
        viewHolder.name.setText(value.getName());
        viewHolder.id.setText(value.getId() + "");
        viewHolder.id.setTag(value);

        return convertView;
    }
}