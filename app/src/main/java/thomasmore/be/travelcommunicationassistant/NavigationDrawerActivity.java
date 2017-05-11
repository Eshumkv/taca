package thomasmore.be.travelcommunicationassistant;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.Serializable;
import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.NavigationAdapter;
import thomasmore.be.travelcommunicationassistant.fragment.BasicEditFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MajorCategoryListFragment;
import thomasmore.be.travelcommunicationassistant.fragment.RoomsAvailableFragment;
import thomasmore.be.travelcommunicationassistant.fragment.BaseFragment;
import thomasmore.be.travelcommunicationassistant.fragment.RoomsCreatedFragment;
import thomasmore.be.travelcommunicationassistant.fragment.HomeFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesConversationFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesListFragment;
import thomasmore.be.travelcommunicationassistant.model.Language;
import thomasmore.be.travelcommunicationassistant.model.Settings;
import thomasmore.be.travelcommunicationassistant.model.User;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.viewmodel.MessagesListViewModel;

public class NavigationDrawerActivity
        extends AppCompatActivity
        implements BaseFragment.OnFragmentInteractionListener,
        View.OnTouchListener {

    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    FrameLayout frame;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navigation_list);
        frame = (FrameLayout) findViewById(R.id.content_frame);

        mDrawerList.setAdapter(new NavigationAdapter(this, Arrays.asList(Helper.navigationItems)));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = mDrawerList.getWidth() * slideOffset;

                frame.setTranslationX(moveFactor);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        Intent intent = getIntent();

        if (intent.hasExtra(Helper.EXTRA_DATA)) {
            Class<?> cls = (Class<?>)intent.getSerializableExtra(Helper.EXTRA_DATA);
            Fragment fragment = (Fragment)Helper.NewInstanceOf(cls);

            if (intent.hasExtra(Helper.EXTRA_DATA_BUNDLE)) {
                Bundle bundle = intent.getBundleExtra(Helper.EXTRA_DATA_BUNDLE);
                fragment.setArguments(bundle);
            }

            Helper.changeFragment(this, fragment, false);
        } else {
            selectNavigationItem(0);
        }

        handleIntent(intent);

        frame.setOnTouchListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.i("SEARCH", query);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the drawer
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        final BaseFragment fragment =
                (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        // If the fragment doesn't handle back press, handle it yourself.
        if (!fragment.onBackPressed()) {
            // If it's not the home fragment, go back to the home fragment.
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if ( !(current instanceof HomeFragment) ) {
                selectNavigationItem(getPositionForNavigationItem(HomeFragment.class));
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onTouch(View v, MotionEvent event) {
        final BaseFragment fragment =
                (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        return fragment.onTouchEvent(v, event);
    }

    private int getPositionForNavigationItem(NavigationItems item)  {
        int position = -1;
        for (int i = 0; i < Helper.navigationItems.length; i++) {
            NavigationItems ti = Helper.navigationItems[i];
            if (ti.getCls() == item.getCls() && ti.getTitleId() == item.getTitleId()) {
                position = i;
                break;
            }
        }

        return position;
    }

    private int getPositionForNavigationItem(Class<?> cls)  {
        int position = -1;
        for (int i = 0; i < Helper.navigationItems.length; i++) {
            NavigationItems ti = Helper.navigationItems[i];
            if (ti.getCls() == cls) {
                position = i;
                break;
            }
        }

        return position;
    }

    private void selectNavigationItem(int position) {
        selectNavigationItem(Helper.navigationItems[position]);
    }

    private void selectNavigationItem(NavigationItems navItem) {
        Fragment fragment = (Fragment) navItem.getInstance();

        if (navItem.getCls().equals(BasicEditFragment.class)) {
            String classname = User.class.getName();
            Bundle bundle = new Bundle();

            Database db = Database.getInstance(this);
            User user = db.getSettings().getLoggedInUser(this);

            bundle.putString(BasicEditFragment.CLASSNAME, classname);
            bundle.putParcelable(classname, user);

            fragment.setArguments(bundle);
        }

        if (navItem.isBackActivity()) {
            Intent intent = new Intent(this, BackActivity.class);
            intent.putExtra(BackActivity.DATA_STRING, navItem.getCls());

            if (navItem.getCls().equals(MajorCategoryListFragment.class)) {
                intent.putExtra(Helper.EXTRA_DATA, navItem.getTitleId() == R.string.nav_category);
            }

            startActivity(intent);
        } else {
            Helper.changeFragment(this, fragment, false);
            getSupportActionBar().setTitle(navItem.getTitleId());
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(getPositionForNavigationItem(navItem), true);
        View view = findViewById(R.id.navigation_layout);
        mDrawerLayout.closeDrawer(view);
    }

    // The click listener for ListView in the navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectNavigationItem(position);
        }
    }

    // Fragment listener
    public void onFragmentInteraction(Class<?> cls, Object value) {

        if (cls.equals(HomeFragment.class)) {
            NavigationItems item = (NavigationItems) value;
            selectNavigationItem(item);
        } else if (cls.equals(MessagesListFragment.class)) {
            MessagesListViewModel model = (MessagesListViewModel) value;

            Intent intent = new Intent(this, BackActivity.class);
            intent.putExtra(BackActivity.DATA_STRING, MessagesConversationFragment.class);
            startActivity(intent);
        } else if (cls.equals(RoomsCreatedFragment.class)) {
            if (value instanceof String) {
                Helper.changeFragment(this, new RoomsAvailableFragment(), false);
            }
        } else if (cls.equals(RoomsAvailableFragment.class)) {
            if (value instanceof String) {
                Helper.changeFragment(this, new RoomsCreatedFragment(), false);
            }
        } else if (cls.equals(BasicEditFragment.class)) {
            // We want to change the user.
            if (value instanceof User) {
                User user = (User) value;

                try {
                    Database db = Database.getInstance(this);
                    db.genericUpdate(User.class, user);

                    Helper.toast(this, R.string.toast_saved);
                } catch (Exception e) {
                    Helper.toast(this, R.string.toast_not_saved);
                }

                Helper.hideKeyboard(this);
                Helper.changeFragment(this, new HomeFragment(), false);
            }
        }
    }
}
