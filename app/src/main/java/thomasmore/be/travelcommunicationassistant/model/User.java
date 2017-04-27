package thomasmore.be.travelcommunicationassistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import thomasmore.be.travelcommunicationassistant.utils.Helper;

/**
 * Created by Eshum on 18/04/2017.
 */

public class User implements Parcelable {
    private long id;
    private String username;
    private String phonenumber;
    private String password;
    private Language language;
    private String imagePath;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public User(Parcel in) {
        id = in.readLong();
        username = in.readString();
        phonenumber = in.readString();
        password = in.readString();
        imagePath = in.readString();
        language = Helper.parseEnum(Language.class, in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(username);
        dest.writeString(phonenumber);
        dest.writeString(password);
        dest.writeString(imagePath);
        dest.writeString(Helper.EnumToString(language));
    }

    public static final Creator<User> CREATOR
            = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
