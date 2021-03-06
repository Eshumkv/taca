package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.MyApp;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.CategoryAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.PictogramAdapter;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class CategorySelectListFragment extends BasePagingFragment<Category> {

    private final static int REQUEST_EDIT = 1;

    MajorCategory majorCategory;

    public CategorySelectListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);

        Helper.setTitle(getActivity(), R.string.nav_category);

        selectedColor = ContextCompat.getColor(getActivity(), R.color.cardSelected);
        normalColor = ContextCompat.getColor(getActivity(), R.color.cardNormal);

        Bundle bundle = getArguments();
        majorCategory = bundle.getParcelable(MajorCategory.class.getName());

        Database db = Database.getInstance(getActivity());
        List<Category> tempList = db.getCategoriesForMajorCategory(majorCategory.getId());

        setupPagingMap(tempList, Category.class, "getName", new Comparator<Category>() {
            @Override
            public int compare(Category lhs, Category rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        setupPagingBar(rootView);

        RelativeLayout bar = (RelativeLayout) rootView.findViewById(R.id.context_menu);
        bar.setVisibility(View.VISIBLE);

        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new CategoryAdapter(getActivity(), pagingMap.get(currentPage)));
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
        text.setText(majorCategory.getName());
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        breadcrumb.addView(text);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditScreen(new Category(), true);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                Category category = (Category)list.getAdapter().getItem(selectedPosition);
                goToEditScreen(category, false);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setMessage(R.string.dialog_delete_category)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Category category = (Category)getList().getAdapter().getItem(selectedPosition);
                                Database db = Database.getInstance(getActivity());

                                if (db.deleteCategory(category.getId())) {
                                    Helper.toast(getActivity(), R.string.toast_deleted);
                                } else {
                                    Helper.toast(getActivity(), R.string.toast_not_deleted);
                                }

                                deselectPrevious(getView());
                                toggleContext();

                                List<Category> tempList = db.getCategoriesForMajorCategory(majorCategory.getId());

                                setupPagingMap(tempList, Category.class, "getName", new Comparator<Category>() {
                                    @Override
                                    public int compare(Category lhs, Category rhs) {
                                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                        return lhs.getName().compareTo(rhs.getName());
                                    }
                                });
                                setupPagingBar(getActivity().findViewById(android.R.id.content));
                                setListAdapter();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            Category category = data.getParcelableExtra(Category.class.getName());
            Database db = Database.getInstance(getActivity());

            category.setMajorCategoryId(majorCategory.getId());

            if (Helper.isEmpty(category.getName())) {
                goToEditScreen(category, false);
                Helper.toast(getActivity(), R.string.toast_error_name_empty);
                return;
            }

            if (Helper.isEmpty(category.getDescription())) {
                goToEditScreen(category, false);
                Helper.toast(getActivity(), R.string.toast_error_description_empty);
                return;
            }

            if (db.genericUpdate(Category.class, category)) {
                Helper.toast(getActivity(), R.string.toast_saved);
            } else {
                long id = db.genericInsert(Category.class, category);

                if (id != -1) {
                    Helper.toast(getActivity(), R.string.toast_saved);
                } else {
                    Helper.toast(getActivity(), R.string.toast_not_saved);
                }
            }

            List<Category> tempList = db.getCategoriesForMajorCategory(majorCategory.getId());
            setupPagingMap(tempList, Category.class, "getName", new Comparator<Category>() {
                @Override
                public int compare(Category lhs, Category rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
            setupPagingBar(getActivity().findViewById(android.R.id.content));
            setListAdapter();
        }
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

    private void goBack() {
        BaseFragment fragment = new MajorCategoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Helper.EXTRA_DATA, true);
        fragment.setArguments(bundle);
        Helper.changeFragment(getActivity(), fragment, false);
    }

    @Override
    protected void setListAdapter() {
        final ListView list = getList();
        list.setAdapter(new CategoryAdapter(getActivity(), pagingMap.get(currentPage)));
    }

    @Override
    protected ListView getList() {
        return (ListView) getActivity().findViewById(R.id.list);
    }

    private void goToEditScreen(Category category, boolean isnew) {
        deselectPrevious(getView());
        toggleContext();

        String className = Category.class.getName();

        // TODO: Remove this
        category.setMajorCategory(majorCategory);

        Intent intent = Helper.getBackActivityIntent(getActivity());
        intent.putExtra(BasicEditFragment.CLASSNAME, className);
        intent.putExtra(className, category);

        if (isnew) {
            intent.putExtra(BasicEditFragment.EXTRA_ADD_INSTEAD_OF_SAVE, true);
        }

        startActivityForResult(intent, REQUEST_EDIT);
    }
}
