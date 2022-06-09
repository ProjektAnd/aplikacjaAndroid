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
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Comment;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {


    private Context context;
    private List<Comment> commentList;

    public ReplyAdapter(Context context, List<Comment> commentList) {

        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyAdapter.ViewHolder holder, int position) {

        Comment comment = commentList.get(position);
        holder.tvComment.setText(comment.getComment());
        holder.tvAccount.setText(comment.getPublisherName());
        Glide.with(context).load(comment.getPublisherImage()).into(holder.imgAccount);

    }

    @Override
    public int getItemCount() {

        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAccount;
        TextView tvAccount, tvComment, tvReply;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvReply = itemView.findViewById(R.id.tv_comment_reply);
            imgAccount = itemView.findViewById(R.id.iv_comment);
            tvAccount = itemView.findViewById(R.id.tv_post_owner_name);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }

    }

}
