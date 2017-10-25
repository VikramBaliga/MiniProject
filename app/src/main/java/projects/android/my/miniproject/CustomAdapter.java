package projects.android.my.miniproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 25-10-2017.
 */

/* Custom adapter to display the task details*/

public class CustomAdapter extends BaseAdapter
{
    Context context;
    String[] task_title,task_desc,task_date;
    int[] taskStatus;

    public  CustomAdapter(Context context,String[] task_title,String[] task_desc,String[] task_date,int[] taskStatus)
    {
        this.context=context;
        this.task_title=task_title;
        this.task_desc=task_desc;
        this.task_date=task_date;
        this.taskStatus=taskStatus;
    }
    @Override
    public int getCount() {
        return task_title.length;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listlayout,null);

        TextView contactName = (TextView) view.findViewById(R.id.name);
        TextView contactNumber = (TextView) view.findViewById(R.id.phone);
        TextView contactDob = (TextView) view.findViewById(R.id.dob);
        ImageView imgtaskS = (ImageView) view.findViewById(R.id.imageView);

        //Set Imageview basd on task status
        if(taskStatus[position]==0)
        {
            imgtaskS.setImageResource(R.drawable.incomplete);
        }
        else
        {
            imgtaskS.setImageResource(R.drawable.complete);
        }


        contactName.setText(task_title[position]);
        contactNumber.setText(task_desc[position]);
        contactDob.setText(task_date[position]);

        return  view;
    }
}