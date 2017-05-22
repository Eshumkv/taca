package thomasmore.be.travelcommunicationassistant.viewmodel;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import thomasmore.be.travelcommunicationassistant.model.Contact;
import thomasmore.be.travelcommunicationassistant.model.Message;
import thomasmore.be.travelcommunicationassistant.utils.Database;

/**
 * Created by Eshum on 16/04/2017.
 */

public class MessagesListViewModel {
    private String contactName;
    private String messagePart;

    public MessagesListViewModel(String contact, String message) {
        this.contactName = contact;
        this.messagePart = message;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMessagePart() {
        return messagePart;
    }

    public void setMessagePart(String messagePart) {
        this.messagePart = messagePart;
    }

    public static List<MessagesListViewModel> fromMessages(List<Message> list, Context ctx) {
        ArrayList<MessagesListViewModel> ret = new ArrayList<>();

        for (Message msg : list) {
            ret.add(fromMessage(msg, ctx.getApplicationContext()));
        }

        return ret;
    }

    public static MessagesListViewModel fromMessage(Message msg, Context ctx) {
        Database db = Database.getInstance(ctx.getApplicationContext());
        Contact contact = db.getContact(msg.getToContactId());

        String name = contact.getName();
        String message = msg.getMessage();

        return new MessagesListViewModel(name, message);
    }
}
