package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.MyApp;
import thomasmore.be.travelcommunicationassistant.NavigationDrawerActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.CategoryAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.MajorCategoryAdapter;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class CategoryListFragment extends BaseFragment {

    public CategoryListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);

        Helper.setTitle(getActivity(), R.string.nav_pictogram);

        Bundle bundle = getArguments();
        final MajorCategory majorCategory = bundle.getParcelable(MajorCategory.class.getName());
        final Category[] categories = new Category[] {
                new Category("Category 1", "HERE'S THE DESCRIPTION!"),
                new Category("Category 2", "DESCRIPTION TIME"),
                new Category("Category 3", "HERE'S THE DESCRIPTION!"),
                new Category("Category 4", "DESCRIPTION TIME"),
                new Category("Category 5", "HERE'S THE DESCRIPTION!"),
                new Category("Category 6", "DESCRIPTION TIME")
        };

        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new CategoryAdapter(getActivity(), Arrays.asList(categories)));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) list.getAdapter().getItem(position);
                category.setMajorCategory(majorCategory);

                Bundle bundle = new Bundle();
                bundle.putParcelable(Category.class.getName(), category);

                Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                intent.putExtra(Helper.EXTRA_DATA, PictogramSelectListFragment.class);
                intent.putExtra(Helper.EXTRA_DATA_BUNDLE, bundle);
                startActivity(intent);
                getActivity().finish();
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
                Helper.changeFragment(getActivity(), new MajorCategoryListFragment(), false);
            }
        });

        breadcrumb.addView(text);

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
        Helper.changeFragment(getActivity(), new MajorCategoryListFragment(), false);
        return true;
    }
}
