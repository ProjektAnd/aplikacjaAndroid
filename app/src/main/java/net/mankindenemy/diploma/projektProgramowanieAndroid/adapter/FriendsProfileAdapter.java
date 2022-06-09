package net.mankindenemy.diploma.projektProgramowanieAndroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Friend;

import java.util.List;

public class FriendsProfileAdapter extends RecyclerView.Adapter<FriendsProfileAdapter.ViewHolder> {

    private Context context;
    private List<Friend> userList;

    public FriendsProfileAdapter(Context context, List<Friend> userList) {

        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public FriendsProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsProfileAdapter.ViewHolder holder, int position) {

        final Friend user = userList.get(position);
        holder.tvNameUser.setText(user.getFriendName());
        Glide.with(context).load(user.getFriendImage()).into(holder.imageUser);

    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageUser;
        TextView tvNameUser;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageUser = itemView.findViewById(R.id.iv_rounded_profile_image);
            tvNameUser = itemView.findViewById(R.id.tv_user_name);
        }

    }

}
