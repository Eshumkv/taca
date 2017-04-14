package thomasmore.be.travelcommunicationassistant;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.adapter.NavigationAdapter;
import thomasmore.be.travelcommunicationassistant.fragments.HomeFragment;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class NavigationDrawerActivity
        extends AppCompatActivity
        implements HomeFragment.OnHomeItemSelectedListener {

    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    FrameLayout frame;

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

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        selectItem(0);

        handleIntent(getIntent());
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

    private void selectItem(Class<?> cls)  {
        int position = -1;
        for (int i = 0; i < Helper.navigationItems.length; i++) {
            if (Helper.navigationItems[i].getCls() == cls) {
                position = i;
                break;
            }
        }

        if (position != -1)  {
            selectItem(position);
        } else {
            throw new RuntimeException("Something went REALLY wrong.");
        }
    }

    private void selectItem(int position) {
        NavigationItems navItem = Helper.navigationItems[position];
        Fragment fragment = (Fragment) navItem.getInstance();
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

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
            selectItem(position);
        }
    }

    // Homescreen listener.
    public void onHomeItemSelected(Class<?> cls) {
        selectItem(cls);
    }
}
