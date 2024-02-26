package com.example.to_do_list.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.to_do_list.Model.To_Do_Model;

import java.util.ArrayList;
import java.util.List;

public class Database_Handler extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String NAME = "To_Do_List_Database";
    private static final String TO_DO_TABLE = "To_Do";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS ="status";
    private static final String CREATE_TO_DO_TABLE = "CREATE TABLE " +TO_DO_TABLE + "(" +ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                       +TASK +" TEXT, " + STATUS +" INTEGER)";
    private SQLiteDatabase db;


    public Database_Handler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TO_DO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        // Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + TO_DO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void Open_Database(){
        db = this.getWritableDatabase();
    }

    public void Insert_Task(To_Do_Model task){
        ContentValues cv= new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TO_DO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<To_Do_Model> Get_All_Tasks(){
        List<To_Do_Model> Task_List = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TO_DO_TABLE, null, null, null, null, null, null, null);
            if(cur !=null){
                if(cur.moveToFirst()){
                    do{
                        To_Do_Model task= new To_Do_Model();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        Task_List.add(task);
                    }while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return Task_List;
    }

    public void Update_Status(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TO_DO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void Update_Task(int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TO_DO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void Delete_Task(int id){
        db.delete(TO_DO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}

