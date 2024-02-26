package com.example.to_do_list;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.to_do_list.Adapter.To_Do_Adapter;
import com.example.to_do_list.Model.To_Do_Model;
import com.example.to_do_list.Utils.Database_Handler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Add_Task extends BottomSheetDialogFragment {

    public static final String TAG = "Action_Bottom_Dialog";

    private EditText New_Task_Text;
    private Button New_Task_Save_btn;
    private Database_Handler db;

    public static Add_Task new_Instance(){
        return new Add_Task();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_Style);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.add_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        New_Task_Text = getView().findViewById(R.id.new_task_text);
        New_Task_Save_btn = getView().findViewById(R.id.new_task_btn);

        db = new Database_Handler(getActivity());
        db.Open_Database();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            New_Task_Text.setText(task);
            if (task.length() > 0)
                New_Task_Save_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }

            New_Task_Text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    if (s.toString().equals("")) {
                        New_Task_Save_btn.setEnabled(false);
                        New_Task_Save_btn.setTextColor(Color.GRAY);
                    } else {
                        New_Task_Save_btn.setEnabled(true);
                        New_Task_Save_btn.setTextColor(ContextCompat.getColor((getContext()), R.color.white));

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            boolean finalIsUpdate = isUpdate;
            New_Task_Save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = New_Task_Text.getText().toString();
                    if (finalIsUpdate) {
                        db.Update_Task(bundle.getInt("id"), text);
                    } else {
                        To_Do_Model task = new To_Do_Model();
                        task.setTask(text);
                        task.setStatus(0);
                        db.Insert_Task(task);
                    }
                    dismiss();
                }
            });
         }
        @Override
        public void onDismiss(DialogInterface dialog){
            Activity activity = getActivity();
            if(activity instanceof DialogCloseListner)
                ((DialogCloseListner)activity).handleDialogClose(dialog);
        }
}
