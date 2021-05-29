package com.snap.eonmemory;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateTaskFragment extends BottomSheetDialogFragment {

    private View view;
    private TextInputLayout createTask_TILayout_title;
    private TextInputEditText createTask_TIEditText_title;
    private Button createTask_button_category;
    private ImageButton createTask_imageButton_calendar, createTask_imageButton_save;
    private PopupMenu categoryList;
    private int maxVarChar;
    private boolean validateTitle;

    public CreateTaskFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_task, container, false);

        initView();
        setListener();

        return view;
    }

    private void isSaveValid(boolean validateTitle) {
        createTask_imageButton_save.setEnabled(validateTitle);
    }

    private void setListener() {
        createTask_TILayout_title.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title = createTask_TILayout_title.getEditText().getText().toString().trim();

                if (title.isEmpty()) {
                    validateTitle = false;
                } else if (title.length() > maxVarChar) {
                    createTask_TILayout_title.setError("Title must not exceed 255 characters");
                    validateTitle = false;
                } else {
                    createTask_TILayout_title.setError(null);
                    validateTitle = true;
                }

                isSaveValid(validateTitle);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createTask_button_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open category list
                setPopupMenu();
            }
        });

        createTask_imageButton_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open calendar picker
                Toast.makeText(getContext(), "Calendar", Toast.LENGTH_SHORT).show();
            }
        });

        createTask_imageButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save task to database
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPopupMenu() {
        categoryList = new PopupMenu(getContext(), view);
        categoryList.getMenuInflater().inflate(R.menu.category_menu, categoryList.getMenu());

        categoryList.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(), "You clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        categoryList.show();
    }

    private void initView() {
        createTask_TILayout_title = view.findViewById(R.id.createTask_TILayout_title);
        createTask_TIEditText_title = view.findViewById(R.id.createTask_TIEditText_title);
        createTask_button_category = view.findViewById(R.id.createTask_button_category);
        createTask_imageButton_calendar = view.findViewById(R.id.createTask_imageButton_calendar);
        createTask_imageButton_save = view.findViewById(R.id.createTask_imageButton_save);

        createTask_TILayout_title.requestFocus();
        createTask_imageButton_save.setEnabled(false);

        maxVarChar = 255;
    }
}