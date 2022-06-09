package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.adapter.ChatAdapter;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Chat;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Friend;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "extra_user";

    private EditText etMessage;
    private Button btSend;
    private FirebaseUser firebaseUser;
    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String userStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        etMessage = findViewById(R.id.et_chat_input);
        btSend = findViewById(R.id.bt_send_message);
        recyclerView = findViewById(R.id.rv_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        setData();

        findViewById(R.id.rv_toolbar).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), FriendActivity.class));
            }
        });

    }


    private void setData() {

        final Friend user = getIntent().getParcelableExtra(EXTRA_USER);
        if (user != null) {
            ImageView imageUser = findViewById(R.id.iv_rounded_user_image);
            Glide.with(getApplicationContext()).load(user.getFriendImage()).into(imageUser);
            TextView name = findViewById(R.id.tv_user_credentials);
            name.setText(user.getFriendName());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.getFriendId());
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User userDetail = snapshot.getValue(User.class);
                    TextView status = findViewById(R.id.tv_user_status);
                    status.setText(userDetail.getUserStatus());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btSend.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (etMessage.getText().toString().isEmpty()) {
                        Toast.makeText(ChatActivity.this, R.string.code_chat_empty_message, Toast.LENGTH_SHORT).show();
                    } else {
                        createChat(user);
                    }
                }
            });

            getChatList(user);
        }
    }

    private void createChat(Friend user) {

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Chat").push();
        Chat chat = new Chat(firebaseUser.getUid(), user.getFriendId(), etMessage.getText().toString());
        firebaseDatabase.setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

                etMessage.setText("");
            }
        });
    }

    private void getChatList(final Friend user) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat != null) {
                        if (chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(user.getFriendId()) || chat.getSender().equals(user.getFriendId()) && chat.getReceiver().equals(firebaseUser.getUid())) {
                            chatList.add(chat);
                        }
                    }
                }
                recyclerView.setAdapter(new ChatAdapter(getApplicationContext(), chatList));
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