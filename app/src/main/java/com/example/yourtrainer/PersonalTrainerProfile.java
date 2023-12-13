package com.example.yourtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PersonalTrainerProfile extends Fragment {
    TextView fullNameTV, usernameTV, emailTV, addressTV, phoneTV, genderTV;
    Button bookBtn;
    ListView specialtiesLV, qualificationLV;
    ImageView profilePicture;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> qualificationArray = new ArrayList<>();
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_personal_trainer_profile,container,false);

        Bundle bundle = this.getArguments();
        //final String trainerEmail = getArguments().getString("trainerEmail");
        final String trainerEmail = bundle.getString("trainerEmail","");

        fullNameTV = v.findViewById(R.id.fullNameTV);
        usernameTV = v.findViewById(R.id.usernameTV);
        genderTV = v.findViewById(R.id.genderTV);
        emailTV = v.findViewById(R.id.emailTV);
        addressTV = v.findViewById(R.id.addressTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        profilePicture = v.findViewById(R.id.profilePicture);
        bookBtn = v.findViewById(R.id.bookBtn);
        specialtiesLV = (ListView) v.findViewById(R.id.specialtiesLV);
        qualificationLV = (ListView) v.findViewById(R.id.qualificationLV);

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BookSessionPage();
                Bundle args = new Bundle();
                args.putString("trainerEmail", trainerEmail);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.child("trainerEmail").getValue().toString().equals(trainerEmail)) {
                        String trainerid = snap.getKey();
                        fullNameTV.setText(snap.child("trainerFullName").getValue().toString());
                        usernameTV.setText(snap.child("trainerUsername").getValue().toString());
                        genderTV.setText(snap.child("trainerGender").getValue().toString());
                        phoneTV.setText(snap.child("trainerPhone").getValue().toString());
                        emailTV.setText(snap.child("trainerEmail").getValue().toString());
                        addressTV.setText(snap.child("trainerAddress").getValue().toString());

                        String imageId = snap.child("imageId").getValue().toString();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference ref = storageReference.child("Personal Trainer/" + imageId);
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri.toString()).into(profilePicture);
                            }
                        });
                        //List View SPECIALTIES
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,arrayList);
                        specialtiesLV.setAdapter(arrayAdapter);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(trainerid).child("Specialties");
                        databaseReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                String trainerSpecialties = dataSnapshot.getValue().toString();
                                arrayList.add(trainerSpecialties);



                                arrayAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                arrayAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //List View QUALIFICATION
                        final ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,qualificationArray);
                        qualificationLV.setAdapter(qualificationAdapter);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(trainerid).child("Qualification");
                        databaseReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                String trainerQualification = dataSnapshot.getValue().toString();
                                qualificationArray.add(trainerQualification);



                                qualificationAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                qualificationAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}
