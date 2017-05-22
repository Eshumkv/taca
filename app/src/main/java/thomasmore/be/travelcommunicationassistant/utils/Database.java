package thomasmore.be.travelcommunicationassistant.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.model.BaseModel;
import thomasmore.be.travelcommunicationassistant.model.Category;
import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.ContactType;
import thomasmore.be.travelcommunicationassistant.model.Language;
import thomasmore.be.travelcommunicationassistant.model.MajorCategory;
import thomasmore.be.travelcommunicationassistant.model.Message;
import thomasmore.be.travelcommunicationassistant.model.MessageType;
import thomasmore.be.travelcommunicationassistant.model.Pictogram;
import thomasmore.be.travelcommunicationassistant.model.QuickMessage;
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

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = user.getContentValues(user);

        values.remove("id");
        long id = db.insert(user.getTable(), null, values);
        user.setId(id);

        if (id != -1) {
            Contact contact = user.toContact();
            db.insert(contact.getTable(), null, contact.getContentValues(contact));
        }

        db.close();

        return id;
    }

    public Contact getContact(long id) {
        Contact contact = get(Contact.class, id);
        contact.setResponsibleTutor(get(Contact.class, contact.getResponsibleTutorId()));
        contact.setUser(get(User.class, contact.getUserId()));
        contact.setCurrentRoom(get(Room.class, contact.getCurrentRoomId()));

        return contact;
    }

    public List<Contact> getContactsOfType(ContactType type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Contact useless = new Contact();

        Cursor cursor = db.query(
                useless.getTable(),                        // Table
                useless.getColumns(),                      // Columns
                where(Contact.TYPE), // Where
                new String[] {                              // Where-params
                        String.valueOf(type.ordinal())
                },
                null,                                   // Group By
                null,                                   // Having
                asc(Room.NAME),                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Contact> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Contact obj = useless.get(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<Contact> getContactsFor(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

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


    public List<Contact> getContactsForOfType(long id, ContactType type) {
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

                if (contact.getType() == type) {
                    list.add(contact);
                }
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

    public boolean removeContactFromContactListFor(long ownerId, long contactId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "ContactList",
                where("ownerId", "contactId"),
                new String[] { String.valueOf(ownerId), String.valueOf(contactId) });
        db.close();

        return numrows > 0;
    }

    public boolean deleteContact(long id) {
        boolean success = genericDelete(Contact.class, id);

        if (success) {
            List<Contact> contacts = getAll(Contact.class);
            for (Contact contact : contacts) {
                removeContactFromContactListFor(contact.getId(), id);
            }
        }

        return success;
    }

    public Contact getWarded(long id) {
        Contact contact = get(Contact.class, id);
        contact.setResponsibleTutor(get(Contact.class, contact.getResponsibleTutorId()));
        contact.setCurrentRoom(get(Room.class, contact.getCurrentRoomId()));

        return contact;
    }

    public boolean deleteCategory(long id) {
        boolean success = genericDelete(Category.class, id);

        if (success) {
            List<Pictogram> pictograms = getPictogramsOfCategory(id);
            for (Pictogram picto : pictograms) {
                genericDelete(Pictogram.class, picto.getId());
            }
        }

        return success;
    }

    public List<Category> getCategoriesForMajorCategory(long majorCategoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Category useless = new Category();

        Cursor cursor = db.query(
                useless.getTable(),                        // Table
                useless.getColumns(),                      // Columns
                where(Category.MAJORCATEGORYID), // Where
                new String[] {                              // Where-params
                        String.valueOf(majorCategoryId)
                },
                null,                                   // Group By
                null,                                   // Having
                asc(Category.NAME),                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Category> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Category obj = useless.get(cursor);
                List<Pictogram> pictograms = getPictogramsOfCategory(obj.getId());
                obj.setPictograms(new ArrayList<>(pictograms));
                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<Category> getCategoriesForMajorCategoryOfWarded(long majorCategoryId, long wardedId) {
        List<Pictogram> pictograms = getPictogramsOfWarded(wardedId);
        HashMap<Long, Category> map = new HashMap<>();

        for (Pictogram pictogram : pictograms) {
            Category cat = get(Category.class, pictogram.getCategoryId());

            if (cat.getMajorCategoryId() == majorCategoryId) {
                List<Pictogram> pictos = getPictogramsOfCategory(cat.getId());
                cat.setPictograms(new ArrayList<>(pictos));
                map.put(cat.getId(), cat);
            }
        }

        return new ArrayList<>(map.values());
    }

    public List<Pictogram> getPictogramsOfWarded(long wardedId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "WardedPictogram",                        // Table
                new String[] {
                        "id",
                        "wardedId",
                        "pictogramId"
                },                      // Columns
                where("wardedId"), // Where
                new String[] {                              // Where-params
                        String.valueOf(wardedId)
                },
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Pictogram> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Pictogram obj = get(Pictogram.class, cursor.getLong(2));
                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<Pictogram> getPictogramsForCategoryOfWarded(long wardedId, long categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "WardedPictogram",                        // Table
                new String[] {
                        "id",
                        "wardedId",
                        "pictogramId"
                },                      // Columns
                where("wardedId"), // Where
                new String[] {                              // Where-params
                        String.valueOf(wardedId)
                },
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Pictogram> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Pictogram obj = get(Pictogram.class, cursor.getLong(2));

                if (obj.getCategoryId() == categoryId) {
                    list.add(obj);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public long addPictogramToPictogramSettings(long wardedId, Pictogram pictogram) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("wardedId", wardedId);
        values.put("pictogramId", pictogram.getId());

        long retid = db.insert("WardedPictogram", null, values);
        db.close();

        return retid;
    }

    public boolean deletePictogramFromWarded(long wardedId, long pictogramId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "WardedPictogram",
                where("wardedId", "pictogramId"),
                new String[] { String.valueOf(wardedId), String.valueOf(pictogramId) });
        db.close();

        return numrows > 0;
    }

    public List<Pictogram> getPictogramsOfCategory(long categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Pictogram useless = new Pictogram();

        Cursor cursor = db.query(
                useless.getTable(),                        // Table
                useless.getColumns(),                      // Columns
                where(Pictogram.CATEGORYID), // Where
                new String[] {
                        String.valueOf(categoryId)
                },
                null,                                   // Group By
                null,                                   // Having
                asc(Pictogram.NAME),                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Pictogram> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Pictogram obj = useless.get(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public long addQuickMessage(QuickMessage qmessage) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Add the quick message
        ContentValues values = qmessage.getContentValues(qmessage);

        values.remove("id");
        qmessage.setId(db.insert(qmessage.getTable(), null, values));

        // Add the messages
        for (Pictogram p : qmessage.getMessage()) {
            values = new ContentValues();
            values.put("quickMessageId", qmessage.getId());
            values.put("pictogramId", p.getId());

            db.insert("QMMessage", null, values);
        }
        db.close();

        return qmessage.getId();
    }

    public boolean addPictogramToQuickMessage(Pictogram pictogram, long qmessageId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("quickMessageId", qmessageId);
        values.put("pictogramId", pictogram.getId());

        long id = db.insert("QMMessage", null, values);

        return id != -1;
    }

    public boolean removePictogramFromQuickMessage(long pictogramId, long qmessageId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "QMMessage",
                where("pictogramId", "quickMessageId"),
                new String[] { String.valueOf(pictogramId), String.valueOf(qmessageId) });
        db.close();

        return numrows > 0;
    }

    public boolean removeQuickMessage(long qmessageId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "QMMessage",
                where("quickMessageId"),
                new String[] {String.valueOf(qmessageId) });

        QuickMessage useless = new QuickMessage();
        db.delete(
            useless.getTable(),
            where(QuickMessage.ID),
            new String[] {String.valueOf(qmessageId) });

        db.close();

        return numrows > 0;
    }

    public List<QuickMessage> getQuickMessages(long wardedId) {
        List<QuickMessage> list = __getQuickMessages(wardedId);

        for (QuickMessage quickMessage : list) {
            ArrayList<Pictogram> messages =
                    new ArrayList<>(__getMessageOfQuickMessage(quickMessage.getId()));
            quickMessage.setMessage(messages);
        }

        return list;
    }

    private List<QuickMessage> __getQuickMessages(long wardedId) {
        SQLiteDatabase db = this.getReadableDatabase();
        QuickMessage useless = new QuickMessage();

        Cursor cursor = db.query(
                useless.getTable(),                        // Table
                useless.getColumns(),                      // Columns
                where(QuickMessage.WARDEDID), // Where
                new String[] {
                        String.valueOf(wardedId)
                },
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<QuickMessage> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                QuickMessage obj = useless.get(cursor);
                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    private List<Pictogram> __getMessageOfQuickMessage(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "QMMessage",                        // Table
                new String[] {
                        "quickMessageId",
                        "pictogramId"
                },                      // Columns
                where("quickMessageId"), // Where
                new String[] {
                        String.valueOf(id)
                },
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Pictogram> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Pictogram obj = get(Pictogram.class, cursor.getLong(1));
                list.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<Message> getMessages(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "Conversation",                        // Table
                new String[] {
                        "contactId",
                        "roomId",
                        "messageId",
                        "fromUserId"
                },                      // Columns
                where("fromUserId"), // Where
                new String[] {
                        String.valueOf(userId)
                },
                null,                                   // Group By
                null,                                   // Having
                null,                                   // Sorting
                null                                    // Dunno
        );

        ArrayList<Message> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Message obj = get(Message.class, cursor.getLong(2));
                obj.setToContactId(cursor.getLong(0));
                obj.setRoomId(cursor.getLong(1));
                obj.setFromUserId(cursor.getLong(3));
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
            long id = db.insert(user.getTable(), null, values);
            user.setId(id);

            if (id != -1) {
                Contact contact = user.toContact();
                db.insert(contact.getTable(), null, contact.getContentValues(contact));
            }
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
        //      TUTORS
        //================================
        ArrayList<Contact> tutors = getTutors(users.get(0));
        for (Contact contact : tutors) {
            ContentValues values = contact.getContentValues(contact);

            values.remove("id");
            contact.setId(db.insert(contact.getTable(), null, values));
        }

        //================================
        //      WARDED
        //================================
        ArrayList<Contact> wardeds = getContacts(users.get(0), rooms.get(0), tutors);
        for (Contact contact : wardeds) {
            ContentValues values = contact.getContentValues(contact);

            values.remove("id");
            contact.setId(db.insert(contact.getTable(), null, values));
        }

        ArrayList<Contact> contacts = new ArrayList<>(wardeds);
        contacts.addAll(tutors);

        //================================
        //      MAJOR CATEGORIES
        //================================
        ArrayList<MajorCategory> majorcategories = getMajorCategories();
        for (MajorCategory category : majorcategories) {
            ContentValues values = category.getContentValues(category);

            values.remove("id");
            category.setId(db.insert(category.getTable(), null, values));
        }

        //================================
        //      CATEGORIES
        //================================
        ArrayList<Category> categories = getCategories(majorcategories);
        for (Category category : categories) {
            ContentValues values = category.getContentValues(category);

            values.remove("id");
            category.setId(db.insert(category.getTable(), null, values));
        }

        //================================
        //      PICTOGRAMS
        //================================
        ArrayList<Pictogram> pictograms = getPictograms(categories);
        for (Pictogram pictogram : pictograms) {
            ContentValues values = pictogram.getContentValues(pictogram);

            values.remove("id");
            pictogram.setId(db.insert(pictogram.getTable(), null, values));
        }

        //================================
        //      QUICK MESSAGES
        //================================
        ArrayList<QuickMessage> quickMessages = getQuickMessages(pictograms, wardeds);
        for (QuickMessage qmessage : quickMessages) {
            // Add the quick message
            ContentValues values = qmessage.getContentValues(qmessage);

            values.remove("id");
            qmessage.setId(db.insert(qmessage.getTable(), null, values));

            // Add the messages
            for (Pictogram p : qmessage.getMessage()) {
                values = new ContentValues();
                values.put("quickMessageId", qmessage.getId());
                values.put("pictogramId", p.getId());

                db.insert("QMMessage", null, values);
            }
        }

        //================================
        //      MESSAGES
        //================================
        ArrayList<Message> messages = getMessages(users, contacts);
        for (Message msg : messages) {
            ContentValues values = msg.getContentValues(msg);

            values.remove("id");
            msg.setId(db.insert(msg.getTable(), null, values));

            values = new ContentValues();
            values.put("contactId", msg.getToContactId());
            values.put("fromUserId", msg.getFromUserId());
            values.put("messageId", msg.getId());
            values.put("roomId", msg.getRoomId());

            db.insert("Conversation", null, values);
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

    private ArrayList<Contact> getTutors(User user) {
        ArrayList<Contact> list = new ArrayList<>();

        Contact tutor = new Contact();
        tutor.setName("Andrey");
        tutor.setPhonenumber("+7225352256");
        tutor.setType(ContactType.Tutor);
        tutor.setUser(user);
        list.add(tutor);

        tutor = new Contact();
        tutor.setName("Svetlana");
        tutor.setPhonenumber("+785465258");
        tutor.setType(ContactType.Tutor);
        tutor.setUser(user);
        list.add(tutor);

        return list;
    }

    private ArrayList<Contact> getContacts(User user, Room room, ArrayList<Contact> tutors) {
        ArrayList<Contact> list = new ArrayList<>();

        Contact tutor = tutors.get(0);

        Contact contact = new Contact();
        contact.setName("Alice");
        contact.setPhonenumber("+722585282256");
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

        tutor = tutors.get(1);

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

    private ArrayList<MajorCategory> getMajorCategories() {
        ArrayList<MajorCategory> list = new ArrayList<>();

        MajorCategory majorCategory = new MajorCategory();
        majorCategory.setName("Verb");
        list.add(majorCategory);

        majorCategory = new MajorCategory();
        majorCategory.setName("Noun");
        list.add(majorCategory);

        return list;
    }

    private ArrayList<Category> getCategories(ArrayList<MajorCategory> mcats) {
        ArrayList<Category> list = new ArrayList<>();

        for (MajorCategory mcat : mcats) {
            for (int i = 0; i < Helper.randomBetween(1, 5); i++) {
                String letter = Helper.randomCharacter();
                String name = letter.toUpperCase() + " Category " + i;

                Category category = new Category();
                category.setMajorCategoryId(mcat.getId());
                category.setMajorCategory(mcat);
                category.setName(name);
                category.setDescription("DESCRIPTION");
                list.add(category);
            }
        }

        return list;
    }

    private ArrayList<Pictogram> getPictograms(ArrayList<Category> cats) {
        ArrayList<Pictogram> list = new ArrayList<>();

        for (Category cat : cats) {
            for (int i = 0; i < Helper.randomBetween(3, 9); i++) {
                String letter = Helper.randomCharacter();
                String name = letter.toUpperCase() + " Pictogram " + i;

                Pictogram pictogram = new Pictogram();
                pictogram.setCategory(cat);
                pictogram.setCategoryId(cat.getId());
                pictogram.setName(name);
                pictogram.setDescription("DESCRIPTION");
                list.add(pictogram);
            }
        }

        return list;
    }

    private ArrayList<QuickMessage> getQuickMessages(ArrayList<Pictogram> pictograms, ArrayList<Contact> wardeds) {
        ArrayList<QuickMessage> list = new ArrayList<>();
        int count = Helper.randomBetween(1, 3);

        for (Contact warded : wardeds) {
            for (int i = 0; i < count; i++) {
                QuickMessage qmessage = new QuickMessage();
                ArrayList<Pictogram> message = new ArrayList<>();

                for (int j = 0; j < Helper.randomBetween(2, 5); j++) {
                    int random = Helper.randomBetween(0, pictograms.size() - 1);
                    Pictogram pictogram = pictograms.get(random);
                    message.add(pictogram);
                }

                qmessage.setMessage(message);
                qmessage.setWardedId(warded.getId());

                list.add(qmessage);
            }
        }

        return list;
    }

    private ArrayList<Message> getMessages(List<User> users, List<Contact> contacts) {
        ArrayList<Message> list = new ArrayList<>();

        for (Contact contact : contacts) {
            if (Helper.randomBetween(0, 1) == 1) continue;

            int hour = Helper.randomBetween(1, 30);
            int minute = Helper.randomBetween(1, 59);

            Message message = new Message();
            message.setTime(new Date(2017, 5, 21, hour, minute));
            message.setMessage("Testing, 1, 2, 3");
            message.setMessageType(MessageType.Text);
            message.setToContactId(contact.getId());
            message.setFromUserId(users.get(0).getId());
            list.add(message);
        }

        return list;
    }
}
