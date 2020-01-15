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
import com.example.foodsharingapplication.model.UploadModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private Context mContext;
    private List<UploadModel> productsList;

    Intent intent ;
    public ProductsAdapter(Context context, List<UploadModel> products) {

        mContext = context;
        productsList = products;

    }

    @NonNull
    @Override
    public ProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_view, parent, false);
        return new ProductsAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        UploadModel products = productsList.get(position);

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