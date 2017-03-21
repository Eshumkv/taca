package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<String> values;

    public SpinnerAdapter(Context context, List<String> values) {
        super(context, R.layout.item_spinner, values);
        this.context = context;
        this.values = values;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.item_spinner, parent, false);
        final TextView textView = (TextView) rowView.findViewById(R.id.value);
        textView.setText(values.get(position));

        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.item_spinner, parent, false);
        final TextView textView = (TextView) rowView.findViewById(R.id.value);
        textView.setText(values.get(position));

        return rowView;
    }
}
