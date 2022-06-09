package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.adapter.PostAdapter;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Friend;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Post;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.UploadPostActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.image.RoundedImageView;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    private RecyclerView recyclerView, rvStory;
    private DatabaseReference databaseReference;
    private List<Post> postList = new ArrayList<>();
    private List<String> friendsList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.rv_post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Post");

        view.findViewById(R.id.bt_create_post).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), UploadPostActivity.class));
            }
        });

        rvStory = view.findViewById(R.id.rv_story);
        rvStory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvStory.setHasFixedSize(true);

        RoundedImageView accountImg = view.findViewById(R.id.bt_profile);
        // Glide.with(getContext()).load(firebaseUser.getPhotoUrl()).into(img); !!!!!!!!
        Glide.with(Objects.requireNonNull(getContext())).load(Objects.requireNonNull(firebaseUser).getPhotoUrl()).into(accountImg);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {

        super.onStart();
        checkFriend();
    }

    private void checkFriend() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friend").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                friendsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    friendsList.add(friend.getFriendId());
                }
                getPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPost() {

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    for (String idFriend : friendsList) {
                        if (post.getPublisherId().equals(idFriend)) {
                            postList.add(post);
                        }
                    }
                }

                PostAdapter postAdapter = new PostAdapter(getContext(), postList);
                recyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(postList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
