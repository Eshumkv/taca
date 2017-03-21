package thomasmore.be.travelcommunicationassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.adapter.CreatedRoomAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreatedRoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreatedRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatedRoomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROOMS = "rooms";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> rooms;

    private OnCreatedRoomFragmentListener mListener;

    public CreatedRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rooms Parameter 1.
     * @return A new instance of fragment CreatedRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatedRoomFragment newInstance(ArrayList<String> rooms) {
        CreatedRoomFragment fragment = new CreatedRoomFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROOMS, rooms);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rooms = (ArrayList<String>) getArguments().getSerializable(ARG_ROOMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_created_room, container, false);

        String[] rooms = new String[] {
                "123456", "Dialog"
        };

        final ListView list = (ListView)view.findViewById(R.id.created_rooms);
        final CreatedRoomAdapter adapter = new CreatedRoomAdapter(
                view.getContext(),
                Arrays.asList(rooms),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String text = v.getTag().toString();
                        if (mListener != null) {
                            mListener.onCreatedRoomFragmentInteraction(text);
                        }
                    }
                });
        list.setAdapter(adapter);
        list.setItemsCanFocus(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreatedRoomFragmentListener) {
            mListener = (OnCreatedRoomFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCreatedRoomFragmentListener {
        void onCreatedRoomFragmentInteraction(String name);
    }
}
