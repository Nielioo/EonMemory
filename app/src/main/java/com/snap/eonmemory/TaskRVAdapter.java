package com.snap.eonmemory;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.Note;
import model.OnCardClickListener;
import model.Task;

public class TaskRVAdapter extends RecyclerView.Adapter<TaskRVAdapter.TaskViewHolder> implements Filterable {

    private ArrayList<Task> taskList;
    private OnCardClickListener cardListener;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    public TaskRVAdapter(ArrayList<Task> taskList, OnCardClickListener cardListener) {
        this.taskList = taskList;
        this.cardListener = cardListener;
    }

    @NonNull
    @Override
    public TaskRVAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_task, parent, false);

        // Initialize
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRVAdapter.TaskViewHolder holder, int position) {
        // Set elements
        Task task = taskList.get(position);

        holder.cardView_textView_title.setText(task.getTitle());
        holder.cardView_checkBox_task.setChecked(toBoolean(task.getStatus()));
        holder.cardView_textView_dueDate.setText(task.getDueDate());
        holder.cardView_textView_time.setText(task.getTime());
    }

    private Boolean toBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Task> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(taskList);
            } else {
                for (Task task: taskList){
                    if(task.getTitle().toLowerCase().trim().contains(charSequence.toString().toLowerCase().trim())){
                        filteredList.add(task);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            taskList.clear();
            taskList.addAll((Collection<? extends Task>) results.values);
            notifyDataSetChanged();
        }
    };

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        // Card elements
        private CheckBox cardView_checkBox_task;
        private TextView cardView_textView_title, cardView_textView_dueDate, cardView_textView_time;

        public TaskViewHolder(@NonNull View taskView) {
            // Something needed
            super(taskView);

            // Initialize
            cardView_checkBox_task = taskView.findViewById(R.id.cardView_checkBox_task);
            cardView_textView_title = taskView.findViewById(R.id.cardView_textView_title);
            cardView_textView_dueDate = taskView.findViewById(R.id.cardView_textView_dueDate);
            cardView_textView_time = taskView.findViewById(R.id.cardView_textView_time);

            // Each card click event
            taskView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardListener.onClick(getAdapterPosition());
                }
            });

            // Each card check event
            cardView_checkBox_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    Task task = taskList.get(position);
                    MediaPlayer mediaPlayer = MediaPlayer.create(taskView.getContext(), R.raw.ting_sound_effect);

                    DocumentReference taskReference = fStore.collection("user_collection").document(userID)
                            .collection("task_collection").document(task.TaskId);

                    if (isChecked) {
                        taskReference.update("status", 1);
                        taskList.remove(position);
                        notifyItemRemoved(position);
                        mediaPlayer.start();
                    }
                    // Nightmare, fires almost everytime
//                    taskReference.update("updated", FieldValue.serverTimestamp());
                }
            });

        }
    }
}
