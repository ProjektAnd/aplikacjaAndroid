package net.mankindenemy.diploma.projektProgramowanieAndroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.ReplyCommentActivity;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    private Context context;
    private List<Comment> commentList;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public CommentAdapter(Context context, List<Comment> commentList) {

        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

        Comment comment = commentList.get(position);

        holder.tvComment.setText(comment.getComment());
        holder.tvAccount.setText(comment.getPublisherName());

        long time = (long) comment.getTime();

        holder.tvTime.setText(getTimeAgo(time));

        Glide.with(context).load(comment.getPublisherImage()).into(holder.imgAccount);

    }

    @Override
    public int getItemCount() {

        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgAccount;
        TextView tvAccount, tvComment, tvReply, tvTime;

        ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvTime = itemView.findViewById(R.id.tv_post_creation_date);
            tvReply = itemView.findViewById(R.id.tv_comment_reply);
            imgAccount = itemView.findViewById(R.id.iv_comment);
            tvAccount = itemView.findViewById(R.id.tv_post_owner_name);
            tvComment = itemView.findViewById(R.id.tv_comment);

            tvReply.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Comment comment = commentList.get(getAdapterPosition());
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PREF), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("KEY", comment.getKey());
            editor.apply();
            Intent intent = new Intent(context, ReplyCommentActivity.class);
            intent.putExtra("EXTRA_COMMENT", comment);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }


    private static String getTimeAgo(long time) {

        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a m";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " m";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hrs";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days";
        }
    }

}
