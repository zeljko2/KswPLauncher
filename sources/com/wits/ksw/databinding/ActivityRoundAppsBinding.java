package com.wits.ksw.databinding;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wits.ksw.R;
import com.wits.ksw.launcher.model.AppViewModel;
import com.wits.ksw.launcher.view.CustomGridView;

public abstract class ActivityRoundAppsBinding extends ViewDataBinding {
    public final CustomGridView appGridView;
    @Bindable
    protected AppViewModel mAppViewModel;

    public abstract void setAppViewModel(AppViewModel appViewModel);

    protected ActivityRoundAppsBinding(Object _bindingComponent, View _root, int _localFieldCount, CustomGridView appGridView2) {
        super(_bindingComponent, _root, _localFieldCount);
        this.appGridView = appGridView2;
    }

    public AppViewModel getAppViewModel() {
        return this.mAppViewModel;
    }

    public static ActivityRoundAppsBinding inflate(LayoutInflater inflater, ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityRoundAppsBinding inflate(LayoutInflater inflater, ViewGroup root, boolean attachToRoot, Object component) {
        return (ActivityRoundAppsBinding) ViewDataBinding.inflateInternal(inflater, R.layout.activity_round_apps, root, attachToRoot, component);
    }

    public static ActivityRoundAppsBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityRoundAppsBinding inflate(LayoutInflater inflater, Object component) {
        return (ActivityRoundAppsBinding) ViewDataBinding.inflateInternal(inflater, R.layout.activity_round_apps, (ViewGroup) null, false, component);
    }

    public static ActivityRoundAppsBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityRoundAppsBinding bind(View view, Object component) {
        return (ActivityRoundAppsBinding) bind(component, view, R.layout.activity_round_apps);
    }
}
