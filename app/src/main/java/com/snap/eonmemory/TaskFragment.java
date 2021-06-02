package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import model.Task;

public class TaskFragment extends Fragment implements OnCardClickListener {

    private View view;
    private RecyclerView home_recyclerView_task;
    private SwipeRefreshLayout task_swipeRefresh;
    private FloatingActionButton task_FAB_create;
    private ArrayList<Task> taskList;
    private TaskRVAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task, container, false);

        initFirebase();
        initView();
        setRecyclerView();
        loadTask();
        setListener();
        setSwipeRefresh();

        return view;
    }

    @Override
    public void onClick(int position) {
//        int id = taskList.get(position).getId();
//        Intent intent = new Intent(getContext(), EditTaskActivity.class);
//        intent.putExtra("id", id);
//        startActivity(intent);
    }

    private void setSwipeRefresh() {
        task_swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTask();

                task_swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void setListener() {
        task_FAB_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTaskFragment createTask = new CreateTaskFragment();
                createTask.show(getParentFragmentManager(), null);
            }
        });
    }

    private void loadTask() {
        CollectionReference taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection");

        taskReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // For each document loop
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    // If it's new
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        // Get the document ID
                        String id = documentChange.getDocument().getId();
                        Task task = documentChange.getDocument().toObject(Task.class).withId(id);

                        // Add task to taskList
                        taskList.add(task);
                        adapter.notifyDataSetChanged();
                    }
                }

                // Reverse the order as newest first
                Collections.reverse(taskList);
            }
        });
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        home_recyclerView_task.setLayoutManager(manager);
        home_recyclerView_task.setAdapter(adapter);
    }

    private void initView() {
        home_recyclerView_task = view.findViewById(R.id.home_recyclerView_task);
        task_swipeRefresh = view.findViewById(R.id.task_swipeRefresh);
        task_FAB_create = view.findViewById(R.id.task_FAB_create);
        taskList = new ArrayList<Task>();
        adapter = new TaskRVAdapter(taskList, this); // Add card listener later
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }
}