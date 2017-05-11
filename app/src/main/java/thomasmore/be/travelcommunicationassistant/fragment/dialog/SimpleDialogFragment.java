package thomasmore.be.travelcommunicationassistant.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import thomasmore.be.travelcommunicationassistant.R;
import thomasmore.be.travelcommunicationassistant.model.Room;

/**
 * Created by Eshum on 24/04/2017.
 */

public class SimpleDialogFragment extends DialogFragment {
    public interface DialogListener {
        void onDialogPositiveClick(boolean correct);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    DialogListener listener;

    public SimpleDialogFragment() {}

    public static final SimpleDialogFragment newInstance(Room room, int layoutId) {
        SimpleDialogFragment dialog = new SimpleDialogFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("layoutId", layoutId);
        bundle.putParcelable("room", room);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int layout = getArguments().getInt("layoutId");
        final Room room = getArguments().getParcelable("room");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(layout, null);
        TextView roomText = (TextView) view.findViewById(R.id.room);
        final EditText passwordEdit = (EditText) view.findViewById(R.id.password);
        roomText.setText(room.getName());

        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = passwordEdit.getText().toString();
                        listener.onDialogPositiveClick(password.equals(room.getPassword()));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(SimpleDialogFragment.this);
                    }
                });

        return builder.create();
    }
}
