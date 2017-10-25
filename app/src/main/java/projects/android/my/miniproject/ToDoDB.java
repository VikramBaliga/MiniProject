package projects.android.my.miniproject;

/**
 * Created by User on 25-10-2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDB extends SQLiteOpenHelper
{
    public ToDoDB(Context context) {
        super(context, "ToDoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table Tasks(KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,KEY_TITLE TEXT,KEY_DESCRIPTION TEXT,KEY_DATE TEXT,KEY_STATUS INTEGER DEFAULT 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
