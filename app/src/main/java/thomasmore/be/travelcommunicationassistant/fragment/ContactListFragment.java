package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.NavigationDrawerActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.ContactsListAdapter;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.ContactType;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class ContactListFragment extends BasePagingFragment<Contact> {

    public final static String EXTRA_CONTACTS_FOR = "ContactsForASpecificPerson";

    private final static int REQUEST_SEARCH = 1;
    private final static int REQUEST_EDIT = 2;

    int tutorColor;

    private boolean isSearch = false;
    private boolean isContactsForPerson = false;
    private Contact contactsFor;

    public ContactListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);

        Helper.setTitle(getActivity(), R.string.nav_contacts);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Helper.EXTRA_SEARCH_INTENT)) {
                Class<?> cls = (Class<?>) bundle.get(Helper.EXTRA_SEARCH_INTENT);

                // Only if we're searching for a contact.
                // Otherwise, no idea what to do with it!
                if (Contact.class.equals(cls)) {
                    isSearch = true;
                }
            }

            if (bundle.containsKey(EXTRA_CONTACTS_FOR)) {
                isContactsForPerson = true;
                contactsFor = bundle.getParcelable(EXTRA_CONTACTS_FOR);
            }
        }

        if (isContactsForPerson) {
            Helper.setTitle(getActivity(), R.string.warded_contact_list_title, contactsFor.getName());
            // Get the contacts for this person!!
        }

        List<Contact> tempList = new ArrayList<>();
        tempList.add(new Contact("Alice", "+7225352256", false));
        tempList.add(new Contact("Bob", "+7225352256", false));
        tempList.add(new Contact("Jan", "+7225352256", false));
        tempList.add(new Contact("Andrey", "+7225352256", true));
        tempList.add(new Contact("Robin", "+7225352256", false));
        tempList.add(new Contact("Alexander", "+7225352256", false));
        tempList.add(new Contact("Koen", "+7225352256", false));
        tempList.add(new Contact("Ivan", "+7225352256", true));
        tempList.add(new Contact("Zoey", "+7225352256", false));
        tempList.add(new Contact("Alice", "+7225352256", false));
        tempList.add(new Contact("Dilbert", "+7225352256", true));
        tempList.add(new Contact("Donovan", "+7225352256", false));

        setupPagingMap(tempList, Contact.class, "getName", new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        setupPagingBar(rootView);

        final ListView list = (ListView) rootView.findViewById(R.id.contacts);
        list.setAdapter(new ContactsListAdapter(getActivity(), pagingMap.get(currentPage)));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSearch) {
                    Contact contact = (Contact) list.getAdapter().getItem(position);
                    Intent intent = new Intent();
                    String classname = Contact.class.getName();
                    intent.putExtra(classname, contact);

                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    onListItemClick(parent, view, position, id);
                }
            }
        });

        selectedColor = ContextCompat.getColor(getActivity(), R.color.cardSelected);
        normalColor = ContextCompat.getColor(getActivity(), R.color.cardNormal);
        tutorColor = ContextCompat.getColor(getActivity(), R.color.card_tutor);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContactsForPerson) {
                    deselectPrevious(getView());
                    toggleContext();

                    Intent intent = new Intent(getActivity(), BackActivity.class);
                    intent.putExtra(BackActivity.DATA_STRING, ContactSearchFragment.class);
                    startActivityForResult(intent, REQUEST_SEARCH);
                } else {
                    goToEditScreen(new Contact(), true);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                Contact contact = (Contact)list.getAdapter().getItem(selectedPosition);
                goToEditScreen(contact, false);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setMessage(R.string.dialog_delete_contact)
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_messages, menu);

        MenuItem groupSpinner = menu.findItem(R.id.menu_group_spinner);
        View view = MenuItemCompat.getActionView(groupSpinner);

        if (view instanceof Spinner) {
            final Spinner spinner = (Spinner) view;
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    getActivity(),
                    R.array.groups_array_all,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                if (isContactsForPerson) {
                    onBackPressed();
                    return true;
                }
                return false;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            Contact contact = data.getParcelableExtra(Contact.class.getName());

            Log.i("Info", contact.getType().name());
            // TODO: Save the contact
        } else if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            Bundle extra = data.getBundleExtra("extra");
            List<Contact> contacts = extra.getParcelableArrayList(Contact.class.getName());

            Log.i("Info", contacts.size() + "");
            // TODO: Save the contacts
        }
    }

    @Override
    public boolean onBackPressed() {
        Activity activity = getActivity();

        if (activity instanceof NavigationDrawerActivity) {
            Helper.changeFragment(activity, new HomeFragment(), false);
        } else {
            getActivity().finish();
        }

        return true;
    }

    private void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout root = (LinearLayout)view;
        CardView card = (CardView)root.findViewById(R.id.card_view);
        card.setCardBackgroundColor(selectedColor);

        // Deselect the previous card
        int temp = selectedPosition;
        deselectPrevious(getView());
        selectedPosition = temp == position ? -1 : position;

        toggleContext();
    }

    private void goToEditScreen(Contact contact, boolean isnew) {
        deselectPrevious(getView());
        toggleContext();

        String className = Contact.class.getName();

        Intent intent = Helper.getBackActivityIntent(getActivity());
        intent.putExtra(BasicEditFragment.CLASSNAME, className);
        intent.putExtra(className, contact);

        if (isnew) {
            intent.putExtra(BasicEditFragment.EXTRA_ADD_INSTEAD_OF_SAVE, true);
        }

        startActivityForResult(intent, REQUEST_EDIT);
    }

    /****
     *
     * PAGING
     *
     */

    @Override
    protected void setListAdapter() {
        final ListView list = getList();
        list.setAdapter(new ContactsListAdapter(getActivity(), pagingMap.get(currentPage)));
    }

    @Override
    protected ListView getList() {
        return (ListView) getActivity().findViewById(R.id.contacts);
    }
}
