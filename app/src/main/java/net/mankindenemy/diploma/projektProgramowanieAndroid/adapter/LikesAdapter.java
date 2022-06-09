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
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.User;

import java.util.List;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public LikesAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public LikesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_likes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikesAdapter.ViewHolder holder, int position) {

        User user = userList.get(position);
        holder.tvNameUser.setText(user.getUserName());
        Glide.with(context).load(user.getUserImage()).into(holder.imageUser);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUser;
        TextView tvNameUser;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.iv_rounded_user_image);
            tvNameUser = itemView.findViewById(R.id.tv_user_credentials);
        }
    }
}
