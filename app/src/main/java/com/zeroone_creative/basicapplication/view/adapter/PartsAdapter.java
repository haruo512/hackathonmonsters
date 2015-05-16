package com.zeroone_creative.basicapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.util.ImageUtil;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.PartsParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shunhosaka on 2014/12/04.
 */
public class PartsAdapter extends BaseAdapter {

    private List<PartsParseObject> mContent = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public PartsAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mContent.size();
    }

    @Override
    public Object getItem(int position) {
        return mContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_image, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PartsParseObject parseObject = (PartsParseObject) getItem(position);
        Picasso.with(mContext).load(parseObject.getUrl()).into(holder.imageView);
        return convertView;
    }

    public void setContent(List<PartsParseObject> partsParseObjectList) {
        this.mContent = partsParseObjectList;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView imageView;
        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.item_image_imageview);
        }
    }

}
