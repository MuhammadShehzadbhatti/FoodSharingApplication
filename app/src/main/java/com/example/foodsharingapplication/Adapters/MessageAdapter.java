package com.example.foodsharingapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsharingapplication.R;
import com.example.foodsharingapplication.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<ChatModel> chatModelList;
    private String imageurl;

    FirebaseAuth chatAuth;

    public MessageAdapter(Context mContext, List<ChatModel> chatModelList) {
        this.chatModelList = chatModelList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        ChatModel chatModel = chatModelList.get(position);

        Log.d("MessageAdapter", "list.message: " + chatModel.getMessage());
        holder.textView.setText(chatModel.getMessage());

       /* if (imageurl.equals("default")) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageurl).into(holder.imageView);
        }*/
    }

    //O(N) -> list of N items pe for loop lagay
    //O(1)

    @Override
    public int getItemCount() {
        Log.d("MessageAdapter", "size:" + chatModelList.size());
        return chatModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.show_message);

        }
    }

    @Override
    public int getItemViewType(int position) {
        chatAuth = FirebaseAuth.getInstance();
        if (chatModelList.get(position).getSender().getUserId().equals(chatAuth.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
