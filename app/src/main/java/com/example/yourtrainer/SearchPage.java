package com.example.yourtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchPage extends Fragment implements PersonalTrainerAdapter.OnListListener{
    EditText keywordET;
    String special, location, gender;
    Button searchBtn;
    RecyclerView searchResult;
    RecyclerView.Adapter personalTrainerAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PersonalTrainer> trainerArray = new ArrayList<>();
    Spinner spinnerSpecialties,spinnerLocation,spinnerGender;
    ArrayList<String> arraySpecialties=new ArrayList<>();
    ArrayList<String> arrayLocation = new ArrayList<>();
    ArrayList<String> arrayGender = new ArrayList<>();
    ArrayList<PersonalTrainer> filteredArray = new ArrayList<>();
    PersonalTrainer personalTrainer = new PersonalTrainer();
    Specialties specialties = new Specialties();
    DatabaseReference trainerReference, specialtiesReference;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_search_page,container,false);


        keywordET = v.findViewById(R.id.keywordET);
        searchBtn = v.findViewById(R.id.searchButton);
        spinnerGender = v.findViewById(R.id.spinnerGender);
        spinnerLocation = v.findViewById(R.id.spinnerLocation);
        spinnerSpecialties = v.findViewById(R.id.spinnerSpecialties);

       spinnerSpecialties.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                special = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchResult = (RecyclerView) v.findViewById(R.id.searchResult);
        searchResult.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        searchResult.setLayoutManager(llm);

        personalTrainerAdapter = new PersonalTrainerAdapter(trainerArray, this);
        searchResult.setAdapter(personalTrainerAdapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainerArray.clear();
                filteredArray.clear();
                String searchText = keywordET.getText().toString().trim();


                if (special.equals("-Specialties-") && location.equals("-Location-") && gender.equals("-Gender-") && searchText.isEmpty()) {
                    trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                    Query query = trainerReference.orderByChild("trainerFullName").startAt(searchText).endAt(searchText + "\uf8ff");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                personalTrainer = new PersonalTrainer();
                                personalTrainer.setTrainerFullName(snap.child("trainerFullName").getValue().toString());
                                personalTrainer.setTrainerUsername(snap.child("trainerUsername").getValue().toString());
                                personalTrainer.setTrainerGender(snap.child("trainerGender").getValue().toString());
                                personalTrainer.setTrainerPhone(snap.child("trainerPhone").getValue().toString());
                                personalTrainer.setTrainerEmail(snap.child("trainerEmail").getValue().toString());
                                personalTrainer.setTrainerAddress(snap.child("trainerAddress").getValue().toString());
                                personalTrainer.setImageId(snap.child("imageId").getValue().toString());

                                trainerArray.add(personalTrainer);

                            }
                            searchResult.setAdapter(personalTrainerAdapter);
                            personalTrainerAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else if(!searchText.isEmpty()){
                    trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                    Query query = trainerReference.orderByChild("trainerFullName").startAt(searchText).endAt(searchText + "\uf8ff");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                personalTrainer = new PersonalTrainer();
                                personalTrainer.setTrainerFullName(snap.child("trainerFullName").getValue().toString());
                                personalTrainer.setTrainerUsername(snap.child("trainerUsername").getValue().toString());
                                personalTrainer.setTrainerGender(snap.child("trainerGender").getValue().toString());
                                personalTrainer.setTrainerPhone(snap.child("trainerPhone").getValue().toString());
                                personalTrainer.setTrainerEmail(snap.child("trainerEmail").getValue().toString());
                                personalTrainer.setTrainerAddress(snap.child("trainerAddress").getValue().toString());
                                personalTrainer.setImageId(snap.child("imageId").getValue().toString());

                                trainerArray.add(personalTrainer);

                            }
                            searchResult.setAdapter(personalTrainerAdapter);
                            personalTrainerAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else
                    //filtered
                if(!special.equals("-Specialties-")) {
                    //Specialties only
                    if (location.equals("-Location-") && gender.equals("-Gender-")){
                        trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                        trainerReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (final DataSnapshot snapTrainer : snapshot.getChildren()) {
                                    String child = snapTrainer.getKey();
                                    specialtiesReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(child).child("Specialties");
                                    specialtiesReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                                            String trainerSpecialties = dataSnapshot.getValue().toString();
                                            if (trainerSpecialties.equals(special)) {
                                                personalTrainer = new PersonalTrainer();
                                                personalTrainer.setTrainerFullName(snapTrainer.child("trainerFullName").getValue().toString());
                                                personalTrainer.setTrainerUsername(snapTrainer.child("trainerUsername").getValue().toString());
                                                personalTrainer.setTrainerGender(snapTrainer.child("trainerGender").getValue().toString());
                                                personalTrainer.setTrainerPhone(snapTrainer.child("trainerPhone").getValue().toString());
                                                personalTrainer.setTrainerEmail(snapTrainer.child("trainerEmail").getValue().toString());
                                                personalTrainer.setTrainerAddress(snapTrainer.child("trainerAddress").getValue().toString());
                                                personalTrainer.setImageId(snapTrainer.child("imageId").getValue().toString());

                                                trainerArray.add(personalTrainer);
                                                personalTrainerAdapter.notifyDataSetChanged();
                                            }

                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            personalTrainerAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                                searchResult.setAdapter(personalTrainerAdapter);
                                personalTrainerAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else
                        //Specialties,Location
                        if (!location.equals("-Location-") && gender.equals("-Gender-")){
                            trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                            trainerReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (final DataSnapshot snapTrainer : snapshot.getChildren()) {
                                        String child = snapTrainer.getKey();
                                        specialtiesReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(child).child("Specialties");
                                        specialtiesReference.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                                                String trainerSpecialties = dataSnapshot.getValue().toString();
                                                if (trainerSpecialties.equals(special)) {
                                                    if (snapTrainer.child("trainerAddress").getValue().toString().equals(location)) {
                                                        personalTrainer = new PersonalTrainer();
                                                        personalTrainer.setTrainerFullName(snapTrainer.child("trainerFullName").getValue().toString());
                                                        personalTrainer.setTrainerUsername(snapTrainer.child("trainerUsername").getValue().toString());
                                                        personalTrainer.setTrainerGender(snapTrainer.child("trainerGender").getValue().toString());
                                                        personalTrainer.setTrainerPhone(snapTrainer.child("trainerPhone").getValue().toString());
                                                        personalTrainer.setTrainerEmail(snapTrainer.child("trainerEmail").getValue().toString());
                                                        personalTrainer.setTrainerAddress(snapTrainer.child("trainerAddress").getValue().toString());
                                                        personalTrainer.setImageId(snapTrainer.child("imageId").getValue().toString());

                                                        trainerArray.add(personalTrainer);
                                                        personalTrainerAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                personalTrainerAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                    searchResult.setAdapter(personalTrainerAdapter);
                                    personalTrainerAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }else
                            //Specialties,Gender
                            if (location.equals("-Location-") && !gender.equals("-Gender-")){
                                trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                                trainerReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (final DataSnapshot snapTrainer : snapshot.getChildren()) {
                                            String child = snapTrainer.getKey();
                                            specialtiesReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(child).child("Specialties");
                                            specialtiesReference.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                                                    String trainerSpecialties = dataSnapshot.getValue().toString();
                                                    if (trainerSpecialties.equals(special)) {
                                                        if (snapTrainer.child("trainerGender").getValue().toString().equals(gender)) {
                                                            personalTrainer = new PersonalTrainer();
                                                            personalTrainer.setTrainerFullName(snapTrainer.child("trainerFullName").getValue().toString());
                                                            personalTrainer.setTrainerUsername(snapTrainer.child("trainerUsername").getValue().toString());
                                                            personalTrainer.setTrainerGender(snapTrainer.child("trainerGender").getValue().toString());
                                                            personalTrainer.setTrainerPhone(snapTrainer.child("trainerPhone").getValue().toString());
                                                            personalTrainer.setTrainerEmail(snapTrainer.child("trainerEmail").getValue().toString());
                                                            personalTrainer.setTrainerAddress(snapTrainer.child("trainerAddress").getValue().toString());
                                                            personalTrainer.setImageId(snapTrainer.child("imageId").getValue().toString());

                                                            trainerArray.add(personalTrainer);
                                                            personalTrainerAdapter.notifyDataSetChanged();
                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    personalTrainerAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                        }
                                        searchResult.setAdapter(personalTrainerAdapter);
                                        personalTrainerAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }else
                                //Specialties,Location,Gender
                                if (!location.equals("-Location-") && !gender.equals("-Gender-")){
                                    trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                                    trainerReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                            for (final DataSnapshot snapTrainer : snapshot.getChildren()) {
                                                String child = snapTrainer.getKey();
                                                specialtiesReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(child).child("Specialties");
                                                specialtiesReference.addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                                                        String trainerSpecialties = dataSnapshot.getValue().toString();
                                                        if (trainerSpecialties.equals(special)) {
                                                            if (snapTrainer.child("trainerGender").getValue().toString().equals(gender)) {
                                                                if (snapTrainer.child("trainerAddress").getValue().toString().equals(location)){
                                                                    personalTrainer = new PersonalTrainer();
                                                                    personalTrainer.setTrainerFullName(snapTrainer.child("trainerFullName").getValue().toString());
                                                                    personalTrainer.setTrainerUsername(snapTrainer.child("trainerUsername").getValue().toString());
                                                                    personalTrainer.setTrainerGender(snapTrainer.child("trainerGender").getValue().toString());
                                                                    personalTrainer.setTrainerPhone(snapTrainer.child("trainerPhone").getValue().toString());
                                                                    personalTrainer.setTrainerEmail(snapTrainer.child("trainerEmail").getValue().toString());
                                                                    personalTrainer.setTrainerAddress(snapTrainer.child("trainerAddress").getValue().toString());
                                                                    personalTrainer.setImageId(snapTrainer.child("imageId").getValue().toString());

                                                                    trainerArray.add(personalTrainer);
                                                                    personalTrainerAdapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                        personalTrainerAdapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                            }
                                            searchResult.setAdapter(personalTrainerAdapter);
                                            personalTrainerAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                }else if (!location.equals("-Location-")){
                    //Location
                    if(special.equals("-Specialties")){
                        trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                        trainerReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                for (final DataSnapshot snapTrainer : snapshot.getChildren()) {
                                    if (snapTrainer.child("trainerAddress").getValue().toString().equals(location)){
                                        personalTrainer = new PersonalTrainer();
                                        personalTrainer.setTrainerFullName(snapTrainer.child("trainerFullName").getValue().toString());
                                        personalTrainer.setTrainerUsername(snapTrainer.child("trainerUsername").getValue().toString());
                                        personalTrainer.setTrainerGender(snapTrainer.child("trainerGender").getValue().toString());
                                        personalTrainer.setTrainerPhone(snapTrainer.child("trainerPhone").getValue().toString());
                                        personalTrainer.setTrainerEmail(snapTrainer.child("trainerEmail").getValue().toString());
                                        personalTrainer.setTrainerAddress(snapTrainer.child("trainerAddress").getValue().toString());
                                        personalTrainer.setImageId(snapTrainer.child("imageId").getValue().toString());

                                        trainerArray.add(personalTrainer);
                                        personalTrainerAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else
                        //Location,Gender
                        if(!gender.equals("-Gender-")){
                            trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                            trainerReference.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    for (final DataSnapshot snapTrainer : snapshot.getChildren()) {
                                        if (snapTrainer.child("trainerAddress").getValue().toString().equals(location) && snapTrainer.child("trainerGender").getValue().toString().equals(gender) ){
                                            personalTrainer = new PersonalTrainer();
                                            personalTrainer.setTrainerFullName(snapTrainer.child("trainerFullName").getValue().toString());
                                            personalTrainer.setTrainerUsername(snapTrainer.child("trainerUsername").getValue().toString());
                                            personalTrainer.setTrainerGender(snapTrainer.child("trainerGender").getValue().toString());
                                            personalTrainer.setTrainerPhone(snapTrainer.child("trainerPhone").getValue().toString());
                                            personalTrainer.setTrainerEmail(snapTrainer.child("trainerEmail").getValue().toString());
                                            personalTrainer.setTrainerAddress(snapTrainer.child("trainerAddress").getValue().toString());
                                            personalTrainer.setImageId(snapTrainer.child("imageId").getValue().toString());

                                            trainerArray.add(personalTrainer);
                                            personalTrainerAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                }else
                    //Gender only
                    if(!gender.equals("-Gender-") && special.equals("-Specialties-") && location.equals("-Location-")){
                        trainerReference = FirebaseDatabase.getInstance().getReference("Personal Trainer");
                        trainerReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                for (final DataSnapshot snapTrainer : snapshot.getChildren()) {
                                    if (snapTrainer.child("trainerGender").getValue().toString().equals(gender) ){
                                        personalTrainer = new PersonalTrainer();
                                        personalTrainer.setTrainerFullName(snapTrainer.child("trainerFullName").getValue().toString());
                                        personalTrainer.setTrainerUsername(snapTrainer.child("trainerUsername").getValue().toString());
                                        personalTrainer.setTrainerGender(snapTrainer.child("trainerGender").getValue().toString());
                                        personalTrainer.setTrainerPhone(snapTrainer.child("trainerPhone").getValue().toString());
                                        personalTrainer.setTrainerEmail(snapTrainer.child("trainerEmail").getValue().toString());
                                        personalTrainer.setTrainerAddress(snapTrainer.child("trainerAddress").getValue().toString());
                                        personalTrainer.setImageId(snapTrainer.child("imageId").getValue().toString());

                                        trainerArray.add(personalTrainer);
                                        personalTrainerAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

            }

        });



        return v;
    }

    @Override
    public void onListClick(int position) {
        Fragment fragment = new PersonalTrainerProfile();
        Bundle args = new Bundle();
        args.putString("trainerEmail", trainerArray.get(position).getTrainerEmail());
        fragment.setArguments(args);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }
}
