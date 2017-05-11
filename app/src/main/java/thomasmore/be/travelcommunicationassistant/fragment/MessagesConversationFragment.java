package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.NavigationDrawerActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.QuickMessageAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.SingleConversationAdapter;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.model.User;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessageSingleConversationViewModel;

import static android.app.Activity.RESULT_OK;

public class MessagesConversationFragment extends BaseFragment {

    public static final int REQUEST_NEW_PICTOGRAM = 1;

    private boolean isPicto = false;

    List<MessageSingleConversationViewModel> tempList;
    ArrayList<Pictogram> tempPictoConvo;

    private LayoutInflater inflater;

    public MessagesConversationFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        final View rootView = inflater.inflate(R.layout.fragment_messages_conversation, container, false);

        tempList = new ArrayList<>();
        tempList.add(new MessageSingleConversationViewModel(getMeString(), "Hello", true));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "Hi there!", false));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "I think we need to talk sometime, right?", false));
        tempList.add(new MessageSingleConversationViewModel(getMeString(), "Yes, we do! do you think you have time tomorrow? I'd love to talk", true));
        tempList.add(new MessageSingleConversationViewModel("Ivan", "Sure! See you tomorrow.", false));

        tempPictoConvo = new ArrayList<>();

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
                    sendMessage(message);
                    msgText.setText("");
                    tempPictoConvo.clear();

                    hideKeyboard();
                    warning.setVisibility(View.GONE);
                }
            }
        });

        ImageView sendpictobutton = (ImageView) rootView.findViewById(R.id.sendpictobutton);
        sendpictobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempPictoConvo.size() == 0) return;

                sendPictogram(tempPictoConvo);

                // TODO: Actually send message? :D
                sendMessage("...");
                msgText.setText("");

                for (int i = tempPictoConvo.size() - 1; i >= 0; i--) {
                    getList(null).removeViewAt(i);
                }

                tempPictoConvo.clear();

                hideKeyboard();
                warning.setVisibility(View.GONE);
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
                isPicto = true;
            }
        });


        ImageView textbutton = (ImageView)rootView.findViewById(R.id.textbutton);
        textbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessageInput.setVisibility(View.VISIBLE);
                pictoMessageInput.setVisibility(View.GONE);
                msgText.requestFocus();
                Helper.showKeyboard(getActivity());
                isPicto = false;
            }
        });

        Button addPictogramButton = (Button) rootView.findViewById(R.id.add);
        addPictogramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity(), BackActivity.class);
            intent.putExtra(BackActivity.DATA_STRING, MajorCategoryListFragment.class);
            intent.putExtra(Helper.EXTRA_SEARCH_INTENT, Pictogram.class);
            startActivityForResult(intent, REQUEST_NEW_PICTOGRAM);
            }
        });

        ImageView deletePictogramButton = (ImageView) rootView.findViewById(R.id.backspace);
        deletePictogramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempPictoConvo.size() == 0) return;

                getList(rootView).removeViewAt(tempPictoConvo.size() - 1);
                tempPictoConvo.remove(tempPictoConvo.size() - 1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_PICTOGRAM && resultCode == RESULT_OK) {
            Pictogram pictogram = data.getParcelableExtra(Pictogram.class.getName());
            tempPictoConvo.add(pictogram);
            getList(null).addView(getPictogramLayout(pictogram));
        }
    }

    private void hideKeyboard() {
        final EditText msgText = (EditText) getActivity().findViewById(R.id.reply);
        msgText.clearFocus();
        Helper.hideKeyboard(getActivity());
    }

    private LinearLayout getList(View v) {
        LinearLayout list;

        if (v != null) {
            list = (LinearLayout) v.findViewById(R.id.pictograms);
        } else {
            list = (LinearLayout) getActivity().findViewById(R.id.pictograms);
        }

        return list;
    }

    private View.OnFocusChangeListener replyFocusListener =  new View.OnFocusChangeListener() {
        public void onFocusChange(View view, boolean gainFocus) {
            //onFocus
            if (gainFocus) {
                //set the text
                //((EditText) view).setText("In focus now");
                TextView warning = (TextView)getActivity().findViewById(R.id.warning);
                warning.setVisibility(View.VISIBLE);
            }
            // No focus
            else {
                // Don't really use it.
            }
        };
    };

    private void sendMessage(String message) {
        if (message == null || message.equals("")) return;

        tempList.add(new MessageSingleConversationViewModel(getMeString(), message, true));

        ListView list = (ListView) getActivity().findViewById(R.id.conversation);
        list.setAdapter(new SingleConversationAdapter(getActivity(), tempList));
    }


    private void sendPictogram(List<Pictogram> pictograms) {
    }

    private String getMeString() {
        return getString(R.string.me);
    }

    private View getPictogramLayout(Pictogram p) {
        View v = QuickMessageAdapter.getPictogramIcon(p, getActivity(), inflater, getList(null));
        return v;
    }
}
