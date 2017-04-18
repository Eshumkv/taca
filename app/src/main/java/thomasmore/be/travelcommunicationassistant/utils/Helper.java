package thomasmore.be.travelcommunicationassistant.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.fragment.BasicEditFragment;
import thomasmore.be.travelcommunicationassistant.fragment.CreatedRoomsFragment;
import thomasmore.be.travelcommunicationassistant.fragment.HomeFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesListFragment;
import thomasmore.be.travelcommunicationassistant.fragment.PersonalInfoFragment;
import thomasmore.be.travelcommunicationassistant.model.Room;

public class Helper {
    public final static NavigationItems[] navigationItems = new NavigationItems[] {
            new NavigationItems(
                    R.string.nav_home,
                    R.drawable.ic_home_black_24dp,
                    HomeFragment.class),
            new NavigationItems(
                    R.string.nav_messages,
                    R.drawable.ic_message_black_24dp,
                    MessagesListFragment.class),
            new NavigationItems(
                    R.string.nav_rooms,
                    R.drawable.ic_group_black_24dp,
                    CreatedRoomsFragment.class),
            new NavigationItems(
                    R.string.nav_pictogram,
                    R.drawable.ic_stars_black_24dp,
                    HomeFragment.class),
            new NavigationItems(
                    R.string.nav_category,
                    R.drawable.ic_dashboard_black_24dp,
                    HomeFragment.class),
            new NavigationItems(
                    R.string.nav_warded,
                    R.drawable.ic_person_black_24dp,
                    HomeFragment.class),
            new NavigationItems(
                    R.string.nav_contacts,
                    R.drawable.ic_contacts_black_24dp,
                    HomeFragment.class),
            new NavigationItems(
                    R.string.nav_personal,
                    R.drawable.ic_info_black_24dp,
                    PersonalInfoFragment.class)
    };

    public static <T> T NewInstanceOf(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) { return null; }
    }

    public static void changeFragment(Activity act, Fragment fragment, boolean addBackToStack) {
        FragmentManager fragmentManager = act.getFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment, fragment.getClass().getName());

        if (addBackToStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public static void setTitle(Activity act, int resId) {
        ActionBar actionBar = ((AppCompatActivity)act).getSupportActionBar();
        actionBar.setTitle(resId);
    }

    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static int getTotalHeightofListView(ListView lv) {

        Adapter adapter = lv.getAdapter();
        int listviewElementsheight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View mView = adapter.getView(i, null, lv);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            listviewElementsheight += mView.getMeasuredHeight();
        }

        return listviewElementsheight;
    }

    public static Intent getBackActivityIntent(Context ctx) {
        Intent intent = new Intent(ctx, BackActivity.class);
        intent.putExtra(BackActivity.DATA_STRING, BasicEditFragment.class);
        return intent;
    }
}
