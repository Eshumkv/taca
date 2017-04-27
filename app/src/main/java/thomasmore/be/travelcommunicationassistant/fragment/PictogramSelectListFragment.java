package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class PictogramSelectListFragment extends BasePagingFragment<Pictogram> {

    Category category;

    public PictogramSelectListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);

        Helper.setTitle(getActivity(), R.string.nav_pictogram);

        Bundle bundle = getArguments();
        category = bundle.getParcelable(Category.class.getName());

        List<Pictogram> tempList = new ArrayList<>();

        setupPagingMap(tempList, Pictogram.class, "getName", new Comparator<Pictogram>() {
            @Override
            public int compare(Pictogram lhs, Pictogram rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new PictogramAdapter(getActivity(), pagingMap.get(currentPage)));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

    private void goBack() {
        MajorCategory cat = category.getMajorCategory();

        Bundle bundle = new Bundle();
        bundle.putParcelable(MajorCategory.class.getName(), cat);

        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(bundle);
        Helper.changeFragment(getActivity(), fragment, false);
    }

    @Override
    protected void setList() {
        final ListView list = (ListView) getActivity().findViewById(R.id.contacts);
        list.setAdapter(new PictogramAdapter(getActivity(), pagingMap.get(currentPage)));
    }
}
