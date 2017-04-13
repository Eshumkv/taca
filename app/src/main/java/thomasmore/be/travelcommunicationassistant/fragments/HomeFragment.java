package thomasmore.be.travelcommunicationassistant.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.misc.HomeScreenValues;

public class HomeFragment extends Fragment {
    private static final HomeScreenValues[] values = new HomeScreenValues[] {
            new HomeScreenValues("Messages", R.drawable.ic_message_black_24dp),
            new HomeScreenValues("Rooms", R.drawable.ic_crop_square_black_24dp),
            new HomeScreenValues("Pictograms", R.drawable.ic_stars_black_24dp),
            new HomeScreenValues("Categories", R.drawable.ic_dashboard_black_24dp),
            new HomeScreenValues("Warded Persons", R.drawable.ic_person_black_24dp),
            new HomeScreenValues("Contacts", R.drawable.ic_contacts_black_24dp),
            new HomeScreenValues("", 0), // Needed to have the last item be in the middle.
            new HomeScreenValues("Personal Info", R.drawable.ic_info_black_24dp),
    };

    public HomeFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final GridView grid = (GridView) rootView.findViewById(R.id.grid);
        grid.setAdapter(new HomeScreenAdapter(getActivity(), Arrays.asList(values)));

        return rootView;
    }
}
