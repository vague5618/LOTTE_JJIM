package com.example.n.lotte_jjim.Activity.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.n.lotte_jjim.Activity.Fragment.PlaceholderFragment;


/**
*  tab 페이지 별로 fragment가 응답하게 하는 Adapter
*/
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionsPagerAdapter";

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //: fragment 초기화
        return PlaceholderFragment.newInstance(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        //: tab 갯수 설정
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "행사상품";
            case 1:
                return "도시락";
            case 2:
                return "과자";
            case 3:
                return "음료";
            case 4:
                return "캔디류";
            case 5:
                return "생활용품";
        }
        return null;
    }
}

