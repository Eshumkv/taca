package thomasmore.be.travelcommunicationassistant.utils;

/**
 * Created by Eshum on 12/04/2017.
 */


public class NavigationItems<T> {
    int titleId;
    int iconId;
    Class<T> cls;

    public NavigationItems(int titleId, int imageId, Class<T> ncls) {
        this.titleId = titleId;
        this.iconId = imageId;
        this.cls = ncls;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getIconId() {
        return iconId;
    }

    public T getInstance() {
        try {
            return cls.newInstance();
        } catch (Exception e) { return null; }
    }
}