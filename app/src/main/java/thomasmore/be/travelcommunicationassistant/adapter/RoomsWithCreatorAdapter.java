package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Room;

public class RoomsWithCreatorAdapter extends MyBaseAdapter<Room> {

    static class ViewHolder {
        private TextView id;
        private TextView name;
        private TextView creator;
    }

    private final int layoutId;

    public RoomsWithCreatorAdapter(Context context, List<Room> values, int layoutId) {
        super(context, values);
        this.layoutId = layoutId;
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
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.creator = (TextView) convertView.findViewById(R.id.creator);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);

            convertView.setTag(viewHolder);
        }

        final Room value = this.values.get(position);
        viewHolder.name.setText(value.getName());
        viewHolder.creator.setText(value.getCreator());
        viewHolder.id.setText(value.getId() + "");
        viewHolder.id.setTag(value);

        return convertView;
    }
}