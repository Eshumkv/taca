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
import thomasmore.be.travelcommunicationassistant.viewmodel.MessagesListViewModel;

/**
 * Created by Eshum on 11/04/2017.
 */

public class ContactsListAdapter extends MyBaseAdapter<Contact> {

    private int tutorColor;
    private int normalColor;

    public ContactsListAdapter(Context ctx, List<Contact> values) {
        super(ctx, values);
        tutorColor = ContextCompat.getColor(ctx, R.color.card_tutor);
        normalColor = ContextCompat.getColor(ctx, R.color.cardNormal);
    }

    static class ViewHolder {
        private TextView name;
        private ImageView image;
        private TextView number;
        private CardView card;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_contacts, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.number = (TextView) convertView.findViewById(R.id.contact_telephone);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.card = (CardView) convertView.findViewById(R.id.card_view);

            convertView.setTag(viewHolder);
        }

        final Contact value = this.values.get(position);
        viewHolder.name.setTag(value);
        viewHolder.name.setText(value.getName());
        viewHolder.number.setText(value.getPhonenumber());
        if (value.getType() == ContactType.Tutor) {
            viewHolder.card.setCardBackgroundColor(tutorColor);
        } else {
            viewHolder.card.setCardBackgroundColor(normalColor);
        }

        return convertView;
    }
}

