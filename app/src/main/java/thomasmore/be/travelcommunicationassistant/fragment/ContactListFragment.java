package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.NavigationDrawerActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.ContactsListAdapter;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.ContactType;
import thomasmore.be.travelcommunicationassistant.model.User;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class ContactListFragment extends BasePagingFragment<Contact> {

    public final static String EXTRA_CONTACTS_FOR = "ContactsForASpecificPerson";
    public final static String EXTRA_SEARCH_SPECIFIC = "AllContactsOfASpecificType";

    private final static int REQUEST_SEARCH = 1;
    private final static int REQUEST_EDIT = 2;

    int tutorColor;

    private boolean isSearch = false;
    private boolean isContactsForPerson = false;
    private Contact contactsFor;

    private boolean searchSpecific = false;
    private ContactType searchType = ContactType.Tutor;

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

                if (bundle.containsKey(EXTRA_SEARCH_SPECIFIC)) {
                    searchSpecific = true;
                    searchType = ContactType.values()[bundle.getInt(EXTRA_SEARCH_SPECIFIC)];
                }
            }

            if (bundle.containsKey(EXTRA_CONTACTS_FOR)) {
                isContactsForPerson = true;
                contactsFor = bundle.getParcelable(EXTRA_CONTACTS_FOR);
            }
        }

        setupList(getCorrectList());
        setupPagingBar(rootView);

        View empty = rootView.findViewById(R.id.empty_text);
        if (pagingMap.get(currentPage).size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

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
                                Database db = Database.getInstance(getActivity());
                                Contact contact = (Contact) getList().getAdapter().getItem(selectedPosition);

                                if (isContactsForPerson) {
                                    if (db.removeContactFromContactListFor(contactsFor.getId(), contact.getId())) {
                                        Helper.toast(getActivity(), R.string.toast_deleted);
                                    } else {
                                        Helper.toast(getActivity(), R.string.toast_not_deleted);
                                    }
                                } else {
                                    if (db.deleteContact(contact.getId())) {
                                        Helper.toast(getActivity(), R.string.toast_deleted);
                                    } else {
                                        Helper.toast(getActivity(), R.string.toast_not_deleted);
                                    }
                                }

                                deselectPrevious(getView());
                                toggleContext();

                                setupList(getCorrectList());
                                setupPagingBar(getActivity().findViewById(android.R.id.content));
                                setListAdapter();

                                View empty = getActivity().findViewById(R.id.empty_text);
                                if (pagingMap.get(currentPage).size() == 0) {
                                    empty.setVisibility(View.VISIBLE);
                                } else {
                                    empty.setVisibility(View.GONE);
                                }
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
        if (searchSpecific) {
            inflater.inflate(R.menu.menu_simple_search, menu);
        } else {
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
                        Database db = Database.getInstance(getActivity());
                        List<Contact> tempList = null;

                        if (isContactsForPerson) {
                            switch (position) {
                                case 0: // Everyone
                                    tempList = db.getContactsFor(contactsFor.getId());
                                    break;
                                case 1: // Tutors
                                    tempList = db.getContactsForOfType(contactsFor.getId(), ContactType.Tutor);
                                    break;
                                case 2: // Warded
                                    tempList = db.getContactsForOfType(contactsFor.getId(), ContactType.Warded);
                                    break;
                            }
                        } else {
                            switch (position) {
                                case 0: // Everyone
                                    tempList = db.getAll(Contact.class);
                                    break;
                                case 1: // Tutors
                                    tempList = db.getContactsOfType(ContactType.Tutor);
                                    break;
                                case 2: // Warded
                                    tempList = db.getContactsOfType(ContactType.Warded);
                                    break;
                            }
                        }

                        setupList(tempList);
                        setupPagingBar(getActivity().findViewById(android.R.id.content));
                        setListAdapter();

                        View empty = getActivity().findViewById(R.id.empty_text);
                        if (pagingMap.get(currentPage).size() == 0) {
                            empty.setVisibility(View.VISIBLE);
                        } else {
                            empty.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
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
            Database db = Database.getInstance(getActivity());

            if (Helper.isEmpty(contact.getName())) {
                goToEditScreen(contact, false);
                Helper.toast(getActivity(), R.string.toast_error_name_empty);
                return;
            }

            if (Helper.isEmpty(contact.getPhonenumber())) {
                goToEditScreen(contact, false);
                Helper.toast(getActivity(), R.string.toast_error_phonenumber_empty);
                return;
            }

            if (db.contactIsUnique(contact)) {
                if (db.genericUpdate(Contact.class, contact)) {
                    Helper.toast(getActivity(), R.string.toast_saved);
                } else {
                    long id = db.genericInsert(Contact.class, contact);

                    if (id != -1) {
                        if (isContactsForPerson) {
                            long tid = db.addContactToContactListFor(contactsFor.getId(), contact);

                            if (tid != -1) {
                                Helper.toast(getActivity(), R.string.toast_saved);
                            } else {
                                Helper.toast(getActivity(), R.string.toast_not_saved);
                            }
                        } else {
                            Helper.toast(getActivity(), R.string.toast_saved);
                        }
                    } else {
                        Helper.toast(getActivity(), R.string.toast_not_saved);
                    }
                }
            } else {
                goToEditScreen(contact, false);
                Helper.toast(getActivity(), R.string.toast_error_contact_not_unique);
                return;
            }

            setupList(getCorrectList());
            setupPagingBar(getActivity().findViewById(android.R.id.content));
            setListAdapter();

            View empty = getActivity().findViewById(R.id.empty_text);
            if (pagingMap.get(currentPage).size() == 0) {
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.GONE);
            }
        } else if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            Bundle extra = data.getBundleExtra("extra");
            List<Contact> contacts = extra.getParcelableArrayList(Contact.class.getName());
            Database db = Database.getInstance(getActivity());

            boolean success = true;

            for (Contact contact : contacts) {
                success &= (db.addContactToContactListFor(contactsFor.getId(), contact) != -1);
            }

            if (success) {
                Helper.toast(getActivity(), R.string.toast_saved);
            } else {
                Helper.toast(getActivity(), R.string.toast_not_saved);
            }

            setupList(getCorrectList());
            setupPagingBar(getActivity().findViewById(android.R.id.content));
            setListAdapter();

            View empty = getActivity().findViewById(R.id.empty_text);
            if (pagingMap.get(currentPage).size() == 0) {
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.GONE);
            }
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

    private List<Contact> getCorrectList() {
        Database db = Database.getInstance(getActivity());
        List<Contact> tempList = null;

        if (isContactsForPerson) {
            Helper.setTitle(getActivity(), R.string.warded_contact_list_title, contactsFor.getName());

            if (searchSpecific) {
                tempList = db.getContactsForOfType(contactsFor.getId(), searchType);
            } else {
                tempList = db.getContactsFor(contactsFor.getId());
            }
        } else {
            if (searchSpecific) {
                tempList = db.getContactsOfType(searchType);
            } else {
                tempList = db.getAll(Contact.class);
            }
        }

        return tempList;
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

        if (contact.isUser()) {
            Fragment fragment = new BasicEditFragment();
            String classname = User.class.getName();
            Bundle bundle = new Bundle();

            Database db = Database.getInstance(getActivity());
            User user = db.getSettings().getLoggedInUser(getActivity());

            bundle.putString(BasicEditFragment.CLASSNAME, classname);
            bundle.putParcelable(classname, user);

            fragment.setArguments(bundle);
            Helper.changeFragment(getActivity(), fragment, false);
            return;
        }

        String className = Contact.class.getName();

        Intent intent = Helper.getBackActivityIntent(getActivity());
        intent.putExtra(BasicEditFragment.CLASSNAME, className);
        intent.putExtra(className, contact);

        if (isnew) {
            intent.putExtra(BasicEditFragment.EXTRA_ADD_INSTEAD_OF_SAVE, true);
        }

        startActivityForResult(intent, REQUEST_EDIT);
    }

    private void setupList(List<Contact> list) {
        setupPagingMap(list, Contact.class, "getName", new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getName().compareTo(rhs.getName());
            }
        });
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
