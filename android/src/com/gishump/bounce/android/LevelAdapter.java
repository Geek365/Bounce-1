package com.gishump.bounce.android;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.view.View.OnClickListener;

public class LevelAdapter extends BaseAdapter {
        private Context mContext;
        private final int gridNumber;
        private final int maximumLevel;
        private OnClickListener callback;

        public LevelAdapter(Context c, int gridNum, int max, OnClickListener callback) {
            mContext = c;
            gridNumber = gridNum;
            maximumLevel = max;
            this.callback = callback;
        }

        public int getCount() {
            if ((gridNumber+1)*16>maximumLevel) { return maximumLevel - ((gridNumber)*16); }
            else { return 16; }
        }

        public int getGridNumber() { return gridNumber; }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int kent, View convertView, ViewGroup parent) {
            Button button;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                button = new Button(mContext);
                button.setLayoutParams(new GridView.LayoutParams(100, 100));
                button.setPadding(0, 0, 0, 0);
            }
            else {
                button = (Button) convertView;
            }
            button.setOnClickListener(callback);
            button.setTag ((gridNumber * 16) + kent + 1);
            button.setText(String.valueOf((gridNumber * 16) + kent + 1));
            return button;
        }
    }
