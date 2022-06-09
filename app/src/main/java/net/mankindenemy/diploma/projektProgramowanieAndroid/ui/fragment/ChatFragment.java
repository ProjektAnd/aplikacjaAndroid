package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class ChatFragment extends Fragment {

    public ChatFragment() {
    }

    private RecyclerView recyclerView;
    private List<Friend> friendList = new ArrayList<>();
    private TextView noList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.rv_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noList = view.findViewById(R.id.tv_empty_list_message);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {

        super.onStart();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Friend").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                friendList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    noList.setVisibility(View.INVISIBLE);
                    friendList.add(friend);
                }

                recyclerView.setAdapter(new FriendsChatAdapter(getContext(), friendList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
