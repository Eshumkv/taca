package thomasmore.be.travelcommunicationassistant.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;

public class PictogramAdapter extends MyBaseAdapter<Pictogram> {

    static class ViewHolder {
        private TextView id;
        private TextView name;
        private TextView description;
        private ImageView image;
        private CardView card;
    }

    private List<Pictogram> selectedPictograms;
    private int selectedColor;
    private int normalColor;

    public PictogramAdapter(Context context, List<Pictogram> values) {
        super(context, values);
        this.selectedPictograms = new ArrayList<>();
    }

    public PictogramAdapter(Context context, List<Pictogram> values, List<Pictogram> selected,
                            int selectedColor, int normalColor) {
        super(context, values);
        this.selectedPictograms = selected;
        this.selectedColor = selectedColor;
        this.normalColor = normalColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_pictogram, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
            viewHolder.card = (CardView) convertView.findViewById(R.id.card_view);

            convertView.setTag(viewHolder);
        }

        final Pictogram value = this.values.get(position);
        viewHolder.name.setText(value.getName());
        viewHolder.description.setText(value.getDescription());
        viewHolder.id.setText(value.getId() + "");
        viewHolder.id.setTag(value);

        if (selectedPictograms.size() != 0) {
            if (selectedPictograms.contains(value)) {
                viewHolder.card.setCardBackgroundColor(selectedColor);
            } else {
                viewHolder.card.setCardBackgroundColor(normalColor);
            }
        }

        return convertView;
    }
}