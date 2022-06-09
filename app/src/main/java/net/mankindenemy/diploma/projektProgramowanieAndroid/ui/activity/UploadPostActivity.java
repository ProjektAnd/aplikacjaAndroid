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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.mankindenemy.diploma.projektProgramowanieAndroid.R;
import net.mankindenemy.diploma.projektProgramowanieAndroid.model.Post;

import java.util.HashMap;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class UploadPostActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etPost;
    private ImageView imgPost;
    private Uri uriImage;
    private FirebaseUser firebaseUser;
    private int ID_REQUEST = 2;
    private ProgressDialog progressDialog;
    private String userStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        imgPost = findViewById(R.id.iv_post_image_content);
        etPost = findViewById(R.id.et_post_text_content);
        findViewById(R.id.tv_upload_file).setOnClickListener(this);
        findViewById(R.id.tv_post_text_content).setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        findViewById(R.id.bt_return).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        progressDialog.setMessage(getString(R.string.code_loading));


    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_post_text_content:
                preparePost();
                break;
            case R.id.tv_upload_file:
                openGallery();
                break;
        }
    }

    private void preparePost() {

        if (!etPost.getText().toString().isEmpty() && uriImage != null) {
            progressDialog.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImagePost");
            final StorageReference imagePath = storageReference.child(uriImage.getLastPathSegment());
            imagePath.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {

                            final String imagePost = uri.toString();
                            Post post = new Post(etPost.getText().toString(), imagePost, firebaseUser.getUid());
                            addPost(post);

                        }
                    }).addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, R.string.code_empty_field, Toast.LENGTH_SHORT).show();
        }
    }

    private void addPost(final Post post) {

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Post").push();
        post.setKey(databaseReference.getKey());
        databaseReference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

                progressDialog.hide();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                DatabaseReference userPost = firebaseDatabase.getReference("PostById").child(firebaseUser.getUid()).child(post.getKey());
                userPost.setValue(post);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ID_REQUEST && resultCode == RESULT_OK && data != null) {
            uriImage = data.getData();
            imgPost.setVisibility(View.VISIBLE);
            imgPost.setImageURI(uriImage);
            imgPost.setBackground(null);
        }
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ID_REQUEST);
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
