package net.mankindenemy.diploma.projektProgramowanieAndroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Post;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.User;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.EditPostActivity;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.ImageActivity;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.ReactedActivity;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.PostDetailActivity;

import java.util.List;

import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.image.RoundedImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {

        this.context = context;
        this.postList = postList;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PostAdapter.ViewHolder holder, final int position) {

        final Post post = postList.get(position);
        publisherInfo(holder.imgUser, holder.nameUser, post.getPublisherId());
        Glide.with(context)
                .load(post.getPostImage())
                .into(holder.imgPost);
        holder.tvPost.setText(post.getPost());
        long time = (long) post.getTime();
        holder.tvTime.setText(getTimeAgo(time));
        holder.imgPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ImageActivity.class);
                intent.putExtra("IMAGE", post);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LikePostByKey").child(post.getKey());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getChildrenCount() == 0) {
                    holder.likeCount.setVisibility(View.INVISIBLE);
                }
                holder.likeCount.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.btPop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, holder.btPop);
                popupMenu.getMenuInflater().inflate(R.menu.menu_post, popupMenu.getMenu());
                Menu menu = popupMenu.getMenu();
                MenuItem itemEdit = menu.findItem(R.id.menu_edit);
                if (firebaseUser.getUid().equals(post.getPublisherId())) {
                    itemEdit.setVisible(true);
                } else {
                    itemEdit.setVisible(false);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                Intent intentEdit = new Intent(context, EditPostActivity.class);
                                intentEdit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentEdit.putExtra("POST", post);
                                context.startActivity(intentEdit);
                                break;
                            case R.id.menu_likes:
                                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.PREF), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(context.getString(R.string.KEY), post.getKey());
                                editor.apply();
                                Intent intent = new Intent(context, ReactedActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        isLikeByKey(post, holder, firebaseUser);
        holder.imgLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (holder.imgLike.getTag().equals("Like")) {
                    User user = new User(firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getUid());
                    FirebaseDatabase.getInstance().getReference("LikePostByKey").child(post.getKey()).child(firebaseUser.getUid()).setValue(user);
                    FirebaseDatabase.getInstance().getReference("PostLikeById").child(firebaseUser.getUid()).child(post.getKey()).setValue(post);

                } else {
                    FirebaseDatabase.getInstance().getReference("LikePostByKey").child(post.getKey()).removeValue();
                    FirebaseDatabase.getInstance().getReference("PostLikeById").child(firebaseUser.getUid()).child(post.getKey()).removeValue();
                }
            }
        });

    }

    private void isLikeByKey(final Post post, @NonNull final ViewHolder holder, final FirebaseUser firebaseUser) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LikePostByKey").child(post.getKey());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(firebaseUser.getUid()).exists()) {
                    holder.imgLike.setImageResource(R.drawable.ic_post_menu_favourite);
                    holder.imgLike.setTag("Liked");
                } else {
                    holder.imgLike.setImageResource(R.drawable.ic_post_menu_favourite_empty);
                    holder.imgLike.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {

        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RoundedImageView imgUser;
        private TextView nameUser;
        private TextView tvPost;
        private TextView tvTime;
        private ImageView imgPost;
        private ImageView imgLike;
        private ImageView imgShare;
        private TextView likeCount;
        private Button btPop;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            imgUser = itemView.findViewById(R.id.iv_rounded_post_owner_picture);
            nameUser = itemView.findViewById(R.id.tv_post_owner_name);
            tvPost = itemView.findViewById(R.id.tv_post_text_content);
            tvTime = itemView.findViewById(R.id.tv_post_creation_date);
            imgPost = itemView.findViewById(R.id.iv_post_image_content);
            imgLike = itemView.findViewById(R.id.iv_reactions);
            imgShare = itemView.findViewById(R.id.iv_share);
            btPop = itemView.findViewById(R.id.bt_menu_post);
            likeCount = itemView.findViewById(R.id.tv_favourites_amount);
            itemView.findViewById(R.id.frameComment).setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            Post post = postList.get(getAdapterPosition());
            long time = (long) post.getTime();
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("EXTRA", post);
            intent.putExtra("TIME_POST", time);
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
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    private void publisherInfo(final RoundedImageView publisherImage, final TextView publisherName, String publisherId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(publisherId);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getUserImage()).into(publisherImage);
                publisherName.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
