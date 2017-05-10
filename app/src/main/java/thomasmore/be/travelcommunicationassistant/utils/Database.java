package thomasmore.be.travelcommunicationassistant.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;

import thomasmore.be.travelcommunicationassistant.model.BaseModel;
import thomasmore.be.travelcommunicationassistant.model.Language;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.model.Settings;
import thomasmore.be.travelcommunicationassistant.model.User;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tacadb";
    private static final int DATABASE_VERSION = 9;

    public static final String CONTACT = "Contact";
    public static final String CONTACTLIST = "ContactList";
    public static final String PICTOGRAM = "Pictogram";
    public static final String CATEGORY = "Category";
    public static final String MAJORCATEGORY = "MajorCategory";
    public static final String WARDEDPICTOGRAM = "WardedPictogram";
    public static final String QUICKMESSAGE = "QuickMessage";
    public static final String QUICKMESSAGEMESSAGE = "QMMessage";
    public static final String ROOM = "Room";
    public static final String MESSAGE = "Message";
    public static final String CONVERSATION = "Conversation";

    private static Database instance;

    private Context context;

    private Database(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = ctx.getApplicationContext();
    }

    public synchronized static Database getInstance(Context ctx) {
        if (instance == null) {
            instance = new Database(ctx.getApplicationContext());
        }

        return instance;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            for (String query : Helper.readSqlFromAssets(this.context, "tacadb.sql")) {
                db.execSQL(query);
            }
        } catch (Exception e) {
            Log.e("SQL", "Could not create database.", e);
        }

        insertAllTestData(db);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        reset(db);
    }

    public void reset() {
        reset(this.getWritableDatabase());
    }

    public void reset(SQLiteDatabase db) {
        // Drop all tables
        try {
            for (String query : Helper.readSqlFromAssets(this.context, "tacadb_drop.sql")) {
                db.execSQL(query);
            }
        } catch (Exception e) {
            Log.e("SQL", "Could not create database.", e);
        }

        // Create tables again
        onCreate(db);
    }

    /****
     *
     * QUERY METHODS
     *
     */
    public ArrayList<User> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        User useless = new User();

        Cursor cursor = db.query(
                useless.getTable(),                        // Table
                useless.getColumns(),                      // Columns
                null,                                   // Where
                null,                                   // Where-params
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<User> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                User obj = useless.get(cursor);

                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }


    /****
     *
     * HELPER METHODS
     *
     */

    private <T> ContentValues getContentValues(T obj) {
        ContentValues values = new ContentValues();

        if (obj instanceof Room) {
            Room room = (Room) obj;

            values.put("id", room.getId());
            values.put("name", room.getName());
            values.put("password", room.getPassword());
        } else if (obj instanceof User) {
            User user = (User) obj;

            values.put(User.ID, user.getId());
            values.put(User.USERNAME, user.getUsername());
            values.put(User.PHONENUMBER, user.getPhonenumber());
            values.put(User.PASSWORD, user.getPassword());
            values.put(User.LANGUAGE, user.getLanguage().ordinal());
            values.put(User.IMAGEPATH, user.getImagePath());
        } else if (obj instanceof Settings) {
            Settings settings = (Settings) obj;

            values.put(Settings.ID, settings.getId());
            values.put(Settings.USERID, settings.getUserId());
        }

        return values;
    }

    private <T> long getId(T obj) {
        if (obj instanceof Room) {
            return ((Room)obj).getId();
        } else if (obj instanceof User) {
            return ((User)obj).getId();
        }

        throw new RuntimeException("Could not get id for given object!");
    }

    public <T extends BaseModel> long genericInsert(Class<T> type, T obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(obj);

        T useless = Helper.NewInstanceOf(type);

        values.remove("id");
        long id = db.insert(useless.getTable(), null, values);
        db.close();

        return id;
    }

    public <T extends BaseModel> boolean genericUpdate(Class<T> type, T obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        T useless = Helper.NewInstanceOf(type);

        int numrows = db.update(
                useless.getTable(),
                getContentValues(obj),
                "id = ?",
                new String[] { String.valueOf(getId(obj)) }
        );
        db.close();

        return numrows > 0;
    }

    private boolean genericDelete(String table, long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                table,
                "id = ?",
                new String[] { String.valueOf(id) });
        db.close();

        return numrows > 0;
    }

    public <T extends BaseModel> T get(Class<T> type, long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        T obj = Helper.NewInstanceOf(type);

        Cursor cursor = db.query(
                obj.getTable(),                           // Table
                obj.getColumns(),
                "id = ?",                               // Where
                new String[] { String.valueOf(id) },    // Where-params
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        if (cursor.moveToFirst()) {
            obj = obj.get(cursor);
        }

        cursor.close();
        db.close();

        return obj;
    }

    public Settings getSettings() {
        return get(Settings.class, 1);
    }

    private void insertAllTestData(SQLiteDatabase db) {

        //================================
        //      USERS
        //================================
        ArrayList<User> users = getUsers();
        for (User user : users) {
            ContentValues values = getContentValues(user);

            values.remove("id");
            user.setId(db.insert(user.getTable(), null, values));
        }

        //================================
        //      SETTINGS
        //================================
        Settings settings = new Settings();
        settings.setId(1);
        settings.setUserId(users.get(0).getId());
        db.insert(settings.getTable(), null, getContentValues(settings));
    }

    private ArrayList<User> getUsers() {
        ArrayList<User> list = new ArrayList<>();

        User u = new User();
        u.setUsername("Ivan");
        u.setPassword("test");
        u.setPhonenumber("+7999856235");
        u.setLanguage(Language.English);
        u.setImagePath("NONE");
        list.add(u);

        u = new User();
        u.setUsername("Anna");
        u.setPassword("test");
        u.setPhonenumber("+7999856235");
        u.setLanguage(Language.Russian);
        u.setImagePath("NONE");
        list.add(u);

        return list;
    }
}
