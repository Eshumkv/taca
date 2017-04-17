package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessageSingleConversationViewModel;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessagesListViewModel;

/**
 * Created by Eshum on 11/04/2017.
 */

public class SingleConversationAdapter extends MyBaseAdapter<MessageSingleConversationViewModel> {

    public SingleConversationAdapter(Context ctx, List<MessageSingleConversationViewModel> values) {
        super(ctx, values);
    }

    static class ViewHolder {
        private TextView name;
        private TextView message;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        MessageSingleConversationViewModel msg = values.get(position);

        if (msg.getUserType() == MessageSingleConversationViewModel.UserType.Self) {

            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_chat_right, null);

                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.message = (TextView) convertView.findViewById(R.id.message);

                convertView.setTag(viewHolder);
            }

            viewHolder.name.setTag(msg);
            viewHolder.name.setText(msg.getName());
            viewHolder.message.setText(msg.getMessage());
        } else {

            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_chat_left, null);

                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.message = (TextView) convertView.findViewById(R.id.message);

                convertView.setTag(viewHolder);
            }

            viewHolder.name.setTag(msg);
            viewHolder.name.setText(msg.getName());
            viewHolder.message.setText(msg.getMessage());
        }

        return convertView;
    }
}

