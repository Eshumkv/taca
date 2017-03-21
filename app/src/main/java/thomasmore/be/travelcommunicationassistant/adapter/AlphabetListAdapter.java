package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;

/**
 * Created by Eshum on 20/03/2017.
 */

public class AlphabetListAdapter extends BaseAdapter {

    public static abstract class Row {
        public abstract String getText();
    }

    public static final class Section extends Row {
        public final String text;

        public Section(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }
    }

    public static final class Item extends Row {
        public final String text;

        public Item(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }
    }

    private List rows;

    public void setRows(List rows) {
        this.rows = rows;
    }
    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Object getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Section) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final LayoutInflater inflater = (LayoutInflater) parent.getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (getItemViewType(position) == 0) { // Item
            if (view == null) {
                view = inflater.inflate(R.layout.item_contacts, parent, false);
            }

            Item item = (Item) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.contact_name);
            textView.setText(item.text);
        } else { // Section
            if (view == null) {
                view = inflater.inflate(R.layout.contacts_row_section, parent, false);
            }

            Section section = (Section) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            textView.setText(section.text);
        }

        return view;
    }
}
