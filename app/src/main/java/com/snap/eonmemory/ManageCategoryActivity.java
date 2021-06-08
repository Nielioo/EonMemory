package com.snap.eonmemory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import model.CategoryList;

public class ManageCategoryActivity extends AppCompatActivity {

    private Toolbar manageCategory_toolbar;
    private RecyclerView manageCategory_recyclerView_category;
    private ArrayList<String> categoryList;
    private ManageCategoryRVAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        initFirebase();
        initView();
        setRecyclerView();
        loadTask();
        setListener();
    }

    private void setListener() {
        manageCategory_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadTask() {
        CollectionReference categoryReference = fStore.collection("user_collection").document(userID)
                .collection("category_collection");

//        categoryReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot data : queryDocumentSnapshots) {
//                    CategoryList categoryObject = data.toObject(CategoryList.class);
//
//                    categoryList = categoryObject.getCategory();
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(ManageCategoryActivity.this, String.valueOf(categoryList.size()), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        categoryReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                categoryList.clear();

                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        CategoryList categoryObject = doc.toObject(CategoryList.class);
                        ArrayList<String> categoryItemList = categoryObject.getCategory();

                        for (String category : categoryItemList) {
                            categoryList.add(category);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        manageCategory_recyclerView_category.setLayoutManager(manager);
        manageCategory_recyclerView_category.setAdapter(adapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        manageCategory_recyclerView_category.addItemDecoration(itemDecor);
    }

    private void initView() {
        manageCategory_toolbar = findViewById(R.id.manageCategory_toolbar_category);
        manageCategory_recyclerView_category = findViewById(R.id.manageCategory_recyclerView_category);
        categoryList = new ArrayList<String>();
        adapter = new ManageCategoryRVAdapter(categoryList);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }
}