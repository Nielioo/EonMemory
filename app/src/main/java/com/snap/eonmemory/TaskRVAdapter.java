package com.snap.eonmemory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Task;

public class TaskRVAdapter extends RecyclerView.Adapter<TaskRVAdapter.TaskViewHolder> {

    private ArrayList<Task> taskList;
    // Card listener

    public TaskRVAdapter(ArrayList<Task> taskList) {
        this.taskList = taskList;
        // Card listener
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
        holder.cardView_checkBox_task.setText(taskList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        // Card elements
        private CheckBox cardView_checkBox_task;

        public TaskViewHolder(@NonNull View taskView) {
            // Something needed
            super(taskView);

            // Initialize
            cardView_checkBox_task = taskView.findViewById(R.id.cardView_checkBox_task);

            // Each card click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Card listener
                }
            });

        }
    }
}
