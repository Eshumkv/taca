package thomasmore.be.travelcommunicationassistant.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;

public class HomeFragment extends Fragment {
    private Menu activityMenu;

    OnHomeItemSelectedListener callback;

    public interface OnHomeItemSelectedListener {
        void onHomeItemSelected(Class<?> cls);
    }

    public HomeFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure the container activity has implemented the callback.
        // If not, throw an exception.
        try {
            callback = (OnHomeItemSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement OnHomeItemSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Used to indicate this fragment has it's own menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final GridView grid = (GridView) rootView.findViewById(R.id.grid);
        grid.setAdapter(new HomeScreenAdapter(getActivity(), getCorrectItems()));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NavigationItems item = getCorrectItems().get(position);
                callback.onHomeItemSelected(item.getCls());
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        activityMenu = menu;
        inflater.inflate(R.menu.menu_home, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<NavigationItems> getCorrectItems() {
        List<NavigationItems> list = new ArrayList<>();

        for (NavigationItems item : Helper.navigationItems) {
            if (item.getTitleId() != R.string.nav_home) {
                // If it's the last item, now "Personal info", then add an empty thing
                if (item.getTitleId() == R.string.nav_personal) {
                    list.add(new NavigationItems(-1, -1, null));
                }

                list.add(item);
            }
        }

        return list;
    }
}
