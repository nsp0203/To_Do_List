package com.example.to_do_list.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do_list.Add_Task;
import com.example.to_do_list.MainActivity;
import com.example.to_do_list.Model.To_Do_Model;
import com.example.to_do_list.R;
import com.example.to_do_list.Utils.Database_Handler;

import java.util.List;

public class To_Do_Adapter extends RecyclerView.Adapter<To_Do_Adapter.ViewHolder> {

    private List<To_Do_Model> To_Do_List;
    private MainActivity activity;
    private Database_Handler db;

    public To_Do_Adapter(Database_Handler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView) ;
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.Open_Database();
        To_Do_Model item = To_Do_List.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.Update_Status(item.getId(),1);
                }
                else {
                    db.Update_Status(item.getId(),0);
                }
            }
        });
    }

    public int getItemCount(){
        return To_Do_List.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setTasks(List<To_Do_Model> To_Do_List){
        this.To_Do_List = To_Do_List;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        To_Do_Model item = To_Do_List.get(position);
        db.Delete_Task(item.getId());
        To_Do_List.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position){
        To_Do_Model item = To_Do_List.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("id", item.getTask());
        Add_Task fragment = new Add_Task();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), Add_Task.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.checkbox);
        }
    }
}
