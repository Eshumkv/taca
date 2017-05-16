package thomasmore.be.travelcommunicationassistant.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import thomasmore.be.travelcommunicationassistant.model.BaseModel;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.ContactType;
import thomasmore.be.travelcommunicationassistant.model.Language;
import thomasmore.be.travelcommunicationassistant.model.MessageType;
import thomasmore.be.travelcommunicationassistant.model.Room;
import thomasmore.be.travelcommunicationassistant.model.Settings;
import thomasmore.be.travelcommunicationassistant.model.User;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tacadb";
    private static final int DATABASE_VERSION = 12;

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
    public <T extends BaseModel> ArrayList<T> getAll(Class<T> type) {
        SQLiteDatabase db = this.getReadableDatabase();
        T useless = Helper.NewInstanceOf(type);

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

        ArrayList<T> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                T obj = (T)useless.get(cursor);

                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<Room> getAllRooms(long userId, boolean isAvailableRooms) {
        SQLiteDatabase db = this.getReadableDatabase();
        Room useless = new Room();

        Cursor cursor = db.query(
                useless.getTable(),                        // Table
                useless.getColumns(),                      // Columns
                where(Room.USERID, Room.ISAVAILABLEROOM), // Where
                new String[] {                              // Where-params
                        String.valueOf(userId),
                        String.valueOf(isAvailableRooms ? 1 : 0)
                },
                null,                                   // Group By
                null,                                   // Having
                asc(Room.NAME),                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Room> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Room obj = useless.get(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public boolean roomIsUnique(Room room) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                room.getTable(),                           // Table
                room.getColumns(),
                where(Room.NAME, Room.CREATOR_DB),                               // Where
                new String[] {
                        room.getName(),
                        room.getCreator()
                },    // Where-params
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        Room obj = null;
        if (cursor.moveToFirst()) {
            obj = room.get(cursor);
        }

        cursor.close();
        db.close();

        return obj == null || obj.getId() == room.getId();
    }

    public boolean contactIsUnique(Contact contact) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                contact.getTable(),                           // Table
                contact.getColumns(),
                where(Contact.PHONENUMBER),                               // Where
                new String[] {
                        contact.getPhonenumber()
                },    // Where-params
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        Contact obj = null;
        if (cursor.moveToFirst()) {
            obj = contact.get(cursor);
        }

        cursor.close();
        db.close();

        return obj == null || obj.getId() == contact.getId();
    }

    public <T extends BaseModel> long genericInsert(Class<T> type, T obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = obj.getContentValues(obj);

        values.remove("id");
        long id = db.insert(obj.getTable(), null, values);
        db.close();

        return id;
    }

    public <T extends BaseModel> boolean genericUpdate(Class<T> type, T obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.update(
                obj.getTable(),
                obj.getContentValues(obj),
                "id = ?",
                new String[] { String.valueOf(obj.getId()) }
        );
        db.close();

        return numrows > 0;
    }

    public <T extends BaseModel> boolean genericDelete(Class<T> type, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        T useless = Helper.NewInstanceOf(type);

        int numrows = db.delete(
                useless.getTable(),
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
            obj = (T)obj.get(cursor);
        }

        cursor.close();
        db.close();

        return obj;
    }

    public Settings getSettings() {
        return get(Settings.class, 1);
    }

    public Contact getContact(long id) {
        Contact contact = get(Contact.class, id);
        contact.setResponsibleTutor(get(Contact.class, contact.getResponsibleTutorId()));
        contact.setUser(get(User.class, contact.getUserId()));
        contact.setCurrentRoom(get(Room.class, contact.getCurrentRoomId()));

        return contact;
    }

    public List<Contact> getContactsFor(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Contact useless = new Contact();

        Cursor cursor = db.query(
                "ContactList",                        // Table
                new String[] {
                        "id",
                        "ownerId",
                        "contactId"
                },                      // Columns
                where("ownerId"), // Where
                new String[] {                              // Where-params
                        String.valueOf(id)
                },
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Contact> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Contact contact = get(Contact.class, cursor.getLong(2));
                list.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public long addContactToContactListFor(long id, Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ownerId", id);
        values.put("contactId", contact.getId());

        long retid = db.insert("ContactList", null, values);
        db.close();

        return retid;
    }

    /****
     *
     * HELPER METHODS
     *
     */
    private String where(String... columns) {
        StringBuilder sb = new StringBuilder();

        if (columns.length == 0) {
            return null;
        }

        if (columns.length == 1) {
            return columns[0] + " = ?";
        }

        String and = " AND ";

        for (String column : columns) {
            sb.append(column).append(" = ?").append(and);
        }

        sb.delete(sb.length() - and.length(), sb.length());

        return sb.toString();
    }

    private String desc(String column) {
        return column + " DESC";
    }

    private String asc(String column) {
        return column + " ASC";
    }

    private void insertAllTestData(SQLiteDatabase db) {

        //================================
        //      USERS
        //================================
        ArrayList<User> users = getUsers();
        for (User user : users) {
            ContentValues values = user.getContentValues(user);

            values.remove("id");
            user.setId(db.insert(user.getTable(), null, values));
        }

        //================================
        //      ROOMS
        //================================
        ArrayList<Room> rooms = getRooms();
        for (Room room : rooms) {
            if (!room.isAvailableRoom()) {
                room.setCreator(users.get(0).getUsername());
                room.setCreaterPhonenumber(users.get(0).getPhonenumber());
            }

            ContentValues values = room.getContentValues(room);

            values.remove("id");
            db.insert(room.getTable(), null, values);
        }

        //================================
        //      SETTINGS
        //================================
        Settings settings = new Settings();
        settings.setId(1);
        settings.setUserId(users.get(0).getId());
        db.insert(settings.getTable(), null, settings.getContentValues(settings));

        //================================
        //      CONTACTS
        //================================
        ArrayList<Contact> contacts = getContacts(users.get(0), rooms.get(0));
        for (Contact contact : contacts) {
            ContentValues values = contact.getContentValues(contact);

            values.remove("id");
            db.insert(contact.getTable(), null, values);
        }
    }

    private ArrayList<User> getUsers() {
        ArrayList<User> list = new ArrayList<>();

        User u = new User();
        u.setUsername("Ivan");
        u.setPassword("test");
        u.setPhonenumber("+7999856235");
        u.setLanguage(Language.English);
        list.add(u);

        u = new User();
        u.setUsername("Anna");
        u.setPassword("test");
        u.setPhonenumber("+7999856235");
        u.setLanguage(Language.Russian);
        list.add(u);

        return list;
    }

    private ArrayList<Room> getRooms() {
        ArrayList<Room> list = new ArrayList<>();

        Room room = new Room();
        room.setName("Test room 1");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(false);
        list.add(room);

        room = new Room();
        room.setName("Anna's room");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(false);
        list.add(room);

        room = new Room();
        room.setName("Gorki");
        room.setPassword("test");
        room.setUserId(1);
        room.setAvailableRoom(true);
        room.setCreator("Mia");
        room.setCreaterPhonenumber("123456789");
        list.add(room);

        return list;
    }

    private ArrayList<Contact> getContacts(User user, Room room) {
        ArrayList<Contact> list = new ArrayList<>();

        Contact tutor = new Contact();
        tutor.setName("Andrey");
        tutor.setPhonenumber("+7225352256");
        tutor.setType(ContactType.Tutor);
        tutor.setUser(user);
        list.add(tutor);

        Contact contact = new Contact();
        contact.setName("Alice");
        contact.setPhonenumber("+72258522256");
        contact.setType(ContactType.Warded);
        contact.setResponsibleTutor(tutor);
        contact.setMessageType(MessageType.Pictogram);
        contact.setCurrentRoom(room);
        contact.setLanguage(Language.Russian);
        contact.setUser(user);
        list.add(contact);

        contact = new Contact();
        contact.setName("Alexander");
        contact.setPhonenumber("+799562584");
        contact.setType(ContactType.Warded);
        contact.setResponsibleTutor(tutor);
        contact.setMessageType(MessageType.Text);
        contact.setCurrentRoom(room);
        contact.setLanguage(Language.English);
        contact.setUser(user);
        list.add(contact);

        contact = new Contact();
        contact.setName("Zoey");
        contact.setPhonenumber("+7589652365");
        contact.setType(ContactType.Warded);
        contact.setResponsibleTutor(tutor);
        contact.setMessageType(MessageType.Text);
        contact.setCurrentRoom(room);
        contact.setLanguage(Language.Dutch);
        contact.setUser(user);
        list.add(contact);

        tutor = new Contact();
        tutor.setName("Svetlana");
        tutor.setPhonenumber("+785465258");
        tutor.setType(ContactType.Tutor);
        tutor.setUser(user);
        list.add(tutor);

        contact = new Contact();
        contact.setName("Vlad");
        contact.setPhonenumber("+745213895");
        contact.setType(ContactType.Warded);
        contact.setResponsibleTutor(tutor);
        contact.setMessageType(MessageType.Pictogram);
        contact.setCurrentRoom(room);
        contact.setLanguage(Language.Russian);
        contact.setUser(user);
        list.add(contact);

        contact = new Contact();
        contact.setName("John");
        contact.setPhonenumber("+784451668");
        contact.setType(ContactType.Warded);
        contact.setResponsibleTutor(tutor);
        contact.setMessageType(MessageType.Text);
        contact.setCurrentRoom(room);
        contact.setLanguage(Language.English);
        contact.setUser(user);
        list.add(contact);

        return list;
    }
}
