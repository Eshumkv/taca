package thomasmore.be.travelcommunicationassistant.adapter.misc;

/**
 * Created by Eshum on 12/04/2017.
 */


public class NavigationItems {
    int titleId;
    int iconId;

    public NavigationItems(int titleId, int imageId) {
        this.titleId = titleId;
        this.iconId = imageId;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getIconId() {
        return iconId;
    }
}