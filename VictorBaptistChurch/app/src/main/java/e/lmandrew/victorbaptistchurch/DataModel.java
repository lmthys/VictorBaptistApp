package e.lmandrew.victorbaptistchurch;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Made from tutorial https://www.journaldev.com/9958/android-navigation-drawer-example-tutorial
 */
public class DataModel {

    public int icon;
    public String name;

    // Constructor.
    public DataModel(int icon, String name) {

        this.icon = icon;
        this.name = name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}