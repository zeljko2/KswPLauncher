package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

public class Constraints extends ViewGroup {
    public static final String TAG = "Constraints";
    ConstraintSet myConstraintSet;

    public Constraints(Context context) {
        super(context);
        super.setVisibility(8);
    }

    public Constraints(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        super.setVisibility(8);
    }

    public Constraints(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        super.setVisibility(8);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ConstraintLayout.LayoutParams {
        public float alpha = 1.0f;
        public boolean applyElevation = false;
        public float elevation = 0.0f;
        public float rotation = 0.0f;
        public float rotationX = 0.0f;
        public float rotationY = 0.0f;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public float transformPivotX = 0.0f;
        public float transformPivotY = 0.0f;
        public float translationX = 0.0f;
        public float translationY = 0.0f;
        public float translationZ = 0.0f;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(LayoutParams source) {
            super((ConstraintLayout.LayoutParams) source);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ConstraintSet);
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.ConstraintSet_android_alpha) {
                    this.alpha = a.getFloat(attr, this.alpha);
                } else if (attr == R.styleable.ConstraintSet_android_elevation) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        this.elevation = a.getFloat(attr, this.elevation);
                        this.applyElevation = true;
                    }
                } else if (attr == R.styleable.ConstraintSet_android_rotationX) {
                    this.rotationX = a.getFloat(attr, this.rotationX);
                } else if (attr == R.styleable.ConstraintSet_android_rotationY) {
                    this.rotationY = a.getFloat(attr, this.rotationY);
                } else if (attr == R.styleable.ConstraintSet_android_rotation) {
                    this.rotation = a.getFloat(attr, this.rotation);
                } else if (attr == R.styleable.ConstraintSet_android_scaleX) {
                    this.scaleX = a.getFloat(attr, this.scaleX);
                } else if (attr == R.styleable.ConstraintSet_android_scaleY) {
                    this.scaleY = a.getFloat(attr, this.scaleY);
                } else if (attr == R.styleable.ConstraintSet_android_transformPivotX) {
                    this.transformPivotX = a.getFloat(attr, this.transformPivotX);
                } else if (attr == R.styleable.ConstraintSet_android_transformPivotY) {
                    this.transformPivotY = a.getFloat(attr, this.transformPivotY);
                } else if (attr == R.styleable.ConstraintSet_android_translationX) {
                    this.translationX = a.getFloat(attr, this.translationX);
                } else if (attr == R.styleable.ConstraintSet_android_translationY) {
                    this.translationY = a.getFloat(attr, this.translationY);
                } else if (attr == R.styleable.ConstraintSet_android_translationZ && Build.VERSION.SDK_INT >= 21) {
                    this.translationZ = a.getFloat(attr, this.translationZ);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    private void init(AttributeSet attrs) {
        Log.v(TAG, " ################# init");
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new ConstraintLayout.LayoutParams(p);
    }

    public ConstraintSet getConstraintSet() {
        if (this.myConstraintSet == null) {
            this.myConstraintSet = new ConstraintSet();
        }
        this.myConstraintSet.clone(this);
        return this.myConstraintSet;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
    }
}
