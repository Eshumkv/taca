package thomasmore.be.travelcommunicationassistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eshum on 18/04/2017.
 */

public class QuickMessage extends BaseModel<QuickMessage> implements Parcelable {
    private long id;
    private long wardedId;
    private ArrayList<Pictogram> message;

    public QuickMessage() {
    }

    public QuickMessage(ArrayList<Pictogram> picts) {
        this.message = picts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Pictogram> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<Pictogram> message) {
        this.message = message;
    }

    public long getWardedId() {
        return wardedId;
    }

    public void setWardedId(long wardedId) {
        this.wardedId = wardedId;
    }

    public QuickMessage(Parcel in) {
        id = in.readLong();

        Bundle bundle = in.readBundle(Pictogram.class.getClassLoader());
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

    // DATABASE HELPER THINGS
    public static final String ID = "id";
    public static final String WARDEDID = "wardedId";

    public String getTable() {
        return "QuickMessage";
    }

    public String[] getColumns() {
        return new String[] {
                ID,
                WARDEDID
        };
    }

    public QuickMessage get(Cursor cursor) {
        QuickMessage obj = new QuickMessage();

        obj.setId(cursor.getLong(0));
        obj.setWardedId(cursor.getLong(1));

        return obj;
    }

    public ContentValues getContentValues(QuickMessage qmessage) {
        ContentValues values = new ContentValues();

        values.put(ID, qmessage.getId());
        values.put(WARDEDID, qmessage.getWardedId());

        return values;
    }
}
