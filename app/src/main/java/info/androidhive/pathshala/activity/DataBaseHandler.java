package info.androidhive.pathshala.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bhavik Chandora on 02-05-2016.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pathshala.db";
    private SQLiteDatabase database;
    // Contacts table name
    private static final String TABLE_INFO = "info";
    private static final String TABLE_ATTENDANCE = "attendance";
 // Columns defined
    private static final String COURSE_NAME = "course_name";
    private static final String ID = "id";
    private static final String S_ROLL = "s_roll";
    private static final String S_NAME = "s_name";
    private static final String DATE = "date";
    private static final String S_ATTENDANCE = "s_attendance";

    String n_email,c_location,c_name;
   Context ctx;
    public DataBaseHandler(Context context,String ngoemail,String centerlocation) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        n_email = ngoemail;
        c_location=centerlocation;
        ctx = context;
    }

    public DataBaseHandler(Context context,String ngoemail,String centerlocation, String coursename) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        n_email = ngoemail;
        c_location=centerlocation;
        c_name=coursename;
        ctx = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_INFO_TABLE = "CREATE TABLE " + TABLE_INFO + "("
                + COURSE_NAME + " TEXT," +ID + " INTEGER,"
                + S_ROLL + " INTEGER,"  +S_NAME+ " TEXT "+")";

        db.execSQL(CREATE_INFO_TABLE);


        String CREATE_ATT_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + "("
                + ID + " INTEGER," +DATE+ " TEXT,"
                + S_ATTENDANCE + " TEXT "+")";

        db.execSQL(CREATE_ATT_TABLE);
        //Toast.makeText(ctx," tables created",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);

    }

    // Adding new data
    public void addStudents(ArrayList<Info> alist) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_INFO);
        String CREATE_INFO_TABLE = "CREATE TABLE " + TABLE_INFO + "("
                + COURSE_NAME + " TEXT," +ID + " INTEGER,"
                + S_ROLL + " INTEGER,"  +S_NAME+ " TEXT "+")";

        db.execSQL(CREATE_INFO_TABLE);

        ContentValues values = new ContentValues();

        Iterator itr = alist.iterator();
        while (itr.hasNext()) {
            Info info = (Info) itr.next();
            values.put(COURSE_NAME, info.coursename); // Course Name
            values.put(ID, info.Id); // Id
            values.put(S_ROLL, info.Roll); // Roll
            values.put(S_NAME, info.name); // Name
            // Inserting Row
            db.insert(TABLE_INFO, null, values);

        }
        //Toast.makeText(ctx," inserted ",Toast.LENGTH_LONG).show();
        db.close(); // Closing database connection
    }

    //method for retrieving students info
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> studentList = new ArrayList<Student>();
        // Select All Query
        String selectQuery = "SELECT  id,s_roll,s_name FROM " + TABLE_INFO + " WHERE course_name LIKE '"+ c_name+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

               Integer id = Integer.parseInt(cursor.getString(0));
               Integer roll =Integer.parseInt(cursor.getString(1));
                String name = cursor.getString(2);
                // Adding contact to list
                studentList.add(new Student(id,roll,name,0,0));
            } while (cursor.moveToNext());
        }

        // return contact list
        return studentList;
    }

    //method for retrieving students info
    public ArrayList<String> getAllCourses() {
        ArrayList<String> ListItem = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  distinct course_name FROM " + TABLE_INFO ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


                String course_name = cursor.getString(0);
                // Adding contact to list
                ListItem.add(course_name);

            } while (cursor.moveToNext());
        }

        // return contact list
        return ListItem;
    }

    public void insertRecord(ArrayList<Attendance> attdList) {
         database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int i=0;i<attdList.size();i++){
            contentValues.put(ID,attdList.get(i).a_id);
            contentValues.put(DATE,attdList.get(i).a_date);
            contentValues.put(S_ATTENDANCE,attdList.get(i).a_attd);
            database.insert(TABLE_ATTENDANCE, null, contentValues);
        }
    }

    public ArrayList<Attendance> getAllAtt() {
        ArrayList<Attendance> studentList = new ArrayList<Attendance>();
        // Select All Query
        String selectQuery = "SELECT  id,date,s_attendance FROM " +TABLE_ATTENDANCE ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Integer id = Integer.parseInt(cursor.getString(0));
                String  date =cursor.getString(1);
                String att = cursor.getString(2);
                // Adding contact to list
                studentList.add(new Attendance(id,date,att));
            } while (cursor.moveToNext());
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        String CREATE_ATT_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + "("
                + ID + " INTEGER," +DATE+ " TEXT,"
                + S_ATTENDANCE + " TEXT "+")";

        db.execSQL(CREATE_ATT_TABLE);
        //onCreate(db);

        // return contact list
        return studentList;
    }


    }


