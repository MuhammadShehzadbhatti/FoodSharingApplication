package com.example.foodsharingapplication.products;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.squareup.picasso.Picasso;
public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolder(View itemView){
        super(itemView);

        mView = itemView;


        //Item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

    }

    public void setDetails(Context ctx, String title, String image, String logo, String price, String time){
        TextView mTitleView = mView.findViewById(R.id.textTitle);
        ImageView mImageView = mView.findViewById(R.id.imageView1);
        ImageView mLogo = mView.findViewById(R.id.imageView2);
        TextView mPrice = mView.findViewById(R.id.textPrice);
        TextView mTime = mView.findViewById(R.id.textTime);


        mTitleView.setText(title);
        mTime.setText(time);
        mPrice.setText(price);
        Picasso.get().load(logo).into(mLogo);
        Picasso.get().load(image).into(mImageView);

    }
    public void removeHolder(){
        LinearLayout card = mView.findViewById(R.id.holder);
        card.setVisibility(View.GONE);


    }

    private  ViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
