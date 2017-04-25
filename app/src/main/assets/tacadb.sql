CREATE TABLE "Contact" (
    "id" INTEGER  NOT NULL ,
    "name" TEXT  NOT NULL ,
    "phonenumber" TEXT  NOT NULL ,
    "imagePath" TEXT  NULL ,
    "messageType" INTEGER  NULL ,
    "language" INTEGER  NULL ,
    "roomSettingsId" INTEGER  NULL ,
    "userId" INTEGER  NOT NULL ,
    "type" INTEGER  NOT NULL ,
    CONSTRAINT "pk_Contact" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "ContactList" (
    "id" INTEGER  NOT NULL ,
    "ownerId" INTEGER  NOT NULL ,
    "contactId" INTEGER  NOT NULL ,
    CONSTRAINT "pk_ContactList" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "Pictogram" (
    "id" INTEGER  NOT NULL ,
    "categoryId" INTEGER  NOT NULL ,
    "name" TEXT  NOT NULL ,
    "description" TEXT  NOT NULL ,
    "imagePath" TEXT  NOT NULL ,
    CONSTRAINT "pk_Pictogram" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "Category" (
    "id" INTEGER  NOT NULL ,
    "magorCategoryId" INTEGER  NOT NULL ,
    "name" TEXT  NOT NULL ,
    "description" TEXT  NOT NULL ,
    "imagePath" TEXT  NOT NULL ,
    CONSTRAINT "pk_Category" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "MajorCategory" (
    "id" INTEGER  NOT NULL ,
    "name" TEXT  NOT NULL ,
    CONSTRAINT "pk_MajorCategory" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "WardedPictogram" (
    "id" INTEGER  NOT NULL ,
    "wardedId" INTEGER  NOT NULL ,
    "pictogramId" INTEGER  NOT NULL ,
    CONSTRAINT "pk_WardedPictogram" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "QuickMessage" (
    "id" INTEGER  NOT NULL ,
    "wardedId" INTEGER  NOT NULL ,
    CONSTRAINT "pk_QuickMessage" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "QMMessage" (
    "id" INTEGER  NOT NULL ,
    "quickMessageId" INTEGER  NOT NULL ,
    "pictogramId" INTEGER  NOT NULL ,
    CONSTRAINT "pk_QMMessage" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "User" (
    "id" INTEGER  NOT NULL ,
    "username" TEXT  NOT NULL ,
    "phonenumber" TEXT  NOT NULL ,
    "password" TEXT  NOT NULL ,
    "language" INTEGER  NOT NULL ,
    "imagePath" TEXT  NOT NULL ,
    CONSTRAINT "pk_User" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "Room" (
    "id" INTEGER  NOT NULL ,
    "name" TEXT  NOT NULL ,
    "password" TEXT  NOT NULL ,
    "creator" TEXT ,
    CONSTRAINT "pk_Room" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "Message" (
    "id" INTEGER  NOT NULL ,
    "time" INTEGER  NOT NULL ,
    "message" TEXT  NOT NULL ,
    "messageType" INTEGER  NOT NULL ,
    CONSTRAINT "pk_Message" PRIMARY KEY (
        "id"
    )
)
--NST
CREATE TABLE "Conversation" (
    "id" INTEGER  NOT NULL ,
    "contactId" INTEGER  NULL ,
    "roomId" INTEGER  NULL ,
    "messageId" INTEGER  NOT NULL ,
    CONSTRAINT "pk_Conversation" PRIMARY KEY (
        "id"
    )
)
--NST
ALTER TABLE "Contact" ADD CONSTRAINT "fk_Contact_userId" FOREIGN KEY("userId")
REFERENCES "User" ("id")
--NST
ALTER TABLE "ContactList" ADD CONSTRAINT "fk_ContactList_ownerId" FOREIGN KEY("ownerId")
REFERENCES "Contact" ("id")
--NST
ALTER TABLE "ContactList" ADD CONSTRAINT "fk_ContactList_contactId" FOREIGN KEY("contactId")
REFERENCES "Contact" ("id")
--NST
ALTER TABLE "Pictogram" ADD CONSTRAINT "fk_Pictogram_categoryId" FOREIGN KEY("categoryId")
REFERENCES "Category" ("id")
--NST
ALTER TABLE "Category" ADD CONSTRAINT "fk_Category_magorCategoryId" FOREIGN KEY("magorCategoryId")
REFERENCES "MajorCategory" ("id")
--NST
ALTER TABLE "WardedPictogram" ADD CONSTRAINT "fk_WardedPictogram_wardedId" FOREIGN KEY("wardedId")
REFERENCES "Contact" ("id")
--NST
ALTER TABLE "WardedPictogram" ADD CONSTRAINT "fk_WardedPictogram_pictogramId" FOREIGN KEY("pictogramId")
REFERENCES "Pictogram" ("id")
--NST
ALTER TABLE "QuickMessage" ADD CONSTRAINT "fk_QuickMessage_wardedId" FOREIGN KEY("wardedId")
REFERENCES "Contact" ("id")
--NST
ALTER TABLE "QMMessage" ADD CONSTRAINT "fk_QMMessage_quickMessageId" FOREIGN KEY("quickMessageId")
REFERENCES "QuickMessage" ("id")
--NST
ALTER TABLE "QMMessage" ADD CONSTRAINT "fk_QMMessage_pictogramId" FOREIGN KEY("pictogramId")
REFERENCES "Pictogram" ("id")
--NST
ALTER TABLE "Conversation" ADD CONSTRAINT "fk_Conversation_contactId" FOREIGN KEY("contactId")
REFERENCES "Contact" ("id")
--NST
ALTER TABLE "Conversation" ADD CONSTRAINT "fk_Conversation_roomId" FOREIGN KEY("roomId")
REFERENCES "Room" ("id")
--NST
ALTER TABLE "Conversation" ADD CONSTRAINT "fk_Conversation_messageId" FOREIGN KEY("messageId")
REFERENCES "Message" ("id")
--NST