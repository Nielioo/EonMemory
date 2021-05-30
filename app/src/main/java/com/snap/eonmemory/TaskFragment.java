package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
//        loadDataDB();
        setListener();
        setSwipeRefresh();

        return view;
    }

    @Override
    public void onClick(int position) {
        int id = taskList.get(position).getId();
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void setSwipeRefresh() {
        task_swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataDB();

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

    private void loadDataDB() {
        // Clear arraylist to prevent same data to be loaded twice
        taskList.clear();

        // Localhost
        String url = "http://192.168.1.6/EonMemory/EonMemoryDB/ReadAllTask.php";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonItem = response.getJSONArray("task");
                            for (int i = 0; i < jsonItem.length(); i++) {
                                JSONObject objectItem = jsonItem.getJSONObject(i);

                                // Get task details
                                Task newTask = new Task();
                                newTask.setId(objectItem.getInt("id"));
                                newTask.setUsername(objectItem.getString("username"));
                                newTask.setTitle(objectItem.getString("title"));
                                newTask.setDescription(objectItem.getString("description"));
                                newTask.setCategory(objectItem.getString("category"));
                                newTask.setDue_date(objectItem.getString("due_date"));
                                newTask.setTime(objectItem.getString("time"));
                                newTask.setCreated(objectItem.getString("created"));
                                newTask.setUpdated(objectItem.getString("updated"));
                                taskList.add(newTask);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(request);
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