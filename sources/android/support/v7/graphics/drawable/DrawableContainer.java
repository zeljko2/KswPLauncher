package android.support.v7.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.SparseArray;

class DrawableContainer extends Drawable implements Drawable.Callback {
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_DITHER = true;
    private static final String TAG = "DrawableContainer";
    private int mAlpha = 255;
    private Runnable mAnimationRunnable;
    private BlockInvalidateCallback mBlockInvalidateCallback;
    private int mCurIndex = -1;
    private Drawable mCurrDrawable;
    private DrawableContainerState mDrawableContainerState;
    private long mEnterAnimationEnd;
    private long mExitAnimationEnd;
    private boolean mHasAlpha;
    private Rect mHotspotBounds;
    private Drawable mLastDrawable;
    private int mLastIndex = -1;
    private boolean mMutated;

    DrawableContainer() {
    }

    public void draw(Canvas canvas) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        Drawable drawable2 = this.mLastDrawable;
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mDrawableContainerState.getChangingConfigurations();
    }

    private boolean needsMirroring() {
        if (!isAutoMirrored() || getLayoutDirection() != 1) {
            return false;
        }
        return DEFAULT_DITHER;
    }

    public boolean getPadding(Rect padding) {
        boolean result;
        Rect r = this.mDrawableContainerState.getConstantPadding();
        if (r != null) {
            padding.set(r);
            result = (((r.left | r.top) | r.bottom) | r.right) != 0 ? DEFAULT_DITHER : false;
        } else {
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                result = drawable.getPadding(padding);
            } else {
                result = super.getPadding(padding);
            }
        }
        if (needsMirroring()) {
            int left = padding.left;
            padding.left = padding.right;
            padding.right = left;
        }
        return result;
    }

    public void getOutline(Outline outline) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.getOutline(outline);
        }
    }

    public void setAlpha(int alpha) {
        if (!this.mHasAlpha || this.mAlpha != alpha) {
            this.mHasAlpha = DEFAULT_DITHER;
            this.mAlpha = alpha;
            Drawable drawable = this.mCurrDrawable;
            if (drawable == null) {
                return;
            }
            if (this.mEnterAnimationEnd == 0) {
                drawable.setAlpha(alpha);
            } else {
                animate(false);
            }
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setDither(boolean dither) {
        if (this.mDrawableContainerState.mDither != dither) {
            this.mDrawableContainerState.mDither = dither;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                drawable.setDither(this.mDrawableContainerState.mDither);
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawableContainerState.mHasColorFilter = DEFAULT_DITHER;
        if (this.mDrawableContainerState.mColorFilter != colorFilter) {
            this.mDrawableContainerState.mColorFilter = colorFilter;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }
    }

    public void setTintList(ColorStateList tint) {
        this.mDrawableContainerState.mHasTintList = DEFAULT_DITHER;
        if (this.mDrawableContainerState.mTintList != tint) {
            this.mDrawableContainerState.mTintList = tint;
            DrawableCompat.setTintList(this.mCurrDrawable, tint);
        }
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mDrawableContainerState.mHasTintMode = DEFAULT_DITHER;
        if (this.mDrawableContainerState.mTintMode != tintMode) {
            this.mDrawableContainerState.mTintMode = tintMode;
            DrawableCompat.setTintMode(this.mCurrDrawable, tintMode);
        }
    }

    public void setEnterFadeDuration(int ms) {
        this.mDrawableContainerState.mEnterFadeDuration = ms;
    }

    public void setExitFadeDuration(int ms) {
        this.mDrawableContainerState.mExitFadeDuration = ms;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.setBounds(bounds);
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            drawable2.setBounds(bounds);
        }
    }

    public boolean isStateful() {
        return this.mDrawableContainerState.isStateful();
    }

    public void setAutoMirrored(boolean mirrored) {
        if (this.mDrawableContainerState.mAutoMirrored != mirrored) {
            this.mDrawableContainerState.mAutoMirrored = mirrored;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                DrawableCompat.setAutoMirrored(drawable, this.mDrawableContainerState.mAutoMirrored);
            }
        }
    }

    public boolean isAutoMirrored() {
        return this.mDrawableContainerState.mAutoMirrored;
    }

    public void jumpToCurrentState() {
        boolean changed = false;
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
            this.mLastDrawable = null;
            this.mLastIndex = -1;
            changed = DEFAULT_DITHER;
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0) {
            this.mExitAnimationEnd = 0;
            changed = DEFAULT_DITHER;
        }
        if (this.mEnterAnimationEnd != 0) {
            this.mEnterAnimationEnd = 0;
            changed = DEFAULT_DITHER;
        }
        if (changed) {
            invalidateSelf();
        }
    }

    public void setHotspot(float x, float y) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspot(drawable, x, y);
        }
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        Rect rect = this.mHotspotBounds;
        if (rect == null) {
            this.mHotspotBounds = new Rect(left, top, right, bottom);
        } else {
            rect.set(left, top, right, bottom);
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspotBounds(drawable, left, top, right, bottom);
        }
    }

    public void getHotspotBounds(Rect outRect) {
        Rect rect = this.mHotspotBounds;
        if (rect != null) {
            outRect.set(rect);
        } else {
            super.getHotspotBounds(outRect);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] state) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            return drawable.setState(state);
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            return drawable2.setState(state);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int level) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            return drawable.setLevel(level);
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            return drawable2.setLevel(level);
        }
        return false;
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mDrawableContainerState.setLayoutDirection(layoutDirection, getCurrentIndex());
    }

    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return -1;
    }

    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return -1;
    }

    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getMinimumWidth();
        }
        return 0;
    }

    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getMinimumHeight();
        }
        return 0;
    }

    public void invalidateDrawable(Drawable who) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        if (drawableContainerState != null) {
            drawableContainerState.invalidateCache();
        }
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().unscheduleDrawable(this, what);
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.setVisible(visible, restart);
        }
        Drawable drawable2 = this.mCurrDrawable;
        if (drawable2 != null) {
            drawable2.setVisible(visible, restart);
        }
        return changed;
    }

    public int getOpacity() {
        Drawable drawable = this.mCurrDrawable;
        if (drawable == null || !drawable.isVisible()) {
            return -2;
        }
        return this.mDrawableContainerState.getOpacity();
    }

    /* access modifiers changed from: package-private */
    public void setCurrentIndex(int index) {
        selectDrawable(index);
    }

    /* access modifiers changed from: package-private */
    public int getCurrentIndex() {
        return this.mCurIndex;
    }

    /* access modifiers changed from: package-private */
    public boolean selectDrawable(int index) {
        if (index == this.mCurIndex) {
            return false;
        }
        long now = SystemClock.uptimeMillis();
        if (this.mDrawableContainerState.mExitFadeDuration > 0) {
            Drawable drawable = this.mLastDrawable;
            if (drawable != null) {
                drawable.setVisible(false, false);
            }
            Drawable drawable2 = this.mCurrDrawable;
            if (drawable2 != null) {
                this.mLastDrawable = drawable2;
                this.mLastIndex = this.mCurIndex;
                this.mExitAnimationEnd = ((long) this.mDrawableContainerState.mExitFadeDuration) + now;
            } else {
                this.mLastDrawable = null;
                this.mLastIndex = -1;
                this.mExitAnimationEnd = 0;
            }
        } else {
            Drawable drawable3 = this.mCurrDrawable;
            if (drawable3 != null) {
                drawable3.setVisible(false, false);
            }
        }
        if (index < 0 || index >= this.mDrawableContainerState.mNumChildren) {
            this.mCurrDrawable = null;
            this.mCurIndex = -1;
        } else {
            Drawable d = this.mDrawableContainerState.getChild(index);
            this.mCurrDrawable = d;
            this.mCurIndex = index;
            if (d != null) {
                if (this.mDrawableContainerState.mEnterFadeDuration > 0) {
                    this.mEnterAnimationEnd = ((long) this.mDrawableContainerState.mEnterFadeDuration) + now;
                }
                initializeDrawableForDisplay(d);
            }
        }
        if (!(this.mEnterAnimationEnd == 0 && this.mExitAnimationEnd == 0)) {
            Runnable runnable = this.mAnimationRunnable;
            if (runnable == null) {
                this.mAnimationRunnable = new Runnable() {
                    public void run() {
                        DrawableContainer.this.animate(DrawableContainer.DEFAULT_DITHER);
                        DrawableContainer.this.invalidateSelf();
                    }
                };
            } else {
                unscheduleSelf(runnable);
            }
            animate(DEFAULT_DITHER);
        }
        invalidateSelf();
        return DEFAULT_DITHER;
    }

    private void initializeDrawableForDisplay(Drawable d) {
        if (this.mBlockInvalidateCallback == null) {
            this.mBlockInvalidateCallback = new BlockInvalidateCallback();
        }
        d.setCallback(this.mBlockInvalidateCallback.wrap(d.getCallback()));
        try {
            if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                d.setAlpha(this.mAlpha);
            }
            if (this.mDrawableContainerState.mHasColorFilter) {
                d.setColorFilter(this.mDrawableContainerState.mColorFilter);
            } else {
                if (this.mDrawableContainerState.mHasTintList) {
                    DrawableCompat.setTintList(d, this.mDrawableContainerState.mTintList);
                }
                if (this.mDrawableContainerState.mHasTintMode) {
                    DrawableCompat.setTintMode(d, this.mDrawableContainerState.mTintMode);
                }
            }
            d.setVisible(isVisible(), DEFAULT_DITHER);
            d.setDither(this.mDrawableContainerState.mDither);
            d.setState(getState());
            d.setLevel(getLevel());
            d.setBounds(getBounds());
            if (Build.VERSION.SDK_INT >= 23) {
                d.setLayoutDirection(getLayoutDirection());
            }
            if (Build.VERSION.SDK_INT >= 19) {
                d.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            }
            Rect hotspotBounds = this.mHotspotBounds;
            if (Build.VERSION.SDK_INT >= 21 && hotspotBounds != null) {
                d.setHotspotBounds(hotspotBounds.left, hotspotBounds.top, hotspotBounds.right, hotspotBounds.bottom);
            }
        } finally {
            d.setCallback(this.mBlockInvalidateCallback.unwrap());
        }
    }

    /* access modifiers changed from: package-private */
    public void animate(boolean schedule) {
        this.mHasAlpha = DEFAULT_DITHER;
        long now = SystemClock.uptimeMillis();
        boolean animating = false;
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            long j = this.mEnterAnimationEnd;
            if (j != 0) {
                if (j <= now) {
                    drawable.setAlpha(this.mAlpha);
                    this.mEnterAnimationEnd = 0;
                } else {
                    this.mCurrDrawable.setAlpha(((255 - (((int) ((j - now) * 255)) / this.mDrawableContainerState.mEnterFadeDuration)) * this.mAlpha) / 255);
                    animating = DEFAULT_DITHER;
                }
            }
        } else {
            this.mEnterAnimationEnd = 0;
        }
        Drawable drawable2 = this.mLastDrawable;
        if (drawable2 != null) {
            long j2 = this.mExitAnimationEnd;
            if (j2 != 0) {
                if (j2 <= now) {
                    drawable2.setVisible(false, false);
                    this.mLastDrawable = null;
                    this.mLastIndex = -1;
                    this.mExitAnimationEnd = 0;
                } else {
                    this.mLastDrawable.setAlpha((this.mAlpha * (((int) ((j2 - now) * 255)) / this.mDrawableContainerState.mExitFadeDuration)) / 255);
                    animating = DEFAULT_DITHER;
                }
            }
        } else {
            this.mExitAnimationEnd = 0;
        }
        if (schedule && animating) {
            scheduleSelf(this.mAnimationRunnable, 16 + now);
        }
    }

    public Drawable getCurrent() {
        return this.mCurrDrawable;
    }

    /* access modifiers changed from: package-private */
    public final void updateDensity(Resources res) {
        this.mDrawableContainerState.updateDensity(res);
    }

    public void applyTheme(Resources.Theme theme) {
        this.mDrawableContainerState.applyTheme(theme);
    }

    public boolean canApplyTheme() {
        return this.mDrawableContainerState.canApplyTheme();
    }

    public final Drawable.ConstantState getConstantState() {
        if (!this.mDrawableContainerState.canConstantState()) {
            return null;
        }
        this.mDrawableContainerState.mChangingConfigurations = getChangingConfigurations();
        return this.mDrawableContainerState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            DrawableContainerState clone = cloneConstantState();
            clone.mutate();
            setConstantState(clone);
            this.mMutated = DEFAULT_DITHER;
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public DrawableContainerState cloneConstantState() {
        return this.mDrawableContainerState;
    }

    /* access modifiers changed from: package-private */
    public void clearMutated() {
        this.mDrawableContainerState.clearMutated();
        this.mMutated = false;
    }

    static abstract class DrawableContainerState extends Drawable.ConstantState {
        boolean mAutoMirrored;
        boolean mCanConstantState;
        int mChangingConfigurations;
        boolean mCheckedConstantSize;
        boolean mCheckedConstantState;
        boolean mCheckedOpacity;
        boolean mCheckedPadding;
        boolean mCheckedStateful;
        int mChildrenChangingConfigurations;
        ColorFilter mColorFilter;
        int mConstantHeight;
        int mConstantMinimumHeight;
        int mConstantMinimumWidth;
        Rect mConstantPadding;
        boolean mConstantSize = false;
        int mConstantWidth;
        int mDensity = 160;
        boolean mDither = DrawableContainer.DEFAULT_DITHER;
        SparseArray<Drawable.ConstantState> mDrawableFutures;
        Drawable[] mDrawables;
        int mEnterFadeDuration = 0;
        int mExitFadeDuration = 0;
        boolean mHasColorFilter;
        boolean mHasTintList;
        boolean mHasTintMode;
        int mLayoutDirection;
        boolean mMutated;
        int mNumChildren;
        int mOpacity;
        final DrawableContainer mOwner;
        Resources mSourceRes;
        boolean mStateful;
        ColorStateList mTintList;
        PorterDuff.Mode mTintMode;
        boolean mVariablePadding = false;

        DrawableContainerState(DrawableContainerState orig, DrawableContainer owner, Resources res) {
            this.mOwner = owner;
            this.mSourceRes = res != null ? res : orig != null ? orig.mSourceRes : null;
            int resolveDensity = DrawableContainer.resolveDensity(res, orig != null ? orig.mDensity : 0);
            this.mDensity = resolveDensity;
            if (orig != null) {
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations;
                this.mCheckedConstantState = DrawableContainer.DEFAULT_DITHER;
                this.mCanConstantState = DrawableContainer.DEFAULT_DITHER;
                this.mVariablePadding = orig.mVariablePadding;
                this.mConstantSize = orig.mConstantSize;
                this.mDither = orig.mDither;
                this.mMutated = orig.mMutated;
                this.mLayoutDirection = orig.mLayoutDirection;
                this.mEnterFadeDuration = orig.mEnterFadeDuration;
                this.mExitFadeDuration = orig.mExitFadeDuration;
                this.mAutoMirrored = orig.mAutoMirrored;
                this.mColorFilter = orig.mColorFilter;
                this.mHasColorFilter = orig.mHasColorFilter;
                this.mTintList = orig.mTintList;
                this.mTintMode = orig.mTintMode;
                this.mHasTintList = orig.mHasTintList;
                this.mHasTintMode = orig.mHasTintMode;
                if (orig.mDensity == resolveDensity) {
                    if (orig.mCheckedPadding) {
                        this.mConstantPadding = new Rect(orig.mConstantPadding);
                        this.mCheckedPadding = DrawableContainer.DEFAULT_DITHER;
                    }
                    if (orig.mCheckedConstantSize) {
                        this.mConstantWidth = orig.mConstantWidth;
                        this.mConstantHeight = orig.mConstantHeight;
                        this.mConstantMinimumWidth = orig.mConstantMinimumWidth;
                        this.mConstantMinimumHeight = orig.mConstantMinimumHeight;
                        this.mCheckedConstantSize = DrawableContainer.DEFAULT_DITHER;
                    }
                }
                if (orig.mCheckedOpacity) {
                    this.mOpacity = orig.mOpacity;
                    this.mCheckedOpacity = DrawableContainer.DEFAULT_DITHER;
                }
                if (orig.mCheckedStateful) {
                    this.mStateful = orig.mStateful;
                    this.mCheckedStateful = DrawableContainer.DEFAULT_DITHER;
                }
                Drawable[] origDr = orig.mDrawables;
                this.mDrawables = new Drawable[origDr.length];
                this.mNumChildren = orig.mNumChildren;
                SparseArray<Drawable.ConstantState> origDf = orig.mDrawableFutures;
                if (origDf != null) {
                    this.mDrawableFutures = origDf.clone();
                } else {
                    this.mDrawableFutures = new SparseArray<>(this.mNumChildren);
                }
                int count = this.mNumChildren;
                for (int i = 0; i < count; i++) {
                    if (origDr[i] != null) {
                        Drawable.ConstantState cs = origDr[i].getConstantState();
                        if (cs != null) {
                            this.mDrawableFutures.put(i, cs);
                        } else {
                            this.mDrawables[i] = origDr[i];
                        }
                    }
                }
                return;
            }
            this.mDrawables = new Drawable[10];
            this.mNumChildren = 0;
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }

        public final int addChild(Drawable dr) {
            int pos = this.mNumChildren;
            if (pos >= this.mDrawables.length) {
                growArray(pos, pos + 10);
            }
            dr.mutate();
            dr.setVisible(false, DrawableContainer.DEFAULT_DITHER);
            dr.setCallback(this.mOwner);
            this.mDrawables[pos] = dr;
            this.mNumChildren++;
            this.mChildrenChangingConfigurations |= dr.getChangingConfigurations();
            invalidateCache();
            this.mConstantPadding = null;
            this.mCheckedPadding = false;
            this.mCheckedConstantSize = false;
            this.mCheckedConstantState = false;
            return pos;
        }

        /* access modifiers changed from: package-private */
        public void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }

        /* access modifiers changed from: package-private */
        public final int getCapacity() {
            return this.mDrawables.length;
        }

        private void createAllFutures() {
            SparseArray<Drawable.ConstantState> sparseArray = this.mDrawableFutures;
            if (sparseArray != null) {
                int futureCount = sparseArray.size();
                for (int keyIndex = 0; keyIndex < futureCount; keyIndex++) {
                    this.mDrawables[this.mDrawableFutures.keyAt(keyIndex)] = prepareDrawable(this.mDrawableFutures.valueAt(keyIndex).newDrawable(this.mSourceRes));
                }
                this.mDrawableFutures = null;
            }
        }

        private Drawable prepareDrawable(Drawable child) {
            if (Build.VERSION.SDK_INT >= 23) {
                child.setLayoutDirection(this.mLayoutDirection);
            }
            Drawable child2 = child.mutate();
            child2.setCallback(this.mOwner);
            return child2;
        }

        public final int getChildCount() {
            return this.mNumChildren;
        }

        public final Drawable getChild(int index) {
            int keyIndex;
            Drawable result = this.mDrawables[index];
            if (result != null) {
                return result;
            }
            SparseArray<Drawable.ConstantState> sparseArray = this.mDrawableFutures;
            if (sparseArray == null || (keyIndex = sparseArray.indexOfKey(index)) < 0) {
                return null;
            }
            Drawable prepared = prepareDrawable(this.mDrawableFutures.valueAt(keyIndex).newDrawable(this.mSourceRes));
            this.mDrawables[index] = prepared;
            this.mDrawableFutures.removeAt(keyIndex);
            if (this.mDrawableFutures.size() == 0) {
                this.mDrawableFutures = null;
            }
            return prepared;
        }

        /* access modifiers changed from: package-private */
        public final boolean setLayoutDirection(int layoutDirection, int currentIndex) {
            boolean changed = false;
            int count = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < count; i++) {
                if (drawables[i] != null) {
                    boolean childChanged = false;
                    if (Build.VERSION.SDK_INT >= 23) {
                        childChanged = drawables[i].setLayoutDirection(layoutDirection);
                    }
                    if (i == currentIndex) {
                        changed = childChanged;
                    }
                }
            }
            this.mLayoutDirection = layoutDirection;
            return changed;
        }

        /* access modifiers changed from: package-private */
        public final void updateDensity(Resources res) {
            if (res != null) {
                this.mSourceRes = res;
                int targetDensity = DrawableContainer.resolveDensity(res, this.mDensity);
                int sourceDensity = this.mDensity;
                this.mDensity = targetDensity;
                if (sourceDensity != targetDensity) {
                    this.mCheckedConstantSize = false;
                    this.mCheckedPadding = false;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final void applyTheme(Resources.Theme theme) {
            if (theme != null) {
                createAllFutures();
                int count = this.mNumChildren;
                Drawable[] drawables = this.mDrawables;
                for (int i = 0; i < count; i++) {
                    if (drawables[i] != null && drawables[i].canApplyTheme()) {
                        drawables[i].applyTheme(theme);
                        this.mChildrenChangingConfigurations |= drawables[i].getChangingConfigurations();
                    }
                }
                updateDensity(theme.getResources());
            }
        }

        public boolean canApplyTheme() {
            int count = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < count; i++) {
                Drawable d = drawables[i];
                if (d == null) {
                    Drawable.ConstantState future = this.mDrawableFutures.get(i);
                    if (future != null && future.canApplyTheme()) {
                        return DrawableContainer.DEFAULT_DITHER;
                    }
                } else if (d.canApplyTheme()) {
                    return DrawableContainer.DEFAULT_DITHER;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void mutate() {
            int count = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < count; i++) {
                if (drawables[i] != null) {
                    drawables[i].mutate();
                }
            }
            this.mMutated = DrawableContainer.DEFAULT_DITHER;
        }

        /* access modifiers changed from: package-private */
        public final void clearMutated() {
            this.mMutated = false;
        }

        public final void setVariablePadding(boolean variable) {
            this.mVariablePadding = variable;
        }

        public final Rect getConstantPadding() {
            if (this.mVariablePadding) {
                return null;
            }
            Rect r = this.mConstantPadding;
            if (r != null || this.mCheckedPadding) {
                return r;
            }
            createAllFutures();
            Rect r2 = null;
            Rect t = new Rect();
            int count = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < count; i++) {
                if (drawables[i].getPadding(t)) {
                    if (r2 == null) {
                        r2 = new Rect(0, 0, 0, 0);
                    }
                    if (t.left > r2.left) {
                        r2.left = t.left;
                    }
                    if (t.top > r2.top) {
                        r2.top = t.top;
                    }
                    if (t.right > r2.right) {
                        r2.right = t.right;
                    }
                    if (t.bottom > r2.bottom) {
                        r2.bottom = t.bottom;
                    }
                }
            }
            this.mCheckedPadding = DrawableContainer.DEFAULT_DITHER;
            this.mConstantPadding = r2;
            return r2;
        }

        public final void setConstantSize(boolean constant) {
            this.mConstantSize = constant;
        }

        public final boolean isConstantSize() {
            return this.mConstantSize;
        }

        public final int getConstantWidth() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantWidth;
        }

        public final int getConstantHeight() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantHeight;
        }

        public final int getConstantMinimumWidth() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumWidth;
        }

        public final int getConstantMinimumHeight() {
            if (!this.mCheckedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumHeight;
        }

        /* access modifiers changed from: protected */
        public void computeConstantSize() {
            this.mCheckedConstantSize = DrawableContainer.DEFAULT_DITHER;
            createAllFutures();
            int count = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            this.mConstantHeight = -1;
            this.mConstantWidth = -1;
            this.mConstantMinimumHeight = 0;
            this.mConstantMinimumWidth = 0;
            for (int i = 0; i < count; i++) {
                Drawable dr = drawables[i];
                int s = dr.getIntrinsicWidth();
                if (s > this.mConstantWidth) {
                    this.mConstantWidth = s;
                }
                int s2 = dr.getIntrinsicHeight();
                if (s2 > this.mConstantHeight) {
                    this.mConstantHeight = s2;
                }
                int s3 = dr.getMinimumWidth();
                if (s3 > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = s3;
                }
                int s4 = dr.getMinimumHeight();
                if (s4 > this.mConstantMinimumHeight) {
                    this.mConstantMinimumHeight = s4;
                }
            }
        }

        public final void setEnterFadeDuration(int duration) {
            this.mEnterFadeDuration = duration;
        }

        public final int getEnterFadeDuration() {
            return this.mEnterFadeDuration;
        }

        public final void setExitFadeDuration(int duration) {
            this.mExitFadeDuration = duration;
        }

        public final int getExitFadeDuration() {
            return this.mExitFadeDuration;
        }

        public final int getOpacity() {
            if (this.mCheckedOpacity) {
                return this.mOpacity;
            }
            createAllFutures();
            int count = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            int op = count > 0 ? drawables[0].getOpacity() : -2;
            for (int i = 1; i < count; i++) {
                op = Drawable.resolveOpacity(op, drawables[i].getOpacity());
            }
            this.mOpacity = op;
            this.mCheckedOpacity = DrawableContainer.DEFAULT_DITHER;
            return op;
        }

        public final boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mStateful;
            }
            createAllFutures();
            int count = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            boolean isStateful = false;
            int i = 0;
            while (true) {
                if (i >= count) {
                    break;
                } else if (drawables[i].isStateful()) {
                    isStateful = DrawableContainer.DEFAULT_DITHER;
                    break;
                } else {
                    i++;
                }
            }
            this.mStateful = isStateful;
            this.mCheckedStateful = DrawableContainer.DEFAULT_DITHER;
            return isStateful;
        }

        public void growArray(int oldSize, int newSize) {
            Drawable[] newDrawables = new Drawable[newSize];
            System.arraycopy(this.mDrawables, 0, newDrawables, 0, oldSize);
            this.mDrawables = newDrawables;
        }

        public synchronized boolean canConstantState() {
            if (this.mCheckedConstantState) {
                return this.mCanConstantState;
            }
            createAllFutures();
            this.mCheckedConstantState = DrawableContainer.DEFAULT_DITHER;
            int count = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < count; i++) {
                if (drawables[i].getConstantState() == null) {
                    this.mCanConstantState = false;
                    return false;
                }
            }
            this.mCanConstantState = DrawableContainer.DEFAULT_DITHER;
            return DrawableContainer.DEFAULT_DITHER;
        }
    }

    /* access modifiers changed from: protected */
    public void setConstantState(DrawableContainerState state) {
        this.mDrawableContainerState = state;
        int i = this.mCurIndex;
        if (i >= 0) {
            Drawable child = state.getChild(i);
            this.mCurrDrawable = child;
            if (child != null) {
                initializeDrawableForDisplay(child);
            }
        }
        this.mLastIndex = -1;
        this.mLastDrawable = null;
    }

    static class BlockInvalidateCallback implements Drawable.Callback {
        private Drawable.Callback mCallback;

        BlockInvalidateCallback() {
        }

        public BlockInvalidateCallback wrap(Drawable.Callback callback) {
            this.mCallback = callback;
            return this;
        }

        public Drawable.Callback unwrap() {
            Drawable.Callback callback = this.mCallback;
            this.mCallback = null;
            return callback;
        }

        public void invalidateDrawable(Drawable who) {
        }

        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            Drawable.Callback callback = this.mCallback;
            if (callback != null) {
                callback.scheduleDrawable(who, what, when);
            }
        }

        public void unscheduleDrawable(Drawable who, Runnable what) {
            Drawable.Callback callback = this.mCallback;
            if (callback != null) {
                callback.unscheduleDrawable(who, what);
            }
        }
    }

    static int resolveDensity(Resources r, int parentDensity) {
        int densityDpi = r == null ? parentDensity : r.getDisplayMetrics().densityDpi;
        if (densityDpi == 0) {
            return 160;
        }
        return densityDpi;
    }
}
