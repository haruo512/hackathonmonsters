package com.zeroone_creative.basicapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.util.ImageUtil;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shunhosaka on 2014/12/04.
 */
public class AnswerAdapter extends BaseAdapter {

    private List<ImageParseObject> mContent = new ArrayList<>();
    private LayoutInflater mInflater;

    public AnswerAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mContent.size();
    }

    @Override
    public ImageParseObject getItem(int position) {
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
        ImageParseObject parseObject = (ImageParseObject) getItem(position);
        holder.imageView.setImageBitmap(ImageUtil.decodeImageBase64(parseObject.getBody()));
        return convertView;
    }

    public void setContent(List<ImageParseObject> imageParseObjectList) {
        this.mContent = imageParseObjectList;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView imageView;
        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.item_image_imageview);
        }
    }

}
