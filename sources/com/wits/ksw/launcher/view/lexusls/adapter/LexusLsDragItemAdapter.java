package com.wits.ksw.launcher.view.lexusls.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wits.ksw.R;
import com.wits.ksw.launcher.bean.lexusls.LexusLsAppSelBean;
import com.wits.ksw.launcher.model.LauncherViewModel;
import com.wits.ksw.launcher.utils.KswUtils;
import com.wits.ksw.launcher.view.lexusls.LexusLsConfig;
import com.wits.ksw.launcher.view.lexusls.drag.DragListener;
import com.wits.ksw.launcher.view.lexusls.drag.DraggableLayout;
import com.wits.ksw.launcher.view.lexusls.drag.LOGE;
import java.util.ArrayList;
import java.util.List;

public class LexusLsDragItemAdapter extends RecyclerView.Adapter<mBaseViewHolder> implements DragListener {
    public static final String TAG = "LexusLsDragItemAdapter";
    private View.OnClickListener clickListener = null;
    private View.OnClickListener clickMenuListener = null;
    private int defaultSelection = -1;
    private ItemDragListener dragListener = null;
    private View.OnLongClickListener longClickListener = null;
    IOnAddAppClickListener mAddAppListener;
    private View mClickView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public ArrayList<LexusLsAppSelBean> mList = new ArrayList<>();
    /* access modifiers changed from: private */
    public LauncherViewModel viewModel;

    public interface IOnAddAppClickListener {
        void onClick(View view);
    }

    public interface ItemDragListener {
        void onItemDragStarted(View view);

        void onItemDropCompleted(View view, View view2, boolean z);
    }

    public interface OnAdapterItemClickListener {
        void onAdapterItemClick(View view, int i);
    }

    public LexusLsDragItemAdapter(ArrayList<LexusLsAppSelBean> list, LauncherViewModel viewModel2, Context context) {
        this.mList = list;
        this.viewModel = viewModel2;
        this.mContext = context;
    }

