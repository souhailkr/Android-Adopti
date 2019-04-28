package com.esprit.souhaikr.adopt.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.entities.Pet;
import com.esprit.souhaikr.adopt.utils.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by SouhaiKr on 30/12/2018.
 */

public class FollowingPostsAdapter extends  RecyclerView.Adapter<FollowingPostsAdapter.MyViewHolder>{

    private Context mContext ;
    private List<Pet> mData ;


    public FollowingPostsAdapter(Context mContext, List<Pet> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public FollowingPostsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.post_item_listview,parent,false);
        return new FollowingPostsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FollowingPostsAdapter.MyViewHolder holder, final int position) {

        holder.post_name.setText(mData.get(position).getName());
        holder.post_desc.setText(mData.get(position).getDesc());

        String user_image = mData.get(position).getUser().getPhoto();

        if (user_image != null) {
            Picasso.get().load(ImageServer.chemin + user_image).into(holder.author_image);
        }



        String url = mData.get(position).getImage();
        if (url != null) {
            Picasso.get().load(ImageServer.chemin+url).into(holder.post_image);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailsActivity.class);


                int id = mData.get(position).getId();
                intent.putExtra("id", String.valueOf(id));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView post_name;
        TextView post_desc;

        ImageView post_image;
        ImageView author_image;

        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            post_name =  itemView.findViewById(R.id.titleTextView) ;
            post_desc =  itemView.findViewById(R.id.detailsTextView) ;

            post_image =  itemView.findViewById(R.id.postImageView);
            author_image =  itemView.findViewById(R.id.authorImageView);

            cardView =  itemView.findViewById(R.id.card_view);


        }
    }
}
