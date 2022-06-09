package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.adapter.FriendsProfileAdapter;
import net.mankindenemy.diploma.projektProgramowanieAndroid.adapter.PostAdapter;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Friend;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Post;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.image.RoundedImageView;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView, rvFriends;
    private List<Post> postList = new ArrayList<>();
    private List<Friend> userList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private TextView emptyPostList;
    private FirebaseUser firebaseUser;
    private String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.bt_create_post).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), UploadPostActivity.class));
            }
        });

        findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });
        emptyPostList = findViewById(R.id.tv_post_feed);
        firebaseDatabase = FirebaseDatabase.getInstance();
        RoundedImageView imgProfile = findViewById(R.id.iv_rounded_profile_image);
        TextView tvToolbar = findViewById(R.id.tv_toolbar);
        TextView tvName = findViewById(R.id.tv_user_name);
        Button editProfile = findViewById(R.id.editProfile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RoundedImageView accountImage = findViewById(R.id.bt_profile);
        Glide.with(getApplicationContext()).load(firebaseUser.getPhotoUrl()).into(accountImage);
        final User user = getIntent().getParcelableExtra(ChatActivity.EXTRA_USER);
        if (user != null) {
            Glide.with(getApplicationContext()).load(user.getUserImage()).into(imgProfile);
            tvToolbar.setText(user.getUserName());
            tvName.setText(user.getUserName());
            getPost(user);
            getFriends(user);
            if (user.getUserId().equals(firebaseUser.getUid())) {
                editProfile.setVisibility(View.VISIBLE);
            } else {
                editProfile.setVisibility(View.GONE);
            }
            findViewById(R.id.tv_show_all_friends).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
                    intent.putExtra("IDUSER", user.getUserId());
                    startActivity(intent);
                }
            });
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.getUserId());
            getDetailMe(databaseReference);
            View view2 = findViewById(R.id.cosmetic_line_friends);
            View view3 = findViewById(R.id.cosmetic_line_posts);
            view2.setVisibility(View.INVISIBLE);
            view3.setVisibility(View.INVISIBLE);
            TextView post = findViewById(R.id.tv_post);
            post.setVisibility(View.INVISIBLE);
            LinearLayout linearLayout = findViewById(R.id.ll_post_create);
            linearLayout.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(getApplicationContext()).load(firebaseUser.getPhotoUrl()).into(imgProfile);
            tvToolbar.setText(firebaseUser.getDisplayName());
            tvName.setText(firebaseUser.getDisplayName());
            getUserPost();
            getUserFriends();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
            getDetailMe(databaseReference);
            findViewById(R.id.tv_show_all_friends).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(), FriendActivity.class));
                }
            });
        }
    }

    private void getUserPost() {

        recyclerView = findViewById(R.id.rv_profile_post_feed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference databaseReference = firebaseDatabase.getReference("PostById").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                    emptyPostList.setVisibility(View.INVISIBLE);
                }

                PostAdapter postAdapter = new PostAdapter(getApplicationContext(), postList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(postList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserFriends() {

        final TextView jmlFriend = findViewById(R.id.rv_friends_list);
        rvFriends = findViewById(R.id.rv_friends);
        rvFriends.setLayoutManager(new GridLayoutManager(this, 3));
        DatabaseReference databaseReference = firebaseDatabase.getReference("Friend").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                jmlFriend.setText(snapshot.getChildrenCount() + " Friend");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend user = dataSnapshot.getValue(Friend.class);
                    if (dataSnapshot.getChildrenCount() < 6) {
                        userList.add(user);
                    }
                }
                rvFriends.setAdapter(new FriendsProfileAdapter(getApplicationContext(), userList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPost(User user) {

        recyclerView = findViewById(R.id.rv_profile_post_feed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference databaseReference = firebaseDatabase.getReference("PostById").child(user.getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                    emptyPostList.setVisibility(View.INVISIBLE);
                }

                PostAdapter postAdapter = new PostAdapter(getApplicationContext(), postList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(postList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFriends(User user) {

        final TextView jmlFriend = findViewById(R.id.rv_friends_list);
        rvFriends = findViewById(R.id.rv_friends);
        rvFriends.setLayoutManager(new GridLayoutManager(this, 3));
        DatabaseReference databaseReference = firebaseDatabase.getReference("Friend").child(user.getUserId());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                jmlFriend.setText(snapshot.getChildrenCount() + " Friend");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend user = dataSnapshot.getValue(Friend.class);
                    userList.add(user);
                }
                rvFriends.setAdapter(new FriendsProfileAdapter(getApplicationContext(), userList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDetailMe(DatabaseReference databaseReference) {

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if (user != null) {
                    TextView schools = findViewById(R.id.tv_education);
                    TextView location = findViewById(R.id.tv_location);
                    TextView email = findViewById(R.id.tv_email);
                    TextView bio = findViewById(R.id.tv_profile_biography);
                    TextView number = findViewById(R.id.tv_phone);
                    schools.setText(user.getUserSchools());
                    email.setText(user.getUserEmail());
                    bio.setText(user.getUserBio());
                    number.setText(user.getUserNumber());
                    location.setText(user.getUserLocation());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
