package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;

import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText etName, etPassword;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        progressDialog = new ProgressDialog(UpdateProfileActivity.this);
        progressDialog.setMessage("Update Profile");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        etName = findViewById(R.id.et_profile_name);
        etPassword = findViewById(R.id.et_profile_password);
        Button btUpdate = findViewById(R.id.bt_update);
        btUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String newPassword = etPassword.getText().toString();
                String newName = etName.getText().toString();

                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build();
                if (newName.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(UpdateProfileActivity.this, R.string.code_empty_field, Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    if (firebaseUser != null) {

                        firebaseUser.updatePassword(newPassword);
                        firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.hide();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
                    }
                }
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
