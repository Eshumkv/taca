package thomasmore.be.travelcommunicationassistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import thomasmore.be.travelcommunicationassistant.AvailableRoomFragment;
import thomasmore.be.travelcommunicationassistant.CreatedRoomFragment;

/**
 * Created by Eshum on 20/03/2017.
 */

public class FixedTabsPerAdapter extends FragmentPagerAdapter {

    public FixedTabsPerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CreatedRoomFragment();
            case 1:
            default:
                return new AvailableRoomFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Created";
            case 1:
            default:
                return "Available";
        }
    }
}