    public mBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new mBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lexus_ls_drag_item_layout, parent, false));
    }

    public void setItemClickEffect(View mClickView2) {
        this.mClickView = mClickView2;
    }

    public void onBindViewHolder(mBaseViewHolder holder, int position) {
        LexusLsAppSelBean appItem = this.mList.get(position);
        holder.tv_title.setText(appItem.getAppName());
        holder.icon.setImageDrawable(appItem.getAppIcon());
        if (this.longClickListener != null) {
            holder.draggable_layout.setOnLongClickListener(this.longClickListener);
        }
        if (this.clickListener != null) {
            holder.draggable_layout.setOnClickListener(this.clickListener);
        }
        holder.draggable_layout.setAnimaView(holder.draggable_layout);
        holder.draggable_layout.setItem(appItem);
        holder.draggable_layout.canDelete(appItem.isDeletable());
        holder.draggable_layout.setDragListener(this);
        holder.draggable_layout.setAnimaView(holder.draggable_layout);
        holder.draggable_layout.setOnClickListener(new mItemOnclick(position, holder.draggable_layout));
        if (this.defaultSelection == -1) {
            this.defaultSelection = KswUtils.getLexusLsLastPosition(this.mContext);
        }
        Log.d(TAG, "onBindViewHolder: defaultSelection=" + this.defaultSelection);
        if (position == this.defaultSelection) {
            Log.e("pos == defaultSelection", "defaultSelection = " + this.defaultSelection);
            holder.layoutLL.setFocusableInTouchMode(true);
            holder.layoutLL.setFocusable(true);
            holder.layoutLL.requestFocus();
            holder.layoutLL.setSelected(true);
            return;
        }
        holder.layoutLL.setSelected(false);
    }

    public void setSelectPosition(int position) {
        if (position >= 0 && position <= this.mList.size()) {
            this.defaultSelection = position;
            notifyDataSetChanged();
        }
    }

    class mItemOnclick implements View.OnClickListener {
        View mView;
        int pos;

        mItemOnclick(int pos2, View mView2) {
            this.pos = pos2;
            this.mView = mView2;
        }

        public void onClick(View v) {
            Log.e(LexusLsDragItemAdapter.TAG, "pos = " + this.pos);
            KswUtils.saveLexusLsLastPosition(LexusLsDragItemAdapter.this.mContext, this.pos);
            LexusLsDragItemAdapter.this.setSelectPosition(this.pos);
            LexusLsAppSelBean appItem = (LexusLsAppSelBean) LexusLsDragItemAdapter.this.mList.get(this.pos);
            if (!LexusLsDragItemAdapter.isMenu(appItem)) {
                ComponentName componentName = new ComponentName(appItem.getAppPkg(), appItem.getAppMainAty());
                Intent intent = new Intent();
                intent.addFlags(270532608);
                intent.setComponent(componentName);
                LexusLsDragItemAdapter.this.mContext.startActivity(intent);
                return;
            }
            String pkgName = appItem.getAppPkg();
            String[] menuPkgs = LexusLsConfig.PKG_MENU_STRS;
            LOGE.E("LexusLsDragItemAdapter pkgName = " + pkgName);
            if (menuPkgs[0].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openNaviApp(v);
            } else if (menuPkgs[1].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openMusicMulti(v);
            } else if (menuPkgs[2].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openBtApp(v);
            } else if (menuPkgs[3].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openApps(v);
            } else if (menuPkgs[4].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openVideoMulti(v);
            } else if (menuPkgs[5].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openLexusCar(v);
            } else if (menuPkgs[6].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openSettings(v);
            } else if (menuPkgs[7].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openAirControl(v);
            } else if (menuPkgs[8].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openDvr(v);
            } else if (menuPkgs[9].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openDashboard(v);
            } else if (menuPkgs[10].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openShouJiHuLian(v);
            } else if (menuPkgs[11].equals(pkgName)) {
                LexusLsDragItemAdapter.this.viewModel.openFileManager(v);
            } else if (menuPkgs[12].equals(pkgName) && LexusLsDragItemAdapter.this.mAddAppListener != null) {
                LexusLsDragItemAdapter.this.mAddAppListener.onClick(v);
            }
        }
    }

    public void setOnClickAddApp(IOnAddAppClickListener tmpListener) {
        this.mAddAppListener = tmpListener;
    }

    public void setItemDragListener(ItemDragListener listener) {
        this.dragListener = listener;
    }

    public void onDragStarted(View source) {
        ItemDragListener itemDragListener = this.dragListener;
        if (itemDragListener != null) {
            itemDragListener.onItemDragStarted(source);
        }
    }

    public void onDropCompleted(View source, View target, boolean success) {
        ItemDragListener itemDragListener = this.dragListener;
        if (itemDragListener != null) {
            itemDragListener.onItemDropCompleted(source, target, success);
        }
    }

    public void setLongClickListener(View.OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setClickListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }

    class mMenuOnclick implements View.OnClickListener {
        int pos;

        mMenuOnclick(int pos2, View mView) {
            this.pos = pos2;
        }

        public void onClick(View v) {
        }
    }

    public void setClickMenuListener(View.OnClickListener tmplistener) {
        this.clickMenuListener = tmplistener;
    }

    public int getItemCount() {
        ArrayList<LexusLsAppSelBean> arrayList = this.mList;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public void notifyData(List<LexusLsAppSelBean> poiItemList) {
        if (poiItemList != null) {
            this.mList.clear();
            this.mList.addAll(poiItemList);
            notifyItemRangeChanged(0, poiItemList.size());
        }
    }

    public class mBaseViewHolder extends RecyclerView.ViewHolder {
        DraggableLayout draggable_layout;
        ImageView icon;
        RelativeLayout layoutLL;
        TextView tv_title;

        public mBaseViewHolder(View itemView) {
            super(itemView);
            this.layoutLL = (RelativeLayout) itemView.findViewById(R.id.layoutLL);
            this.draggable_layout = (DraggableLayout) itemView.findViewById(R.id.draggable_layout);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public static boolean isMenu(LexusLsAppSelBean bean) {
        if (bean.getAppPkg().contains(LexusLsConfig.PKG_DEFINED_MENU_LEXUSLS)) {
            return true;
        }
        return false;
    }
}
