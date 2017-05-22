package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
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

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.ConversationsAdapter;
import thomasmore.be.travelcommunicationassistant.model.Message;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessagesListViewModel;

public class MessagesListFragment extends BaseFragment {

    public MessagesListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages_list, container, false);

        Database db = Database.getInstance(getActivity());
        long userId = db.getSettings().getLoggedInUser(getActivity()).getId();
        List<Message> messages = db.getMessages(userId);

        List<MessagesListViewModel> tempList = MessagesListViewModel.fromMessages(messages, getActivity());

        final ListView list = (ListView) rootView.findViewById(R.id.conversations);
        list.setAdapter(new ConversationsAdapter(getActivity(), tempList));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessagesListViewModel msg = (MessagesListViewModel) list.getAdapter().getItem(position);

                TextView text = (TextView) view.findViewById(R.id.contact_name);
                MessagesListViewModel model = (MessagesListViewModel)text.getTag();
                callback.onFragmentInteraction(MessagesListFragment.class, model);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
