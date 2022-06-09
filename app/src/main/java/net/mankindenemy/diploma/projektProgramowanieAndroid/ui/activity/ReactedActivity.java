package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.adapter.LikesAdapter;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Post;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReactedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> userList = new ArrayList<>();

    private String userStatus;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reacted);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.rv_reactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {

        super.onStart();
        Post post = new Post();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREF), MODE_PRIVATE);
        String key = sharedPreferences.getString(getString(R.string.KEY), post.getKey());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LikePostByKey").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }

                recyclerView.setAdapter(new LikesAdapter(getApplicationContext(), userList));
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
