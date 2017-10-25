package projects.android.my.miniproject;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    ListView list;
    String[] task_title;
    String[] task_desc;
    String[] task_date;
    int[] taskStatus;
    SQLiteDatabase db;
    CustomAdapter customAdapter;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);

        ToDoDB customDB = new ToDoDB(this);
        db = customDB.getWritableDatabase();

        LoadValues();
    }

    public void LoadValues()
    {
        Cursor records = db.query("Tasks",null,null,null,null,null,null);
        if(records.getCount() > 0)
        {
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

            customAdapter = new CustomAdapter(this, task_title, task_desc, task_date,taskStatus);
            list.setAdapter(customAdapter);
            list.setOnItemClickListener(new ListItemSelectionListner(this,list));
            list.setOnItemLongClickListener(new ListItemSelectionListner(this,list));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId())
        {
            case R.id.add :
                final ContentValues values = new ContentValues();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                view = getLayoutInflater().inflate(R.layout.customdialog,null);
                builder.setView(view);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        EditText txtTitle = (EditText)view.findViewById(R.id.txtTitle);
                        EditText txtDesc = (EditText)view.findViewById(R.id.txtDesc);
                        DatePicker taskDate = (DatePicker)view.findViewById(R.id.taskDate);

                        /*to do Date fix*/


                        /* OnDateChnaged Event Not Triggering*/
                       // taskDate.init(2017,10,25,new TaskDateChangedListner(MainActivity.this));
                        if(txtTitle.getText().length() > 0 && txtDesc.getText().length() > 0) {
                            values.put("KEY_TITLE", txtTitle.getText().toString());
                            values.put("KEY_DESCRIPTION", txtDesc.getText().toString());
                            db.insert("Tasks", null, values);

                            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();
                            LoadValues();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Title and Description are Required",Toast.LENGTH_LONG).show();
                        }
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
                break;
            case R.id.done:
                Intent intent = new Intent(MainActivity.this,CompletedTasks.class);
                startActivity(intent);
        }
        return  true;
    }


}
