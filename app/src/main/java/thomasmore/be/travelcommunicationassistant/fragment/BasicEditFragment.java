package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class BasicEditFragment extends BaseFragment {

    public final static String CLASSNAME = "ClassNameForBasicEdit";

    private String classname;
    private HashMap<String, View> dynamicViews = new HashMap<>();

    public BasicEditFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basic_edit, container, false);

        Bundle bundle = getArguments();
        classname = bundle.getString(CLASSNAME);
        setupLayout(bundle, rootView, inflater);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simple_save, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_save:
                getActivity().setResult(Activity.RESULT_OK, getIntent());
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }

    private void setupLayout(Bundle bundle, View root, LayoutInflater inflater) {
        LinearLayout layout = (LinearLayout) root.findViewById(R.id.content);

        if (classname.equals(Room.class.getName())) {
            Room room = bundle.getParcelable(classname);

            layout.addView(getHidden("Id", room.getId()));
            layout.addView(getEditText("Name", room.getName(), inflater, layout));
            layout.addView(getEditText("Password", room.getPassword(), inflater, layout));

            Helper.setTitle(getActivity(), R.string.nav_room_single);
        }
    }

    private View getEditText(String label, String defaultText, LayoutInflater inflater, ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_edit_text, parent, false);
        TextView labelText = (TextView) v.findViewById(R.id.label);
        EditText editText = (EditText) v.findViewById(R.id.text);

        v.setTag(label);
        labelText.setText(label);
        editText.setText(defaultText);

        dynamicViews.put(label, v);

        return v;
    }

    private View getHidden(String label, Object value) {
        View v = new View(getActivity());
        v.setTag(value);
        v.setVisibility(View.GONE);

        dynamicViews.put(label, v);

        return v;
    }

    private Intent getIntent() {
        Intent intent = new Intent();
        intent.putExtra(CLASSNAME, classname);

        if (classname.equals(Room.class.getName())) {
            intent.putExtra(classname, getRoomData());
        }

        return intent;
    }

    private Room getRoomData() {
        Room room = new Room();

        room.setId((Long)getHiddenValue("Id"));
        room.setName(getTextFromEdit("Name"));
        room.setPassword(getTextFromEdit("Password"));

        return room;
    }

    private String getTextFromEdit(String label) {
        LinearLayout layout = (LinearLayout)dynamicViews.get(label);
        EditText text = (EditText) layout.findViewById(R.id.text);
        return text.getText().toString();
    }

    private Object getHiddenValue(String label) {
        View v = dynamicViews.get(label);
        return v.getTag();
    }
}
