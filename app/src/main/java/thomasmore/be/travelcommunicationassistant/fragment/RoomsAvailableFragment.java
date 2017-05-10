package thomasmore.be.travelcommunicationassistant.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.RoomsWithCreatorAdapter;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class RoomsAvailableFragment extends BaseFragment {

    int selectedColor;
    int normalColor;
    int selectedPosition = -1;

    Button addButton;
    Button deleteButton;

    public RoomsAvailableFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_rooms_available, container, false);

        Helper.setTitle(getActivity(), R.string.nav_rooms_available);

        Button created = (Button) rootView.findViewById(R.id.tabs_created);
        created.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onFragmentInteraction(RoomsAvailableFragment.class, "Change");
            }
        });

        selectedColor = ContextCompat.getColor(getActivity(), R.color.cardSelected);
        normalColor = ContextCompat.getColor(getActivity(), R.color.cardNormal);

        Database db = Database.getInstance(getActivity());
        long id = db.getSettings().getUserId();
        List<Room> rooms = db.getAllRooms(id, true);

        final ListView list = (ListView) rootView.findViewById(R.id.rooms);
        list.setAdapter(new RoomsWithCreatorAdapter(getActivity(), rooms, R.layout.item_rooms_available));
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

        addButton = (Button) rootView.findViewById(R.id.c_add);
        deleteButton = (Button) rootView.findViewById(R.id.c_delete);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchScreen();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setMessage(R.string.dialog_delete_room)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Room room = (Room)list.getAdapter().getItem(selectedPosition);

                                Database db = Database.getInstance(getActivity());
                                if (db.genericDelete(Room.class, room.getId())) {
                                    Helper.toast(getActivity(), R.string.toast_deleted);
                                } else {
                                    Helper.toast(getActivity(), R.string.toast_not_deleted);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getBundleExtra("extra");
                List<Room> rooms = extra.getParcelableArrayList(Room.class.getName());
                Database db = Database.getInstance(getActivity());
                long userId = db.getSettings().getLoggedInUser(getActivity()).getId();

                for (Room room : rooms) {
                    room.setAvailableRoom(true);
                    room.setUserId(userId);
                    db.genericInsert(Room.class, room);
                }

                Helper.toast(getActivity(), R.string.toast_saved);

                rooms = db.getAllRooms(userId, true);

                final ListView list = (ListView) getActivity().findViewById(R.id.rooms);
                list.setAdapter(new RoomsWithCreatorAdapter(getActivity(), rooms, R.layout.item_rooms_available));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simple_back, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouchEvent(View v, MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                deselectPrevious(getView());
                toggleContext();
                break;
            default:
                return false;
        }
        return true;
    }

    protected void setListAdapter() {
        Database db = Database.getInstance(getActivity());
        long id = db.getSettings().getUserId();
        List<Room> rooms = db.getAllRooms(id, true);
        getList().setAdapter(new RoomsWithCreatorAdapter(getActivity(), rooms, R.layout.item_rooms_available));
    }

    protected ListView getList() {
        return (ListView) getActivity().findViewById(R.id.rooms);
    }

    private void deselectPrevious(View v) {
        if (selectedPosition != -1) {
            ListView list = (ListView) v.findViewById(R.id.rooms);
            LinearLayout prevRoot =
                    (LinearLayout) Helper.getViewByPosition(selectedPosition, list);
            CardView card = (CardView) prevRoot.findViewById(R.id.card_view);
            card.setCardBackgroundColor(normalColor);
        }

        selectedPosition = -1;
    }

    private void toggleContext() {
        if (selectedPosition != -1) {
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void goToSearchScreen() {
        deselectPrevious(getView());
        toggleContext();

        Intent intent = new Intent(getActivity(), BackActivity.class);
        intent.putExtra(BackActivity.DATA_STRING, RoomsSearchFragment.class);
        startActivityForResult(intent, 1);
    }
}
