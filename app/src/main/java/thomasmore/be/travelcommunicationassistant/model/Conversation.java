package thomasmore.be.travelcommunicationassistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Conversation implements Parcelable {
    private long id;
    private Contact contact;
    private Room room;
    private Message message;

    public Conversation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Conversation(Parcel in) {
        id = in.readLong();
        contact = in.readParcelable(Contact.class.getClassLoader());
        room = in.readParcelable(Room.class.getClassLoader());
        message = in.readParcelable(Message.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(contact, 0);
        dest.writeParcelable(room, 0);
        dest.writeParcelable(message, 0);
    }

    public static final Creator<Conversation> CREATOR
            = new Creator<Conversation>() {
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

}
