package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import model.OnCardClickListener;
import model.Task;
import model.setRefresh;

public class TaskFragment extends Fragment implements OnCardClickListener, setRefresh {

    private View view;
    private RecyclerView home_recyclerView_task;
    private SwipeRefreshLayout task_swipeRefresh;
    private FloatingActionButton task_FAB_create;
    private ArrayList<Task> taskList;
    private TaskRVAdapter adapter;
    private String category;

    private Query taskReference;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            category = bundle.getString("category");
        }

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
        Task task = taskList.get(position);
        String taskId = task.TaskId;

        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
    }

    @Override
    public void setSwipeRefresh() {
        task_swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                taskList.clear();
                loadTask();

                task_swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void setListener() {
        task_FAB_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                CreateTaskFragment createTask = new CreateTaskFragment(TaskFragment.this);
                createTask.show(fm, null);
            }
        });
    }

    private void loadTask() {
        taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection");

        taskReference.orderBy("created", Query.Direction.DESCENDING);

        taskReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                taskList.clear();

                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        String id = doc.getId();
                        Task task = doc.toObject(Task.class).withId(id);

                        // Add task to taskList
                        if (task.getStatus() == 0) {
                            if (category != null) {
                                if (task.getCategory().equalsIgnoreCase(category)) {
                                    taskList.add(task);
                                }
                            } else {
                                taskList.add(task);
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged();
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
        adapter = new TaskRVAdapter(taskList, this);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }
}