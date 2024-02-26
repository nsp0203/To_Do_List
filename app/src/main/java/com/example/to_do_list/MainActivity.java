package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.to_do_list.Adapter.To_Do_Adapter;
import com.example.to_do_list.Model.To_Do_Model;
import com.example.to_do_list.Utils.Database_Handler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListner{

    private RecyclerView task_recyclerview;
    private To_Do_Adapter tasks_adapter;
    private MaterialButton btn;

    private List<To_Do_Model> Task_List;
    private Database_Handler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new Database_Handler(this);
        db.Open_Database();
        Task_List = new ArrayList<>();
        task_recyclerview = findViewById(R.id.task_recyclerview);
        task_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        tasks_adapter=new To_Do_Adapter(db, this);
        task_recyclerview.setAdapter(tasks_adapter);

        btn = findViewById(R.id.btn);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItem_TouchHelper(tasks_adapter));
        itemTouchHelper.attachToRecyclerView(task_recyclerview);

        Task_List = db.Get_All_Tasks();
        Collections.reverse(Task_List);
        tasks_adapter.setTasks(Task_List);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_Task.new_Instance().show(getSupportFragmentManager(),Add_Task.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        Task_List = db.Get_All_Tasks();;
        tasks_adapter.setTasks(Task_List);
        tasks_adapter.notifyDataSetChanged();
    }
}