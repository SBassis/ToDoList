package com.lib.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
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

public class activity_add_new_task extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public static final String DATA = "DATA";
    ArraySet<Task> listTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        EditText editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        Switch switchTaskStatus = findViewById(R.id.switchTaskStatus);
        EditText editTextTaskDate = findViewById(R.id.editTextTaskDate);
        Button SaveTaskBtn = findViewById(R.id.savebtn);

      //  setupSharedPrefs();

        SaveTaskBtn.setOnClickListener(view -> {

                String description = editTextTaskDescription.getText().toString();
                boolean status = switchTaskStatus.isChecked();
                String dateString = editTextTaskDate.getText().toString();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dateString);

                if (!description.isEmpty()) {
                    prefs = getSharedPreferences(DATA, Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    Task newTask = new Task(description, status, date);
                    List<Task> existingTasks = loadTasksFromSharedPreferences();
                    existingTasks.add(newTask);
                    saveTasksToSharedPreferences(existingTasks);
                    Toast.makeText(activity_add_new_task.this, "Task added", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(activity_add_new_task.this, "Description is empty!", Toast.LENGTH_SHORT).show();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

    }



    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
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
        editor.apply();
    }


}