package thomasmore.be.travelcommunicationassistant.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.PagingAdapter;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

public abstract class BasePagingFragment<T> extends BaseFragment {

    protected int selectedPosition = -1;
    protected int selectedColor;
    protected int normalColor;

    Button addButton;
    Button editButton;
    Button deleteButton;

    protected String currentPage = "A";
    protected HashMap<String, List<T>> pagingMap;

    public BasePagingFragment() {
        // Empty constructor required for fragment subclasses
    }


    protected void deselectPrevious(View v) {
        if (selectedPosition != -1) {
            ListView list = getList();
            LinearLayout prevRoot =
                    (LinearLayout) Helper.getViewByPosition(selectedPosition, list);
            CardView card = (CardView) prevRoot.findViewById(R.id.card_view);
            card.setCardBackgroundColor(normalColor);
        }

        selectedPosition = -1;
        toggleContext();
    }

    protected void toggleContext() {
        if (selectedPosition != -1) {
            if (editButton != null)
                editButton.setVisibility(View.VISIBLE);

            if (deleteButton != null)
                deleteButton.setVisibility(View.VISIBLE);
        } else {
            if (editButton != null)
                editButton.setVisibility(View.GONE);

            if (deleteButton != null)
                deleteButton.setVisibility(View.GONE);
        }
    }

    // Paging

    protected void setupPagingMap(List<T> list, Class<T> type, String methodName, Comparator<T> comparator) {

        Collections.sort(list, comparator);
        if (list.size() != 0) {
            try {
                T value = list.get(0);
                String temp = (String) type.getMethod(methodName).invoke(value);
                currentPage = temp.substring(0, 1).toUpperCase();
                pagingMap = getMap(list, methodName, type);
            } catch (Exception e) {
                resetMap();
                e.printStackTrace();
            }
        } else {
            resetMap();
        }
    }

    protected void resetMap() {
        currentPage = "A";
        pagingMap = new HashMap<>();
        pagingMap.put(currentPage, new ArrayList<T>());
    }

    protected void setupPagingBar(View root) {
        LinearLayout bar = (LinearLayout) root.findViewById(R.id.paging);
        bar.setVisibility(View.VISIBLE);

        addButton = (Button) root.findViewById(R.id.c_add);
        editButton = (Button) root.findViewById(R.id.c_edit);
        deleteButton = (Button) root.findViewById(R.id.c_delete);

        Button first = (Button) root.findViewById(R.id.paging_first);
        Button previous = (Button) root.findViewById(R.id.paging_previous);
        Button current = (Button) root.findViewById(R.id.paging_current);
        Button next = (Button) root.findViewById(R.id.paging_next);
        Button last = (Button) root.findViewById(R.id.paging_last);

        final ArrayList<String> list = getPagingItems();

        if (list.size() == 1) {
            bar.setVisibility(View.GONE);
        }

        first.setText(list.get(0));
        current.setText(currentPage);
        last.setText(list.get(list.size()-1));

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current(list);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last();
            }
        });
    }

    private ArrayList<String> getPagingItems() {
        ArrayList<String> tlist = new ArrayList<>();
        Iterator it = pagingMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            tlist.add((String)pair.getKey());
        }

        Collections.sort(tlist);
        return tlist;
    }

    private <T> int getIndex(List<T> list, T item) {
        int position = 0;

        for (T s : list) {
            if (s.equals(item)) {
                return position;
            }
            position++;
        }

        return -1;
    }

    protected void first() {
        ArrayList<String> list = getPagingItems();
        currentPage = list.get(0);
        deselectPrevious(getView());
        setListAdapter();
    }

    protected void previous() {
        ArrayList<String> list = getPagingItems();
        int position = getIndex(list, currentPage);

        if (position > 0) {
            currentPage = list.get(position-1);
        }
        deselectPrevious(getView());
        setListAdapter();
    }

    protected void current(List<String> pages) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.dialog_select_paging_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setAdapter(new PagingAdapter(getActivity(), pages), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentPage = getPagingItems().get(which);
                deselectPrevious(getView());
                setListAdapter();
            }
        });
        builder.create().show();
    }

    protected void next() {
        ArrayList<String> list = getPagingItems();
        int position = getIndex(list, currentPage);

        if (position != -1 && position < list.size()-1) {
            currentPage = list.get(position+1);
        }
        deselectPrevious(getView());
        setListAdapter();
    }

    protected void last() {
        ArrayList<String> list = getPagingItems();
        currentPage = list.get(list.size()-1);
        deselectPrevious(getView());
        setListAdapter();
    }

    protected abstract void setListAdapter();
    protected abstract ListView getList();

    public static <T> HashMap<String, List<T>> getMap(List<T> list, String methodName, Class<T> cls) {
        HashMap<String, List<T>> map = new HashMap<>();

        for (T c : list) {
            try {
                String temp = (String) cls.getMethod(methodName).invoke(c);
                String firstCharacter = temp.substring(0, 1).toUpperCase();
                if (map.get(firstCharacter) == null) {
                    map.put(firstCharacter, new ArrayList<T>());
                }
                map.get(firstCharacter).add(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }
}
