package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;

public class CategoryAdapter extends MyBaseAdapter<Category> {

    static class ViewHolder {
        private TextView id;
        private TextView name;
        private TextView description;
        private TextView linked;
        private ImageView image;
    }

    public CategoryAdapter(Context context, List<Category> values) {
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
            convertView = inflater.inflate(R.layout.item_category, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.linked = (TextView) convertView.findViewById(R.id.linked);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);

            convertView.setTag(viewHolder);
        }

        final Category value = this.values.get(position);
        viewHolder.name.setText(value.getName());
        viewHolder.description.setText(value.getDescription());
        viewHolder.linked.setText("" + value.getLinkedPictograms());
        viewHolder.id.setText(value.getId() + "");
        viewHolder.id.setTag(value);

        return convertView;
    }
}