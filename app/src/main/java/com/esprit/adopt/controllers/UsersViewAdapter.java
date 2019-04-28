package com.esprit.souhaikr.adopt.controllers;

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

import com.esprit.souhaikr.adopt.R;
import com.esprit.souhaikr.adopt.entities.User;
import com.esprit.souhaikr.adopt.interfaces.APIInterface;
import com.esprit.souhaikr.adopt.utils.BottomNavigationActivity;
import com.esprit.souhaikr.adopt.utils.UserProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SouhaiKr on 09/12/2018.
 */

public class UsersViewAdapter extends  RecyclerView.Adapter<UsersViewAdapter.MyViewHolder>{

    private Context mContext ;
    private List<User> mData ;


    public UsersViewAdapter(Context mContext, List<User> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public UsersViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.layout_useritem,parent,false);
        return new UsersViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersViewAdapter.MyViewHolder holder, final int position) {

        holder.user_name.setText(mData.get(position).getFirstname());
        String url = mData.get(position).getPhoto() ;
        if(url!=null) {
            Picasso.get().load(ImageServer.chemin+url).into(holder.user_image);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,UserProfileActivity.class);



                int id = mData.get(position).getId() ;
                intent.putExtra("id",String.valueOf(id));


                mContext.startActivity(intent);

            }
        });

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnFollow.getText().toString().equalsIgnoreCase("Follow me")) {
                    holder.btnFollow.setText("Following");

                   int id = mData.get(position).getId() ;


                    APIInterface apiInterface;

                    apiInterface = APIClient.getClient().create(APIInterface.class);

                    Call<User> callApi = apiInterface.followUser(BottomNavigationActivity.id,id);
                    callApi.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> callApi, Response<User> response) {


                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }});}

                mData.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(),mData.size());

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
        Button btnFollow ;

        public MyViewHolder(View itemView) {
            super(itemView);

           user_name =  itemView.findViewById(R.id.name) ;
           user_image =  itemView.findViewById(R.id.user_image);
            cardView =  itemView.findViewById(R.id.cardview_id);
            btnFollow = itemView.findViewById(R.id.btnFollow) ;


        }
    }
}
