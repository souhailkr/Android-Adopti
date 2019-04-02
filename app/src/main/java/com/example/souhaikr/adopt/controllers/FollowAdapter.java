package com.example.souhaikr.adopt.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.entities.User;
import com.example.souhaikr.adopt.interfaces.APIInterface;
import com.example.souhaikr.adopt.utils.UserProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SouhaiKr on 18/12/2018.
 */

public class FollowAdapter extends BaseAdapter  {
    Context context;
    List<User> rowItems;

    public FollowAdapter(Context context, List<User> rowItems) {
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
        ImageView user_pic;
        TextView user_name;
        LinearLayout linear_layout ;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

//        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.follower_listview, null);
            holder = new ViewHolder();

            holder.user_name =  convertView
                    .findViewById(R.id.follower_name);
            holder.user_pic = convertView
                    .findViewById(R.id.follower_image);
        holder.linear_layout = convertView
                .findViewById(R.id.linear_layout);




            convertView.setTag(holder);
//        } else {
            holder = (ViewHolder) convertView.getTag();

            // holder.tvName.setText("Bla Bla Bla");

            holder.user_name.setText(rowItems.get(position).getFirstname());
            String url = rowItems.get(position).getPhoto() ;
            //String chemin = "http://192.168.1.5:3000/uploads/" ;

            if (url!=null) {
                Picasso.get().load(ImageServer.chemin+url).into(holder.user_pic);
            }

            holder.linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,UserProfileActivity.class);



                    int id = rowItems.get(position).getId() ;
                    intent.putExtra("id",String.valueOf(id));


                    context.startActivity(intent);

                }
            });





        return convertView;
    }
}
