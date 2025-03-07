package skin.support.load;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;

public class SkinPrefixBuildInLoader implements SkinCompatManager.SkinLoaderStrategy {
    public String loadSkinInBackground(Context context, String skinName) {
        SkinCompatResources.getInstance().setupSkin(context.getResources(), context.getPackageName(), skinName, this);
        return skinName;
    }

    public String getTargetResourceEntryName(Context context, String skinName, int resId) {
        return skinName + "_" + context.getResources().getResourceEntryName(resId);
    }

    public ColorStateList getColor(Context context, String skinName, int resId) {
        return null;
    }

    public ColorStateList getColorStateList(Context context, String skinName, int resId) {
        return null;
    }

    public Drawable getDrawable(Context context, String skinName, int resId) {
        return null;
    }

    public int getType() {
        return 2;
    }
}
