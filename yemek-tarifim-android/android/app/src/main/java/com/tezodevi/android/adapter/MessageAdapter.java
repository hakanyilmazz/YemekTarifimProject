package com.tezodevi.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tezodevi.android.databinding.MessageLayoutBinding;
import com.tezodevi.android.model.MessageModel;
import com.tezodevi.android.util.ImageHelper;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<MessageModel> messageList;

    public MessageAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageLayoutBinding binding = MessageLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel messageModel = messageList.get(position);

        holder.binding.messageLayoutUsernameText.setText(messageModel.getUsername());
        holder.binding.messageLayoutMessageText.setText(messageModel.getMessage());
        holder.binding.imageView.setImageBitmap(ImageHelper.toBitmap(messageModel.getPhoto()));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final MessageLayoutBinding binding;

        public MessageViewHolder(MessageLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
