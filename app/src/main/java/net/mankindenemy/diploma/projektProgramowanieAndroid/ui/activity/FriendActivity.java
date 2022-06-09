package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.adapter.FriendsChatAdapter;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    private RecyclerView rvFriends;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private List<Friend> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        findViewById(R.id.bt_return).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        String id = getIntent().getStringExtra("IDUSER");

        if (id != null) {
            DatabaseReference databaseReference = firebaseDatabase.getReference("Friend").child(id);
            getUserFriends(databaseReference);
        } else {
            DatabaseReference databaseReference = firebaseDatabase.getReference("Friend").child(firebaseUser.getUid());
            getUserFriends(databaseReference);
        }
    }


    private void getUserFriends(DatabaseReference databaseReference) {

        rvFriends = findViewById(R.id.rv_friends);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        databaseReference.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend user = dataSnapshot.getValue(Friend.class);
                    userList.add(user);
                }

                rvFriends.setAdapter(new FriendsChatAdapter(getApplicationContext(), userList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
