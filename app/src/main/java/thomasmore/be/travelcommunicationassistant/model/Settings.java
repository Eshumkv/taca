package thomasmore.be.travelcommunicationassistant.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Set;

import thomasmore.be.travelcommunicationassistant.utils.Database;

/**
 * Created by Eshum on 10/05/2017.
 */

public class Settings extends BaseModel<Settings> {
    private long id;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    // TODO: Fix this please :(
    public User getLoggedInUser(Context ctx) {
        return Database.getInstance(ctx.getApplicationContext()).get(User.class, 1);
    }

    // DATABASE HELPER THINGS
    public static final String ID = "id";
    public static final String USERID = "loggedInUser";

    public String getTable() {
        return "Settings";
    }

    public String[] getColumns() {
        return new String[] {
                ID,
                USERID
        };
    }

    public Settings get(Cursor cursor) {
        Settings obj = new Settings();

        obj.setId(cursor.getLong(0));
        obj.setUserId(cursor.getLong(1));

        return obj;
    }

    public ContentValues getContentValues(Settings settings) {
        ContentValues values = new ContentValues();

        values.put(ID, settings.getId());
        values.put(USERID, settings.getUserId());

        return values;
    }
}
