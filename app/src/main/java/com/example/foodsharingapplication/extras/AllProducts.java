package com.example.foodsharingapplication.extras;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllProducts extends RecyclerView.Adapter<AllProducts.ProductsViewHolder> {
    private Context mContext;
    private List<Products> productsList;

    Intent intent ;
    public AllProducts(Context context, List<Products> products) {

        mContext = context;
        productsList = products;

    }

    @NonNull
    @Override
    public AllProducts.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_grid, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProducts.ProductsViewHolder holder, int position) {
        Products products = productsList.get(position);
        intent = new Intent(mContext, productdetails.class);
        final String productTitle;
        final String productDesc;
        final String productLoc;
        final float productPrice;
        final String productImgUrl;
        if (products.getProductName()!=null) {
            productTitle = products.getProductName();
            holder.productName.setText(products.getProductName());
            intent.putExtra("pTitle", products.getProductName());
        }
        if (products.getProductDescription()!=null) {
            productDesc = products.getProductDescription();
            //holder.productDescription.setText(products.getProductDescription());
            intent.putExtra("pDesc", products.getProductDescription());
        }
        if (products.getProductImageUri()!=null) {
            productImgUrl = products.getProductImageUri();
            Picasso.get().load(products.getProductImageUri()).centerCrop().fit().into(holder.productImages);
            intent.putExtra("pImgUrl", products.getProductImageUri());
            if (holder.productImages != null) {
                holder.productImages.setMaxHeight(100);
            }
        }
        if (products.getProductPrice()>0) {
            productPrice = products.getProductPrice();
            holder.productPrice.setText(String.valueOf(products.getProductPrice()));
            intent.putExtra("pPrice", products.getProductPrice());
        }
        else if(products.getProductPrice()<=0){

            holder.productPrice.setText(String.valueOf(products.getProductPrice()));
            intent.putExtra("pPrice", "Free");
        }

        //holder.productDescription.setText(productDesc);
        //holder.productPrice.setText(String.valueOf(productPrice));
        //Picasso.get().load(productImgUrl).centerCrop().fit().into(holder.productImages);
        //if (holder.productImages != null) {
        //    holder.productImages.setMaxHeight(100);
        //}
        if (holder.productName != null) {
            holder.productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //SignleProduct signleProduct=new SignleProduct();
                    //signleProduct.setpName(productTitle);
                    v.getContext().startActivity(intent);
                }
            });
        }


       /* holder.productName.setText(products.getProductName());
        //holder.productLocation.setText(products.getProductLocation());
        holder.productPrice.setText(String.valueOf(products.getProductPrice()));
        Picasso.get().load(products.getProductImageUri()).centerCrop().fit().into(holder.productImages);
        if (holder.productImages!=null){
            holder.productImages.setMaxHeight(100);
        }*/

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImages;
        private TextView productName;
        private TextView productLocation;
        private TextView productPrice;
        private TextView productDescription;
        private TextView productRegion;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productImages = itemView.findViewById(R.id.productImages);
            productName = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            // productLocation = itemView.findViewById(R.id.productLocation);
        }
    }
}
