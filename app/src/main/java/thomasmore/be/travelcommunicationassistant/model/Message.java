package thomasmore.be.travelcommunicationassistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Date;

import thomasmore.be.travelcommunicationassistant.utils.Helper;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Message extends BaseModel<Message> implements Parcelable {
    private long id;
    private Date time;
    private String message;
    private MessageType messageType;
    private long toContactId;
    private long fromUserId;
    private Long roomId;

    public Message() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public long getToContactId() {
        return toContactId;
    }

    public void setToContactId(long toContactId) {
        this.toContactId = toContactId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Message(Parcel in) {
        id = in.readLong();
        time = new Date(in.readLong());
        message = in.readString();
        messageType = MessageType.valueOf(in.readString());
        toContactId = in.readLong();
        fromUserId = in.readLong();
        roomId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(time.getTime());
        dest.writeString(message);
        dest.writeString(messageType.name());
        dest.writeLong(toContactId);
        dest.writeLong(fromUserId);
        dest.writeLong(roomId);
    }

    public static final Creator<Message> CREATOR
            = new Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    // DATABASE HELPER THINGS
    public static final String ID = "id";
    public static final String TIME = "time";
    public static final String MESSAGE = "message";
    public static final String MESSAGETYPE = "messageType";

    public String getTable() {
        return "Message";
    }

    public String[] getColumns() {
        return new String[] {
                ID,
                TIME,
                MESSAGE,
                MESSAGETYPE
        };
    }

    public Message get(Cursor cursor) {
        Message obj = new Message();

        obj.setId(cursor.getLong(0));
        obj.setTime(Helper.getDateFromDB(cursor, 1));
        obj.setMessage(cursor.getString(2));
        obj.setMessageType(MessageType.values()[cursor.getInt(3)]);

        return obj;
    }

    public ContentValues getContentValues(Message msg) {
        ContentValues values = new ContentValues();

        values.put(ID, msg.getId());
        values.put(TIME, Helper.getDateForDB(msg.getTime()));
        values.put(MESSAGE, msg.getMessage());
        values.put(MESSAGETYPE, msg.getMessageType().ordinal());

        return values;
    }
}
