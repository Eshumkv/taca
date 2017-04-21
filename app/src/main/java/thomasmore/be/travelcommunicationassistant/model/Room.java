package thomasmore.be.travelcommunicationassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Room implements Parcelable {
    private long id;
    private String name;
    private String password;

    public Room() {
    }

    public Room(Parcel in) {
        id = in.readLong();
        name = in.readString();
        password = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(password);
    }

    public static final Parcelable.Creator<Room> CREATOR
            = new Parcelable.Creator<Room>() {
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

}
