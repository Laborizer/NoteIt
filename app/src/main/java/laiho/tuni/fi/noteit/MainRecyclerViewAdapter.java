package laiho.tuni.fi.noteit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<Note> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public MainRecyclerViewAdapter (Context context, List<Note> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycler_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        String note = mData.get(i).getDescription();
        int points = mData.get(i).getAwardPoints();
        viewHolder.descriptionTextView.setText(note);
        viewHolder.pointsTextView.setText(Integer.toString(points));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView descriptionTextView;
        TextView pointsTextView;
        Button checkButton;

        ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.noteDescription);
            pointsTextView = itemView.findViewById(R.id.notePoints);
            checkButton = itemView.findViewById(R.id.completeButton);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Note getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
