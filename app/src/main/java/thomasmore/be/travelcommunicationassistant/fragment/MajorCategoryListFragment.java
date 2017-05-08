package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
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

import java.util.Arrays;

import thomasmore.be.travelcommunicationassistant.MyApp;
import thomasmore.be.travelcommunicationassistant.NavigationDrawerActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.MajorCategoryAdapter;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public class MajorCategoryListFragment extends BaseFragment {
    private boolean isCategory = false;

    private boolean isSearch = false;
    private boolean isSearchMajorCat = false;
    private Class<?> searchClass;

    public MajorCategoryListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isCategory = bundle.getBoolean(Helper.EXTRA_DATA);

            if (bundle.containsKey(Helper.EXTRA_SEARCH_INTENT)) {
                isSearch = true;
                searchClass = (Class<?>) bundle.get(Helper.EXTRA_SEARCH_INTENT);

                if (searchClass.equals(MajorCategory.class)) {
                    isSearchMajorCat = true;
                }
            }
        }

        Helper.setTitle(getActivity(), isCategory ? R.string.nav_category : R.string.nav_pictogram);

        MajorCategory[] mcategories = new MajorCategory[] {
                new MajorCategory("Noun"),
                new MajorCategory("Verb")
        };

        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new MajorCategoryAdapter(getActivity(), Arrays.asList(mcategories)));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MajorCategory category = (MajorCategory) list.getAdapter().getItem(position);

                if (isSearch && isSearchMajorCat) {
                    Intent intent = new Intent();
                    String classname = MajorCategory.class.getName();
                    intent.putExtra(classname, category);
                    intent.putExtra(BasicEditFragment.CLASSNAME, classname);

                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(MajorCategory.class.getName(), category);

                    if (isCategory) {
                        Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                        intent.putExtra(Helper.EXTRA_DATA, CategorySelectListFragment.class);
                        intent.putExtra(Helper.EXTRA_DATA_BUNDLE, bundle);

                        startActivity(intent);
                        getActivity().finish();
                        return;
                    }

                    CategoryListFragment fragment = new CategoryListFragment();

                    if (isSearch) {
                        bundle.putSerializable(Helper.EXTRA_SEARCH_INTENT, searchClass);
                    }

                    fragment.setArguments(bundle);
                    Helper.changeFragment(getActivity(), fragment, false);
                }
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
        getActivity().finish();
        return true;
    }
}
