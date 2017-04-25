package thomasmore.be.travelcommunicationassistant.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.adapter.EditSpinnerAdapter;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.ContactType;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.utils.Helper;

import static android.app.Activity.RESULT_OK;

public class BasicEditFragment extends BaseFragment {

    public final static String CLASSNAME = "ClassNameForBasicEdit";

    private final static int REQUEST_GALLERY = 1;
    private final static int REQUEST_PHOTO = 2;

    private String imagePath = null;

    private String classname;
    private HashMap<String, View> dynamicViews = new HashMap<>();

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
                getActivity().setResult(Activity.RESULT_OK, getIntent());
                getActivity().finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Pick an image
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.i("INFO", uri.toString());

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(), uri);
                imageView.setImageBitmap(Helper.resizeBitmap(bitmap, 500, 500));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            Log.i("INFO", imagePath);

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);
            imageView.setImageBitmap(Helper.getImage(getActivity(), imagePath));
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

            layout.addView(getHidden("Id", contact.getId()));
            layout.addView(getImage("Image", contact.getImagePath(), inflater, layout, root));
            layout.addView(getEditText("Name", contact.getName(), inflater, layout));
            layout.addView(getEditText("Phone number", contact.getPhonenumber(), inflater, layout));
            layout.addView(getSpinner("Role", contact.getType(), inflater, layout));

            Helper.setTitle(getActivity(), R.string.nav_contacts_single);
        }
    }

    private View getEditText(String label, String defaultText, LayoutInflater inflater, ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_edit_text, parent, false);
        TextView labelText = (TextView) v.findViewById(R.id.label);
        EditText editText = (EditText) v.findViewById(R.id.text);

        v.setTag(label);
        labelText.setText(label);
        editText.setText(defaultText);

        dynamicViews.put(label, v);

        return v;
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
        imageView.setImageBitmap(Helper.getImage(getActivity(), path));
        imageView.setTag(path);

        RelativeLayout contextMenu = (RelativeLayout) root.findViewById(R.id.context_menu);
        contextMenu.setVisibility(View.VISIBLE);

        Button changeButton = (Button) contextMenu.findViewById(R.id.c_menu_change);
        changeButton.setOnClickListener(new View.OnClickListener() {
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
        });

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
        labelText.setText(label);

        ArrayList<String> list = Helper.enumToList(enumType.getClass());
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(enumType.ordinal());

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
        }

        return intent;
    }

    private Room getRoomData() {
        Room room = new Room();

        room.setId((Long)getHiddenValue("Id"));
        room.setName(getTextFromEdit("Name"));
        room.setPassword(getTextFromEdit("Password"));

        return room;
    }

    private Contact getContactData() {
        Contact contact = new Contact();

        contact.setId((Long)getHiddenValue("Id"));
        contact.setName(getTextFromEdit("Name"));
        contact.setPhonenumber(getTextFromEdit("Phone number"));
        contact.setType(ContactType.valueOf(getStringFromSpinner("Role")));

        return contact;
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
}
