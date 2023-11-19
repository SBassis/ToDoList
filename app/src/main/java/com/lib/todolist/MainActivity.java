package com.lib.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Task> adapter;
    private List<Task> taskList;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static final String DATA = "DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton addButton = findViewById(R.id.addbtn);
        ListView listView = findViewById(R.id.main_tasks);
        setupSharedPrefs();
        SavetoSharedPreference();
        taskList = loadTasksFromSharedPreferences();

        adapter = new ArrayAdapter<Task>(this, R.layout.listview_items, taskList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.listview_items, parent, false);
                }
                TextView descriptionTextView = convertView.findViewById(R.id.taskDescription);
                TextView dateTextView = convertView.findViewById(R.id.taskDate);
                Switch statusSwitch = convertView.findViewById(R.id.taskStatusSwitch);
                statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Task currentTask = getItem(position);
                        if (currentTask != null) {
                            currentTask.setStatus(isChecked);
                            if (isChecked) {
                                taskList.remove(currentTask);//remove if completed
                            }
                            saveTasksToSharedPreferences(taskList);
                            notifyDataSetChanged();
                        }
                    }
                });

                Task currentTask = getItem(position);

                if (currentTask != null) {
                    descriptionTextView.setText("Task Description: " + currentTask.getDescription());
                    statusSwitch.setChecked(currentTask.isStatus());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateTextView.setText("Task Date: " + dateFormat.format(currentTask.getDate()));
                }

                return convertView;
            }
        };

        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, activity_add_new_task.class);
                startActivity(intent);

            }
        });
    }
    private void saveTasksToSharedPreferences(List<Task> taskList) {
        Gson gson = new Gson();
        String tasksJson = gson.toJson(taskList);
        editor.putString(DATA, tasksJson);
        editor.commit();
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }


    private void SavetoSharedPreference() {
        // Create a list of tasks with your data, including dates
        List<Task> TasksExamples = new ArrayList<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            TasksExamples.add(new Task("Study Mobile", false, sdf.parse("2023-11-19")));
            TasksExamples.add(new Task("Reading Qur'an", false, sdf.parse("2023-11-20")));
            TasksExamples.add(new Task("Feed The Cats", false, sdf.parse("2023-11-21")));
            TasksExamples.add(new Task("Study AI", false, sdf.parse("2023-11-21")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String tasksJson = gson.toJson(TasksExamples);
        editor.putString(DATA, tasksJson);
        editor.commit();
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


}
