package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import thomasmore.be.travelcommunicationassistant.MyApp;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.CategoryAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.PagingAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.PictogramAdapter;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class PictogramSelectListFragment extends BasePagingFragment<Pictogram> {

    private final static int REQUEST_EDIT = 1;

    Category category;

    public PictogramSelectListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);

        Helper.setTitle(getActivity(), R.string.nav_pictogram);

        selectedColor = ContextCompat.getColor(getActivity(), R.color.cardSelected);
        normalColor = ContextCompat.getColor(getActivity(), R.color.cardNormal);

        Bundle bundle = getArguments();
        category = bundle.getParcelable(Category.class.getName());

        Database db = Database.getInstance(getActivity());
        List<Pictogram> tempList = db.getPictogramsOfCategory(category.getId());

        setupPagingMap(tempList, Pictogram.class, "getName", new Comparator<Pictogram>() {
            @Override
            public int compare(Pictogram lhs, Pictogram rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        setupPagingBar(rootView);

        RelativeLayout bar = (RelativeLayout) rootView.findViewById(R.id.context_menu);
        bar.setVisibility(View.VISIBLE);

        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new PictogramAdapter(getActivity(), pagingMap.get(currentPage)));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout root = (LinearLayout)view;
                CardView card = (CardView)root.findViewById(R.id.card_view);
                card.setCardBackgroundColor(selectedColor);

                // Deselect the previous card
                int temp = selectedPosition;
                deselectPrevious(rootView);
                selectedPosition = temp == position ? -1 : position;

                toggleContext();
            }
        });

        LinearLayout breadcrumbLayout = (LinearLayout) rootView.findViewById(R.id.breadcrumblayout);
        LinearLayout breadcrumb = (LinearLayout) rootView.findViewById(R.id.breadcrumb);
        breadcrumbLayout.setVisibility(View.VISIBLE);
        TextView text = (TextView) inflater.inflate(R.layout.item_breadcrumb, null, false);
        text.setText(category.getMajorCategory().getName());
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.changeFragment(getActivity(), new MajorCategoryListFragment(), false);
            }
        });

        breadcrumb.addView(text);
        breadcrumb.addView(inflater.inflate(R.layout.item_breadcrumb, null, false));

        TextView text2 = (TextView) inflater.inflate(R.layout.item_breadcrumb, null, false);
        text2.setText(category.getName());
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        breadcrumb.addView(text2);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditScreen(new Pictogram(), true);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                Pictogram pictogram = (Pictogram)list.getAdapter().getItem(selectedPosition);
                goToEditScreen(pictogram, false);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setMessage(R.string.dialog_delete_pictogram)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Pictogram pictogram = (Pictogram) getList().getAdapter().getItem(selectedPosition);
                                Database db = Database.getInstance(getActivity());

                                if (db.genericDelete(Pictogram.class, pictogram.getId())) {
                                    Helper.toast(getActivity(), R.string.toast_deleted);
                                } else {
                                    Helper.toast(getActivity(), R.string.toast_not_deleted);
                                }

                                deselectPrevious(getView());
                                toggleContext();

                                List<Pictogram> tempList = db.getPictogramsOfCategory(category.getId());

                                setupPagingMap(tempList, Pictogram.class, "getName", new Comparator<Pictogram>() {
                                    @Override
                                    public int compare(Pictogram lhs, Pictogram rhs) {
                                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                        return lhs.getName().compareTo(rhs.getName());
                                    }
                                });
                                setupPagingBar(getActivity().findViewById(android.R.id.content));
                                setListAdapter();
                                deselectPrevious(getView());
                                toggleContext();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deselectPrevious(getView());
                                toggleContext();
                            }
                        })
                        .create()
                        .show();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simple_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_search:
                MyApp app = Helper.getApp(getActivity());
                Bundle bundle = new Bundle();
                bundle.putString(MyApp.SEARCH_TERM, MyApp.SEARCH_PICTOGRAM);
                app.setSearch_extra(bundle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onBackPressed() {
        goBack();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            Pictogram pictogram = data.getParcelableExtra(Pictogram.class.getName());
            Database db = Database.getInstance(getActivity());

            if (Helper.isEmpty(pictogram.getName())) {
                goToEditScreen(pictogram, false);
                Helper.toast(getActivity(), R.string.toast_error_name_empty);
                return;
            }

            if (Helper.isEmpty(pictogram.getDescription())) {
                goToEditScreen(pictogram, false);
                Helper.toast(getActivity(), R.string.toast_error_description_empty);
                return;
            }

            if (db.genericUpdate(Pictogram.class, pictogram)) {
                Helper.toast(getActivity(), R.string.toast_saved);
            } else {
                long id = db.genericInsert(Pictogram.class, pictogram);

                if (id != -1) {
                    Helper.toast(getActivity(), R.string.toast_saved);
                } else {
                    Helper.toast(getActivity(), R.string.toast_not_saved);
                }
            }

            List<Pictogram> tempList = db.getPictogramsOfCategory(category.getId());
            setupPagingMap(tempList, Pictogram.class, "getName", new Comparator<Pictogram>() {
                @Override
                public int compare(Pictogram lhs, Pictogram rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
            setupPagingBar(getActivity().findViewById(android.R.id.content));
            setListAdapter();
        }
    }

    private void goBack() {
        MajorCategory cat = category.getMajorCategory();

        Bundle bundle = new Bundle();
        bundle.putParcelable(MajorCategory.class.getName(), cat);

        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(bundle);
        Helper.changeFragment(getActivity(), fragment, false);
    }

    @Override
    protected void setListAdapter() {
        final ListView list = getList();
        list.setAdapter(new PictogramAdapter(getActivity(), pagingMap.get(currentPage)));
    }

    @Override
    protected ListView getList() {
        return (ListView) getActivity().findViewById(R.id.list);
    }

    private void goToEditScreen(Pictogram pictogram, boolean isnew) {
        deselectPrevious(getView());
        toggleContext();

        String className = Pictogram.class.getName();

        // TODO: REMOVE ME
        pictogram.setCategory(category);

        Intent intent = Helper.getBackActivityIntent(getActivity());
        intent.putExtra(BasicEditFragment.CLASSNAME, className);
        intent.putExtra(className, pictogram);

        if (isnew) {
            intent.putExtra(BasicEditFragment.EXTRA_ADD_INSTEAD_OF_SAVE, true);
        }

        startActivityForResult(intent, REQUEST_EDIT);
    }
}
