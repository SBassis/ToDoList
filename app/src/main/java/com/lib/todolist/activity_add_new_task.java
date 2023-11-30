package com.lib.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
public class activity_add_new_task extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public static final String DATA = "DATA";
    ArraySet<Task> listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        EditText TaskDescription = findViewById(R.id.etDescription);
        Switch TaskStatus = findViewById(R.id.switchStatus);
        EditText TaskDueDate = findViewById(R.id.etTaskDate);
        Button SaveTaskBtn = findViewById(R.id.savebtn);

        TaskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalender(TaskDueDate);
            }
        });

        SaveTaskBtn.setOnClickListener(view -> {

            String description = TaskDescription.getText().toString();
            boolean status = TaskStatus.isChecked();
            String dateString = TaskDueDate.getText().toString();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dateString);

                if (!description.isEmpty()) {
                    prefs = getSharedPreferences(MainActivity.DATA, Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    Task newTask = new Task(description, status, date);
                    List<Task> preTasks = loadTasksFromSharedPreferences();
                    preTasks.add(newTask);
                    saveTasksToSharedPreferences(preTasks);
                    Toast.makeText(activity_add_new_task.this, "Task added!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(activity_add_new_task.this, "Don't let the Description empty!", Toast.LENGTH_SHORT).show();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void showCalender(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        calendar.set(y, m, d);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        editText.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                y, m, d);

        datePickerDialog.show();
    }

    private List<Task> loadTasksFromSharedPreferences() {
        Gson gson = new Gson();
        String json = prefs.getString(DATA, "");

        if (json.isEmpty()) {
            return new ArrayList<>();
        } else {
            Type type = new TypeToken<List<Task>>() {
            }.getType();
            return gson.fromJson(json, type);
        }
    }

    private void saveTasksToSharedPreferences(List<Task> taskList) {
        Gson gson = new Gson();
        String tasksJson = gson.toJson(taskList);
        editor.putString(DATA, tasksJson);
        editor.commit(); // Use apply() for asynchronous saving
    }
}