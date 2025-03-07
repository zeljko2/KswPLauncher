package com.wits.ksw.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wits.ksw.R;
import com.wits.ksw.settings.audi.widget.AudiConstraintLayout;

public abstract class ActivityAudiMib3Binding extends ViewDataBinding {
    public final TextView auSetEqItem;
    public final TextView auSetFacItem;
    public final TextView auSetLagItem;
    public final TextView auSetMoreItem;
    public final TextView auSetNaviItem;
    public final TextView auSetSoundItem;
    public final TextView auSetSysItem;
    public final TextView auSetSysinfoItem;
    public final TextView auSetTimeItem;
    public final AudiConstraintLayout audiHomeParentPanel;

    protected ActivityAudiMib3Binding(Object _bindingComponent, View _root, int _localFieldCount, TextView auSetEqItem2, TextView auSetFacItem2, TextView auSetLagItem2, TextView auSetMoreItem2, TextView auSetNaviItem2, TextView auSetSoundItem2, TextView auSetSysItem2, TextView auSetSysinfoItem2, TextView auSetTimeItem2, AudiConstraintLayout audiHomeParentPanel2) {
        super(_bindingComponent, _root, _localFieldCount);
        this.auSetEqItem = auSetEqItem2;
        this.auSetFacItem = auSetFacItem2;
        this.auSetLagItem = auSetLagItem2;
        this.auSetMoreItem = auSetMoreItem2;
        this.auSetNaviItem = auSetNaviItem2;
        this.auSetSoundItem = auSetSoundItem2;
        this.auSetSysItem = auSetSysItem2;
        this.auSetSysinfoItem = auSetSysinfoItem2;
        this.auSetTimeItem = auSetTimeItem2;
        this.audiHomeParentPanel = audiHomeParentPanel2;
    }

    public static ActivityAudiMib3Binding inflate(LayoutInflater inflater, ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityAudiMib3Binding inflate(LayoutInflater inflater, ViewGroup root, boolean attachToRoot, Object component) {
        return (ActivityAudiMib3Binding) ViewDataBinding.inflateInternal(inflater, R.layout.activity_audi_mib3, root, attachToRoot, component);
    }

    public static ActivityAudiMib3Binding inflate(LayoutInflater inflater) {
        return inflate(inflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityAudiMib3Binding inflate(LayoutInflater inflater, Object component) {
        return (ActivityAudiMib3Binding) ViewDataBinding.inflateInternal(inflater, R.layout.activity_audi_mib3, (ViewGroup) null, false, component);
    }

    public static ActivityAudiMib3Binding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityAudiMib3Binding bind(View view, Object component) {
        return (ActivityAudiMib3Binding) bind(component, view, R.layout.activity_audi_mib3);
    }
}
