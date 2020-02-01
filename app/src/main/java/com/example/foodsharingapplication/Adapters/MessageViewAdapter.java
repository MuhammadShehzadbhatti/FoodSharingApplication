package com.example.foodsharingapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.User;

import java.util.List;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.ViewHolder> {

    private Context mContext;
    private List<User> userModels;
    private View.OnClickListener listener;

    public MessageViewAdapter(Context mContext, List<User> userModels) {
        this.mContext = mContext;
        this.userModels = userModels;
    }

    @NonNull
    @Override
    public MessageViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new MessageViewAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageViewAdapter.ViewHolder holder, int position) {

        User userModel = userModels.get(position);
        holder.username.setText("Chat With " + userModel.getUserName());

        holder.itemView.setTag(userModel);
        holder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView productName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            productName = itemView.findViewById(R.id.product_title);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
