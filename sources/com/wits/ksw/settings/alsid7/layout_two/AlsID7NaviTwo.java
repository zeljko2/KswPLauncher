package com.wits.ksw.settings.alsid7.layout_two;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import com.wits.ksw.R;
import com.wits.ksw.settings.alsid7.adapter.AlsID7NaviAdapter;
import com.wits.ksw.settings.id7.bean.MapBean;
import com.wits.ksw.settings.utlis_view.FileUtils;
import com.wits.ksw.settings.utlis_view.KeyConfig;
import com.wits.ksw.settings.utlis_view.ScanNaviList;
import com.wits.pms.statuscontrol.PowerManagerApp;
import java.util.List;

public class AlsID7NaviTwo extends RelativeLayout {
    private Context context;
    private LinearLayoutManager layoutManager;
    /* access modifiers changed from: private */
    public List<MapBean> mapBanList;
    private AlsID7NaviAdapter naviAdapter;
    private int naviMin = 1;
    private RecyclerView navi_recycle;
    private RadioGroup rdg_naviv;
    private RelativeLayout relate_app;
    private RelativeLayout relate_naviv;

    public AlsID7NaviTwo(Context context2) {
        super(context2);
        this.context = context2;
        View view = LayoutInflater.from(context2).inflate(R.layout.als_layout_set_navi_two, (ViewGroup) null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        initData();
        initView(view);
        view.setLayoutParams(layoutParams);
        addView(view);
    }

    public void updateMapList(List<MapBean> mapList) {
        this.mapBanList = mapList;
        Log.d("Navi", "mapBanList size: " + this.mapBanList.size());
        this.naviAdapter.notifyDataSetChanged();
    }

    private void initData() {
        try {
            this.naviMin = PowerManagerApp.getSettingsInt(KeyConfig.NAVI_VOICE_MIX);
            Log.d("Navi", "==min==:" + this.naviMin);
            this.mapBanList = ScanNaviList.getInstance().getMapList();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void initView(View view) {
        this.relate_naviv = (RelativeLayout) view.findViewById(R.id.relate_naviv);
        this.relate_app = (RelativeLayout) view.findViewById(R.id.relate_app);
        this.navi_recycle = (RecyclerView) view.findViewById(R.id.navi_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        this.layoutManager = linearLayoutManager;
        linearLayoutManager.setOrientation(1);
        this.navi_recycle.setLayoutManager(this.layoutManager);
        this.naviAdapter = new AlsID7NaviAdapter(this.context, this.mapBanList);
        Log.d("Navi", "init  mapBanList " + this.mapBanList.size());
        this.navi_recycle.setAdapter(this.naviAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(this.context, 1);
        divider.setDrawable(ContextCompat.getDrawable(this.context, R.mipmap.id7_lp_line));
        this.navi_recycle.addItemDecoration(divider);
        this.naviAdapter.registCheckListener(new AlsID7NaviAdapter.IrbtCheckListener() {
            public void checkListener(int pos) {
                MapBean mapBean = (MapBean) AlsID7NaviTwo.this.mapBanList.get(pos);
                if (mapBean != null) {
                    FileUtils.savaStringData(KeyConfig.NAVI_DEFUAL, mapBean.getPackageName());
                }
            }
        });
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rdg_naviv);
        this.rdg_naviv = radioGroup;
        switch (this.naviMin) {
            case 0:
                radioGroup.check(R.id.rdb_naviv1);
                break;
            case 1:
                radioGroup.check(R.id.rdb_naviv2);
                break;
            case 2:
                radioGroup.check(R.id.rdb_naviv3);
                break;
            case 3:
                radioGroup.check(R.id.rdb_naviv4);
                break;
            case 4:
                radioGroup.check(R.id.rdb_naviv5);
                break;
            case 5:
                radioGroup.check(R.id.rdb_naviv6);
                break;
        }
        this.rdg_naviv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_naviv1 /*2131297186*/:
                        FileUtils.savaIntData(KeyConfig.NAVI_VOICE_MIX, 0);
                        return;
                    case R.id.rdb_naviv2 /*2131297187*/:
                        FileUtils.savaIntData(KeyConfig.NAVI_VOICE_MIX, 1);
                        return;
                    case R.id.rdb_naviv3 /*2131297188*/:
                        FileUtils.savaIntData(KeyConfig.NAVI_VOICE_MIX, 2);
                        return;
                    case R.id.rdb_naviv4 /*2131297189*/:
                        FileUtils.savaIntData(KeyConfig.NAVI_VOICE_MIX, 3);
                        return;
                    case R.id.rdb_naviv5 /*2131297190*/:
                        FileUtils.savaIntData(KeyConfig.NAVI_VOICE_MIX, 4);
                        return;
                    case R.id.rdb_naviv6 /*2131297191*/:
                        FileUtils.savaIntData(KeyConfig.NAVI_VOICE_MIX, 5);
                        return;
                    default:
                        return;
                }
            }
        });
    }

    public void showLayout(int index) {
        switch (index) {
            case 0:
                this.relate_naviv.setVisibility(0);
                this.relate_app.setVisibility(8);
                return;
            case 1:
                this.relate_naviv.setVisibility(8);
                this.relate_app.setVisibility(0);
                return;
            default:
                return;
        }
    }
}
