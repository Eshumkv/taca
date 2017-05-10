package thomasmore.be.travelcommunicationassistant.model;

import android.database.Cursor;

/**
 * Created by Eshum on 10/05/2017.
 */

public abstract class BaseModel {
    public abstract String getTable();

    public abstract String[] getColumns();

    public abstract <T> T get(Cursor cursor);
}
