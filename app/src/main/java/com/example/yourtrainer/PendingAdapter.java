package com.example.yourtrainer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder> {
    private List<BookSession> itemList;
    private PendingAdapter.OnListListener listListener;
    public PendingAdapter(ArrayList<BookSession> pendingList, PendingAdapter.OnListListener onListListener) {
        this.itemList = pendingList;
        this.listListener = onListListener;
    }

    @NonNull
    @Override
    public PendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_list, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        PendingAdapter.ViewHolder slv = new PendingAdapter.ViewHolder(layoutView, (OnListListener) listListener);
        return slv;
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAdapter.ViewHolder holder, int position) {
        holder.sessionType.setText(itemList.get(position).getBookSessionType());
        holder.sessionStatus.setText(itemList.get(position).getBookSessionStatus());
        holder.sessionDate.setText(itemList.get(position).getDateSession());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView sessionType, sessionStatus, sessionDate;
        PendingAdapter.OnListListener onListListener;
        public ViewHolder(@NonNull View itemView, PendingAdapter.OnListListener listListener) {
            super(itemView);

            sessionType = itemView.findViewById(R.id.sessionType);
            sessionStatus = itemView.findViewById(R.id.sessionStatus);
            sessionDate = itemView.findViewById(R.id.sessionDate);
            this.onListListener=listListener;
            itemView.setOnClickListener(this);

        }

        public void onClick(View v) {
            onListListener.onListClick(getAdapterPosition());
        }
    }
    public interface OnListListener {
        void onListClick(int adapterPosition);
    }
}
