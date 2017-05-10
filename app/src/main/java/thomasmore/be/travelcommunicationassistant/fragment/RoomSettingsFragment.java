package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;

import static android.app.Activity.RESULT_OK;

public class RoomSettingsFragment extends BaseFragment {

    public final static String CONTACT = "ExtraContactData";
    private final static int REQUEST_SEARCHBUTTON = 101;

    private Spinner currentRoomSpinner;
    private EditText tutorEdit;

    private Contact contact;

    public RoomSettingsFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_room_settings, container, false);

        Bundle bundle = getArguments();
        contact = bundle.getParcelable(CONTACT);

        // TODO: REMOVE THIS
        if (contact.getResponsibleTutor() == null) {
            contact.setResponsibleTutor(new Contact("Ivan", "456167894", true));
        }

        Helper.setTitle(getActivity(), R.string.warded_room_settings_title, contact.getName());

        LinearLayout editText = (LinearLayout) rootView.findViewById(R.id.edit_text);
        LinearLayout editSearch = (LinearLayout) rootView.findViewById(R.id.edit_search);

        TextView currentRoomLabel = (TextView) editText.findViewById(R.id.label);
        currentRoomSpinner = (Spinner) editText.findViewById(R.id.spinner);

        TextView tutorLabel = (TextView) editSearch.findViewById(R.id.label);
        tutorEdit = (EditText) editSearch.findViewById(R.id.text);
        ImageButton searchButton = (ImageButton) editSearch.findViewById(R.id.search);

        // ------- Current room
        Room[] rooms = new Room[] {
        };

        currentRoomLabel.setText(R.string.current_room);

        ArrayList<Room> list = new ArrayList<>(Arrays.asList(rooms));
        ArrayAdapter<Room> adapter  = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currentRoomSpinner.setAdapter(adapter);
        currentRoomSpinner.setSelection(0);

        // --------- Responsible tutor
        tutorLabel.setText(R.string.responsible_tutor);
        tutorEdit.setText(contact.getResponsibleTutor().getName());
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);
                intent.putExtra(BackActivity.DATA_STRING, ContactListFragment.class);
                intent.putExtra(Helper.EXTRA_SEARCH_INTENT, Contact.class);
                startActivityForResult(intent, REQUEST_SEARCHBUTTON);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_save:
                Intent intent = new Intent();
                getActivity().setResult(RESULT_OK, intent);
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
        if (requestCode == REQUEST_SEARCHBUTTON && resultCode == RESULT_OK) {
            Contact responsible_tutor = data.getParcelableExtra(Contact.class.getName());

            contact.setResponsibleTutor(responsible_tutor);
            tutorEdit.setText(responsible_tutor.getName());
        }
    }
}
