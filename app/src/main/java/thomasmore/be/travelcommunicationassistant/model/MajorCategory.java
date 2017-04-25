package thomasmore.be.travelcommunicationassistant.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eshum on 18/04/2017.
 */

public class MajorCategory implements Parcelable {
    private long id;
    private String name;
    private ArrayList<Category> categories;

    public MajorCategory() {
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

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public MajorCategory(Parcel in) {
        id = in.readLong();
        name = in.readString();

        Bundle bundle = in.readBundle();

        categories = bundle.getParcelableArrayList(Category.class.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Category.class.getName(), categories);
        dest.writeBundle(bundle);
    }

    public static final Creator<MajorCategory> CREATOR
            = new Creator<MajorCategory>() {
        public MajorCategory createFromParcel(Parcel in) {
            return new MajorCategory(in);
        }

        public MajorCategory[] newArray(int size) {
            return new MajorCategory[size];
        }
    };

}
