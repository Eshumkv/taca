package thomasmore.be.travelcommunicationassistant.fragment;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;

import static android.app.Activity.RESULT_OK;

public class WardedPersonFragment extends BaseFragment {

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

        Button pictogramButton = (Button) rootView.findViewById(R.id.pictogram);
        Button roomButton = (Button) rootView.findViewById(R.id.room);
        Button contactButton = (Button) rootView.findViewById(R.id.contacts);
        Button qmessageButton = (Button) rootView.findViewById(R.id.quickmessages);
        Button infoButton = (Button) rootView.findViewById(R.id.info);

        roomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);

                intent.putExtra(BackActivity.DATA_STRING, RoomSettingsFragment.class);
                intent.putExtra(RoomSettingsFragment.CONTACT, contact);
                startActivityForResult(intent, 1);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = Contact.class.getName();

                Intent intent = Helper.getBackActivityIntent(getActivity());
                intent.putExtra(BasicEditFragment.CLASSNAME, className);
                intent.putExtra(className, contact);
                intent.putExtra(Helper.EXTRA_DATA, true);
                startActivityForResult(intent, 1);
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

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Contact contact = data.getParcelableExtra(Contact.class.getName());

                Log.i("Info", contact.getLanguage().toString() + "");
            }
        }
    }

    private void goBackToList() {
        Intent intent = new Intent(getActivity(), BackActivity.class);
        intent.putExtra(BackActivity.DATA_STRING, WardedPersonListFragment.class);
        startActivity(intent);
        getActivity().finish();
    }
}
