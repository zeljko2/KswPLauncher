package skin.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class SkinCompatScrollView extends ScrollView implements SkinCompatSupportable {
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinCompatScrollView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SkinCompatScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinCompatScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SkinCompatBackgroundHelper skinCompatBackgroundHelper = new SkinCompatBackgroundHelper(this);
        this.mBackgroundTintHelper = skinCompatBackgroundHelper;
        skinCompatBackgroundHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        SkinCompatBackgroundHelper skinCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (skinCompatBackgroundHelper != null) {
            skinCompatBackgroundHelper.onSetBackgroundResource(resId);
        }
    }

    public void applySkin() {
        SkinCompatBackgroundHelper skinCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (skinCompatBackgroundHelper != null) {
            skinCompatBackgroundHelper.applySkin();
        }
    }
}
