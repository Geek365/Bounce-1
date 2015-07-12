package com.gishump.bounce.android;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

public class LevelGridPager extends PagerAdapter {

    private GridView[] pages;

    public LevelGridPager(GridView[] pages) {
        this.pages = pages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GridView page = pages[position];
        container.addView(page);
        return page;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pages[position]);
    }

    @Override
    public int getCount() {
        return pages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
