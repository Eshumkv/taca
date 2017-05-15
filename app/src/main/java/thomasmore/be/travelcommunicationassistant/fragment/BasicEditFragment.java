package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import thomasmore.be.travelcommunicationassistant.BackActivity;
import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.ContactType;
import thomasmore.be.travelcommunicationassistant.model.Language;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.model.MessageType;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.model.User;
import thomasmore.be.travelcommunicationassistant.utils.Database;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class BasicEditFragment extends BaseFragment {

    public final static String CLASSNAME = "ClassNameForBasicEdit";

    private final static int REQUEST_GALLERY = 1;
    private final static int REQUEST_PHOTO = 2;
    private final static int REQUEST_SEARCHBUTTON = 101;

    private String imagePath = null;

    private String classname;
    private HashMap<String, View> dynamicViews = new HashMap<>();

    private boolean isIt = false;

    private EditText lastAddedEditText;

    public BasicEditFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basic_edit, container, false);

        Bundle bundle = getArguments();
        classname = bundle.getString(CLASSNAME);
        setupLayout(bundle, rootView, inflater);

        // Hide the softinput
        // This assumes all edit screens have at least ONE edittext!
        final EditText text = (EditText) rootView.findViewById(R.id.text);
        text.requestFocus();
        text.clearFocus();

        // Set the last edittext as the done button.
        lastAddedEditText.setSingleLine();
        lastAddedEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

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
                getActivity().finish();
                return true;
            case R.id.action_save:
                if (!classname.equals(User.class.getName())) {
                    getActivity().setResult(Activity.RESULT_OK, getIntent());
                    getActivity().finish();
                } else {
                    callback.onFragmentInteraction(BasicEditFragment.class, getUserData());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (!classname.equals(User.class.getName())) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Pick an image
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);
            imageView.setImageURI(uri);
            imageView.setTag(uri.toString());
        } else if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);
            imageView.setImageBitmap(Helper.getImage(getActivity(), imagePath));
            imageView.setTag(imagePath);
        } else if (requestCode == REQUEST_SEARCHBUTTON && resultCode == RESULT_OK) {
            String classname = data.getStringExtra(CLASSNAME);

            if (classname.equals(MajorCategory.class.getName())) {
                MajorCategory majorCategory = data.getParcelableExtra(classname);

                View v = dynamicViews.get("Major Category");
                v.setTag(majorCategory);

                EditText editText = (EditText) v.findViewById(R.id.text);
                editText.setText(majorCategory.getName());
            } else if (classname.equals(Category.class.getName())) {
                Category category = data.getParcelableExtra(classname);

                View v = dynamicViews.get("Category");
                v.setTag(category);

                EditText editText = (EditText) v.findViewById(R.id.text);
                editText.setText(category.getFullName());
            }
        }
    }

    private void setupLayout(Bundle bundle, View root, LayoutInflater inflater) {
        LinearLayout layout = (LinearLayout) root.findViewById(R.id.content);

        if (classname.equals(Room.class.getName())) {
            Room room = bundle.getParcelable(classname);

            layout.addView(getHidden("Id", room.getId()));
            layout.addView(getEditText("Name", room.getName(), inflater, layout));
            layout.addView(getEditText("Password", room.getPassword(), inflater, layout));

            Helper.setTitle(getActivity(), R.string.nav_room_single);
        } else if (classname.equals(Contact.class.getName())) {
            Contact contact = bundle.getParcelable(classname);

            isIt = bundle.containsKey(Helper.EXTRA_DATA);

            layout.addView(getHidden("Id", contact.getId()));
            layout.addView(getImage("Image", contact.getImagePath(), inflater, layout, root));
            layout.addView(getEditText("Name", contact.getName(), inflater, layout));
            layout.addView(getEditText("Phone number", contact.getPhonenumber(), inflater, layout));

            if (isIt) {
                // TODO: REMOVE THIS PLEASE
                if (contact.getLanguage() == null) {
                    contact.setLanguage(Language.English);
                }

                // TODO: REMOVE THIS TOO
                if (contact.getMessageType() == null) {
                    contact.setMessageType(MessageType.Pictogram);
                }

                layout.addView(getSpinner("Language", contact.getLanguage(), inflater, layout));
                layout.addView(getSpinner("Type of message", contact.getMessageType(), inflater, layout));
            } else {
                // TODO: REMOVE THIS TOO
                if (contact.getType() == null) {
                    contact.setType(ContactType.Warded);
                }

                layout.addView(getSpinner("Role", contact.getType(), inflater, layout));
            }

            if (isIt) {
                Helper.setTitle(getActivity(), R.string.nav_personal);
            } else {
                Helper.setTitle(getActivity(), R.string.nav_contacts_single);
            }
        } else if (classname.equals(User.class.getName())) {
            User user = bundle.getParcelable(classname);

            layout.addView(getHidden("Id", user.getId()));
            layout.addView(getImage("Image", user.getImagePath(), inflater, layout, root));
            layout.addView(getEditText("Name", user.getUsername(), inflater, layout));
            layout.addView(getEditText("Phone number", user.getPhonenumber(), inflater, layout));
            layout.addView(getPasswordText("Password", user.getPassword(), inflater, layout));
            layout.addView(getSpinner("Language", user.getLanguage(), inflater, layout));

            Helper.setTitle(getActivity(), R.string.nav_personal);
        } else if (classname.equals(Category.class.getName())) {
            Category category = bundle.getParcelable(classname);

            layout.addView(getHidden("Id", category.getId()));
            layout.addView(getImage("Image", category.getImagePath(), inflater, layout, root));
            layout.addView(getEditText("Name", category.getName(), inflater, layout));
            layout.addView(getEditText("Description", category.getDescription(), inflater, layout));
            layout.addView(getSearchOption(
                    "Major Category",
                    category.getMajorCategory(),
                    MajorCategory.class,
                    "getName",
                    MajorCategoryListFragment.class,
                    inflater, layout));

            Helper.setTitle(getActivity(), R.string.nav_category);
        } else if (classname.equals(Pictogram.class.getName())) {
            Pictogram pictogram = bundle.getParcelable(classname);

            layout.addView(getHidden("Id", pictogram.getId()));
            layout.addView(getImage("Image", pictogram.getImagePath(), inflater, layout, root));
            layout.addView(getEditText("Name", pictogram.getName(), inflater, layout));
            layout.addView(getEditText("Description", pictogram.getDescription(), inflater, layout));
            layout.addView(getSearchOption(
                    "Category",
                    pictogram.getCategory(),
                    Category.class,
                    "getFullName",
                    MajorCategoryListFragment.class,
                    inflater, layout));

            Helper.setTitle(getActivity(), R.string.nav_pictogram);
        }
    }

    private View getEditText(String label, String defaultText, LayoutInflater inflater, ViewGroup parent) {
        return _getEditTextWithType(label, defaultText, inflater, parent, false);
    }

    private View getPasswordText(String label, String defaultText, LayoutInflater inflater, ViewGroup parent) {
        return _getEditTextWithType(label, defaultText, inflater, parent, true);
    }

    private View _getEditTextWithType(String label, String defaultText, LayoutInflater inflater, ViewGroup parent, boolean isPasswordField) {
        View v = inflater.inflate(R.layout.item_edit_text, parent, false);
        TextView labelText = (TextView) v.findViewById(R.id.label);
        EditText editText = (EditText) v.findViewById(R.id.text);

        v.setTag(label);
        labelText.setText(getResourceIdForLabel(label));
        editText.setText(defaultText);

        if (isPasswordField) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        dynamicViews.put(label, v);
        lastAddedEditText = editText;

        return v;
    }

    private int getResourceIdForLabel(String label) {
        switch (label) {
            case "Name":
                return R.string.name;
            case "Password":
                return R.string.password;
            case "Major Category":
                return R.string.majorcategory;
            case "Category":
                return R.string.category;
            case "Description":
                return R.string.description;
            case "Phone number":
                return R.string.phonenumber;
            case "Role":
                return R.string.role;
            case "Language":
                return R.string.language;
            case "Type of message":
                return R.string.message_type;
            default:
                return 0;
        }
    }

    private View getHidden(String label, Object value) {
        View v = new View(getActivity());
        v.setTag(value);
        v.setVisibility(View.GONE);

        dynamicViews.put(label, v);

        return v;
    }

    private View getImage(String label, String path, LayoutInflater inflater, ViewGroup parent, View root) {
        View v = inflater.inflate(R.layout.item_edit_image, parent, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.image);

        v.setTag(label);

        //imageView.setImageBitmap(Helper.getImage(getActivity(), path));

        Uri uri = Uri.parse(path == null ? "" : path);
        imageView.setImageURI(uri);

        Drawable bm = imageView.getDrawable();
        if (bm == null) {
            imageView.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.contact));
        }

        imageView.setTag(path);

        View.OnClickListener changeImage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Has a camera? Ask what to do.
                if (Helper.hasCamera(getActivity())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder
                            .setTitle(R.string.dialog_edit_image_title)
                            .setItems(R.array.dialog_choices_array, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        takePicture();
                                    } else {
                                        goToGallery();
                                    }
                                }
                            });
                    builder.create().show();
                } else {
                    // No camera, so go to gallery
                    goToGallery();
                }
            }
        };

        RelativeLayout contextMenu = (RelativeLayout) root.findViewById(R.id.context_menu);
        contextMenu.setVisibility(View.VISIBLE);

        Button changeButton = (Button) contextMenu.findViewById(R.id.c_menu_change);
        changeButton.setOnClickListener(changeImage);
        imageView.setOnClickListener(changeImage);

        dynamicViews.put(label, v);

        return v;
    }

    private void goToGallery() {
        // Pick a picture from the gallery
        Intent intent = new Intent();

        // Show only images
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imagePath != null) {
            File toDelete = new File(imagePath);
            toDelete.delete();
            imagePath = null;
        }

        // Make sure there is an app to handle the camera.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File file = null;

            try {
                file = Helper.createImageFile(getActivity());
            } catch(IOException e) {
                e.printStackTrace();
            }

            // Was the file successfully created?
            if (file != null) {
                imagePath = file.getAbsolutePath();
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "thomasmore.be.travelcommunicationassistant", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        }
    }

    private <T extends Enum<T>> View getSpinner(String label, T enumType, LayoutInflater inflater, ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_edit_spinner, parent, false);
        TextView labelText = (TextView) v.findViewById(R.id.label);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);

        v.setTag(label);
        labelText.setText(getResourceIdForLabel(label));

        ArrayList<String> list = Helper.enumToList(enumType.getClass());
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(enumType.ordinal());

        dynamicViews.put(label, v);

        return v;
    }

    private <T> View getSearchOption(
            String label,
            T value,
            final Class<T> type,
            String methodName,
            final Class<?> fragmentType,
            LayoutInflater inflater,
            ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_edit_search, parent, false);
        TextView labelText = (TextView) v.findViewById(R.id.label);
        EditText editText = (EditText) v.findViewById(R.id.text);
        String defaultText = "";

        View.OnClickListener searchAction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackActivity.class);
                intent.putExtra(BackActivity.DATA_STRING, fragmentType);
                intent.putExtra(Helper.EXTRA_SEARCH_INTENT, type);
                startActivityForResult(intent, REQUEST_SEARCHBUTTON);
            }
        };

        try {
            defaultText = (String) type.getMethod(methodName).invoke(value);
        } catch (Exception e) {}

        labelText.setText(getResourceIdForLabel(label));
        editText.setText(defaultText);
        editText.setOnClickListener(searchAction);

        ImageButton searchButton = (ImageButton) v.findViewById(R.id.search);
        searchButton.setOnClickListener(searchAction);

        v.setTag(value);

        dynamicViews.put(label, v);

        return v;
    }

    private Intent getIntent() {
        Intent intent = new Intent();
        intent.putExtra(CLASSNAME, classname);

        if (classname.equals(Room.class.getName())) {
            intent.putExtra(classname, getRoomData());
        } else if (classname.equals(Contact.class.getName())) {
            intent.putExtra(classname, getContactData());
        } else if (classname.equals(Category.class.getName())) {
            intent.putExtra(classname, getCategoryData());
        } else if (classname.equals(Pictogram.class.getName())) {
            intent.putExtra(classname, getPictogramData());
        }


        return intent;
    }

    private Room getRoomData() {
        Room room = new Room();

        Database db = Database.getInstance(getActivity());
        long userid = db.getSettings().getLoggedInUser(getActivity()).getId();

        room.setId((Long)getHiddenValue("Id"));
        room.setUserId(userid);
        room.setName(getTextFromEdit("Name"));
        room.setPassword(getTextFromEdit("Password"));
        room.setAvailableRoom(false);

        return room;
    }

    private Contact getContactData() {
        Contact contact = new Contact();

        contact.setId((Long)getHiddenValue("Id"));
        contact.setName(getTextFromEdit("Name"));
        contact.setPhonenumber(getTextFromEdit("Phone number"));
        contact.setImagePath(getPathFromImage());

        if (isIt) {
            contact.setLanguage(Language.valueOf(getStringFromSpinner("Language")));
            contact.setMessageType(MessageType.valueOf(getStringFromSpinner("Type of message")));
        } else {
            contact.setType(ContactType.valueOf(getStringFromSpinner("Role")));
        }

        return contact;
    }

    private User getUserData() {
        User user = new User();

        user.setId((Long)getHiddenValue("Id"));
        user.setUsername(getTextFromEdit("Name"));
        user.setPhonenumber(getTextFromEdit("Phone number"));
        user.setPassword(getTextFromEdit("Password"));
        user.setLanguage(Language.valueOf(getStringFromSpinner("Language")));
        user.setImagePath(getPathFromImage());

        return user;
    }

    private Category getCategoryData() {
        Category category = new Category();

        category.setId((Long)getHiddenValue("Id"));
        category.setName(getTextFromEdit("Name"));
        category.setMajorCategory((MajorCategory)getValueFromSearch("Major Category"));
        category.setDescription(getTextFromEdit("Description"));
        category.setImagePath(getPathFromImage());

        return category;
    }

    private Pictogram getPictogramData() {
        Pictogram pictogram = new Pictogram();

        pictogram.setId((Long)getHiddenValue("Id"));
        pictogram.setName(getTextFromEdit("Name"));
        pictogram.setDescription(getTextFromEdit("Description"));
        pictogram.setImagePath(getPathFromImage());

        return pictogram;
    }

    private String getTextFromEdit(String label) {
        LinearLayout layout = (LinearLayout)dynamicViews.get(label);
        EditText text = (EditText) layout.findViewById(R.id.text);
        return text.getText().toString();
    }

    private Object getHiddenValue(String label) {
        View v = dynamicViews.get(label);
        return v.getTag();
    }

    private String getStringFromSpinner(String label) {
        LinearLayout layout = (LinearLayout)dynamicViews.get(label);
        Spinner spinner = (Spinner) layout.findViewById(R.id.spinner);
        return (String)spinner.getSelectedItem();
    }

    private <T> T getValueFromSearch(String label) {
        View v = dynamicViews.get(label);
        return (T) v.getTag();
    }

    private String getPathFromImage() {
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);
        String path = (String)imageView.getTag();
        return path == null ? "" : path;
    }
}
