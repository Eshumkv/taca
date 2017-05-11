package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import thomasmore.be.travelcommunicationassistant.adapter.RoomsWithCreatorAdapter;
import thomasmore.be.travelcommunicationassistant.fragment.dialog.SimpleDialogFragment;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class RoomsSearchFragment
        extends BaseFragment
        implements SimpleDialogFragment.DialogListener {

    private boolean searchOnName = true;
    private HashMap<Long, Room> roomsMap;
    private int counter = 0;

    public RoomsSearchFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms_search, container, false);

        roomsMap = new HashMap<>();

        Helper.setTitle(getActivity(), R.string.nav_rooms_search);

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
        Room[] rooms = new Room[] {};

        final ListView list = (ListView) rootView.findViewById(R.id.rooms);
        final RoomsWithCreatorAdapter roomAdapter =
                new RoomsWithCreatorAdapter(getActivity(), Arrays.asList(rooms), R.layout.item_rooms_search);
        list.setAdapter(roomAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout root = (LinearLayout)view;
                CheckBox checkbox = (CheckBox)root.findViewById(R.id.checked);
                checkbox.setChecked(!checkbox.isChecked());

                Room room = (Room)list.getAdapter().getItem(position);
                if (checkbox.isChecked()) {
                    roomsMap.put(room.getId(), room);
                } else {
                    roomsMap.remove(room.getId());
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
                List<Room> rooms = search(text, searchOnName);
                list.setAdapter(new RoomsWithCreatorAdapter(getActivity(), rooms, R.layout.item_rooms_search));
                Helper.hideKeyboard(getActivity());
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
                askAllPasswords();
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

    private List<Room> search(String searchText, boolean searchOnName) {
        ArrayList<Room> list = new ArrayList<>();
        List<Room> toSearch = getFakeServerRooms();

        for (Room room : toSearch) {
            if (searchOnName) {
                if (room.getCreator().toLowerCase().contains(searchText.toLowerCase())) {
                    list.add(room);
                }
            } else {
                if (room.getCreaterPhonenumber().toLowerCase().contains(searchText.toLowerCase())) {
                    list.add(room);
                }
            }
        }


        return list;
    }

    private void returnToPrevious() {
        Intent intent = new Intent();
        Bundle extra = new Bundle();

        extra.putParcelableArrayList(Room.class.getName(), getRooms());
        intent.putExtra("extra", extra);

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private ArrayList<Room> getRooms() {
        ArrayList<Room> list = new ArrayList<>(roomsMap.size());

        Iterator it = roomsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            list.add((Room)pair.getValue());
        }

        return list;
    }

    private boolean askAllPasswords() {
        int i = 0;
        Iterator it = roomsMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            if (i == counter) {
                Room room = (Room) pair.getValue();

                SimpleDialogFragment dialog =
                        SimpleDialogFragment.newInstance(room, R.layout.dialog_room_password);
                dialog.show(getFragmentManager(), "SomeTag");
                return false;
            }

            i++;
        }

        return true;
    }

    @Override
    public void onDialogPositiveClick(boolean correct) {
        if (correct) {
            counter++;
            if (askAllPasswords()) {
                returnToPrevious();
            }
        } else {
            counter = 0;
            Toast.makeText(getActivity(), R.string.warning_wrong_password, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        counter = 0;
    }

    private List<Room> getFakeServerRooms() {
        ArrayList<Room> rooms = new ArrayList<>();

        Room room = new Room();
        room.setId(2);
        room.setName("Gorki");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(true);
        room.setCreator("Mia");
        room.setCreaterPhonenumber("123456789");
        rooms.add(room);

        room = new Room();
        room.setId(3);
        room.setName("Table room");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(true);
        room.setCreator("John");
        room.setCreaterPhonenumber("99999999");
        rooms.add(room);

        room = new Room();
        room.setId(4);
        room.setName("Have fun");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(true);
        room.setCreator("Elena");
        room.setCreaterPhonenumber("123456789");
        rooms.add(room);

        room = new Room();
        room.setId(5);
        room.setName("Family Room!");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(true);
        room.setCreator("Alexander");
        room.setCreaterPhonenumber("123456789");
        rooms.add(room);

        room = new Room();
        room.setId(6);
        room.setName("Room for you");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(true);
        room.setCreator("Dmitry");
        room.setCreaterPhonenumber("123456789");
        rooms.add(room);

        room = new Room();
        room.setId(7);
        room.setName("Gorki 2");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(true);
        room.setCreator("Mia");
        room.setCreaterPhonenumber("123456789");
        rooms.add(room);

        return rooms;
    }
}
