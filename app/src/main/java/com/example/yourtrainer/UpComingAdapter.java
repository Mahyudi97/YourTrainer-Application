package com.example.yourtrainer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

class UpComingAdapter extends RecyclerView.Adapter<UpComingAdapter.ViewHolder> {
    private List<BookSession> itemList;
    private UpComingAdapter.OnListListener listListener;
    public UpComingAdapter(ArrayList<BookSession> upcomingList, UpComingAdapter.OnListListener onListListener) {
        this.itemList = upcomingList;
        this.listListener = onListListener;
    }

    @NonNull
    @Override
    public UpComingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_list, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        UpComingAdapter.ViewHolder slv = new UpComingAdapter.ViewHolder(layoutView,listListener);
        return slv;
    }

    @Override
    public void onBindViewHolder(@NonNull UpComingAdapter.ViewHolder holder, int position) {
        holder.sessionType.setText(itemList.get(position).getBookSessionType());
        holder.sessionStatus.setText(itemList.get(position).getBookSessionStatus());
        holder.sessionDate.setText(itemList.get(position).getDateSession());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnListListener {
        void onListClick(int adapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView sessionType, sessionStatus, sessionDate;
        UpComingAdapter.OnListListener onListListener;
        public ViewHolder(@NonNull View itemView, UpComingAdapter.OnListListener  listListener) {
            super(itemView);

            sessionType = itemView.findViewById(R.id.sessionType);
            sessionStatus = itemView.findViewById(R.id.sessionStatus);
            sessionDate = itemView.findViewById(R.id.sessionDate);
            this.onListListener = listListener;
            itemView.setOnClickListener(this);

        }

        public void onClick(View v) {
            onListListener.onListClick(getAdapterPosition());
        }
    }
}
