package thomasmore.be.travelcommunicationassistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Pictogram implements Parcelable {
    private long id;
    private Category category;
    private String name;
    private String description;
    private String imagePath;

    public Pictogram() {
    }

    public Pictogram(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Pictogram(Parcel in) {
        id = in.readLong();
        category = in.readParcelable(Category.class.getClassLoader());
        name = in.readString();
        description = in.readString();
        imagePath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(category, 0);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imagePath);
    }

    public static final Creator<Pictogram> CREATOR
            = new Creator<Pictogram>() {
        public Pictogram createFromParcel(Parcel in) {
            return new Pictogram(in);
        }

        public Pictogram[] newArray(int size) {
            return new Pictogram[size];
        }
    };

}
