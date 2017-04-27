package thomasmore.be.travelcommunicationassistant.utils;

/**
 * Created by Eshum on 12/04/2017.
 */


public class NavigationItems<T> {
    int titleId;
    int iconId;
    Class<T> cls;
    Class<?> subClass;
    boolean backActivity;

    public NavigationItems(int titleId, int imageId, Class<T> ncls) {
        this.titleId = titleId;
        this.iconId = imageId;
        this.cls = ncls;
        this.backActivity = false;
    }

    public NavigationItems(int titleId, int imageId, Class<T> ncls, Class<?> subClass) {
        this(titleId, imageId, ncls, subClass, false);
    }

    public NavigationItems(int titleId, int imageId, Class<T> ncls, boolean backActivity) {
        this(titleId, imageId, ncls, null, backActivity);
    }

    public NavigationItems(int titleId, int imageId, Class<T> ncls, Class<?> subClass, boolean backActivity) {
        this.titleId = titleId;
        this.iconId = imageId;
        this.cls = ncls;
        this.subClass = subClass;
        this.backActivity = backActivity;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getIconId() {
        return iconId;
    }

    public Class<T> getCls() { return cls; }

    public T getInstance() {
        return Helper.NewInstanceOf(cls);
    }

    public boolean isBackActivity() {
        return backActivity;
    }

    public Class<?> getSubClass() {
        return subClass;
    }
}