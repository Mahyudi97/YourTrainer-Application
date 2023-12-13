package com.example.yourtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookSessionPage extends Fragment {
    Button bookSessionBtn;
    TextView dateTV;
    CalendarView calendarView;
    String dateSessionDate,bookSessionType,trainerId="";
    Spinner spinner;
    BookSession bookSession;
    DatabaseReference databaseReference,bookSessionRef;
    long maxId=0;
    long dateCount=0;
    int num=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_book_session_page,container,false);
        calendarView = v.findViewById(R.id.calendarView);
        spinner = v.findViewById(R.id.bookSessionType);


        bookSessionRef = FirebaseDatabase.getInstance().getReference().child("Book Session");
        bookSessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId= snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Bundle bundle = this.getArguments();
        //final String trainerEmail = getArguments().getString("trainerEmail");
        final String trainerEmail = bundle.getString("trainerEmail","");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.child("trainerEmail").getValue().toString().equals(trainerEmail)) {
                        trainerId = snap.getKey();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,arrayList);
        spinner.setAdapter(arrayAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.child("trainerEmail").getValue().toString().equals(trainerEmail)) {

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(snap.getKey()).child("Specialties");
                        databaseReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                String trainerSpecialties = snapshot.getValue().toString();
                                arrayList.add(trainerSpecialties);



                                arrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                arrayAdapter.notifyDataSetChanged();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                dateSessionDate = (dayOfMonth+"/"+month+"/"+year).toString();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookSessionType = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        bookSessionBtn = v.findViewById(R.id.bookBtn);
        bookSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser firebaseUser1= FirebaseAuth.getInstance().getCurrentUser();
                String userId1=firebaseUser1.getUid();
                bookSession = new BookSession();
                bookSession.setTrainerId(trainerId);
                bookSession.setClientId(userId1);
                bookSession.setBookSessionType(bookSessionType);
                bookSession.setBookSessionStatus("Pending");
                bookSession.setDateSession(dateSessionDate);
                bookSessionRef.child(String.valueOf(maxId)).setValue(bookSession);




                Toast.makeText(getActivity(),"Successfully Book Session on "+dateSessionDate,Toast.LENGTH_LONG).show();
                num++;



            }
        });


        return v;
    }


}
