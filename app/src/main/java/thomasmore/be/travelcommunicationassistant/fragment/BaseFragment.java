package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;

public abstract class BaseFragment extends Fragment {

    OnFragmentInteractionListener callback;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Class<?> cls, Object value);
    }

    public BaseFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure the container activity has implemented the callback.
        // If not, throw an exception.
        try {
            callback = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Used to indicate this fragment has it's own menu
        setHasOptionsMenu(true);
    }

    @Override
    public abstract View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState);

    public boolean onBackPressed() {
        return false;
    }
}
