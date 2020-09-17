// Copyright (c) 1998-2015 Core Solutions Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION CNT.5.0.1
// ============================================================================
// CHANGE LOG
// CNT.5.0.1 : 2015-XX-XX, frank.yu, creation
// ============================================================================
package usage.ywb.personal.audio.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import usage.ywb.personal.audio.R;

/**
 * @author frank.yu
 */
public class LyricAdapter extends BaseAdapter {

    private Context context;
    private List<String> texts;

    private int position;

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }


    public LyricAdapter(Context context, List<String> texts) {
        this.context = context;
        this.texts = texts;
    }

    public void reset(List<String> texts) {
        this.texts = texts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return texts.size();
    }

    @Override
    public Object getItem(int position) {
        return texts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText(texts.get(position));
        if(this.position == position){
            textView.setTextColor(context.getResources().getColor(R.color.drag));
        }else{
            textView.setTextColor(context.getResources().getColor(R.color.gray_light));
        }
        return textView;
    }


}
