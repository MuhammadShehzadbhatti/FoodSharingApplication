package com.example.foodsharingapplication.products;

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
import com.example.foodsharingapplication.extras.AllProducts;
import com.example.foodsharingapplication.extras.Products;
import com.example.foodsharingapplication.extras.productdetails;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.model.UserUploadFoodModel;
import com.example.foodsharingapplication.userOrdersAndUploadedAds.UserOrderAndUploads;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private Context mContext;
    private List<UserUploadFoodModel> productsList;
    private View.OnClickListener listener;

    Intent intent ;
    public ProductsAdapter(Context context, List<UserUploadFoodModel> products) {

        mContext = context;
        productsList = products;

    }

    @NonNull
    @Override
    public ProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_grid, parent, false);
        return new ProductsAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        UserUploadFoodModel products = productsList.get(position);
        intent = new Intent(mContext, UserOrderAndUploads.class);
        final String productTitle;
        final String productDesc;
        final String productLoc;
        final String productPrice;
        final String productImgUrl;
            System.out.println("pTitle, " +products.getFoodTitle());
            System.out.println("pDesc, " +products.getFoodTitle());
            productTitle = products.getFoodTitle();
            holder.productName.setText(productTitle);
            intent.putExtra("pTitle", productTitle);

            productDesc = products.getFoodDescription();
            //holder.productDescription.setText(products.getProductDescription());
            intent.putExtra("pDesc", productDesc);

            productPrice = products.getFoodPrice();
            holder.productPrice.setText(String.valueOf(productPrice));
            intent.putExtra("pPrice", productPrice);

        holder.itemView.setTag(products);
        holder.itemView.setOnClickListener(listener);

        /*if (holder.productName != null) {
            holder.productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //SignleProduct signleProduct=new SignleProduct();
                    //signleProduct.setpName(productTitle);
                    v.getContext().startActivity(intent);
                }
            });
        }*/

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
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
            productName = itemView.findViewById(R.id.textTitle);
            productPrice = itemView.findViewById(R.id.textPrice);
            // productLocation = itemView.findViewById(R.id.productLocation);
        }
    }
}