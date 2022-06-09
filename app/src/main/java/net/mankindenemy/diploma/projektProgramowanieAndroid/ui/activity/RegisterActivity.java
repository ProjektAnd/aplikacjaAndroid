package net.mankindenemy.diploma.projektProgramowanieAndroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.User;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import java.util.Objects;

import net.mankindenemy.diploma.projektProgramowanieAndroid.ui.image.RoundedImageView;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1;
    private EditText etName, etNumber, etEmail, etBio, etPassword, etSchools, etLocation;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri pictUriImage;
    private RoundedImageView imageProfile;
    private FirebaseUser firebaseUser;
    private String userStatus = "Offline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firebaseAuth = FirebaseAuth.getInstance();
        findViewById(R.id.bt_register_complete).setOnClickListener(this);
        findViewById(R.id.tv_login).setOnClickListener(this);
        etName = findViewById(R.id.ed_name);
        etNumber = findViewById(R.id.et_phone_number);
        etEmail = findViewById(R.id.et_email_address);
        etBio = findViewById(R.id.et_bio);
        etSchools = findViewById(R.id.ed_education);
        etLocation = findViewById(R.id.ed_location);
        etPassword = findViewById(R.id.et_password);
        imageProfile = findViewById(R.id.iv_rounded_post_owner_picture);
        imageProfile.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.code_account_is_created));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.iv_rounded_post_owner_picture:
                openGallery();
                break;
            case R.id.bt_register_complete:
                createUser();
                break;
        }
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            pictUriImage = data.getData();
            imageProfile.setImageURI(pictUriImage);
        }
    }

    private void createUser() {

        final String name = etName.getText().toString();
        final String number = etNumber.getText().toString();
        final String email = etEmail.getText().toString();
        final String bio = etBio.getText().toString();
        final String password = etPassword.getText().toString();
        final String schools = etSchools.getText().toString();
        final String location = etLocation.getText().toString();

        if (name.isEmpty() || number.isEmpty()
                || bio.isEmpty() || email.isEmpty()
                || schools.isEmpty() || location.isEmpty()
                || password.isEmpty()) {
            Toast.makeText(this, R.string.code_empty_field, Toast.LENGTH_SHORT).show();
        } else {
            if (pictUriImage != null) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageProfile");
                                final StorageReference ImageFilePath = storageReference.child(Objects.requireNonNull(pictUriImage.getLastPathSegment()));
                                ImageFilePath.putFile(pictUriImage)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                ImageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                                    @Override
                                                    public void onSuccess(final Uri uri) {

                                                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).setPhotoUri(uri)
                                                                .build();
                                                        firebaseUser.updateProfile(profileUpdate)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()) {
                                                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                                                                            User user = new User(name, number, email, bio, databaseReference.getKey(), uri.toString(), password, firebaseUser.getUid(), schools, location, userStatus);
                                                                            databaseReference.setValue(user)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            progressDialog.hide();
                                                                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                                                            finish();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });
                progressDialog.show();
            } else {
                Toast.makeText(this, R.string.code_empty_profile_picture, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
