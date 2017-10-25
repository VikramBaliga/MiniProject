package projects.android.my.miniproject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 25-10-2017.
 */

/*
This class is used to Update task details and change the status of Task to complete or incompleted
 */
public class ListItemSelectionListner implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener
{
    Context context;
    SQLiteDatabase db;
    View view;
    ListView listView;
    String task_Title,task_Desc;
    public  ListItemSelectionListner(Context context,ListView list)
    {
        this.context = context;
        listView=list;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        TextView txtTaskTitle = (TextView)view.findViewById(R.id.name);
        TextView  txtTaskDesc = (TextView)view.findViewById(R.id.phone);
        task_Title = txtTaskTitle.getText().toString();
        task_Desc = txtTaskDesc.getText().toString();

        ToDoDB customDB = new ToDoDB(context);
        db = customDB.getWritableDatabase();
        //GEt Id based on title and desc
        Cursor cursor = db.rawQuery("SELECT KEY_ID FROM Tasks where KEY_TITLE = '"+task_Title+"' AND KEY_DESCRIPTION = '"+task_Desc+"'",null);

        if(cursor == null)
        {
          Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
        }
        else
        {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("KEY_ID");
            int keyID = cursor.getInt(index);
            //Call to update details
            UpdateRecord(keyID);
           // Toast.makeText(context,"Temp ="+temp,Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateRecord(final int keyId)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        view = LayoutInflater.from(context).inflate(R.layout.updatetask,null);
        builder.setView(view);
        final EditText updatedTitle = (EditText)view.findViewById(R.id.txtuTitle);
        final EditText updatedDesc =  (EditText)view.findViewById(R.id.txtuDesc);
        //Prepopulate existing data
        updatedTitle.setHint(task_Title);
        updatedDesc.setHint(task_Desc);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Save Updated details
                ContentValues updatedValues = new ContentValues();
                updatedValues.put("KEY_TITLE",updatedTitle.getText().toString());
                updatedValues.put("KEY_DESCRIPTION",updatedDesc.getText().toString());
                db.update("Tasks",updatedValues,"KEY_ID ="+keyId,null);
                //Load the new Data
                UpdateListView();
                Toast.makeText(context,"Updated",Toast.LENGTH_LONG).show();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void UpdateListView()
    {

        //Refresh the view
        String[] task_title;
        String[] task_desc;
        String[] task_date;
        int[] taskStatus;
        Cursor records = db.query("Tasks",null,null,null,null,null,null);
        if(records.getCount() > 0) {
            records.moveToFirst();
            task_title = new String[records.getCount()];
            task_desc = new String[records.getCount()];
            task_date = new String[records.getCount()];
            taskStatus = new int[records.getCount()];
            int pos = 0;
            do {
                task_title[pos] = records.getString(1);
                task_desc[pos] = records.getString(2);
                task_date[pos] = records.getString(3);
                int index = records.getColumnIndex("KEY_STATUS");
                taskStatus[pos] = records.getInt(index);
                pos++;
            } while (records.moveToNext());

            CustomAdapter customAdapter = new CustomAdapter(context, task_title, task_desc, task_date, taskStatus);
            listView.setAdapter(customAdapter);
        }
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Method to updated the task status
        String task_Title,task_Desc;

        TextView txtTaskTitle = (TextView)view.findViewById(R.id.name);
        TextView  txtTaskDesc = (TextView)view.findViewById(R.id.phone);
        task_Title = txtTaskTitle.getText().toString();
        task_Desc = txtTaskDesc.getText().toString();

        ToDoDB customDB = new ToDoDB(context);
        db = customDB.getWritableDatabase();
        //String query = "SELECT KEY_DESCRIPTION FROM Tasks where = '"+text+;
        Cursor cursor = db.rawQuery("SELECT KEY_STATUS,KEY_ID FROM Tasks where KEY_TITLE = '"+task_Title+"' AND KEY_DESCRIPTION = '"+task_Desc+"'",null);

        if(cursor == null)
        {
            Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
        }
        else
        {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("KEY_STATUS");
            int status = cursor.getInt(index);
            index = cursor.getColumnIndex("KEY_ID");
            int keyId = cursor.getInt(index);
         //   Toast.makeText(context,"Status ="+status,Toast.LENGTH_LONG).show();
            ContentValues updateStaus = new ContentValues();
            if(status==0)
                updateStaus.put("KEY_STATUS",1);
            else
                updateStaus.put("KEY_STATUS",0);

            db.update("Tasks",updateStaus,"KEY_ID ="+keyId,null);
            UpdateListView();
        //    Toast.makeText(context,"Updated",Toast.LENGTH_LONG).show();
        }
        return  true;
    }
}
