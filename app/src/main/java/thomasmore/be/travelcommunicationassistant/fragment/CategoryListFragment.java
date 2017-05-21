package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
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

import java.util.Arrays;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.MyApp;
import thomasmore.be.travelcommunicationassistant.NavigationDrawerActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.CategoryAdapter;
import thomasmore.be.travelcommunicationassistant.adapter.MajorCategoryAdapter;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class CategoryListFragment extends BaseFragment {

    private final static int REQUEST_ADDPICTOGRAM = 1;

    private boolean isSearch = false;
    private boolean isSearchCat = false;
    private Class<?> searchClass;

    private boolean isPictogramSettingsList = false;
    private Contact warded;

    private Bundle cachedBundle;

    public CategoryListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);

        int title = R.string.nav_pictogram;

        final Bundle bundle = getArguments();
        final MajorCategory majorCategory = bundle.getParcelable(MajorCategory.class.getName());

        if (bundle.containsKey(Helper.EXTRA_SEARCH_INTENT)) {
            isSearch = true;
            searchClass = (Class<?>) bundle.get(Helper.EXTRA_SEARCH_INTENT);

            if (searchClass.equals(Category.class)) {
                isSearchCat = true;
            }
        }

        if (bundle.containsKey(WardedPersonFragment.EXTRA_PICTOGRAM_SETTINGS)) {
            isPictogramSettingsList = true;
            warded = bundle.getParcelable(WardedPersonFragment.EXTRA_PICTOGRAM_SETTINGS);
        }

        if (bundle.containsKey(Helper.EXTRA_MULTIPLE)) {
            title = R.string.add_pictogram_title;
        }

        cachedBundle = bundle;

        Helper.setTitle(getActivity(), title);

        Database db = Database.getInstance(getActivity());
        List<Category> categories = null;

        if (isPictogramSettingsList) {
            categories = db.getCategoriesForMajorCategoryOfWarded(majorCategory.getId(), warded.getId());
        } else {
            categories = db.getCategoriesForMajorCategory(majorCategory.getId());
        }

        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new CategoryAdapter(getActivity(), categories));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) list.getAdapter().getItem(position);
                category.setMajorCategory(majorCategory);

                if (isSearch && isSearchCat) {
                    Intent intent = new Intent();
                    String classname = Category.class.getName();
                    intent.putExtra(classname, category);
                    intent.putExtra(BasicEditFragment.CLASSNAME, classname);

                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    if (isSearch || isPictogramSettingsList) {
                        BaseFragment fragment = new PictogramListFragment();
                        Bundle bundle = new Bundle();

                        bundle.putSerializable(Helper.EXTRA_SEARCH_INTENT, searchClass);
                        bundle.putSerializable(Helper.EXTRA_DATA, PictogramListFragment.class);
                        bundle.putParcelable(Category.class.getName(), category);

                        if (isPictogramSettingsList) {
                            bundle.putParcelable(WardedPersonFragment.EXTRA_PICTOGRAM_SETTINGS, warded);
                        }

                        if (cachedBundle.containsKey(Helper.EXTRA_MULTIPLE)) {
                            bundle.putBoolean(Helper.EXTRA_MULTIPLE,
                                    cachedBundle.getBoolean(Helper.EXTRA_MULTIPLE));
                        }

                        fragment.setArguments(bundle);
                        Helper.changeFragment(getActivity(), fragment, false);
                    } else {
                        Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putParcelable(Category.class.getName(), category);
                        intent.putExtra(Helper.EXTRA_DATA_BUNDLE, bundle);
                        intent.putExtra(Helper.EXTRA_DATA, PictogramSelectListFragment.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
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

        // We are at the Pictogram Settings screen of a warded person.
        if (isPictogramSettingsList) {
            RelativeLayout bar = (RelativeLayout) rootView.findViewById(R.id.context_menu);
            bar.setVisibility(View.VISIBLE);

            Helper.setTitle(getActivity(), R.string.warded_pictograms_title, warded.getName());

            Button addButton = (Button) bar.findViewById(R.id.c_add);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);
                intent.putExtra(BackActivity.DATA_STRING, MajorCategoryListFragment.class);
                intent.putExtra(Helper.EXTRA_SEARCH_INTENT, Pictogram.class);
                intent.putExtra(Helper.EXTRA_MULTIPLE, true);
                startActivityForResult(intent, REQUEST_ADDPICTOGRAM);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isPictogramSettingsList) {
            inflater.inflate(R.menu.menu_simple_save, menu);
        } else {
            inflater.inflate(R.menu.menu_simple_search, menu);

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName()));
        }

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
            case R.id.action_save:
                getActivity().finish();
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

        if (requestCode == REQUEST_ADDPICTOGRAM && resultCode == RESULT_OK) {
            Bundle extra = data.getBundleExtra("extra");
            List<Pictogram> pictograms = extra.getParcelableArrayList(Pictogram.class.getName());

            Log.i("INFO", pictograms.size() + "");
            Helper.toast(getActivity(), R.string.toast_saved);
        }
    }

    private void goBack() {
        BaseFragment fragment = new MajorCategoryListFragment();
        fragment.setArguments(cachedBundle);
        Helper.changeFragment(getActivity(), fragment, false);
    }
}
