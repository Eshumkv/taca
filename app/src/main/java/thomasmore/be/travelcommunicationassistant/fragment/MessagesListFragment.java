package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.ConversationsAdapterMy;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessagesListViewModel;

public class MessagesListFragment extends Fragment {
    private Menu activityMenu;

    OnConversationItemSelectedListener callback;

    public interface OnConversationItemSelectedListener {
        void onConversationItemSelected(MessagesListViewModel convo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure the container activity has implemented the callback.
        // If not, throw an exception.
        try {
            callback = (MessagesListFragment.OnConversationItemSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement OnConversationItemSelectedListener");
        }
    }

    public MessagesListFragment() {
        // Empty constructor required for fragment subclasses
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
        View rootView = inflater.inflate(R.layout.fragment_messages_list, container, false);

        List<MessagesListViewModel> tempList = new ArrayList<>();
        tempList.add(new MessagesListViewModel("Ivan", "What're you doing?"));
        tempList.add(new MessagesListViewModel("Viktor", "Hello"));
        tempList.add(new MessagesListViewModel("Anna", "Sometimes"));
        tempList.add(new MessagesListViewModel("John", "I think so ..."));
        tempList.add(new MessagesListViewModel("Bobby", "Have fun :)"));

        ListView list = (ListView) rootView.findViewById(R.id.conversations);
        list.setAdapter(new ConversationsAdapterMy(getActivity(), tempList));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.contact_name);
                MessagesListViewModel model = (MessagesListViewModel)text.getTag();
                callback.onConversationItemSelected(model);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        activityMenu = menu;
        inflater.inflate(R.menu.menu_messages, menu);

        MenuItem groupSpinner = menu.findItem(R.id.menu_group_spinner);
        View view = MenuItemCompat.getActionView(groupSpinner);

        if (view instanceof Spinner) {
            final Spinner spinner = (Spinner) view;
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    getActivity(),
                    R.array.groups_array_all,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

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
}
