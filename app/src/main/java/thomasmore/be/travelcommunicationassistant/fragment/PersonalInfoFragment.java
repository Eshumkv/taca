package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thomasmore.be.travelcommunicationassistant.R;

public class PersonalInfoFragment extends BaseFragment {
    public PersonalInfoFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_info, container, false);
        return rootView;
    }
}
