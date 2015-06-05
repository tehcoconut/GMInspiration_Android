package com.gminspiration.tehcoconut.mobileapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by tehcoconut on 5/29/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String[] titles;
    private String name;
    private String date;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid;

        TextView textView;
        ImageView iv_profile;
        TextView tv_profileName;
        TextView tv_date;

        public ViewHolder(View itemView, int ViewType){
            super(itemView);

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.tv_listItem); // Creating TextView object with the id of textView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{


                tv_profileName = (TextView) itemView.findViewById(R.id.tv_name);         // Creating Text View object from header.xml for name
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);       // Creating Text View object from header.xml for email
                iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }
    }

    public MyAdapter(String titles[], String name, String date){
        this.titles = titles;
        this.name = name;
        this.date = date;
    }

    @Override
    public int getItemCount() {
        return titles.length+1;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == TYPE_ITEM){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_drawer,viewGroup,false);
            ViewHolder vhItem = new ViewHolder(v,i);

            return vhItem;
        }else if (i == TYPE_HEADER){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_header,viewGroup,false);
            ViewHolder vhHeader = new ViewHolder(v,i);

            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int i) {
        if(holder.Holderid == 1){
            holder.textView.setText(titles[i-1]);
        }else{
            holder.tv_profileName.setText(name);
            holder.tv_date.setText(date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
