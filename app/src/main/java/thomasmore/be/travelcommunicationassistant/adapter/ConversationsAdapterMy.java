package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessagesListViewModel;

/**
 * Created by Eshum on 11/04/2017.
 */

public class ConversationsAdapterMy extends MyBaseAdapter<MessagesListViewModel> {

    public ConversationsAdapterMy(Context ctx, List<MessagesListViewModel> values) {
        super(ctx, values);
    }

    static class ViewHolder {
        private TextView name;
        private ImageView image;
        private TextView message;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_messages_conversation, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.message = (TextView) convertView.findViewById(R.id.contact_message);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        }

        final MessagesListViewModel value = this.values.get(position);
        viewHolder.name.setTag(value);
        viewHolder.name.setText(value.getContactName());
        viewHolder.message.setText(value.getMessagePart());

        return convertView;
    }
}

