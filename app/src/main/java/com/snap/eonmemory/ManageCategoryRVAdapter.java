package com.snap.eonmemory;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Task;

public class ManageCategoryRVAdapter extends RecyclerView.Adapter<ManageCategoryRVAdapter.ManageCategoryViewHolder> {

    private ArrayList<String> categoryList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    public ManageCategoryRVAdapter(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ManageCategoryRVAdapter.ManageCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_category, parent, false);

        // Initialize
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        return new ManageCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCategoryRVAdapter.ManageCategoryViewHolder holder, int position) {
        String category = categoryList.get(position);

        if (category.equalsIgnoreCase("Personal")) {
            holder.manageCategory_textView_category.setVisibility(View.GONE);
            holder.manageCategory_imageView_clear.setVisibility(View.GONE);
            holder.manageCategory_imageView_edit.setVisibility(View.GONE);
        } else {
            holder.manageCategory_textView_category.setText(category);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ManageCategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView manageCategory_textView_category;
        private ImageView manageCategory_imageView_clear, manageCategory_imageView_edit;

        public ManageCategoryViewHolder(@NonNull View manageCategoryView) {
            super(manageCategoryView);

            manageCategory_textView_category = manageCategoryView.findViewById(R.id.manageCategory_textView_category);
            manageCategory_imageView_clear = manageCategoryView.findViewById(R.id.manageCategory_imageView_clear);
            manageCategory_imageView_edit = manageCategoryView.findViewById(R.id.manageCategory_imageView_edit);

            manageCategory_imageView_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String category = manageCategory_textView_category.getText().toString().trim();

                    // Delete category
                    DocumentReference categoryList = fStore.collection("user_collection").document(userID)
                            .collection("category_collection").document("category_list");

                    categoryList.update("category", FieldValue.arrayRemove(category));

                    // Revert category to Personal
                    CollectionReference categoryReference = fStore.collection("user_collection").document(userID)
                            .collection("task_collection");

                    categoryReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot data : queryDocumentSnapshots) {
                                String id = data.getId();
                                Task task = data.toObject(Task.class).withId(id);
                                String currentCategory = task.getCategory();

                                if (currentCategory.equalsIgnoreCase(category)) {
                                    DocumentReference taskReference = fStore.collection("user_collection")
                                            .document(userID).collection("task_collection").document(task.TaskId);

                                    taskReference.update("category", "Personal");
                                }
                            }
                        }
                    });
                }
            });

            manageCategory_imageView_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String category = manageCategory_textView_category.getText().toString().trim();

                    Intent intent = new Intent(manageCategory_imageView_edit.getContext(), EditCategoryActivity.class);
                    intent.putExtra("category", category);
                    manageCategory_imageView_edit.getContext().startActivity(intent);
                }
            });
        }
    }
}
