package thomasmore.be.travelcommunicationassistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Category extends BaseModel<Category> implements Parcelable {
    private long id;
    private long majorCategoryId;
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

        if (majorCategory != null) {
            this.majorCategoryId = majorCategory.getId();
        }
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

    public long getMajorCategoryId() {
        return majorCategoryId;
    }

    public void setMajorCategoryId(long majorCategoryId) {
        this.majorCategoryId = majorCategoryId;
    }

    public int getLinkedPictograms() {
        if (pictograms == null) {
            return 0;
        }
        return pictograms.size();
    }

    public String getFullName() {
        String mcat = "null";
        if (majorCategory != null) {
            mcat = majorCategory.getName();
        }
        return mcat + " > " + name;
    }

    public Category(Parcel in) {
        id = in.readLong();
        majorCategory = in.readParcelable(MajorCategory.class.getClassLoader());
        name = in.readString();
        description = in.readString();
        imagePath = in.readString();

        Bundle bundle = in.readBundle(Pictogram.class.getClassLoader());
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

    // DATABASE HELPER THINGS
    public static final String ID = "id";
    public static final String MAJORCATEGORYID = "majorCategoryId";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String IMAGEPATH = "imagePath";

    public String getTable() {
        return "Category";
    }

    public String[] getColumns() {
        return new String[] {
                ID,
                NAME,
                DESCRIPTION,
                IMAGEPATH,
                MAJORCATEGORYID
        };
    }

    public Category get(Cursor cursor) {
        Category obj = new Category();

        obj.setId(cursor.getLong(0));
        obj.setName(cursor.getString(1));
        obj.setDescription(cursor.getString(2));
        obj.setImagePath(cursor.getString(3));
        obj.setMajorCategoryId(cursor.getLong(4));

        return obj;
    }

    public ContentValues getContentValues(Category cat) {
        ContentValues values = new ContentValues();

        values.put(ID, cat.getId());
        values.put(NAME, cat.getName());
        values.put(DESCRIPTION, cat.getDescription());
        values.put(IMAGEPATH, cat.getImagePath());
        values.put(MAJORCATEGORYID, cat.getMajorCategoryId());

        return values;
    }
}
