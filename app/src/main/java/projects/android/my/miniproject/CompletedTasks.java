package projects.android.my.miniproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/* This activity is opened when we click on the completed tasks on the home screen
    Shows a list of completed tasks
    On long click deletes the selected completed task
 */
public class CompletedTasks extends AppCompatActivity implements AdapterView.OnItemLongClickListener
{

    ListView list;
    String[] task_title;
    String[] task_desc;
    String[] task_date;
    int[] taskStatus;
    SQLiteDatabase db;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);
        list = (ListView) findViewById(R.id.completedlistView);

        //Connect to the DB
        ToDoDB customDB = new ToDoDB(this);
        db = customDB.getWritableDatabase();

        //Call this Method to load the Completed Tasks
        LoadCompletedTasks();

    }

    private void LoadCompletedTasks()
    {
        //Query to get the list of Completed tasks
        Cursor completedTasks = db.rawQuery("SELECT * FROM Tasks where KEY_STATUS = 1",null);

        //If there are completed then load
        if(completedTasks.getCount() > 0)
        {
            completedTasks.moveToFirst();
            task_title = new String[completedTasks.getCount()];
            task_desc = new String[completedTasks.getCount()];
            task_date = new String[completedTasks.getCount()];
            taskStatus = new int[completedTasks.getCount()];
            int pos = 0;
            do {
                task_title[pos] = completedTasks.getString(1);
                task_desc[pos] = completedTasks.getString(2);
                task_date[pos] = completedTasks.getString(3);
                int index = completedTasks.getColumnIndex("KEY_STATUS");
                taskStatus[pos] = completedTasks.getInt(index);
                pos++;
            } while (completedTasks.moveToNext());

            customAdapter = new CustomAdapter(this, task_title, task_desc, task_date,taskStatus);
            customAdapter.notifyDataSetChanged();
            list.setAdapter(customAdapter);

            list.setOnItemLongClickListener(this);

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        //This is called on long click of list view
        // Deletes a task
        String task_Title,task_Desc;
        TextView txtTaskTitle = (TextView)view.findViewById(R.id.name);
        TextView  txtTaskDesc = (TextView)view.findViewById(R.id.phone);
        task_Title = txtTaskTitle.getText().toString();
        task_Desc = txtTaskDesc.getText().toString();

        //Fetch ID based on task title and desc
        Cursor cursor  = db.rawQuery("SELECT KEY_ID FROM Tasks where KEY_TITLE = '"+task_Title+"' AND KEY_DESCRIPTION = '"+task_Desc+"'",null);
        {
            cursor.moveToFirst();
            do
            {
                int keyId = cursor.getColumnIndex("KEY_ID");
                int key = cursor.getInt(keyId);

                //Delete Task
                db.execSQL("DELETE FROM Tasks WHERE KEY_ID ="+key);
                Toast.makeText(CompletedTasks.this,"Deleted",Toast.LENGTH_LONG).show();
                LoadCompletedTasks();


            }while (cursor.moveToNext());
        }
        return  true;
     }
}
