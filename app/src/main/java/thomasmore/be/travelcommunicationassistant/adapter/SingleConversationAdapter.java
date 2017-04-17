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

public class SingleConversationAdapter extends BaseAdapter {

    private Context context;
    private List<MessageSingleConversationViewModel> values;

    public SingleConversationAdapter(Context ctx, List<MessageSingleConversationViewModel> values) {
        this.values = new ArrayList<>(values);
        context = ctx;
    }

    static class ViewHolder {
        private TextView name;
        private TextView message;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return values.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
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

