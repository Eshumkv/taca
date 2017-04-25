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

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.RoomsCreatedAdapter;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class RoomsCreatedFragment extends BaseFragment {

    int selectedColor;
    int normalColor;
    int selectedPosition = -1;

    Button addButton;
    Button editButton;
    Button deleteButton;

    public RoomsCreatedFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_rooms_created, container, false);

        Helper.setTitle(getActivity(), R.string.nav_rooms_created);

        Button available = (Button) rootView.findViewById(R.id.tabs_available);
        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onFragmentInteraction(RoomsCreatedFragment.class, "Change");
            }
        });

        selectedColor = ContextCompat.getColor(getActivity(), R.color.cardSelected);
        normalColor = ContextCompat.getColor(getActivity(), R.color.cardNormal);

        Room[] rooms = new Room[] {
                new Room("Room 1", "GoodPassword", null),
                new Room("Some Room", "IHaveAGoodPassword", null),
                new Room("Ivan's Room", "BESTIES", null)
        };

        final ListView list = (ListView) rootView.findViewById(R.id.rooms);
        list.setAdapter(new RoomsCreatedAdapter(getActivity(), Arrays.asList(rooms)));
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
        editButton = (Button) rootView.findViewById(R.id.c_edit);
        deleteButton = (Button) rootView.findViewById(R.id.c_delete);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditScreen(new Room());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                Room room = (Room)list.getAdapter().getItem(selectedPosition);
                goToEditScreen(room);
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
                                deselectPrevious(getView());
                                toggleContext();
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
                Room room = data.getParcelableExtra(Room.class.getName());

                Log.i("Info", room.getName());
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
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void goToEditScreen(Room room) {
        deselectPrevious(getView());
        toggleContext();

        String className = Room.class.getName();

        Intent intent = Helper.getBackActivityIntent(getActivity());
        intent.putExtra(BasicEditFragment.CLASSNAME, className);
        intent.putExtra(className, room);
        startActivityForResult(intent, 1);
    }
}
