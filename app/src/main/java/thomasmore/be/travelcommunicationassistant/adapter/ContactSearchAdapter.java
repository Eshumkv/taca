package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.Room;

public class ContactSearchAdapter extends MyBaseAdapter<Contact> {

    static class ViewHolder {
        private TextView id;
        private TextView name;
        private TextView phonenumber;
        private ImageView image;
    }

    private final int layoutId;

    public ContactSearchAdapter(Context context, List<Contact> values, int layoutId) {
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
            viewHolder.name = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.phonenumber = (TextView) convertView.findViewById(R.id.contact_telephone);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.contact_image);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);

            convertView.setTag(viewHolder);
        }

        final Contact value = this.values.get(position);
        viewHolder.name.setText(value.getName());
        viewHolder.phonenumber.setText(value.getPhonenumber());

        Uri uri = Uri.parse(value.getImagePath() == null ? "" : value.getImagePath());
        viewHolder.image.setImageURI(uri);

        Drawable bm = viewHolder.image.getDrawable();
        if (bm == null) {
            viewHolder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.contact));
        }

        viewHolder.id.setText(value.getId() + "");
        viewHolder.id.setTag(value);

        return convertView;
    }
}