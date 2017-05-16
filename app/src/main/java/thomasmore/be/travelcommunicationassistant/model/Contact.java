package thomasmore.be.travelcommunicationassistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import thomasmore.be.travelcommunicationassistant.utils.Helper;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Contact extends BaseModel<Contact> implements Parcelable {
    private long id;
    private String name;
    private String phonenumber;
    private String imagePath;
    private ContactType type;
    private @Nullable MessageType messageType;
    private @Nullable Language language;
    private @Nullable Room currentRoom;
    private @Nullable long currentRoomId;
    private @Nullable Contact responsibleTutor;
    private @Nullable long responsibleTutorId;
    private long userId;
    private User user;
    private @Nullable ArrayList<Pictogram> pictogramSettings;
    private @Nullable ArrayList<Contact> contactList;
    private @Nullable ArrayList<QuickMessage> quickMessages;

    public Contact() {
    }

    public Contact(String name, String number, boolean tutor) {
        this.name = name;
        this.phonenumber = number;
        this.type = tutor ? ContactType.Tutor : ContactType.Warded;
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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    @Nullable
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(@Nullable MessageType messageType) {
        this.messageType = messageType;
    }

    @Nullable
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(@Nullable Language language) {
        this.language = language;
    }

    @Nullable
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(@Nullable Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    @Nullable
    public Contact getResponsibleTutor() {
        return responsibleTutor;
    }

    public void setResponsibleTutor(@Nullable Contact responsibleTutor) {
        this.responsibleTutor = responsibleTutor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Nullable
    public ArrayList<Pictogram> getPictogramSettings() {
        return pictogramSettings;
    }

    public void setPictogramSettings(@Nullable ArrayList<Pictogram> pictogramSettings) {
        this.pictogramSettings = pictogramSettings;
    }

    @Nullable
    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(@Nullable ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    @Nullable
    public ArrayList<QuickMessage> getQuickMessages() {
        return quickMessages;
    }

    public void setQuickMessages(@Nullable ArrayList<QuickMessage> quickMessages) {
        this.quickMessages = quickMessages;
    }

    @Nullable
    public long getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(@Nullable long currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    @Nullable
    public long getResponsibleTutorId() {
        return responsibleTutorId;
    }

    public void setResponsibleTutorId(@Nullable long responsibleTutorId) {
        this.responsibleTutorId = responsibleTutorId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Contact(Parcel in) {
        id = in.readLong();
        name = in.readString();
        phonenumber = in.readString();
        imagePath = in.readString();
        type = Helper.parseEnum(ContactType.class, in.readString());
        messageType = Helper.parseEnum(MessageType.class, in.readString());
        language = Helper.parseEnum(Language.class, in.readString());
        currentRoom = in.readParcelable(Room.class.getClassLoader());
        responsibleTutor = in.readParcelable(Contact.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());

        Bundle bundle = in.readBundle();

        pictogramSettings = bundle.getParcelableArrayList(Pictogram.class.getName());
        contactList = bundle.getParcelableArrayList(Contact.class.getName());
        quickMessages = bundle.getParcelableArrayList(QuickMessage.class.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(phonenumber);
        dest.writeString(imagePath);
        dest.writeString(Helper.EnumToString(type));
        dest.writeString(Helper.EnumToString(messageType));
        dest.writeString(Helper.EnumToString(language));
        dest.writeParcelable(currentRoom, 0);
        dest.writeParcelable(responsibleTutor, 0);
        dest.writeParcelable(user, 0);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Pictogram.class.getName(), pictogramSettings);
        bundle.putParcelableArrayList(Contact.class.getName(), contactList);
        bundle.putParcelableArrayList(QuickMessage.class.getName(), quickMessages);

        dest.writeBundle(bundle);
    }

    public static final Creator<Contact> CREATOR
            = new Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };


    // DATABASE HELPER THINGS
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PHONENUMBER = "phonenumber";
    public static final String IMAGEPATH = "imagePath";
    public static final String MESSAGETYPE = "messageType";
    public static final String LANGUAGE = "language";
    public static final String CURRENTROOMID = "currentRoomId";
    public static final String RESPONSIBLETUTORID = "responsibleTutorId";
    public static final String USERID = "userId";
    public static final String TYPE = "type";

    public String getTable() {
        return "Contact";
    }

    public String[] getColumns() {
        return new String[] {
                ID,
                NAME,
                PHONENUMBER,
                IMAGEPATH,
                MESSAGETYPE,
                LANGUAGE,
                CURRENTROOMID,
                RESPONSIBLETUTORID,
                USERID,
                TYPE
        };
    }

    public Contact get(Cursor cursor) {
        Contact obj = new Contact();

        obj.setId(cursor.getLong(0));
        obj.setName(cursor.getString(1));
        obj.setPhonenumber(cursor.getString(2));
        obj.setImagePath(cursor.getString(3));

        obj.setUserId(cursor.getLong(8));
        obj.setType(ContactType.values()[cursor.getInt(9)]);

        if (obj.getType() == ContactType.Warded) {
            obj.setMessageType(MessageType.values()[cursor.getInt(4)]);
            obj.setLanguage(Language.values()[cursor.getInt(5)]);
            obj.setCurrentRoomId(cursor.getLong(6));
            obj.setResponsibleTutorId(cursor.getLong(7));
        }

        return obj;
    }

    public ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();

        values.put(ID, contact.getId());
        values.put(NAME, contact.getName());
        values.put(PHONENUMBER, contact.getPhonenumber());
        values.put(IMAGEPATH, contact.getImagePath());

        if (contact.getType() == ContactType.Warded) {
            if (contact.getMessageType() == null) {
                contact.setMessageType(MessageType.Pictogram);
            }

            if (contact.getLanguage() == null) {
                contact.setLanguage(Language.Russian);
            }

            Long roomId = null;
            if (contact.getCurrentRoom() != null) {
                roomId = contact.getCurrentRoom().getId();
            }

            Long tutorId = null;
            if (contact.getResponsibleTutor() != null) {
                tutorId = contact.getResponsibleTutor().getId();
            }

            values.put(MESSAGETYPE, contact.getMessageType().ordinal());
            values.put(LANGUAGE, contact.getLanguage().ordinal());
            values.put(CURRENTROOMID, roomId);
            values.put(RESPONSIBLETUTORID, tutorId);
        }

        values.put(USERID, contact.getUser().getId());
        values.put(TYPE, contact.getType().ordinal());

        return values;
    }
}
