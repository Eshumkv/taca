package thomasmore.be.travelcommunicationassistant.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import thomasmore.be.travelcommunicationassistant.MyApp;
import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.fragment.BasicEditFragment;
import thomasmore.be.travelcommunicationassistant.fragment.ContactListFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MajorCategoryListFragment;
import thomasmore.be.travelcommunicationassistant.fragment.RoomsCreatedFragment;
import thomasmore.be.travelcommunicationassistant.fragment.HomeFragment;
import thomasmore.be.travelcommunicationassistant.fragment.MessagesListFragment;
import thomasmore.be.travelcommunicationassistant.fragment.WardedPersonListFragment;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.model.User;

public class Helper {
    public final static NavigationItems[] navigationItems = new NavigationItems[] {
            new NavigationItems<>(
                    R.string.nav_home,
                    R.drawable.ic_home_black_24dp,
                    HomeFragment.class),
            new NavigationItems<>(
                    R.string.nav_messages,
                    R.drawable.ic_message_black_24dp,
                    MessagesListFragment.class),
            new NavigationItems<>(
                    R.string.nav_rooms,
                    R.drawable.ic_group_black_24dp,
                    RoomsCreatedFragment.class),
            new NavigationItems<>(
                    R.string.nav_pictogram,
                    R.drawable.ic_stars_black_24dp,
                    MajorCategoryListFragment.class, Pictogram.class, true),
            new NavigationItems<>(
                    R.string.nav_category,
                    R.drawable.ic_dashboard_black_24dp,
                    MajorCategoryListFragment.class, Category.class, true),
            new NavigationItems<>(
                    R.string.nav_warded,
                    R.drawable.ic_person_black_24dp,
                    WardedPersonListFragment.class,true),
            new NavigationItems<>(
                    R.string.nav_contacts,
                    R.drawable.ic_contacts_black_24dp,
                    ContactListFragment.class),
            new NavigationItems<>(
                    R.string.nav_personal,
                    R.drawable.ic_info_black_24dp,
                    BasicEditFragment.class, User.class)
    };

    public static final String EXTRA_DATA = "data";
    public static final String EXTRA_DATA_BUNDLE = "databundle";
    public static final String EXTRA_SEARCH_INTENT = "I really need to search";
    public static final String EXTRA_MULTIPLE = "Excuse me, I need multiple of these.";

    public static final Random RANDOM = new Random();

    public static MyApp getApp(Activity activity) {
        return (MyApp) activity.getApplication();
    }

    public static <T> T NewInstanceOf(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) { return null; }
    }

    public static void changeFragment(Activity act, Fragment fragment, boolean addBackToStack) {
        changeFragment(((AppCompatActivity)act), fragment, addBackToStack);
    }

    public static void changeFragment(AppCompatActivity act, Fragment fragment, boolean addBackToStack) {
        FragmentManager fragmentManager = act.getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment, fragment.getClass().getName());

        if (addBackToStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public static void setTitle(Activity act, int resId, Object... vars) {
        AppCompatActivity activity = (AppCompatActivity) act;
        String title = activity.getResources().getString(resId, vars);
        ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);
        } else {
            Log.i("ERROR", "Could not set title");
        }
    }

    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static int getTotalHeightofListView(ListView lv) {

        Adapter adapter = lv.getAdapter();
        int listviewElementsheight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View mView = adapter.getView(i, null, lv);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            listviewElementsheight += mView.getMeasuredHeight();
        }

        return listviewElementsheight;
    }

    public static Intent getBackActivityIntent(Context ctx) {
        Intent intent = new Intent(ctx, BackActivity.class);
        intent.putExtra(BackActivity.DATA_STRING, BasicEditFragment.class);
        return intent;
    }

    public static String readFromAssets(Context ctx, String file) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            final AssetManager assets = ctx.getResources().getAssets();
            final InputStreamReader isr =
                    new InputStreamReader(assets.open(file));
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not read from assets!");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.e("ERROR", "Could not close reader.", e);
                }
            }
        }

        return sb.toString();
    }

    public static List<String> readSqlFromAssets(Context ctx, String file) {
        String full = readFromAssets(ctx, file);
        String[] split = full.split("\n");
        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (!split[i].equals("--NST")) {
                sb.append(split[i]);
            } else {
                list.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        list.add(sb.toString());

        return list;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    public static <T extends Enum<T>> String EnumToString(T enumerationValue) {
        if (enumerationValue == null) {
            return null;
        }

        return enumerationValue.name();
    }

    public static <T extends Enum<T>> T parseEnum(Class<T> type, String p) {
        return p == null ?
                null :
                T.valueOf(type, p);
    }

    public static <T extends Enum<T>> ArrayList<String> enumToList(Class<T> type) {
        ArrayList<String> list = new ArrayList<>();

        for (T e : type.getEnumConstants()) {
            list.add(e.name());
        }

        return list;
    }

    public static Bitmap getImage(Context ctx, String path) {
        ctx = ctx.getApplicationContext();
        Bitmap temp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.contact);

        if (path == null || path.equals("NONE")) {
            return temp;
        }

        // Load image from file
        try {
            File imgFile = new File(path == null ? "" : path);
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        } catch (Exception e) {
            // It didn't work,
            // Load image from contentprovider
            try {
                InputStream is = ctx.getContentResolver().openInputStream(Uri.parse(path));
                Bitmap bitmap = Helper.resizeBitmap(BitmapFactory.decodeStream(is), 500, 500);
                is.close();
                return bitmap;
            } catch (Exception ie) {}
        }

        // Still didn't work, give temp
        return temp;
    }

    // Courtesy of http://stackoverflow.com/a/10703256
    public static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();

        return resizedBitmap;
    }

    // Courtesy of: http://stackoverflow.com/a/3414749
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        context = context.getApplicationContext();
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean hasCamera(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static File createImageFile(Context ctx) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "TACA_" + timestamp;
        File storageDir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                filename,
                ".jpg",
                storageDir
        );

        return image;
    }

    public static void toast(Activity act, String text) {
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Activity act, int resId, Object... vars) {
        String text = act.getResources().getString(resId, vars);
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
    }

    public static byte parcelableBool(boolean bool) {
        return (byte) (bool ? 1 : 0);
    }

    public static boolean getParcelableBool(byte b) {
        return b != 0;
    }

    public static int dp(Context ctx, int dp) {
        final float scale = ctx.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    public static boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }

    // Includes both values!
    public static int randomBetween(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static String randomCharacter() {
        return String.valueOf((char)(RANDOM.nextInt(26) + 'a'));
    }
}
