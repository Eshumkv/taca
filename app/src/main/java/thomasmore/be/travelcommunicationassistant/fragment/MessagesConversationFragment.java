package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.NavigationDrawerActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.SingleConversationAdapter;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessageSingleConversationViewModel;

public class MessagesConversationFragment extends BaseFragment {

    public MessagesConversationFragment() {
        // Empty constructor required for fragment subclasses
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

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle("Ivan");

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simple_back, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }
}
