package com.snap.eonmemory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import model.Task;

public class TaskFragment extends Fragment {

    private RecyclerView home_recyclerView_task;
    private ArrayList<Task> taskList;
    private TaskRVAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        initView(view);
        setRecyclerView();
        addDummyData();
        loadDataDB();
        return view;
    }

    private void loadDataDB() {
        String url = "";
    }

    private void addDummyData() {
        taskList.add(new Task(1, "Halo", "hi", "hah", "a", "f"));
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        home_recyclerView_task.setLayoutManager(manager);
        home_recyclerView_task.setAdapter(adapter);
    }

    private void initView(View view) {
        home_recyclerView_task = view.findViewById(R.id.home_recyclerView_task);
        taskList = new ArrayList<Task>();
        adapter = new TaskRVAdapter(taskList); // Add card listener later
    }
}