package thomasmore.be.travelcommunicationassistant.utils;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.fragments.HomeFragment;
import thomasmore.be.travelcommunicationassistant.fragments.PersonalInfoFragment;

public class Helper {
    public final static NavigationItems[] navigationItems = new NavigationItems[] {
            new NavigationItems(R.string.nav_home, R.drawable.ic_home_black_24dp, HomeFragment.class),
            new NavigationItems(R.string.nav_messages, R.drawable.ic_message_black_24dp, PersonalInfoFragment.class),
            new NavigationItems(R.string.nav_rooms, R.drawable.ic_crop_square_black_24dp, HomeFragment.class),
            new NavigationItems(R.string.nav_pictogram, R.drawable.ic_stars_black_24dp, HomeFragment.class),
            new NavigationItems(R.string.nav_category, R.drawable.ic_dashboard_black_24dp, HomeFragment.class),
            new NavigationItems(R.string.nav_warded, R.drawable.ic_person_black_24dp, HomeFragment.class),
            new NavigationItems(R.string.nav_contacts, R.drawable.ic_contacts_black_24dp, HomeFragment.class),
            new NavigationItems(R.string.nav_personal, R.drawable.ic_info_black_24dp, HomeFragment.class)
    };
}
