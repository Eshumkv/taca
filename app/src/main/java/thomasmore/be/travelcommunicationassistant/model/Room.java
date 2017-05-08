package thomasmore.be.travelcommunicationassistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Room implements Parcelable {
    private long id;
    private String name;
    private String password;
    private @Nullable String creator;

    public Room() {
    }

    public Room(String name, String password, String creator) {
        this.name = name;
        this.password = password;
        this.creator = creator;
    }

    public Room(long id, String name, String password, String creator) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.creator = creator;
    }

    public Room(Parcel in) {
        id = in.readLong();
        name = in.readString();
        password = in.readString();
        creator = in.readString();
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return getName();
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
        dest.writeString(creator);
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
