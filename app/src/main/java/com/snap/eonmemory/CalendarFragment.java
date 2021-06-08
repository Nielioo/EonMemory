package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.OnCardClickListener;
import model.Task;

public class CalendarFragment extends Fragment implements OnCardClickListener {

    private View view;
    private RecyclerView calendar_recyclerView_task;
    private CalendarView calendar_calendarView;
    private ArrayList<Task> taskList;
    private TaskRVAdapter adapter;
    private String date;

    private Query taskReference;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initFirebase();
        initView();
        initCurrentDate();
        setRecyclerView();
        loadTask(date);
        setListener();

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

    private void setListener() {
        calendar_calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                taskList.clear();
                adapter.notifyDataSetChanged();

                month = month + 1;
                date = dayOfMonth + "/" + month + "/" + year;

                loadTask(date);
            }
        });
    }

    private void loadTask(String date) {
        taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection")
                .whereEqualTo("dueDate", date)
                .orderBy("created", Query.Direction.DESCENDING);

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
                            taskList.add(task);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        calendar_recyclerView_task.setLayoutManager(manager);
        calendar_recyclerView_task.setAdapter(adapter);
    }

    private void initCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        date = sdf.format(Calendar.getInstance().getTime());
    }

    private void initView() {
        calendar_calendarView = view.findViewById(R.id.calendar_calendarView);
        calendar_recyclerView_task = view.findViewById(R.id.calendar_recyclerView_task);
        taskList = new ArrayList<Task>();
        adapter = new TaskRVAdapter(taskList, this);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }
}