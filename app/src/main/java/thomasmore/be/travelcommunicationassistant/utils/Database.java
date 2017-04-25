package thomasmore.be.travelcommunicationassistant.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import thomasmore.be.travelcommunicationassistant.model.Room;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tacadb";
    private static final int DATABASE_VERSION = 3;

    public static final String CONTACT = "Contact";
    public static final String CONTACTLIST = "ContactList";
    public static final String PICTOGRAM = "Pictogram";
    public static final String CATEGORY = "Category";
    public static final String MAJORCATEGORY = "MajorCategory";
    public static final String WARDEDPICTOGRAM = "WardedPictogram";
    public static final String QUICKMESSAGE = "QuickMessage";
    public static final String QUICKMESSAGEMESSAGE = "QMMessage";
    public static final String USER = "User";
    public static final String ROOM = "Room";
    public static final String MESSAGE = "Message";
    public static final String CONVERSATION = "Conversation";

    private static Database instance;

    private Context context;

    private Database(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = ctx;
    }

    public synchronized static Database getInstance(Context ctx) {
        if (instance == null)
            instance = new Database(ctx);
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
        reset();
    }

    public void reset() {
        SQLiteDatabase db = this.getWritableDatabase();

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
        }

        return values;
    }

    private <T> long getId(T obj) {
        if (obj instanceof Room) {
            return ((Room)obj).getId();
        }

        throw new RuntimeException("Could not get id for given object!");
    }

    private <T> String getTable(T obj) {
        if (obj instanceof Room) {
            return ROOM;
        }

        throw new RuntimeException("Could not find table name for given object!");
    }

    public <T> long genericInsert(T obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(obj);

        values.remove("id");
        long id = db.insert(getTable(obj), null, values);
        db.close();

        return id;
    }

    private <T> boolean genericUpdate(T obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.update(
                getTable(obj),
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
}
