package thomasmore.be.travelcommunicationassistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by Eshum on 18/04/2017.
 */

public class Pictogram extends BaseModel<Pictogram> implements Parcelable {
    private long id;
    private long categoryId;
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

        if (category != null) {
            this.categoryId = category.getId();
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public Pictogram(Parcel in) {
        id = in.readLong();
        categoryId = in.readLong();
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
        dest.writeLong(categoryId);
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

    // DATABASE HELPER THINGS
    public static final String ID = "id";
    public static final String CATEGORYID = "categoryId";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String IMAGEPATH = "imagePath";

    public String getTable() {
        return "Pictogram";
    }

    public String[] getColumns() {
        return new String[] {
                ID,
                NAME,
                DESCRIPTION,
                IMAGEPATH,
                CATEGORYID
        };
    }

    public Pictogram get(Cursor cursor) {
        Pictogram obj = new Pictogram();

        obj.setId(cursor.getLong(0));
        obj.setName(cursor.getString(1));
        obj.setDescription(cursor.getString(2));
        obj.setImagePath(cursor.getString(3));
        obj.setCategoryId(cursor.getLong(4));

        return obj;
    }

    public ContentValues getContentValues(Pictogram pictogram) {
        ContentValues values = new ContentValues();

        values.put(ID, pictogram.getId());
        values.put(NAME, pictogram.getName());
        values.put(DESCRIPTION, pictogram.getDescription());
        values.put(IMAGEPATH, pictogram.getImagePath());
        values.put(CATEGORYID, pictogram.getCategoryId());

        return values;
    }
}
