package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.fragment.MenuFragment;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.fragment.ChatFragment;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.fragment.HomeFragment;
import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.fragment.RequestFragment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private long mBackPressed;
    private String userStatus;
    private FirebaseUser firebaseUser;
    private ImageView home, users, messages,  menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        findViewById(R.id.bt_profile).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
        home = findViewById(R.id.bt_home);
        users = findViewById(R.id.bt_friends);
        messages = findViewById(R.id.bt_chat);
        menu = findViewById(R.id.bt_settings_menu);
        home.setImageResource(R.drawable.ic_menu_home_selected);
        home.setOnClickListener(this);
        users.setOnClickListener(this);
        messages.setOnClickListener(this);
        menu.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {

        if (mBackPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), R.string.code_confirm_exit, Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_search_users:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
            case R.id.bt_home:
                loadFragment(new HomeFragment());
                home.setImageResource(R.drawable.ic_menu_home_selected);
                users.setImageResource(R.drawable.ic_menu_friends);
                messages.setImageResource(R.drawable.ic_menu_messages);
                menu.setImageResource(R.drawable.ic_menu_menu);
                break;
            case R.id.bt_friends:
                loadFragment(new RequestFragment());
                home.setImageResource(R.drawable.ic_menu_home);
                users.setImageResource(R.drawable.ic_menu_friends_selected);
                messages.setImageResource(R.drawable.ic_menu_messages);
                menu.setImageResource(R.drawable.ic_menu_menu);
                break;
            case R.id.bt_chat:
                loadFragment(new ChatFragment());
                home.setImageResource(R.drawable.ic_menu_home);
                users.setImageResource(R.drawable.ic_menu_friends);
                messages.setImageResource(R.drawable.ic_menu_messages_selected);
                menu.setImageResource(R.drawable.ic_menu_menu);
                break;
            case R.id.bt_settings_menu:
                loadFragment(new MenuFragment());
                home.setImageResource(R.drawable.ic_menu_home);
                users.setImageResource(R.drawable.ic_menu_friends);
                messages.setImageResource(R.drawable.ic_menu_messages);
                menu.setImageResource(R.drawable.ic_menu_menu_selected);
                break;
        }
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
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
