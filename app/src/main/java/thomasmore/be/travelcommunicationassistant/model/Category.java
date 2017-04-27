package thomasmore.be.travelcommunicationassistant.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Category implements Parcelable {
    private long id;
    private MajorCategory majorCategory;
    private String name;
    private String description;
    private String imagePath;
    private ArrayList<Pictogram> pictograms;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MajorCategory getMajorCategory() {
        return majorCategory;
    }

    public void setMajorCategory(MajorCategory majorCategory) {
        this.majorCategory = majorCategory;
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

    public ArrayList<Pictogram> getPictograms() {
        return pictograms;
    }

    public void setPictograms(ArrayList<Pictogram> pictograms) {
        this.pictograms = pictograms;
    }

    public Category(Parcel in) {
        id = in.readLong();
        majorCategory = in.readParcelable(MajorCategory.class.getClassLoader());
        name = in.readString();
        description = in.readString();
        imagePath = in.readString();

        Bundle bundle = in.readBundle();

        pictograms = bundle.getParcelableArrayList(Pictogram.class.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(majorCategory, 0);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imagePath);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Pictogram.class.getName(), pictograms);
        dest.writeBundle(bundle);
    }

    public static final Creator<Category> CREATOR
            = new Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

}
