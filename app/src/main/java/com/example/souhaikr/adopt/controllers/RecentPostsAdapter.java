package com.example.souhaikr.adopt.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.entities.User;
import com.example.souhaikr.adopt.interfaces.APIInterface;
import com.example.souhaikr.adopt.utils.DetailsActivity;
import com.example.souhaikr.adopt.utils.UserProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SouhaiKr on 07/01/2019.
 */

public class RecentPostsAdapter extends  RecyclerView.Adapter<RecentPostsAdapter.MyViewHolder>{

    private Context mContext ;
    private List<Pet> mData ;


    public RecentPostsAdapter(Context mContext, List<Pet> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public RecentPostsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.recent_pet_item,parent,false);
        return new RecentPostsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentPostsAdapter.MyViewHolder holder, final int position) {

        holder.user_name.setText(mData.get(position).getName());
        String url = mData.get(position).getImage() ;
        if(url!=null) {
            Picasso.get().load(ImageServer.chemin+url).into(holder.user_image);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,DetailsActivity.class);



                int id = mData.get(position).getId() ;
                intent.putExtra("id",String.valueOf(id));


                mContext.startActivity(intent);

            }
        });







    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;
        ImageView user_image;
        CardView cardView ;


        public MyViewHolder(View itemView) {
            super(itemView);

            user_name =  itemView.findViewById(R.id.name) ;
            user_image =  itemView.findViewById(R.id.pet_image);
            cardView =  itemView.findViewById(R.id.cardview_id);



        }
    }
}
