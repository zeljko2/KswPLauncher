package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.ref.WeakReference;

public final class ViewStubCompat extends View {
    private OnInflateListener mInflateListener;
    private int mInflatedId;
    private WeakReference<View> mInflatedViewRef;
    private LayoutInflater mInflater;
    private int mLayoutResource;

    public interface OnInflateListener {
        void onInflate(ViewStubCompat viewStubCompat, View view);
    }

    public ViewStubCompat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewStubCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLayoutResource = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewStubCompat, defStyle, 0);
        this.mInflatedId = a.getResourceId(R.styleable.ViewStubCompat_android_inflatedId, -1);
        this.mLayoutResource = a.getResourceId(R.styleable.ViewStubCompat_android_layout, 0);
        setId(a.getResourceId(R.styleable.ViewStubCompat_android_id, -1));
        a.recycle();
        setVisibility(8);
        setWillNotDraw(true);
    }

    public int getInflatedId() {
        return this.mInflatedId;
    }

    public void setInflatedId(int inflatedId) {
        this.mInflatedId = inflatedId;
    }

    public int getLayoutResource() {
        return this.mLayoutResource;
    }

    public void setLayoutResource(int layoutResource) {
        this.mLayoutResource = layoutResource;
    }

    public void setLayoutInflater(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(0, 0);
    }

    public void draw(Canvas canvas) {
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
    }

    public void setVisibility(int visibility) {
        WeakReference<View> weakReference = this.mInflatedViewRef;
        if (weakReference != null) {
            View view = (View) weakReference.get();
            if (view != null) {
                view.setVisibility(visibility);
                return;
            }
            throw new IllegalStateException("setVisibility called on un-referenced view");
        }
        super.setVisibility(visibility);
        if (visibility == 0 || visibility == 4) {
            inflate();
        }
    }

    public View inflate() {
        LayoutInflater factory;
        ViewParent viewParent = getParent();
        if (viewParent == null || !(viewParent instanceof ViewGroup)) {
            throw new IllegalStateException("ViewStub must have a non-null ViewGroup viewParent");
        } else if (this.mLayoutResource != 0) {
            ViewGroup parent = (ViewGroup) viewParent;
            if (this.mInflater != null) {
                factory = this.mInflater;
            } else {
                factory = LayoutInflater.from(getContext());
            }
            View view = factory.inflate(this.mLayoutResource, parent, false);
            int i = this.mInflatedId;
            if (i != -1) {
                view.setId(i);
            }
            int index = parent.indexOfChild(this);
            parent.removeViewInLayout(this);
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams != null) {
                parent.addView(view, index, layoutParams);
            } else {
                parent.addView(view, index);
            }
            this.mInflatedViewRef = new WeakReference<>(view);
            OnInflateListener onInflateListener = this.mInflateListener;
            if (onInflateListener != null) {
                onInflateListener.onInflate(this, view);
            }
            return view;
        } else {
            throw new IllegalArgumentException("ViewStub must have a valid layoutResource");
        }
    }

    public void setOnInflateListener(OnInflateListener inflateListener) {
        this.mInflateListener = inflateListener;
    }
}
