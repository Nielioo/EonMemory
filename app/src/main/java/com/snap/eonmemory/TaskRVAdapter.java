package com.snap.eonmemory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Task;

public class TaskRVAdapter extends RecyclerView.Adapter<TaskRVAdapter.TaskViewHolder> {

    private ArrayList<Task> taskList;
    private OnCardClickListener cardClickListener;

    public TaskRVAdapter(ArrayList<Task> taskList, OnCardClickListener cardListener) {
        this.taskList = taskList;
        this.cardClickListener = cardListener;
    }


    @NonNull
    @Override
    public TaskRVAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRVAdapter.TaskViewHolder holder, int position) {
        // Set elements
        holder.cardView_textView_title.setText(taskList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        // Card elements
        private CheckBox cardView_checkBox_task;
        private TextView cardView_textView_title;

        public TaskViewHolder(@NonNull View taskView) {
            // Something needed
            super(taskView);

            // Initialize
            cardView_checkBox_task = taskView.findViewById(R.id.cardView_checkBox_task);
            cardView_textView_title = taskView.findViewById(R.id.cardView_textView_title);

            // Each card click event
            taskView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardClickListener.onClick(getAdapterPosition());
                }
            });

        }
    }
}
