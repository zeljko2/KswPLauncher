package com.wits.ksw.databinding;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.wits.ksw.R;
import com.wits.ksw.settings.audi_mib3.vm.AudiMib3SystemViewModel;

public abstract class AudiMib3AuxBinding extends ViewDataBinding {
    public final SeekBar audioSeekbar;
    public final TextView audioSeekbarRightText;
    public final TextView audioSeekbarTitle;
    public final LinearLayout hzCallLinearLayout;
    public final LinearLayout hzMediaLinearLayout;
    public final ConstraintLayout linearLayout4;
    @Bindable
    protected AudiMib3SystemViewModel mVm;
    public final AppCompatTextView title;
    public final View titleDivider;
    public final View vDivider;

    public abstract void setVm(AudiMib3SystemViewModel audiMib3SystemViewModel);

    protected AudiMib3AuxBinding(Object _bindingComponent, View _root, int _localFieldCount, SeekBar audioSeekbar2, TextView audioSeekbarRightText2, TextView audioSeekbarTitle2, LinearLayout hzCallLinearLayout2, LinearLayout hzMediaLinearLayout2, ConstraintLayout linearLayout42, AppCompatTextView title2, View titleDivider2, View vDivider2) {
        super(_bindingComponent, _root, _localFieldCount);
        this.audioSeekbar = audioSeekbar2;
        this.audioSeekbarRightText = audioSeekbarRightText2;
        this.audioSeekbarTitle = audioSeekbarTitle2;
        this.hzCallLinearLayout = hzCallLinearLayout2;
        this.hzMediaLinearLayout = hzMediaLinearLayout2;
        this.linearLayout4 = linearLayout42;
        this.title = title2;
        this.titleDivider = titleDivider2;
        this.vDivider = vDivider2;
    }

    public AudiMib3SystemViewModel getVm() {
        return this.mVm;
    }

    public static AudiMib3AuxBinding inflate(LayoutInflater inflater, ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static AudiMib3AuxBinding inflate(LayoutInflater inflater, ViewGroup root, boolean attachToRoot, Object component) {
        return (AudiMib3AuxBinding) ViewDataBinding.inflateInternal(inflater, R.layout.audi_mib3_aux, root, attachToRoot, component);
    }

    public static AudiMib3AuxBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static AudiMib3AuxBinding inflate(LayoutInflater inflater, Object component) {
        return (AudiMib3AuxBinding) ViewDataBinding.inflateInternal(inflater, R.layout.audi_mib3_aux, (ViewGroup) null, false, component);
    }

    public static AudiMib3AuxBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static AudiMib3AuxBinding bind(View view, Object component) {
        return (AudiMib3AuxBinding) bind(component, view, R.layout.audi_mib3_aux);
    }
}
