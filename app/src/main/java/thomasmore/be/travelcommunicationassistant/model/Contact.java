package thomasmore.be.travelcommunicationassistant.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Contact implements Parcelable {
    private long id;
    private String name;
    private String phonenumber;
    private String imagePath;
    private ContactType type;
    private @Nullable MessageType messageType;
    private @Nullable Language language;
    private @Nullable Room currentRoom;
    private @Nullable Contact responsibleTutor;
    private @Nullable User user;
    private @Nullable ArrayList<Pictogram> pictogramSettings;
    private @Nullable ArrayList<Contact> contactList;
    private @Nullable ArrayList<QuickMessage> quickMessages;

    public Contact() {
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

    @Nullable
    public User getUser() {
        return user;
    }

    public void setUser(@Nullable User user) {
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

    public Contact(Parcel in) {
        id = in.readLong();
        name = in.readString();
        phonenumber = in.readString();
        imagePath = in.readString();
        type = ContactType.valueOf(in.readString());
        messageType = MessageType.valueOf(in.readString());
        language = Language.valueOf(in.readString());
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
        dest.writeString(type.name());
        dest.writeString(messageType.name());
        dest.writeString(language.name());
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

}
