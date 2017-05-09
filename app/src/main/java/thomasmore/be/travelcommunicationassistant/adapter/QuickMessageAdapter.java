package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.model.QuickMessage;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class QuickMessageAdapter extends MyBaseAdapter<QuickMessage> {

    static class ViewHolder {
        private TextView id;
        private LinearLayout pictogramList;
    }

    public QuickMessageAdapter(Context context, List<QuickMessage> values) {
        super(context, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.item_quick_message, null);

            viewHolder = new ViewHolder();
            viewHolder.pictogramList = (LinearLayout) convertView.findViewById(R.id.pictogram_list);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);

            convertView.setTag(viewHolder);
        }

        final QuickMessage value = this.values.get(position);

        // Fill the linearlayout
        if (value.getMessage() != null) {
            for (Pictogram p : value.getMessage()) {
                viewHolder.pictogramList.addView(getPictogramIcon(p, context, inflater, viewHolder.pictogramList));
            }
        }

        viewHolder.id.setText(value.getId() + "");
        viewHolder.id.setTag(value);

        return convertView;
    }

    public static View getPictogramIcon(Pictogram pictogram, Context context, LayoutInflater inflater, ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_pictogram_icon, parent, false);

        ImageView image = (ImageView) v.findViewById(R.id.pictogram);
        TextView text = (TextView) v.findViewById(R.id.pictogram_text);

        image.setImageBitmap(Helper.getImage(context, pictogram.getImagePath()));
        text.setText(pictogram.getName());

        return v;
    }
}