package com.wits.ksw.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.ViewDataBinding;
import android.util.SparseIntArray;
import android.view.View;
import com.wits.ksw.R;
import com.wits.ksw.generated.callback.OnClickListener;
import com.wits.ksw.launcher.model.LauncherViewModel;

public class AlsId7UiVideoSubViewBindingHdpi1920x720Impl extends AlsId7UiVideoSubViewBinding implements OnClickListener.Listener {
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;
    private static final SparseIntArray sViewsWithIds;
    private final View.OnClickListener mCallback262;
    private long mDirtyFlags;

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sViewsWithIds = sparseIntArray;
        sparseIntArray.put(R.id.textView2, 2);
        sparseIntArray.put(R.id.textView3, 3);
    }

    public AlsId7UiVideoSubViewBindingHdpi1920x720Impl(DataBindingComponent bindingComponent, View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }

    private AlsId7UiVideoSubViewBindingHdpi1920x720Impl(DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0, bindings[2], bindings[3], bindings[0], bindings[1]);
        this.mDirtyFlags = -1;
        this.videoConstraintLayout.setTag((Object) null);
        this.videoImageView.setTag((Object) null);
        setRootTag(root);
        this.mCallback262 = new OnClickListener(this, 1);
        invalidateAll();
    }

    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 2;
        }
        requestRebind();
    }

    public boolean hasPendingBindings() {
        synchronized (this) {
            if (this.mDirtyFlags != 0) {
                return true;
            }
            return false;
        }
    }

    public boolean setVariable(int variableId, Object variable) {
        if (6 != variableId) {
            return false;
        }
        setMediaViewModel((LauncherViewModel) variable);
        return true;
    }

    public void setMediaViewModel(LauncherViewModel MediaViewModel) {
        this.mMediaViewModel = MediaViewModel;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(6);
        super.requestRebind();
    }

    /* access modifiers changed from: protected */
    public boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void executeBindings() {
        long dirtyFlags;
        synchronized (this) {
            dirtyFlags = this.mDirtyFlags;
            this.mDirtyFlags = 0;
        }
        LauncherViewModel mediaViewModel = this.mMediaViewModel;
        View.OnFocusChangeListener mediaViewModelVideoViewFocusChangeListener = null;
        if (!((dirtyFlags & 3) == 0 || mediaViewModel == null)) {
            mediaViewModelVideoViewFocusChangeListener = mediaViewModel.videoViewFocusChangeListener;
        }
        if ((2 & dirtyFlags) != 0) {
            this.videoImageView.setOnClickListener(this.mCallback262);
        }
        if ((3 & dirtyFlags) != 0) {
            this.videoImageView.setOnFocusChangeListener(mediaViewModelVideoViewFocusChangeListener);
        }
    }

    public final void _internalCallbackOnClick(int sourceId, View callbackArg_0) {
        LauncherViewModel mediaViewModel = this.mMediaViewModel;
        if (mediaViewModel != null) {
            mediaViewModel.openVideoMulti(callbackArg_0);
        }
    }
}
