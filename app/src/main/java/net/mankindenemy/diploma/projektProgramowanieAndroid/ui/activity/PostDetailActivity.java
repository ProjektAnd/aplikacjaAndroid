package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.adapter.CommentAdapter;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Comment;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Post;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private TextView tvName, tvAccount, tvTime, tvTextPost;
    private ImageView imgPost, imgFavourite, imgComment, imgShare, image_profile;
    private RecyclerView recyclerView;
    private EditText etComment;
    private Button btComment;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private List<Comment> commentList = new ArrayList<>();
    private TextView tvComment;
    private String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        findViewById(R.id.bt_return).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        initViews();
        initData();
        tvComment = findViewById(R.id.tv_comment);
    }

    private void initViews() {

        tvName = findViewById(R.id.tv_user_name);
        tvAccount = findViewById(R.id.tv_post_owner_name);
        tvTime = findViewById(R.id.tv_post_creation_date);
        tvTextPost = findViewById(R.id.tv_post_text_content);
        imgPost = findViewById(R.id.iv_post_image_content);
        imgFavourite = findViewById(R.id.iv_reactions);
        imgComment = findViewById(R.id.iv_comment);
        image_profile = findViewById(R.id.iv_rounded_post_owner_picture);
        imgShare = findViewById(R.id.iv_share);
        recyclerView = findViewById(R.id.rvComment);
        etComment = findViewById(R.id.et_comment);
        btComment = findViewById(R.id.bt_enter_comment);
        recyclerView = findViewById(R.id.rvComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        final Post post = getIntent().getParcelableExtra("EXTRA");
        if (post != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(post.getPublisherId());
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user = snapshot.getValue(User.class);
                    tvName.setText(user.getUserName() + " Post");
                    tvAccount.setText(user.getUserName());
                    Glide.with(getApplicationContext())
                            .load(user.getUserImage())
                            .into(image_profile);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            tvTextPost.setText(post.getPost());
            Glide.with(getApplicationContext())
                    .load(post.getPostImage())
                    .into(imgPost);

            String time = getTimeAgo(getIntent().getExtras().getLong("TIME_POST"));
            tvTime.setText(time);

            btComment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    DatabaseReference databaseReference = firebaseDatabase.getReference("Comment").child(post.getKey()).push();
                    String tvComment = etComment.getText().toString();
                    String name = firebaseUser.getDisplayName();
                    String image = firebaseUser.getPhotoUrl().toString();
                    String id = firebaseUser.getUid();

                    Comment comment = new Comment(tvComment, name, image, id);
                    comment.setKey(databaseReference.getKey());
                    databaseReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            });

            if (firebaseDatabase != null) {
                DatabaseReference reference = firebaseDatabase.getReference("Comment").child(post.getKey());
                reference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        commentList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            commentList.add(comment);
                            etComment.setText("");
                            tvComment.setText("" + commentList.size());
                        }

                        recyclerView.setAdapter(new CommentAdapter(getApplicationContext(), commentList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
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


    private void userStatus(String userStatus) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userStatus", userStatus);
        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {

        super.onResume();
        userStatus = "Online";
        userStatus(userStatus);
    }

    @Override
    protected void onPause() {

        super.onPause();
        userStatus = "Offline";
        userStatus(userStatus);
    }

}
