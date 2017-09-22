package io.github.kolacbb.translate.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.data.entity.SmsEntry;

/**
 * Created by Kola on 2016/6/26.
 */
public class SmsAdapter extends BaseAdapter {
    List<SmsEntry> mData;
    private LayoutInflater mInflater;

    public SmsAdapter(Context context, List<SmsEntry> data){
        this.mData = data;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<SmsEntry> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.card_sms_entry, null);
            holder.addressTV = (TextView) convertView.findViewById(R.id.txt_sms_address);
            holder.dateTv = (TextView) convertView.findViewById(R.id.txt_sms_date);
            holder.contentTV = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SmsEntry sms = mData.get(position);
        holder.addressTV.setText(sms.getAddress());
        holder.dateTv.setText(sms.getDate());
        holder.contentTV.setText(sms.getContent());
        return convertView;
    }

    class ViewHolder {
        public TextView addressTV;
        public TextView dateTv;
        public TextView contentTV;
    }
}
