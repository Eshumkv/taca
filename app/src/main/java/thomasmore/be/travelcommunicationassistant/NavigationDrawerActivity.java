package thomasmore.be.travelcommunicationassistant;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
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

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.NavigationAdapter;
import thomasmore.be.travelcommunicationassistant.fragment.RoomsAvailableFragment;
import thomasmore.be.travelcommunicationassistant.fragment.BaseFragment;
import thomasmore.be.travelcommunicationassistant.fragment.RoomsCreatedFragment;
import thomasmore.be.travelcommunicationassistant.fragment.HomeFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesConversationFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesListFragment;
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
        selectNavigationItem(0);

        handleIntent(getIntent());

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
                (BaseFragment) getFragmentManager().findFragmentById(R.id.content_frame);

        // If the fragment doesn't handle back press, handle it yourself.
        if (!fragment.onBackPressed()) {
            // If it's not the home fragment, go back to the home fragment.
            Fragment current = getFragmentManager().findFragmentById(R.id.content_frame);
            if ( !(current instanceof HomeFragment) ) {
                selectNavigationItem(HomeFragment.class);
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
                (BaseFragment) getFragmentManager().findFragmentById(R.id.content_frame);

        return fragment.onTouchEvent(v, event);
    }

    private void selectNavigationItem(Class<?> cls)  {
        int position = -1;
        for (int i = 0; i < Helper.navigationItems.length; i++) {
            if (Helper.navigationItems[i].getCls() == cls) {
                position = i;
                break;
            }
        }

        if (position != -1)  {
            selectNavigationItem(position);
        } else {
            throw new RuntimeException("Something went REALLY wrong.");
        }
    }

    private void selectNavigationItem(int position) {
        NavigationItems navItem = Helper.navigationItems[position];
        Helper.changeFragment(this, (Fragment) navItem.getInstance(), false);

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        getSupportActionBar().setTitle(navItem.getTitleId());
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
            Class<?> fragmentClass = (Class<?>) value;
            selectNavigationItem(fragmentClass);
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
        }
    }
}
