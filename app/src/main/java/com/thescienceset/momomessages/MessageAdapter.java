package com.thescienceset.momomessages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> dataSet;
    Context mContext;

    public MessageAdapter(ArrayList<Message> data, Context context){
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    private static class ViewHolder{
        TextView tvType;
        TextView tvName;
        TextView tvAmount;
        TextView tvReference;
        TextView tvDate;
    }


    private int lastPosition = -1;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = getItem(position);
        ViewHolder viewHolder;
        final View view;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.tvAmount = convertView.findViewById(R.id.tvAmount);
            viewHolder.tvDate = convertView.findViewById(R.id.tvDate);
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvReference = convertView.findViewById(R.id.tvReference);
            viewHolder.tvType = convertView.findViewById(R.id.tvType);

            view = convertView;

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition)? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = position;

        viewHolder.tvReference.setText(message.getReference());
        viewHolder.tvName.setText(message.getName());
        viewHolder.tvDate.setText(message.getDate());
        viewHolder.tvAmount.setText(message.getAmount().toString());
        viewHolder.tvType.setText(message.getType());

        return convertView;
    }
}
