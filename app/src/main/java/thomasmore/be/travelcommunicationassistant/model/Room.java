package thomasmore.be.travelcommunicationassistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import thomasmore.be.travelcommunicationassistant.utils.Helper;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Room extends BaseModel<Room> implements Parcelable {
    private long id;
    private String name;
    private String password;
    private long userId;
    private boolean isAvailableRoom;
    private @Nullable String creator;
    private @Nullable String createrPhonenumber;

    public Room() {
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isAvailableRoom() {
        return isAvailableRoom;
    }

    public void setAvailableRoom(boolean availableRoom) {
        isAvailableRoom = availableRoom;
    }

    @Nullable
    public String getCreaterPhonenumber() {
        return createrPhonenumber;
    }

    public void setCreaterPhonenumber(@Nullable String createrPhonenumber) {
        this.createrPhonenumber = createrPhonenumber;
    }

    public Room(Parcel in) {
        id = in.readLong();
        name = in.readString();
        password = in.readString();
        userId = in.readLong();
        creator = in.readString();
        isAvailableRoom = Helper.getParcelableBool(in.readByte());
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
        dest.writeLong(userId);
        dest.writeString(creator);
        dest.writeByte(Helper.parcelableBool(isAvailableRoom));
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

    // DATABASE HELPER THINGS
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String USERID = "userId";
    public static final String CREATOR_DB = "creator";
    public static final String CREATOR_PHONE = "creatorPhonenumber";
    public static final String ISAVAILABLEROOM = "isAvailableRoom";


    public String getTable() {
        return "Room";
    }

    public String[] getColumns() {
        return new String[] {
                ID,
                NAME,
                PASSWORD,
                USERID,
                CREATOR_DB,
                ISAVAILABLEROOM,
                CREATOR_PHONE
        };
    }

    public Room get(Cursor cursor) {
        Room obj = new Room();

        obj.setId(cursor.getLong(0));
        obj.setName(cursor.getString(1));
        obj.setPassword(cursor.getString(2));
        obj.setUserId(cursor.getLong(3));
        obj.setCreator(cursor.getString(4));
        obj.setAvailableRoom(cursor.getInt(5) != 0);
        obj.setCreaterPhonenumber(cursor.getString(6));

        return obj;
    }

    public ContentValues getContentValues(Room room) {
        ContentValues values = new ContentValues();

        values.put(Room.ID, room.getId());
        values.put(Room.NAME, room.getName());
        values.put(Room.PASSWORD, room.getPassword());
        values.put(Room.USERID, room.getUserId());
        values.put(Room.CREATOR_DB, room.getCreator());
        values.put(Room.ISAVAILABLEROOM, room.isAvailableRoom());
        values.put(Room.CREATOR_PHONE, room.getCreaterPhonenumber());

        return values;
    }
}
