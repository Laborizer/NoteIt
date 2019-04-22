package laiho.tuni.fi.noteit;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

/**
 * MainRecyclerViewAdapter is an adapter class which controls the flow of information in the
 * RecyclerView. It holds the data set for the View as well as handles onClicks of the Notes.
 *
 * @author Lauri Laiho
 * @version 1.0
 * @since 2019-04-07
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    /**
     * List of Notes which are shown in the RecyclerView
     */
    private List<Note> mData;

    /**
     * Inflates the View components into corresponding XML-layout.
     */
    private LayoutInflater mInflater;

    /**
     * Listener which controls the presses on the items
     */
    private ItemClickListener mClickListener;

    /**
     * JsonController, which controls the flow of JSON data.
     */
    private JsonController jsonController;

    /**
     * Holds information about the Application and where the class is used from.
     */
    private Context context;

    /**
     * Constructor for the MainRecyclerViewAdapter
     *
     * @param context Application context from where its used.
     * @param data List of Notes to act as data.
     * @param controller Controller for usage of JSON.
     */
    public MainRecyclerViewAdapter (Context context, List<Note> data, JsonController controller) {
        this.mData = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
        this.jsonController = controller;
    }

    /**
     * Method creates a ViewHolder which holds the different views defined in the XML-layout.
     *
     * @param viewGroup Parent to which new ViewHolder is attached.
     * @param i Type of view.
     * @return ViewHolder Represents a new item in the RecyclerView.
     */
    @NonNull
    @Override
    public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycler_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * Method is called when data is displayed or changed in a specific position.
     *
     * @param viewHolder Specific ViewHolder in which data will be displayed.
     * @param i Position of the ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        String note = mData.get(i).getDescription();
        int points = mData.get(i).getAwardPoints();
        viewHolder.descriptionTextView.setText(note);
        viewHolder.pointsTextView.setText(Integer.toString(points));
        if(i %2 == 1)
        {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffdb8e"));
        }
        else
        {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#e5c379"));
        }
    }

    /**
     * @return int Amount of entries in the Data set.
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * ViewHolder is a class which holds the different View components of a RecyclerView item. It
     * has two TextViews for displaying contents of a Note and the amount of points awarded for
     * clearing it. It also has a RadioButton which clears the Note.
     *
     * @author Lauri Laiho
     * @version 1.0
     * @since 2019-04-07
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * TextView for description of the Note.
         */
        private TextView descriptionTextView;

        /**
         * TextView for points of the Note.
         */
        private TextView pointsTextView;

        /**
         * RadioButton for clearing the Note.
         */
        private RadioButton completeButton;

        /**
         * Constructor for a ViewHolder.
         *
         * @param itemView View to which the child views are attached to.
         */
        ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.noteDescription);
            pointsTextView = itemView.findViewById(R.id.notePoints);
            completeButton = itemView.findViewById(R.id.completeButton);

            completeButton.setOnClickListener(this);

        }

        /**
         * Method which determines what happens when a view is pressed. If the view is the
         * RadioButton of a Note, then it will be cleared.
         *
         * @param view View in which onClick has been invoked in.
         */
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
    }

    /**
     * Returns a Note of given index from the List of Notes.
     *
     * @param id Index of the note to be returned.
     * @return Note The Note of a specified index.
     */
    public Note getItem(int id) {
        return mData.get(id);
    }

    /**
     * Sets the itemClickListener
     *
     * @param itemClickListener The ItemClickListener to be set.
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * Interface which defines the ItemClickListeners to implement onItemClick -method.
     *
     * @author Lauri Laiho
     * @version 1.0
     * @since 2019-07-04
     */
    public interface ItemClickListener {

        /**
         * Abstract method which has to be implemented. Guides the used of the onItemClick.
         *
         * @param view View that was clicked.
         * @param position Position of the item clicked.
         * @param points Amount of points in the given position.
         */
        void onItemClick(View view, int position, int points);
    }

    /**
     * Method removes data from the RecyclerView Data set as well as removes it from JSON data by
     * creating a new JSONArray from the dataset and overwriting the old one.
     *
     * @param position Position of the removed item.
     */
    public void removeAt(int position) {
        mData.remove(position);
        JSONArray arr = jsonController.createNoteJsonArray(this.mData);
        jsonController.writeJson("AllNotes.json", arr.toString());
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }
}
