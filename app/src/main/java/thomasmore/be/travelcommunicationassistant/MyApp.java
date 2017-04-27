package thomasmore.be.travelcommunicationassistant;

import android.app.Application;
import android.os.Bundle;

/**
 * Unfortunately used for globals :(
 */

public class MyApp extends Application {

    public static final String SEARCH_TERM = "Search_term";
    public static final String SEARCH_PICTOGRAM = "SearchPictogram";

    private Bundle search_extra;

    public Bundle getSearch_extra() {
        return search_extra;
    }

    public void setSearch_extra(Bundle search_extra) {
        this.search_extra = search_extra;
    }
}
