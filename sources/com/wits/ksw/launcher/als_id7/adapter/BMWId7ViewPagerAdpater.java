package com.wits.ksw.launcher.als_id7.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.wits.ksw.launcher.als_id7.fragment.DashVideoFragment;
import com.wits.ksw.launcher.als_id7.fragment.MusicPhoneFragment;
import com.wits.ksw.launcher.als_id7.fragment.NaviCarFragment;
import java.util.ArrayList;
import java.util.List;

public class BMWId7ViewPagerAdpater extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList();
    private Fragment musicFragment = new MusicPhoneFragment();
    private Fragment naviFragment = new NaviCarFragment();
    private Fragment videoFragment = new DashVideoFragment();

    public BMWId7ViewPagerAdpater(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentList.add(this.naviFragment);
        this.fragmentList.add(this.musicFragment);
        this.fragmentList.add(this.videoFragment);
    }

    public Fragment getItem(int position) {
        List<Fragment> list = this.fragmentList;
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    public int getCount() {
        List<Fragment> list = this.fragmentList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
