package thomasmore.be.travelcommunicationassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class WardedPersonFragment extends BaseFragment {

    public final static String EXTRA_PICTOGRAM_SETTINGS = "PictogramSettingsNeedsThis";

    private final static int REQUEST_EDIT_INFO = 1;
    private final static int REQUEST_ROOMSETTINGS = 2;
    private final static int REQUEST_CONTACTLIST = 3;
    private final static int REQUEST_QUICKMESSAGE = 4;
    private final static int REQUEST_PICTOGRAMSETTINGS = 5;

    public WardedPersonFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_warded_person, container, false);

        Bundle bundle = getArguments();
        final Contact contact = bundle.getParcelable(Contact.class.getName());

        Button listButton = (Button) rootView.findViewById(R.id.c_list);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToList();
            }
        });

        ImageView image = (ImageView) rootView.findViewById(R.id.image);
        TextView name = (TextView) rootView.findViewById(R.id.name);

        name.setText(contact.getName());
        image.setImageBitmap(Helper.getImage(getActivity(), contact.getImagePath()));

        final LinearLayout pictogramButton = (LinearLayout) rootView.findViewById(R.id.pictogram);
        final LinearLayout roomButton = (LinearLayout) rootView.findViewById(R.id.room);
        final LinearLayout contactsButton = (LinearLayout) rootView.findViewById(R.id.contacts);
        final LinearLayout qmessageButton = (LinearLayout) rootView.findViewById(R.id.qmessage);
        final LinearLayout infoButton = (LinearLayout) rootView.findViewById(R.id.info);

        setDetailsOfButton(pictogramButton, R.string.warded_person_pictogram, R.drawable.ic_stars_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);

                intent.putExtra(BackActivity.DATA_STRING, MajorCategoryListFragment.class);

                intent.putExtra(EXTRA_PICTOGRAM_SETTINGS, contact);
                startActivityForResult(intent, REQUEST_PICTOGRAMSETTINGS);
            }
        });

        setDetailsOfButton(roomButton, R.string.warded_person_room, R.drawable.ic_group_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);

                intent.putExtra(BackActivity.DATA_STRING, RoomSettingsFragment.class);
                intent.putExtra(RoomSettingsFragment.CONTACT, contact);
                startActivityForResult(intent, REQUEST_ROOMSETTINGS);
            }
        });

        setDetailsOfButton(contactsButton, R.string.warded_person_contacts, R.drawable.ic_contacts_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);

                intent.putExtra(BackActivity.DATA_STRING, ContactListFragment.class);
                intent.putExtra(ContactListFragment.EXTRA_CONTACTS_FOR, contact);
                startActivityForResult(intent, REQUEST_CONTACTLIST);
            }
        });

        setDetailsOfButton(qmessageButton, R.string.warded_person_qmessage, R.drawable.ic_textsms_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);

                intent.putExtra(BackActivity.DATA_STRING, QuickMessageListFragment.class);
                intent.putExtra(Contact.class.getName(), contact);
                startActivityForResult(intent, REQUEST_QUICKMESSAGE);
            }
        });

        setDetailsOfButton(infoButton, R.string.warded_person_info, R.drawable.ic_info_black_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = Contact.class.getName();

                Intent intent = Helper.getBackActivityIntent(getActivity());
                intent.putExtra(BasicEditFragment.CLASSNAME, className);
                intent.putExtra(className, contact);
                intent.putExtra(Helper.EXTRA_DATA, true);
                startActivityForResult(intent, REQUEST_EDIT_INFO);
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

        if (requestCode == REQUEST_EDIT_INFO && resultCode == RESULT_OK) {
            Contact contact = data.getParcelableExtra(Contact.class.getName());

            Helper.toast(getActivity(), R.string.toast_warded_info_saved);
        } else if (requestCode == REQUEST_ROOMSETTINGS && resultCode == RESULT_OK) {
            Helper.toast(getActivity(), R.string.toast_room_settings_saved);
        }
    }

    private void goBackToList() {
        Intent intent = new Intent(getActivity(), BackActivity.class);
        intent.putExtra(BackActivity.DATA_STRING, WardedPersonListFragment.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void setDetailsOfButton(View v, int text, int image, View.OnClickListener cl) {
        final TextView textView = (TextView) v.findViewById(R.id.text);
        final ImageView imageView = (ImageView) v.findViewById(R.id.image);

        int width = Helper.dp(getActivity(), 95);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        v.setOnClickListener(cl);
        v.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        textView.setText(text);
        textView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        imageView.setImageResource(image);
    }
}
