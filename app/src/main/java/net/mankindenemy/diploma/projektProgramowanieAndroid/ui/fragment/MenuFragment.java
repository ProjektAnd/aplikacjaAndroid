package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.LanguageActivity;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.LoginActivity;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity.CreditsActivity;

import java.util.HashMap;

import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.image.RoundedImageView;

public class MenuFragment extends Fragment implements View.OnClickListener {

    public MenuFragment() {
    }

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        RoundedImageView imageView = view.findViewById(R.id.iv_rounded_user_image);
        TextView textView = view.findViewById(R.id.tv_user_credentials);
        Glide.with(getContext()).load(firebaseUser.getPhotoUrl()).into(imageView);
        textView.setText(firebaseUser.getDisplayName());

        view.findViewById(R.id.bt_log_out).setOnClickListener(this);
//        view.findViewById(R.id.bt_post_feed).setOnClickListener(this);
//        view.findViewById(R.id.bt_chat_feed).setOnClickListener(this);
        view.findViewById(R.id.bt_language).setOnClickListener(this);
        view.findViewById(R.id.bt_about_project).setOnClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_log_out:
                firebaseAuth.signOut();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userStatus", "Offline");
                databaseReference.updateChildren(hashMap);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
//            case R.id.bt_post_feed:
//                startActivity(new Intent(getContext(), LikesActivity.class));
//                break;
//            case R.id.bt_chat_feed:
//                startActivity(new Intent(getContext(), FriendActivity.class));
//                break;
            case R.id.bt_about_project:
                startActivity(new Intent(getContext(), CreditsActivity.class));
                break;
            case  R.id.bt_language:
                startActivity(new Intent(getContext(), LanguageActivity.class));
                break;
        }
    }

}
