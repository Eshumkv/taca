package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
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

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.MyApp;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.PictogramAdapter;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class PictogramListFragment extends BasePagingFragment<Pictogram> {

    private final static int REQUEST_ADDPICTOGRAM = 1;

    private Category category;

    private boolean isPictogramSettingsList = false;
    private Contact warded;

    private boolean selectMultiple = false;

    private ArrayList<Pictogram> selectedPictograms;

    private Bundle cachedBundle;

    public PictogramListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);

        int title = R.string.nav_pictogram;

        selectedColor = ContextCompat.getColor(getActivity(), R.color.cardSelected);
        normalColor = ContextCompat.getColor(getActivity(), R.color.cardNormal);
        selectedPictograms = new ArrayList<>();

        final Bundle bundle = getArguments();
        category = bundle.getParcelable(Category.class.getName());

        if (bundle.containsKey(WardedPersonFragment.EXTRA_PICTOGRAM_SETTINGS)) {
            isPictogramSettingsList = true;
            warded = bundle.getParcelable(WardedPersonFragment.EXTRA_PICTOGRAM_SETTINGS);
            Helper.setTitle(getActivity(), R.string.warded_pictograms_title, warded.getName());
        }

        if (bundle.containsKey(Helper.EXTRA_MULTIPLE)) {
            selectMultiple = bundle.getBoolean(Helper.EXTRA_MULTIPLE);
            title = R.string.add_pictogram_title;
        }
        cachedBundle = bundle;

        if (warded == null) {
            Helper.setTitle(getActivity(), title);
        }

        setupPagingMap(getCorrectList(), Pictogram.class, "getName", new Comparator<Pictogram>() {
            @Override
            public int compare(Pictogram lhs, Pictogram rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        setupPagingBar(rootView);

        RelativeLayout bar = (RelativeLayout) rootView.findViewById(R.id.context_menu);

        if (selectMultiple) {
            bar.setVisibility(View.GONE);
        } else {
            bar.setVisibility(View.VISIBLE);
        }

        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new PictogramAdapter(getActivity(), pagingMap.get(currentPage)));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pictogram p = (Pictogram) getList().getAdapter().getItem(position);

                if (isPictogramSettingsList || selectMultiple) {
                    LinearLayout root = (LinearLayout)view;
                    CardView card = (CardView)root.findViewById(R.id.card_view);

                    if (selectedPictograms.contains(p)) {
                        selectedPictograms.remove(p);
                        card.setCardBackgroundColor(normalColor);
                    } else {
                        selectedPictograms.add(p);
                        card.setCardBackgroundColor(selectedColor);
                    }

                    toggleContext();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Pictogram.class.getName(), p);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
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
                BaseFragment fragment = new MajorCategoryListFragment();
                bundle.putSerializable(Helper.EXTRA_SEARCH_INTENT, Pictogram.class);
                fragment.setArguments(bundle);
                Helper.changeFragment(getActivity(), fragment, false);
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
                if (isPictogramSettingsList) {
                    Intent intent = new Intent(getActivity(), BackActivity.class);
                    intent.putExtra(BackActivity.DATA_STRING, MajorCategoryListFragment.class);
                    intent.putExtra(Helper.EXTRA_SEARCH_INTENT, Pictogram.class);
                    intent.putExtra(Helper.EXTRA_MULTIPLE, true);
                    startActivityForResult(intent, REQUEST_ADDPICTOGRAM);
                } else {
                    goToEditScreen(new Pictogram());
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == -1) return;
                Pictogram pictogram = (Pictogram)list.getAdapter().getItem(selectedPosition);
                goToEditScreen(pictogram);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPictograms.size() == 0) return;

                int message = R.string.dialog_delete_pictogram;

                if (selectedPictograms.size() > 1) {
                    message = R.string.dialog_delete_pictograms;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setMessage(message)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Database db = Database.getInstance(getActivity());

                                // TODO: delete it
                                if (isPictogramSettingsList) {
                                    boolean success = true;
                                    for (Pictogram p : selectedPictograms) {
                                        success &= db.deletePictogramFromWarded(warded.getId(), p.getId());
                                    }

                                    if (success) {
                                        Helper.toast(getActivity(), R.string.toast_deleted);
                                    } else {
                                        Helper.toast(getActivity(), R.string.toast_not_deleted);
                                    }
                                } else {
                                    // Do the other thing
                                    Pictogram pictogram = (Pictogram) getList().getAdapter().getItem(selectedPosition);
                                }

                                deselectPrevious(getView());
                                toggleContext();

                                setupPagingMap(getCorrectList(), Pictogram.class, "getName", new Comparator<Pictogram>() {
                                    @Override
                                    public int compare(Pictogram lhs, Pictogram rhs) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isPictogramSettingsList) {
            inflater.inflate(R.menu.menu_simple_save, menu);
        } else if (selectMultiple) {
            inflater.inflate(R.menu.menu_simple_add, menu);
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
                Intent intent = new Intent();
                Bundle extra = new Bundle();

                extra.putParcelableArrayList(Pictogram.class.getName(), selectedPictograms);
                intent.putExtra("extra", extra);

                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ADDPICTOGRAM && resultCode == RESULT_OK) {
            Bundle extra = data.getBundleExtra("extra");
            List<Pictogram> pictograms = extra.getParcelableArrayList(Pictogram.class.getName());
            PictogramListFragment.addPictogramsToWarded(warded.getId(), pictograms, getActivity());

            setupPagingMap(getCorrectList(), Pictogram.class, "getName", new Comparator<Pictogram>() {
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

    @Override
    public boolean onBackPressed() {
        goBack();
        return true;
    }

    public static void addPictogramsToWarded(long wardedId, List<Pictogram> pictograms, Context ctx) {
        ctx = ctx.getApplicationContext();
        Database db = Database.getInstance(ctx);

        boolean success = true;
        for (Pictogram pictogram : pictograms) {
            long id = db.addPictogramToPictogramSettings(wardedId, pictogram);
            success &= (id != -1);
        }

        if (success) {
            Helper.toast(ctx, R.string.toast_saved);
        } else {
            Helper.toast(ctx, R.string.toast_not_saved);
        }
    }

    private void goBack() {
        Bundle bundle = new Bundle();

        if (cachedBundle.containsKey(Helper.EXTRA_MULTIPLE)) {
            bundle.putBoolean(Helper.EXTRA_MULTIPLE, cachedBundle.getBoolean(Helper.EXTRA_MULTIPLE));
        }

        if (isPictogramSettingsList) {
            bundle.putParcelable(MajorCategory.class.getName(), category.getMajorCategory());

            CategoryListFragment fragment = new CategoryListFragment();

            bundle.putParcelable(WardedPersonFragment.EXTRA_PICTOGRAM_SETTINGS, warded);
            fragment.setArguments(bundle);
            Helper.changeFragment(getActivity(), fragment, false);
            return;
        }

        MajorCategory cat = category.getMajorCategory();

        bundle.putParcelable(MajorCategory.class.getName(), cat);
        bundle.putSerializable(Helper.EXTRA_SEARCH_INTENT, Pictogram.class);

        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(bundle);
        Helper.changeFragment(getActivity(), fragment, false);
    }

    private void goToEditScreen(Pictogram pictogram) {
        deselectPrevious(getView());
        toggleContext();

        String className = Pictogram.class.getName();

        // TODO: REMOVE ME
        pictogram.setCategory(category);

        Intent intent = Helper.getBackActivityIntent(getActivity());
        intent.putExtra(BasicEditFragment.CLASSNAME, className);
        intent.putExtra(className, pictogram);
        startActivityForResult(intent, 1);
    }

    private List<Pictogram> getCorrectList() {
        List<Pictogram> tempList = null;
        Database db = Database.getInstance(getActivity());

        if (isPictogramSettingsList) {
            tempList = db.getPictogramsForCategoryOfWarded(warded.getId(), category.getId());
        } else {
            tempList = db.getPictogramsOfCategory(category.getId());
        }

        return tempList;
    }

    @Override
    protected void setListAdapter() {
        final ListView list = getList();
        List<Pictogram> picts = pagingMap.get(currentPage);

        // If we can't select multiple, across different pages,
        // clear the list when we move to a new page.
        if (!selectMultiple) {
            selectedPictograms.clear();
        }

        list.setAdapter(new PictogramAdapter(getActivity(), picts,
                selectedPictograms, selectedColor, normalColor));
    }

    @Override
    protected ListView getList() {
        return (ListView) getActivity().findViewById(R.id.list);
    }

    @Override
    protected void toggleContext() {
        super.toggleContext();

        if (isPictogramSettingsList) {
            if (selectedPictograms.size() > 0) {
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }
        }
    }
}
