package com.example.souhaikr.adopt.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.utils.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by SouhaiKr on 08/12/2018.
 */

public class CustomerAdapter extends BaseAdapter {
    Context context;
    List<Pet> rowItems;

    public CustomerAdapter(Context context, List<Pet> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* private view holder class */
    private class ViewHolder {
        ImageView pet_pic;
        TextView pet_name;
        RelativeLayout view ;

        TextView pet_desc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

//        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.pet_name =  convertView
                    .findViewById(R.id.pet_name);
            holder.pet_pic = convertView
                    .findViewById(R.id.pet_pic);

            holder.pet_desc =  convertView
                    .findViewById(R.id.pet_desc);
            holder.view =  convertView
                    .findViewById(R.id.view_foreground);



            convertView.setTag(holder);
//        } else {
            holder = (ViewHolder) convertView.getTag();

        // holder.tvName.setText("Bla Bla Bla");

        holder.pet_name.setText(rowItems.get(position).getName());
            holder.pet_desc.setText(rowItems.get(position).getDesc());

            String url = rowItems.get(position).getImage() ;


        Picasso.get().load(ImageServer.chemin+url).into(holder.pet_pic);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DetailsActivity.class);




                    int id = rowItems.get(position).getId() ;
                    intent.putExtra("id",String.valueOf(id));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

                }
            });



        return convertView;
    }
}


//public class CustomerAdapter extends  RecyclerView.Adapter<CustomerAdapter.MyViewHolder> implements ListAdapter {
//
//    private Context mContext ;
//    private List<Pet> mData ;
//
//
//    public CustomerAdapter(Context mContext, List<Pet> mData) {
//        this.mContext = mContext;
//        this.mData = mData;
//    }
//
//    @Override
//    public CustomerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view ;
//        LayoutInflater mInflater = LayoutInflater.from(mContext);
//        view = mInflater.inflate(R.layout.list_item,parent,false);
//        return new CustomerAdapter.MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(CustomerAdapter.MyViewHolder holder, final int position) {
//
//
//        holder.pet_name.setText(mData.get(position).getName());
//        holder.pet_desc.setText(mData.get(position).getDesc());
//
//        String url = mData.get(position).getImage() ;
//
//
//        Picasso.get().load(ImageServer.chemin+url).into(holder.pet_pic);
//
//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, DetailsActivity.class);
//
//
//
//
//                int id = mData.get(position).getId() ;
//                intent.putExtra("id",String.valueOf(id));
//                mContext.startActivity(intent);
//
//            }
//        });
//
//    }
//
//
//
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView pet_pic;
//        TextView pet_name;
//        RelativeLayout view ;
//
//        TextView pet_desc;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//
//            pet_name =
//                    itemView.findViewById(R.id.pet_name);
//            pet_pic = itemView.findViewById(R.id.pet_pic);
//
//            pet_desc =  itemView.
//                    findViewById(R.id.pet_desc);
//            view =  itemView
//                    .findViewById(R.id.view_foreground);
//
//        }
//    }
//}
