package thomasmore.be.travelcommunicationassistant.utils;

/**
 * Created by Eshum on 11/04/2017.
 */


public class HomeScreenValues {
    private String text;
    private int resourceId;

    public HomeScreenValues(String t, int id) {
        this.text = t;
        this.resourceId = id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getText() {
        return text;
    }
}