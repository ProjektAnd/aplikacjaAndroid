package net.mankindenemy.diploma.projektProgramowanieAndroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Friend;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.FriendRequest;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.User;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.ChatActivity;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {

        this.context = context;
        this.userList = userList;
    }

    private List<String> listIdFriend = new ArrayList<>();

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ViewHolder holder, final int position) {

        final User user = userList.get(position);
        holder.tvBioUser.setText(user.getUserBio());
        holder.tvNameUser.setText(user.getUserName());
        Glide.with(context).load(user.getUserImage()).into(holder.imageUser);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ChatActivity.EXTRA_USER, user);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        isAdd(holder);
        holder.btAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (holder.btAdd.getTag().equals("add")) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FriendRequest").child(user.getUserId()).push();
                    FriendRequest friendRequestFriends = new FriendRequest(firebaseUser.getUid(), user.getUserId(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), databaseReference.getKey(), user.getKey(), user.getUserStatus());
                    databaseReference.setValue(friendRequestFriends).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {

                            holder.btAdd.setVisibility(View.INVISIBLE);
                        }
                    });
                    SharedPreferences sharedPreferences = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("KEYFRIENDREQUEST", databaseReference.getKey());
                    editor.apply();

                } else {
                    FirebaseDatabase.getInstance().getReference("FriendRequest").child(user.getUserId()).removeValue();
                }

            }
        });

        checkFriend(holder.btAdd);
    }

    private void isAdd(@NonNull final ViewHolder holder) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FriendRequest");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(firebaseUser.getUid()).exists()) {
                    holder.btAdd.setVisibility(View.INVISIBLE);
                    holder.btAdd.setTag("added");
                } else {
                    holder.btAdd.setVisibility(View.VISIBLE);
                    holder.btAdd.setTag("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageUser;
        TextView tvNameUser, tvBioUser;
        Button btAdd;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageUser = itemView.findViewById(R.id.iv_rounded_user_image);
            tvNameUser = itemView.findViewById(R.id.tv_user_credentials);
            tvBioUser = itemView.findViewById(R.id.tv_user_description);
            btAdd = itemView.findViewById(R.id.bt_add_friend);
        }

    }

    private void checkFriend(final Button btAdd) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friend");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listIdFriend.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    listIdFriend.add(friend.getFriendId());
                    listIdFriend.add(friend.getFriendRequestId());
                }

                getAllUser(btAdd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllUser(final Button btAdd) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    for (String id : listIdFriend) {
                        if (user.getUserId().equals(id) && firebaseUser.getUid().equals(id)) {
                            btAdd.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
