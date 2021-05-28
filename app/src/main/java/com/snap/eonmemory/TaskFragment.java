package com.snap.eonmemory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Task;

public class TaskFragment extends Fragment {

    private View view;
    private RecyclerView home_recyclerView_task;
    private ArrayList<Task> taskList;
    private TaskRVAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        initView();
        setRecyclerView();
        addDummyData();
        loadDataDB();

        return view;
    }

    private void loadDataDB() {
        // Localhost
        String url = "http://192.168.1.6/EonMemory/ReadAllTask.php";

//        RequestQueue myqueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonItem = response.getJSONArray("barang");
                            for (int i = 0; i < jsonItem.length(); i++) {
                                JSONObject objectItem = jsonItem.getJSONObject(i);

//                                Item newItem = new Item();
//                                newItem.setId(objectItem.getInt("id"));
//                                newItem.setTitle(objectItem.getString("nama"));
//                                newItem.setImage_path(objectItem.getString("image_path"));
//                                newItem.setCreated(objectItem.getString("created"));
//                                newItem.setAmount(objectItem.getInt("jumlah"));
//                                itemList.add(newItem);
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

                    }
                }
        );

        myqueue.add(request);
    }

    private void addDummyData() {
        taskList.add(new Task(1, "Halo", "hi", "hah", "a", "f", "1", "2"));
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        home_recyclerView_task.setLayoutManager(manager);
        home_recyclerView_task.setAdapter(adapter);
    }

    private void initView() {
        home_recyclerView_task = view.findViewById(R.id.home_recyclerView_task);
        taskList = new ArrayList<Task>();
        adapter = new TaskRVAdapter(taskList); // Add card listener later
    }
}