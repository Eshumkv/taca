package thomasmore.be.travelcommunicationassistant.viewmodel;

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
}
