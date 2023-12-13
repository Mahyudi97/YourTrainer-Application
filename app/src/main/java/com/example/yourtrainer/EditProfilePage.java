package com.example.yourtrainer;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.strictmode.CleartextNetworkViolation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class EditProfilePage extends Fragment {
    EditText fullNameET, usernameET, genderET, addressET, emailET, phoneET,passwordET;
    Button updateBtn, uploadBtn;
    ImageView profilePicture;
    Client client;
    private StorageTask uploadTask;
    public Uri imageUri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.acivity_edit_profile_page,container,false);

        fullNameET = v.findViewById(R.id.fullNameET);
        usernameET = v.findViewById(R.id.usernameET);
        genderET = v.findViewById(R.id.genderET);
        emailET = v.findViewById(R.id.emailET);
        addressET = v.findViewById(R.id.addressET);
        phoneET = v.findViewById(R.id.phoneET);
        passwordET = v.findViewById(R.id.passwordET);
        updateBtn = v.findViewById(R.id.updateBtn);
        uploadBtn = v.findViewById(R.id.uploadBtn);
        profilePicture = v.findViewById(R.id.profilePicture);
        storageReference = FirebaseStorage.getInstance().getReference("Client");
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String userId=firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Client").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                fullNameET.setText(snapshot.child("clientFullName").getValue().toString());
                usernameET.setText(snapshot.child("clientUsername").getValue().toString());
                genderET.setText(snapshot.child("clientGender").getValue().toString());
                phoneET.setText(snapshot.child("clientPhone").getValue().toString());
                emailET.setText(snapshot.child("clientEmail").getValue().toString());
                addressET.setText(snapshot.child("clientAddress").getValue().toString());

                String imageId = snapshot.child("imageId").getValue().toString();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                //get imageId
                StorageReference ref = storageReference.child("Client/"+imageId);
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //replace image with image from firebase
                         //Picasso.get().load(uri.toString()).into(picture);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(getActivity(),"Upload in progress",Toast.LENGTH_LONG).show();
                }else {
                    FileUploader();
                }
            }
        });



        return v;
    }

    private void FileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void FileUploader(){
        final String imageId;
        imageId=System.currentTimeMillis()+"."+getExtension(imageUri);
        client = new Client();
        client.setClientFullName(fullNameET.getText().toString().trim());
        client.setClientUsername(usernameET.getText().toString().trim());
        client.setClientPhone(phoneET.getText().toString().trim());
        client.setClientAddress(addressET.getText().toString().trim());
        client.setClientGender(genderET.getText().toString().trim());
        client.setClientEmail(emailET.getText().toString().trim());
        client.setImageId(imageId);
        databaseReference.setValue(client);

        StorageReference ref = storageReference.child(imageId);
        uploadTask=ref.putFile(imageUri);
                /*.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(getActivity(),"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getActivity(),"Image Uploaded Unsuccessfully",Toast.LENGTH_LONG).show();

                    }
                });*/

        ProfilePage fragment = new ProfilePage();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, "TAG");
        transaction.commit();

    }

    private String getExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return (mimeTypeMap.getExtensionFromMimeType(cr.getType(uri)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData() !=null){
            imageUri=data.getData();
            profilePicture.setImageURI(imageUri);
        }
    }
}
