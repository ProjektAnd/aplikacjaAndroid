package net.mankindenemy.diploma.projektProgramowanieAndroid.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;

public class FriendsRequestAdapter extends RecyclerView.Adapter<FriendsRequestAdapter.ViewHolder> {

    private Context context;
    private List<FriendRequest> userList;

    public FriendsRequestAdapter(Context context, List<FriendRequest> userList) {

        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public FriendsRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendsRequestAdapter.ViewHolder holder, final int position) {

        final FriendRequest user = userList.get(position);
        holder.tvNameUser.setText(user.getRequestName());
        Glide.with(context).load(user.getRequestImage()).into(holder.imageUser);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.btConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friend").child(firebaseUser.getUid()).child(user.getRequestId());
                final Friend friend = new Friend(user.getRequestName(), user.getRequestImage(), user.getRequestId(), firebaseUser.getUid());
                databaseReference.setValue(friend).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friend").child(friend.getFriendId()).child(firebaseUser.getUid());
                                Friend friendId = new Friend(firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getUid(), user.getRequestId());
                                reference.setValue(friendId);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        SharedPreferences sharedPreferences = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
                        String key = sharedPreferences.getString("KEYFRIENDREQUEST", "");
                        FirebaseDatabase.getInstance().getReference("FriendRequest").child(firebaseUser.getUid()).child(key).removeValue();
                        Toast.makeText(context, R.string.code_friend_proposal_accepted, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageUser;
        TextView tvNameUser;
        Button btConfirm, btDelete;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageUser = itemView.findViewById(R.id.iv_rounded_user_image);
            tvNameUser = itemView.findViewById(R.id.tv_user_credentials);
            btConfirm = itemView.findViewById(R.id.bt_accept_friend_request);
            btDelete = itemView.findViewById(R.id.bt_deny_friend_request);
        }

    }

}
