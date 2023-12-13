package com.example.yourtrainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends Fragment implements  UpComingAdapter.OnListListener, PendingAdapter.OnListListener{
    BookSession bookSession = new BookSession();
    private ArrayList<BookSession> upcomingList = new ArrayList<>();
    private ArrayList<BookSession> pendingList = new ArrayList<>();
    private RecyclerView pendingRV;
    private RecyclerView upcomingRV;
    private RecyclerView.Adapter pendingAdapter;
    private RecyclerView.Adapter upcomingAdapter;
    private RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=  inflater.inflate(R.layout.activity_home_page,container,false);

        //Upcoming RecycerlerView
        upcomingRV = v.findViewById(R.id.upcomingRV);
        upcomingRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        upcomingRV.setLayoutManager(llm);
        upcomingAdapter = new UpComingAdapter(upcomingList,this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Book Session");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                String userId=firebaseUser.getUid();
                for (DataSnapshot snap : snapshot.getChildren()){
                    if (snap.child("clientId").getValue().toString().equals(userId)) {
                        if (snap.child("bookSessionStatus").getValue().toString().equals("Approve")) {

                            bookSession = new BookSession();
                            bookSession.setDateSession(snap.child("dateSession").getValue().toString());
                            bookSession.setBookSessionStatus(snap.child("bookSessionStatus").getValue().toString());
                            bookSession.setBookSessionType(snap.child("bookSessionType").getValue().toString());
                            bookSession.setClientId(snap.child("clientId").getValue().toString());
                            bookSession.setTrainerId(snap.child("trainerId").getValue().toString());

                            upcomingList.add(bookSession);
                        }
                    }
                }
                upcomingRV.setAdapter(upcomingAdapter);
                upcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Pending RecycerlerView
        pendingRV = v.findViewById(R.id.pendingRV);
        pendingRV.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        pendingRV.setLayoutManager(lm);
        pendingAdapter = new PendingAdapter(pendingList,this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Book Session");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                String userId=firebaseUser.getUid();
                for (DataSnapshot snap : snapshot.getChildren()){
                    if (snap.child("clientId").getValue().toString().equals(userId)) {
                        if (snap.child("bookSessionStatus").getValue().toString().equals("Pending")) {
                            bookSession = new BookSession();
                            bookSession.setDateSession(snap.child("dateSession").getValue().toString());
                            bookSession.setBookSessionStatus(snap.child("bookSessionStatus").getValue().toString());
                            bookSession.setBookSessionType(snap.child("bookSessionType").getValue().toString());
                            bookSession.setClientId(snap.child("clientId").getValue().toString());
                            bookSession.setTrainerId(snap.child("trainerId").getValue().toString());

                            pendingList.add(bookSession);
                        }
                    }
                }
                pendingRV.setAdapter(pendingAdapter);
                pendingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }

    @Override
    public void onListClick(int adapterPosition) {

    }
}
