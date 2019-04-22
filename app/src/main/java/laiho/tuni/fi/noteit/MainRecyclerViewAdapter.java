package laiho.tuni.fi.noteit;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<Note> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private JsonController jsonController;
    Context context;

    public MainRecyclerViewAdapter (Context context, List<Note> data, JsonController controller) {
        this.mData = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
        this.jsonController = controller;
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
        if(i %2 == 1)
        {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffdb8e"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#e5c379"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
                int i = getAdapterPosition();
                mClickListener.onItemClick(
                        view,
                        getAdapterPosition(),
                        mData.get(getAdapterPosition()).getAwardPoints());
                removeAt(i);
            }else if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition(), 0);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            removeAt(getAdapterPosition());
            return true;
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
        void onItemClick(View view, int position, int points);
    }

    public void removeAt(int position) {


        mData.remove(position);
        JSONArray arr = jsonController.createNoteJsonArray(this.mData);
        jsonController.writeJson("AllNotes.json", arr.toString());
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
        Log.d("test", "removeAt: " + position + " " + mData.size());
    }
}
