package thomasmore.be.travelcommunicationassistant.viewmodel;

/**
 * Created by Eshum on 16/04/2017.
 */

public class MessageSingleConversationViewModel {
    private String name;
    private String message;
    private UserType userType;

    public MessageSingleConversationViewModel(String name, String message, boolean self) {
        this.name = name;
        this.message = message;
        this.userType = self ? UserType.Self : UserType.Other;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public enum UserType {
        Self,
        Other,
    }
}
