package net.mankindenemy.diploma.projektProgramowanieAndroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Chat;

import java.util.List;
import java.util.Objects;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ID_SENDER) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_sender, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_receiver, parent, false));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvChat;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvChat = itemView.findViewById(R.id.tv_chat);
        }

    }

    private Context context;
    private List<Chat> chatList;

    public ChatAdapter(Context context, List<Chat> chatList) {

        this.context = context;
        this.chatList = chatList;
    }

    private int ID_RECEIVER = 0;
    private int ID_SENDER = 1;

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {

        if (position == ID_RECEIVER) {
            holder.tvChat.setText(chatList.get(position).getMessage());
        } else {
            holder.tvChat.setText(chatList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {

        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(Objects.requireNonNull(firebaseUser).getUid())) {
            return ID_SENDER;
        } else {
            return ID_RECEIVER;
        }
    }
}