package com.example.yourtrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfilePage extends Fragment {
    TextView fullNameET, usernameET, emailET, addressET, phoneET, genderET;
    Button editBtn,uploadBtn;
    ImageView profilePicture;

    DatabaseReference databaseReference;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_profile_page,container,false);

        fullNameET = v.findViewById(R.id.fullNameTV);
        usernameET = v.findViewById(R.id.usernameTV);
        genderET = v.findViewById(R.id.genderTV);
        emailET = v.findViewById(R.id.emailTV);
        addressET = v.findViewById(R.id.addressTV);
        phoneET = v.findViewById(R.id.phoneTV);
        editBtn = v.findViewById(R.id.editBtn);
        profilePicture = v.findViewById(R.id.profilePicture);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
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
                        Picasso.get().load(uri.toString()).into(profilePicture);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfilePage fragment = new EditProfilePage();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, "TAG");
                transaction.commit();

            }
        });
        return v;
    }

}
