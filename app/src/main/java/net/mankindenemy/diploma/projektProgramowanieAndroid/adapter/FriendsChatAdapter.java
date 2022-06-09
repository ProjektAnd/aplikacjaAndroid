package net.mankindenemy.diploma.projektProgramowanieAndroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Friend;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.ChatActivity;

import java.util.List;

public class FriendsChatAdapter extends RecyclerView.Adapter<FriendsChatAdapter.ViewHolder> {

    private Context context;
    private List<Friend> userList;

    public FriendsChatAdapter(Context context, List<Friend> userList) {

        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public FriendsChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsChatAdapter.ViewHolder holder, int position) {

        final Friend user = userList.get(position);
        holder.tvNameUser.setText(user.getFriendName());
        Glide.with(context).load(user.getFriendImage()).into(holder.imageUser);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.EXTRA_USER, user);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.btAdd.setVisibility(View.INVISIBLE);
        holder.bio.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageUser;
        TextView tvNameUser, bio;
        Button btAdd;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageUser = itemView.findViewById(R.id.iv_rounded_user_image);
            tvNameUser = itemView.findViewById(R.id.tv_user_credentials);
            btAdd = itemView.findViewById(R.id.bt_add_friend);
            bio = itemView.findViewById(R.id.tv_user_description);
        }

    }

}
