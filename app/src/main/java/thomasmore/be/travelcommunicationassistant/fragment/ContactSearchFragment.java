package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.ContactSearchAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.RoomsWithCreatorAdapter;
import thomasmore.be.travelcommunicationassistant.fragment.dialog.SimpleDialogFragment;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.ContactType;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class ContactSearchFragment
        extends BaseFragment
        implements SimpleDialogFragment.DialogListener {

    private boolean searchOnName = true;
    private HashMap<Long, Contact> map;
    private int counter = 0;

    public ContactSearchFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms_search, container, false);

        map = new HashMap<>();

        Helper.setTitle(getActivity(), R.string.nav_contact_search);

        final Spinner spinner = (Spinner) rootView.findViewById(R.id.search_by);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.searchterms_array_all,
                R.layout.item_spinner_white);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchOnName = position == 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Empty list to start with
        Contact[] rooms = new Contact[] {};

        final ListView list = (ListView) rootView.findViewById(R.id.rooms);
        final ContactSearchAdapter contactAdapter =
                new ContactSearchAdapter(getActivity(), Arrays.asList(rooms), R.layout.item_contact_search);
        list.setAdapter(contactAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout root = (LinearLayout)view;
                CheckBox checkbox = (CheckBox)root.findViewById(R.id.checked);
                checkbox.setChecked(!checkbox.isChecked());

                Contact room = (Contact)list.getAdapter().getItem(position);
                if (checkbox.isChecked()) {
                    map.put(room.getId(), room);
                } else {
                    map.remove(room.getId());
                }
            }
        });


        final EditText searchText = (EditText) rootView.findViewById(R.id.search);
        searchText.requestFocus();
        searchText.clearFocus();

        final Button searchButton = (Button) rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchText.getText().toString().trim();
                if (text.equals("")) return;
                List<Contact> contacts = search(text, searchOnName);
                list.setAdapter(new ContactSearchAdapter(getActivity(), contacts, R.layout.item_contact_search));
                Helper.hideKeyboard(getActivity());
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simple_add, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_save:
                Intent intent = new Intent();
                Bundle extra = new Bundle();

                ArrayList<Contact> contacts = new ArrayList<>();

                Iterator it = map.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    contacts.add((Contact)pair.getValue());
                }

                extra.putParcelableArrayList(Contact.class.getName(), contacts);
                intent.putExtra("extra", extra);

                getActivity().setResult(Activity.RESULT_OK, intent);
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

    private List<Contact> search(String searchText, boolean searchOnName) {
        ArrayList<Contact> list = new ArrayList<>();

        List<Contact> toSearch = getFakeServerContacts();

        for (Contact contact : toSearch) {
            if (searchOnName) {
                if (contact.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    list.add(contact);
                }
            } else {
                if (contact.getPhonenumber().toLowerCase().contains(searchText.toLowerCase())) {
                    list.add(contact);
                }
            }
        }


        return list;
    }

    private List<Contact> getFakeServerContacts() {
        List<Contact> list = new ArrayList<>();

        Contact contact = new Contact();
        contact.setId(1);
        contact.setName("Alice");
        contact.setPhonenumber("+79995236412");
        list.add(contact);

        contact = new Contact();
        contact.setId(2);
        contact.setName("Bob");
        contact.setPhonenumber("+7225352256");
        contact.setType(ContactType.Tutor);
        list.add(contact);

        contact = new Contact();
        contact.setId(3);
        contact.setName("Jan");
        contact.setPhonenumber("+7123456789");
        list.add(contact);

        contact = new Contact();
        contact.setId(4);
        contact.setName("Andrey");
        contact.setPhonenumber("+79995236412");
        list.add(contact);

        contact = new Contact();
        contact.setId(5);
        contact.setName("Donovan");
        contact.setPhonenumber("+7225352256");
        contact.setType(ContactType.Tutor);
        list.add(contact);

        contact = new Contact();
        contact.setId(6);
        contact.setName("Alexander");
        contact.setPhonenumber("+79995236412");
        list.add(contact);

        contact = new Contact();
        contact.setId(7);
        contact.setName("Koen");
        contact.setPhonenumber("+79995236412");
        list.add(contact);

        contact = new Contact();
        contact.setId(8);
        contact.setName("Ivan");
        contact.setPhonenumber("+7123456789");
        contact.setType(ContactType.Tutor);
        list.add(contact);

        contact = new Contact();
        contact.setId(9);
        contact.setName("Zoey");
        contact.setPhonenumber("+7123456789");
        list.add(contact);

        return list;
    }
}
