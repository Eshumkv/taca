package thomasmore.be.travelcommunicationassistant;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.NavigationAdapter;
import thomasmore.be.travelcommunicationassistant.fragment.BaseFragment;
import thomasmore.be.travelcommunicationassistant.fragment.BasicEditFragment;
import thomasmore.be.travelcommunicationassistant.fragment.HomeFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesConversationFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesListFragment;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessagesListViewModel;

public class BackActivity
        extends AppCompatActivity
        implements BaseFragment.OnFragmentInteractionListener{

    public final static String DATA_STRING = "BACKACTIVITY_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Helper.changeFragment(this, getCorrectFragment(bundle), true);
        }
    }

    @Override
    public void onBackPressed() {
        final BaseFragment fragment =
                (BaseFragment) getFragmentManager().findFragmentById(R.id.content_frame);

        // If the fragment doesn't handle back press, handle it yourself.
        if (!fragment.onBackPressed()) {
            super.onBackPressed();
        }
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
            fragment.setArguments(getBundleForFragment(classname, bundle));
        }

        return fragment;
    }

    private Bundle getBundleForFragment(String classname, Bundle oldBundle) {
        Bundle bundle = new Bundle();
        bundle.putString(BasicEditFragment.CLASSNAME, classname);

        if (classname.equals(Room.class.getName())) {
            Room room = oldBundle.getParcelable(classname);
            bundle.putParcelable(Room.class.getName(), room);
        }

        return bundle;
    }
}
