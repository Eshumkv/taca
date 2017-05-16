CREATE TABLE "Contact" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "name" TEXT  NOT NULL ,
    "phonenumber" TEXT  NOT NULL ,
    "imagePath" TEXT  NULL ,
    "messageType" INTEGER  NULL ,
    "language" INTEGER  NULL ,
    "currentRoomId" INTEGER  NULL ,
    "responsibleTutorId" INTEGER  NULL ,
    "userId" INTEGER  NOT NULL ,
    "type" INTEGER  NOT NULL ,
    FOREIGN KEY ("userId") REFERENCES "User"("id"),
    FOREIGN KEY ("responsibleTutorId") REFERENCES "Contact" ("id"),
    FOREIGN KEY ("currentRoomId") REFERENCES "Room"("id")
);
--NST
CREATE TABLE "ContactList" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "ownerId" INTEGER  NOT NULL ,
    "contactId" INTEGER  NOT NULL ,
    FOREIGN KEY ("ownerId") REFERENCES "Contact"("id"),
    FOREIGN KEY ("contactId") REFERENCES "Contact"("id")
);
--NST
CREATE TABLE "MajorCategory" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "name" TEXT  NOT NULL
);
--NST
CREATE TABLE "Category" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "magorCategoryId" INTEGER  NOT NULL ,
    "name" TEXT  NOT NULL ,
    "description" TEXT  NOT NULL ,
    "imagePath" TEXT  NOT NULL ,
    FOREIGN KEY ("magorCategoryId") REFERENCES "MajorCategory"("id")
);
--NST
CREATE TABLE "Pictogram" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "categoryId" INTEGER  NOT NULL ,
    "name" TEXT  NOT NULL ,
    "description" TEXT  NOT NULL ,
    "imagePath" TEXT  NOT NULL ,
    FOREIGN KEY ("categoryId") REFERENCES "Category"("id")
);
--NST
CREATE TABLE "WardedPictogram" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "wardedId" INTEGER  NOT NULL ,
    "pictogramId" INTEGER  NOT NULL ,
    FOREIGN KEY ("wardedId") REFERENCES "Contact"("id"),
    FOREIGN KEY ("pictogramId") REFERENCES "Pictogram"("id")
);
--NST
CREATE TABLE "QuickMessage" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "wardedId" INTEGER  NOT NULL ,
    FOREIGN KEY ("wardedId") REFERENCES "Contact"("id")
);
--NST
CREATE TABLE "QMMessage" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "quickMessageId" INTEGER  NOT NULL ,
    "pictogramId" INTEGER  NOT NULL ,
    FOREIGN KEY ("quickMessageId") REFERENCES "QuickMessage"("id"),
    FOREIGN KEY ("pictogramId") REFERENCES "Pictogram"("id")
);
--NST
CREATE TABLE "User" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "username" TEXT  NOT NULL ,
    "phonenumber" TEXT  NOT NULL ,
    "password" TEXT  NOT NULL ,
    "language" INTEGER  NOT NULL ,
    "imagePath" TEXT
);
--NST
CREATE TABLE "Room" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "name" TEXT  NOT NULL ,
    "password" TEXT  NOT NULL ,
    "userId" INTEGER NULL,
    "creator" TEXT,
    "isAvailableRoom" INTEGER NOT NULL,
    "creatorPhonenumber" TEXT,
    FOREIGN KEY ("userId") REFERENCES "User"("id")
);
--NST
CREATE TABLE "Message" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "time" INTEGER  NOT NULL ,
    "message" TEXT  NOT NULL ,
    "messageType" INTEGER  NOT NULL
);
--NST
CREATE TABLE "Conversation" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "contactId" INTEGER  NULL ,
    "roomId" INTEGER  NULL ,
    "messageId" INTEGER  NOT NULL ,
    FOREIGN KEY ("contactId") REFERENCES "Contact"("id"),
    FOREIGN KEY ("roomId") REFERENCES "Room"("id"),
    FOREIGN KEY ("messageId") REFERENCES "Message"("id")
);
--NST
CREATE TABLE "Settings" (
    "id" INTEGER PRIMARY KEY,
    "loggedInUser" INTEGER NULL
);