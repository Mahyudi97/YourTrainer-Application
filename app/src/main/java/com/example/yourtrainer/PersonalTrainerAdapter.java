package com.example.yourtrainer;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PersonalTrainerAdapter extends RecyclerView.Adapter<PersonalTrainerAdapter.ViewHolder> {
    private List<PersonalTrainer> itemList;
    private Context context;
    private PersonalTrainerAdapter.OnListListener listListener;
    private OnListListener onListListener;

    public PersonalTrainerAdapter(ArrayList<PersonalTrainer> itemList, PersonalTrainerAdapter.OnListListener onListListener) {
        this.itemList = itemList;
        this.listListener = onListListener;
    }


    @NonNull
    @Override
    public PersonalTrainerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        PersonalTrainerAdapter.ViewHolder slv = new PersonalTrainerAdapter.ViewHolder(layoutView,listListener);
        return slv;
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonalTrainerAdapter.ViewHolder holder, int position) {
        holder.fullName.setText(itemList.get(position).getTrainerFullName());
        holder.username.setText(itemList.get(position).getTrainerUsername());

        String imageId = itemList.get(position).getImageId();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        //get imageId
        StorageReference ref = storageReference.child("Personal Trainer/"+imageId);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //replace image with image from firebase
                Picasso.get().load(uri.toString()).into(holder.profilePicture);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView fullName, username;
        final public ImageView profilePicture;
        PersonalTrainerAdapter.OnListListener onListListener;
        public ViewHolder(@NonNull View itemView, PersonalTrainerAdapter.OnListListener onlistListener) {
            super(itemView);
            fullName =  itemView.findViewById(R.id.fullName);
            username = itemView.findViewById(R.id.username);
            profilePicture = itemView.findViewById(R.id.profile_image);
            this.onListListener = onlistListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onListListener.onListClick(getAdapterPosition());
        }
    }

    public interface OnListListener {
        void onListClick (int position);
    }


}
