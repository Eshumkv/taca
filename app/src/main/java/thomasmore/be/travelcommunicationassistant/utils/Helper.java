package thomasmore.be.travelcommunicationassistant.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.fragment.HomeFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesListFragment;
import thomasmore.be.travelcommunicationassistant.fragment.PersonalInfoFragment;

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
                    R.drawable.ic_crop_square_black_24dp,
                    HomeFragment.class),
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
}
