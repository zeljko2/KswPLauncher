package android.support.v7.view;

import android.content.Context;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;

public class SupportActionModeWrapper extends ActionMode {
    final Context mContext;
    final ActionMode mWrappedObject;

    public SupportActionModeWrapper(Context context, ActionMode supportActionMode) {
        this.mContext = context;
        this.mWrappedObject = supportActionMode;
    }

    public Object getTag() {
        return this.mWrappedObject.getTag();
    }

    public void setTag(Object tag) {
        this.mWrappedObject.setTag(tag);
    }

    public void setTitle(CharSequence title) {
        this.mWrappedObject.setTitle(title);
    }

    public void setSubtitle(CharSequence subtitle) {
        this.mWrappedObject.setSubtitle(subtitle);
    }

    public void invalidate() {
        this.mWrappedObject.invalidate();
    }

    public void finish() {
        this.mWrappedObject.finish();
    }

    public Menu getMenu() {
        return MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu) this.mWrappedObject.getMenu());
    }

    public CharSequence getTitle() {
        return this.mWrappedObject.getTitle();
    }

    public void setTitle(int resId) {
        this.mWrappedObject.setTitle(resId);
    }

    public CharSequence getSubtitle() {
        return this.mWrappedObject.getSubtitle();
    }

    public void setSubtitle(int resId) {
        this.mWrappedObject.setSubtitle(resId);
    }

    public View getCustomView() {
        return this.mWrappedObject.getCustomView();
    }

    public void setCustomView(View view) {
        this.mWrappedObject.setCustomView(view);
    }

    public MenuInflater getMenuInflater() {
        return this.mWrappedObject.getMenuInflater();
    }

    public boolean getTitleOptionalHint() {
        return this.mWrappedObject.getTitleOptionalHint();
    }

    public void setTitleOptionalHint(boolean titleOptional) {
        this.mWrappedObject.setTitleOptionalHint(titleOptional);
    }

    public boolean isTitleOptional() {
        return this.mWrappedObject.isTitleOptional();
    }

    public static class CallbackWrapper implements ActionMode.Callback {
        final ArrayList<SupportActionModeWrapper> mActionModes = new ArrayList<>();
        final Context mContext;
        final SimpleArrayMap<Menu, Menu> mMenus = new SimpleArrayMap<>();
        final ActionMode.Callback mWrappedCallback;

        public CallbackWrapper(Context context, ActionMode.Callback supportCallback) {
            this.mContext = context;
            this.mWrappedCallback = supportCallback;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return this.mWrappedCallback.onCreateActionMode(getActionModeWrapper(mode), getMenuWrapper(menu));
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return this.mWrappedCallback.onPrepareActionMode(getActionModeWrapper(mode), getMenuWrapper(menu));
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrappedCallback.onActionItemClicked(getActionModeWrapper(mode), MenuWrapperFactory.wrapSupportMenuItem(this.mContext, (SupportMenuItem) item));
        }

        public void onDestroyActionMode(ActionMode mode) {
            this.mWrappedCallback.onDestroyActionMode(getActionModeWrapper(mode));
        }

        private Menu getMenuWrapper(Menu menu) {
            Menu wrapper = this.mMenus.get(menu);
            if (wrapper != null) {
                return wrapper;
            }
            Menu wrapper2 = MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu) menu);
            this.mMenus.put(menu, wrapper2);
            return wrapper2;
        }

        public android.view.ActionMode getActionModeWrapper(ActionMode mode) {
            int count = this.mActionModes.size();
            for (int i = 0; i < count; i++) {
                SupportActionModeWrapper wrapper = this.mActionModes.get(i);
                if (wrapper != null && wrapper.mWrappedObject == mode) {
                    return wrapper;
                }
            }
            SupportActionModeWrapper wrapper2 = new SupportActionModeWrapper(this.mContext, mode);
            this.mActionModes.add(wrapper2);
            return wrapper2;
        }
    }
}
