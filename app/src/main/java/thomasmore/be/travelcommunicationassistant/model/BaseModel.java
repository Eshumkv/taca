package thomasmore.be.travelcommunicationassistant.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Eshum on 10/05/2017.
 */

public abstract class BaseModel<T> {
    public abstract String getTable();

    public abstract String[] getColumns();

    public abstract T get(Cursor cursor);

    public abstract ContentValues getContentValues(T obj);
}
