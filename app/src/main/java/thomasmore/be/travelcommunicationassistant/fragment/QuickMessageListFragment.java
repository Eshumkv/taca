package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.QuickMessageAdapter;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.model.QuickMessage;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;

import static android.app.Activity.RESULT_OK;

public class QuickMessageListFragment extends BasePagingFragment<QuickMessage> {

    public final static int REQUEST_EDIT = 1;

    private Contact contact;

    public QuickMessageListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);

        selectedColor = ContextCompat.getColor(getActivity(), R.color.cardSelected);
        normalColor = ContextCompat.getColor(getActivity(), R.color.cardNormal);

        Bundle bundle = getArguments();
        contact = bundle.getParcelable(Contact.class.getName());

        Helper.setTitle(getActivity(), R.string.warded_quick_message_list_title, contact.getName());

        Database db = Database.getInstance(getActivity());

        setupContextMenu(rootView);

        RelativeLayout bar = (RelativeLayout) rootView.findViewById(R.id.context_menu);
        bar.setVisibility(View.VISIBLE);

        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new QuickMessageAdapter(getActivity(), db.getQuickMessages(contact.getId())));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout root = (LinearLayout)view;
                CardView card = (CardView)root.findViewById(R.id.card_view);
                card.setCardBackgroundColor(selectedColor);

                // Deselect the previous card
                int temp = selectedPosition;
                deselectPrevious(rootView);
                selectedPosition = temp == position ? -1 : position;

                toggleContext();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickMessage qm = new QuickMessage();
                qm.setMessage(new ArrayList<Pictogram>());
                goToEditScreen(qm);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                QuickMessage category = (QuickMessage)list.getAdapter().getItem(selectedPosition);
                goToEditScreen(category);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setMessage(R.string.dialog_delete_qmessage)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                QuickMessage qmessage = (QuickMessage) getList().getAdapter().getItem(selectedPosition);
                                Database db = Database.getInstance(getActivity());

                                if (db.removeQuickMessage(qmessage.getId())) {
                                    Helper.toast(getActivity(), R.string.toast_saved);
                                } else {
                                    Helper.toast(getActivity(), R.string.toast_not_saved);
                                }

                                deselectPrevious(getView());
                                toggleContext();

                                setListAdapter();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deselectPrevious(getView());
                                toggleContext();
                            }
                        })
                        .create()
                        .show();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simple_save, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_save:
                getActivity().setResult(Activity.RESULT_OK);
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
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            Contact contact = data.getParcelableExtra(Contact.class.getName());
        }
    }

    private void goToEditScreen(QuickMessage message) {
        deselectPrevious(getView());
        toggleContext();

        if (message.getWardedId() == 0) {
            message.setWardedId(contact.getId());
        }

        BaseFragment fragment = new QuickMessageEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(QuickMessage.class.getName(), message);
        fragment.setArguments(bundle);
        Helper.changeFragment(getActivity(), fragment, true);
    }

    @Override
    protected void setListAdapter() {
        Database db = Database.getInstance(getActivity());
        final ListView list = getList();
        list.setAdapter(new QuickMessageAdapter(getActivity(), db.getQuickMessages(contact.getId())));
    }

    @Override
    protected ListView getList() {
        return (ListView) getActivity().findViewById(R.id.list);
    }
}
