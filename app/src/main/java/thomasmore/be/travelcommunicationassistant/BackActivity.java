package thomasmore.be.travelcommunicationassistant;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import thomasmore.be.travelcommunicationassistant.fragment.BaseFragment;
import thomasmore.be.travelcommunicationassistant.fragment.BasicEditFragment;
import thomasmore.be.travelcommunicationassistant.fragment.ContactListFragment;
import thomasmore.be.travelcommunicationassistant.fragment.HomeFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MajorCategoryListFragment;
import thomasmore.be.travelcommunicationassistant.fragment.RoomSettingsFragment;
import thomasmore.be.travelcommunicationassistant.fragment.dialog.SimpleDialogFragment;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class BackActivity
        extends AppCompatActivity
        implements BaseFragment.OnFragmentInteractionListener,
        SimpleDialogFragment.DialogListener {

    public final static String DATA_STRING = "BACKACTIVITY_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (!handleSearch(getIntent())) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Helper.changeFragment(this, getCorrectFragment(bundle), true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        final BaseFragment fragment =
                (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        // If the fragment doesn't handle back press, handle it yourself.
        if (!fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleSearch(getIntent());
    }

    private boolean handleSearch(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Bundle bundle = Helper.getApp(this).getSearch_extra();

            // Search on pictogram
            if (bundle.getString(MyApp.SEARCH_TERM).equals(MyApp.SEARCH_PICTOGRAM)) {
                String query = "PICTOGRAM:" + intent.getStringExtra(SearchManager.QUERY);
                //use the query to search your data somehow
                Log.i("SEARCH", query);
                Helper.changeFragment(this, new MajorCategoryListFragment(), true);
            }
            return true;
        }

        return false;
    }

    // Fragment listener
    public void onFragmentInteraction(Class<?> cls, Object value) {

        if (cls.equals(HomeFragment.class)) {
        }
    }

    private Fragment getCorrectFragment(Bundle bundle) {
        Class<?> cls = (Class<?>) bundle.get(DATA_STRING);

        Fragment fragment = (Fragment) Helper.NewInstanceOf(cls);

        if (cls.equals(BasicEditFragment.class)) {
            String classname = bundle.getString(BasicEditFragment.CLASSNAME);
            Bundle newBundle = getBundleForBasicEdit(classname, bundle);

            if (bundle.containsKey(Helper.EXTRA_DATA)) {
                newBundle.putBoolean(Helper.EXTRA_DATA, bundle.getBoolean(Helper.EXTRA_DATA));
            }

            fragment.setArguments(newBundle);
        } else {
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    private Bundle getBundleForBasicEdit(String classname, Bundle oldBundle) {
        Bundle bundle = new Bundle();

        bundle.putString(BasicEditFragment.CLASSNAME, classname);

        if (classname.equals(Room.class.getName())) {
            Room room = oldBundle.getParcelable(classname);
            bundle.putParcelable(Room.class.getName(), room);
        } else if (classname.equals(Contact.class.getName())) {
            Contact contact = oldBundle.getParcelable(classname);
            bundle.putParcelable(Contact.class.getName(), contact);
        } else if (classname.equals(Category.class.getName())) {
            Category category = oldBundle.getParcelable(classname);
            bundle.putParcelable(Category.class.getName(), category);
        } else if (classname.equals(Pictogram.class.getName())) {
            Pictogram pictogram = oldBundle.getParcelable(classname);
            bundle.putParcelable(Pictogram.class.getName(), pictogram);
        }

        return bundle;
    }


    @Override
    public void onDialogPositiveClick(boolean correct) {
        final BaseFragment fragment =
                (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        fragment.onDialogPositiveClick(correct);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        final BaseFragment fragment =
                (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        fragment.onDialogNegativeClick(dialog);
    }
}
