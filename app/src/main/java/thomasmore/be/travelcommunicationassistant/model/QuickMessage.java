package thomasmore.be.travelcommunicationassistant.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Eshum on 18/04/2017.
 */

public class QuickMessage implements Parcelable {
    private long id;
    private ArrayList<Pictogram> message;

    public QuickMessage() {
    }

    public QuickMessage(Parcel in) {
        id = in.readLong();

        Bundle bundle = in.readBundle();
        message = bundle.getParcelableArrayList(Pictogram.class.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Pictogram.class.getName(), message);
        dest.writeBundle(bundle);
    }

    public static final Creator<QuickMessage> CREATOR
            = new Creator<QuickMessage>() {
        public QuickMessage createFromParcel(Parcel in) {
            return new QuickMessage(in);
        }

        public QuickMessage[] newArray(int size) {
            return new QuickMessage[size];
        }
    };

}
