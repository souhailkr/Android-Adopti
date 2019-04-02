package com.example.souhaikr.adopt.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.souhaikr.adopt.R;
import com.example.souhaikr.adopt.entities.Pet;
import com.example.souhaikr.adopt.utils.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by SouhaiKr on 09/12/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Pet> mData ;


    public RecyclerViewAdapter(Context mContext, List<Pet> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.linear_layout_pet,parent,false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, final int position) {

        holder.tv_book_title.setText(mData.get(position).getName());
        holder.pet_breed.setText(mData.get(position).getBreed());

        String url = mData.get(position).getImage() ;


        Picasso.get().load(ImageServer.chemin+url).into(holder.img_book_thumbnail);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,DetailsActivity.class);

                // passing data to the book activity

               int id = mData.get(position).getId() ;
                intent.putExtra("id",String.valueOf(id));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                // start the activity
                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_title;
        TextView pet_breed;

        ImageView img_book_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_book_title = (TextView) itemView.findViewById(R.id.pet_title_id) ;
            pet_breed = (TextView) itemView.findViewById(R.id.pet_breed) ;

            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.pet_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_p);



        }
    }


}