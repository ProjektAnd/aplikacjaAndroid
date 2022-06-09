package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import net.mankindenemy.diploma.projektProgramowanieAndroid.adapter.ReplyAdapter;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReplyCommentActivity extends AppCompatActivity {

    private EditText etComment;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private List<Comment> commentList = new ArrayList<>();

    private String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_comment);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Comment comment = getIntent().getParcelableExtra(getString(R.string.COMMENT));
        if (comment != null) {
            etComment = findViewById(R.id.et_comment);
            ImageView imgAccount = findViewById(R.id.iv_comment);
            TextView tvAccount = findViewById(R.id.tv_post_owner_name);
            TextView tvComment = findViewById(R.id.tv_comment);

            tvComment.setText(comment.getComment());
            tvAccount.setText(comment.getPublisherName());
            Glide.with(getApplicationContext()).load(comment.getPublisherImage()).into(imgAccount);

            findViewById(R.id.bt_enter_comment).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (etComment.getText().toString().isEmpty()) {
                        Toast.makeText(ReplyCommentActivity.this, R.string.code_empty_post_comment, Toast.LENGTH_SHORT).show();
                    } else {
                        createReply();
                    }
                }
            });
        }
    }

    private void getListReplyComment() {

        Comment comment = new Comment();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREF), MODE_PRIVATE);
        String keyComment = sharedPreferences.getString(getString(R.string.KEY), comment.getKey());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CommentReply").child(keyComment);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment valuesComment = dataSnapshot.getValue(Comment.class);
                    commentList.add(valuesComment);
                }

                recyclerView.setAdapter(new ReplyAdapter(getApplicationContext(), commentList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createReply() {
        Comment comment = new Comment();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.PREF), MODE_PRIVATE);
        String keyComment = sharedPreferences.getString(getString(R.string.KEY), comment.getKey());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CommentReply").child(keyComment).push();
        Comment valueComment = new Comment(etComment.getText().toString(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getUid());
        valueComment.setKey(databaseReference.getKey());
        databaseReference.setValue(valueComment).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

                etComment.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        recyclerView = findViewById(R.id.rvReplyComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getListReplyComment();
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
