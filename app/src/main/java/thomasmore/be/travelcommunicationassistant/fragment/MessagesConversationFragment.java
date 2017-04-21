package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Fragment;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
        final View rootView = inflater.inflate(R.layout.fragment_messages_conversation, container, false);

        List<MessageSingleConversationViewModel> tempList = new ArrayList<>();
        tempList.add(new MessageSingleConversationViewModel("Me", "Hello", true));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "Hi there!", false));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "I think we need to talk sometime, right?", false));
        tempList.add(new MessageSingleConversationViewModel("Me", "Yes, we do! do you think you have time tommorow? I'd love to talk", true));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "Sure! See you tommorow.", false));

        ListView list = (ListView) rootView.findViewById(R.id.conversation);
        list.setAdapter(new SingleConversationAdapter(getActivity(), tempList));
        list.requestFocus();

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle("Ivan");

        final EditText msgText = (EditText) rootView.findViewById(R.id.reply);
        final TextView warning = (TextView) rootView.findViewById(R.id.warning);
        msgText.setOnFocusChangeListener(replyFocusListener);

        ImageView sendbutton = (ImageView) rootView.findViewById(R.id.sendbutton);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msgText.getText().toString();

                if (!message.trim().equals("")) {
                    msgText.setText("Sent!");
                    hideKeyboard();
                    warning.setVisibility(View.GONE);
                }
            }
        });

        final LinearLayout textMessageInput = (LinearLayout)rootView.findViewById(R.id.send);
        final LinearLayout pictoMessageInput = (LinearLayout)rootView.findViewById(R.id.sendpicto);

        ImageView pictobutton = (ImageView)rootView.findViewById(R.id.pictogrambutton);
        pictobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessageInput.setVisibility(View.GONE);
                pictoMessageInput.setVisibility(View.VISIBLE);
                hideKeyboard();
                warning.setVisibility(View.GONE);
            }
        });


        ImageView textbutton = (ImageView)rootView.findViewById(R.id.textbutton);
        textbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessageInput.setVisibility(View.VISIBLE);
                pictoMessageInput.setVisibility(View.GONE);
                msgText.requestFocus();
            }
        });

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

    private void hideKeyboard() {
        final EditText msgText = (EditText) getActivity().findViewById(R.id.reply);
        msgText.clearFocus();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private View.OnFocusChangeListener replyFocusListener =  new View.OnFocusChangeListener() {
        public void onFocusChange(View view, boolean gainFocus) {
            //onFocus
            if (gainFocus) {
                //set the text
                ((EditText) view).setText("In focus now");
                TextView warning = (TextView)getActivity().findViewById(R.id.warning);
                warning.setVisibility(View.VISIBLE);
            }
            // No focus
            else {
                // Don't really use it.
            }
        };
    };
}
