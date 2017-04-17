package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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
import thomasmore.be.travelcommunicationassistant.adapter.ConversationsAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.SingleConversationAdapter;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessageSingleConversationViewModel;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessagesListViewModel;

public class MessagesConversationFragment extends Fragment {
    private Menu activityMenu;

    public MessagesConversationFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_messages_conversation, container, false);

        List<MessageSingleConversationViewModel> tempList = new ArrayList<>();
        tempList.add(new MessageSingleConversationViewModel("Me", "Hello", true));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "Hi there!", false));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "I think we need to talk sometime, right?", false));
        tempList.add(new MessageSingleConversationViewModel("Me", "Yes, we do! do you think you have time tommorow? I'd love to talk", true));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "Sure! See you tommorow.", false));

        ListView list = (ListView) rootView.findViewById(R.id.conversation);
        list.setAdapter(new SingleConversationAdapter(getActivity(), tempList));

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        activityMenu = menu;
        inflater.inflate(R.menu.menu_simple_back, menu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
