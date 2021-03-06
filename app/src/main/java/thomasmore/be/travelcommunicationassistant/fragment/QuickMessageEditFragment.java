package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.LoginActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.HomeScreenAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.QuickMessageAdapter;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.model.QuickMessage;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;
import thomasmore.be.travelcommunicationassistant.utils.NavigationItems;

import static android.app.Activity.RESULT_OK;

public class QuickMessageEditFragment extends BaseFragment {

    public static final int REQUEST_NEW_PICTOGRAM = 1;

    private QuickMessage qmessage;
    private ArrayList<Integer> selectedPositions;
    private int selectedColor;

    private Button addButton;
    private Button removeButton;
    private Button backspaceButton;
    private LinearLayout list;

    private ArrayList<View> viewList;

    private LayoutInflater inflater;

    public QuickMessageEditFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_quick_message_edit, container, false);

        Bundle bundle = getArguments();
        qmessage = bundle.getParcelable(QuickMessage.class.getName());
        selectedPositions = new ArrayList<>(qmessage.getMessage().size());
        viewList = new ArrayList<>();
        list = (LinearLayout) rootView.findViewById(R.id.list);

        addButton = (Button) rootView.findViewById(R.id.c_add);
        removeButton = (Button) rootView.findViewById(R.id.c_edit);
        backspaceButton = (Button) rootView.findViewById(R.id.c_delete);

        selectedColor = ContextCompat.getColor(getActivity(), R.color.cardSelected);

        Helper.setTitle(getActivity(), R.string.quick_message_edit_title);

        View empty = rootView.findViewById(R.id.empty_text);
        if (qmessage.getMessage().size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        for (int i = 0; i < qmessage.getMessage().size(); i++) {
            Pictogram p = qmessage.getMessage().get(i);
            list.addView(getPictogramLayout(p, i));
        }

        backspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qmessage.getMessage().size() == 0) return;

                removePictogram(qmessage.getMessage().size()-1);
                toggleContext();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositions.size() == 0) return;

                // Sort them from high to low
                Collections.sort(selectedPositions, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2.compareTo(o1);
                    }
                });

                for (int position : selectedPositions) {
                    removePictogram(position);
                }

                selectedPositions.clear();

                // Go through the items, and reset the positions
                for (int i = 0; i < viewList.size(); i++) {
                    View view = viewList.get(i);
                    SelectedItem item = (SelectedItem) view.getTag();
                    item.pos = i;
                }

                toggleContext();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);
                intent.putExtra(BackActivity.DATA_STRING, MajorCategoryListFragment.class);
                intent.putExtra(Helper.EXTRA_SEARCH_INTENT, Pictogram.class);
                startActivityForResult(intent, REQUEST_NEW_PICTOGRAM);
            }
        });

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
                getFragmentManager().popBackStackImmediate();
                return true;
            case R.id.action_save:
                Database db = Database.getInstance(getActivity());

                db.removeQuickMessage(qmessage.getId());

                if (qmessage.getMessage().size() != 0) {
                    if (db.addQuickMessage(qmessage) != 0) {
                        Helper.toast(getActivity(), R.string.toast_saved);
                    } else {
                        Helper.toast(getActivity(), R.string.toast_not_saved);
                    }
                } else {
                    Helper.toast(getActivity(), R.string.toast_not_saved);
                }


                Contact warded = db.getWarded(qmessage.getWardedId());

                Bundle bundle = new Bundle();
                bundle.putParcelable(Contact.class.getName(), warded);

                Fragment fragment = new QuickMessageListFragment();
                fragment.setArguments(bundle);
                Helper.changeFragment(getActivity(), fragment, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_PICTOGRAM && resultCode == RESULT_OK) {
            Pictogram pictogram = data.getParcelableExtra(Pictogram.class.getName());
            addPictogram(pictogram);
        }
    }

    private View getPictogramLayout(Pictogram p, int position) {
        View v = QuickMessageAdapter.getPictogramIcon(p, getActivity(), inflater, list);
        final LinearLayout border = (LinearLayout) v.findViewById(R.id.border);

        v.setTag(new SelectedItem(false, position));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedItem item = (SelectedItem) v.getTag();

                if (item.isSelected) {
                    item.isSelected = false;
                    selectedPositions.remove((Integer)item.pos);

                    border.setBackgroundResource(0);
                } else {
                    item.isSelected = true;
                    selectedPositions.add(item.pos);

                    border.setBackgroundColor(selectedColor);
                }

                toggleContext();
            }
        });

        viewList.add(v);

        return v;
    }

    protected void toggleContext() {
        if (selectedPositions.size() != 0) {
            backspaceButton.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.VISIBLE);
        } else {
            backspaceButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.GONE);
        }
    }

    private void addPictogram(Pictogram p) {
        int position = qmessage.getMessage().size();
        qmessage.getMessage().add(p);
        list.addView(getPictogramLayout(p, position));

        View empty = getActivity().findViewById(R.id.empty_text);
        if (qmessage.getMessage().size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
    }

    private void removePictogram(int position) {
        qmessage.getMessage().remove(position);
        list.removeViewAt(position);

        int index = -1;
        for (int i = 0; i < viewList.size(); i++) {
            View v = viewList.get(i);
            SelectedItem item = (SelectedItem) v.getTag();
            if (item.pos == position) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            viewList.remove(index);
        }

        View empty = getActivity().findViewById(R.id.empty_text);
        if (qmessage.getMessage().size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
    }

    private class SelectedItem {
        boolean isSelected;
        int pos;

        SelectedItem(boolean selected, int posit) {
            pos = posit;
            isSelected = selected;
        }
    }
}
