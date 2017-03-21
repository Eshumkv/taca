package thomasmore.be.travelcommunicationassistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import thomasmore.be.travelcommunicationassistant.AvailableRoomFragment;
import thomasmore.be.travelcommunicationassistant.CreatedRoomFragment;
import thomasmore.be.travelcommunicationassistant.fragments.CategoryFragment;
import thomasmore.be.travelcommunicationassistant.fragments.PictogramFragment;
import thomasmore.be.travelcommunicationassistant.fragments.StartFragment;

/**
 * Created by Eshum on 20/03/2017.
 */

public class TreeviewTabsPager extends FragmentPagerAdapter {

    public TreeviewTabsPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StartFragment();
            case 1:
                return new CategoryFragment();
            default:
                return new PictogramFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Start";
            case 1:
                return "Category";
            default:
                return "Pictogram";
        }
    }
}
