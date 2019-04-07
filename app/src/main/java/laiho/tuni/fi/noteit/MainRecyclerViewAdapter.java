package laiho.tuni.fi.noteit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<Note> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private FileController fileController;
    Context context;

    public MainRecyclerViewAdapter (Context context, List<Note> data, FileController controller) {
        this.mData = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
        this.fileController = controller;
        Log.d("Adapter", "MainRecyclerViewAdapter: " + mData.size() + "vs " + data.size());
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
        private TextView descriptionTextView;
        private TextView pointsTextView;
        private RadioButton completeButton;

        ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.noteDescription);
            pointsTextView = itemView.findViewById(R.id.notePoints);
            completeButton = itemView.findViewById(R.id.completeButton);

            //itemView.setOnClickListener(this);
            completeButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(view.equals(completeButton)){
                removeAt(getAdapterPosition());
            }else if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public Note getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void radioOnClick(View v) {

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void removeAt(int position) {
        fileController.deleteNote("Note" + position + ".txt", mData.get(position).getAwardPoints());
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
        Log.d("test", "removeAt: " + position + " " + mData.size());
    }
}
