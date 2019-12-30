package info.androidhive.pathshala.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import info.androidhive.pathshala.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Takeattendance extends AppCompatActivity {
  String n_email,c_location,coursename,date;

    Context context;
    ArrayList<Student> sList = new ArrayList<>();
    ArrayList<Student> sList1 = new ArrayList<>();
    ArrayList<CheckBox> checks = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView lvStudents;
    TextView textViewRoll, textViewName, textViewP, textViewA;

    // Arryalist to hold the data for absentees and the presentees
    ArrayList<Attendance> attdList = new ArrayList<>();
    //ArrayList<Integer> selected = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeattendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");
        c_location = bundle.getString("centerlocation");
        coursename = bundle.getString("coursename");
        date = bundle.getString("selecteddate");


        context = this;
        lvStudents = (ListView) findViewById(R.id.student_list);
        textViewRoll = (TextView) findViewById(R.id.textViewroll);
        textViewName = (TextView) findViewById(R.id.textViewname);
        textViewP = (TextView) findViewById(R.id.textViewp);
        textViewA = (TextView) findViewById(R.id.textViewa);


      //  LayoutInflater mInflater = LayoutInflater.from(this);
       // boolean internet = new Takeattendance().isNetworkAvailable();

        if (isNetworkAvailable()) {
            new Background(n_email, c_location, coursename).execute();
       } else {
            //sqlite
            DataBaseHandler database = new DataBaseHandler(context, n_email, c_location, coursename);
            sList = database.getAllStudents();
            customAdapter = new CustomAdapter(context, sList);
            lvStudents.setAdapter(customAdapter);
            //}

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


     class Background extends AsyncTask<String, String, String> {
        String str3,str4 , strgo;
        public Background(String string3 , String string4 , String stringGo){
            str3 = string3;
            str4 = string4;
            strgo = stringGo;
        }

        @Override
        protected String doInBackground(String[] params) {
            String responseBody = "";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", str3)
                    .add("center_location", str4)
                    .add("course_name", strgo)

                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/attendance.php").post(formBody)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    responseBody = response.body().string();
                } else {
                    throw new IOException("Unexpected code " + response);

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return responseBody;


        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if(str=="$0"){
                Toast.makeText(context,"No students in the course selected",Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    JSONObject obj = new JSONObject(str);
                    JSONArray students = obj.getJSONArray("students");
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i);
                        String name = student.optString("name");
                        int id = student.optInt("id");
                        int roll = student.optInt("roll");
                        sList.add(new Student(id, roll, name,0,0));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            customAdapter = new CustomAdapter(context, sList);
            lvStudents.setAdapter(customAdapter);


        } // end of PostExecute method

    } // end of Background class

    public void addcheckboxes(View view){
        CheckBox cb = (CheckBox)view;
        checks.add(cb);

    }

    // when Checkbox with tag checkbox1 gets selected
    public void submit(View view) {

        int status = 0;

        int checkboxId = view.getId();
        String strcheckboxId = Integer.toString(checkboxId);
        int idLength = strcheckboxId.length()-1;
        String stri = strcheckboxId.substring(0,idLength);
       int  i = Integer.parseInt(stri);



        Iterator itr = attdList.iterator();
        while (itr.hasNext()) {
            Attendance atd = (Attendance) itr.next();
            int inds = attdList.indexOf(atd);
            int aid = atd.a_id;
            String attnd = atd.a_attd;
            if (aid == i && attnd == "P") {
                attdList.remove(inds);

                for(Student schecked:sList){
                    if(schecked.Id == i ){
                        int index = sList.indexOf(schecked);
                        sList.set(index,new Student(i,schecked.Roll,schecked.Name,0,0));
                        break;
                    }
                }
                status = 1;
                break;
            }else if(aid == i && attnd == "A"){
                status = 2;
                //attdList.add(new Attendance(i,date,"p"));
                CheckBox checkbox = (CheckBox)findViewById(checkboxId);
                checkbox.setChecked(false);
                Toast.makeText(context,"Choose only one checkbox",Toast.LENGTH_LONG).show();
                break;
            }
        }
          if(status==0) {
              //selected.add(checkboxId);

              for(Student schecked:sList){
                  if(schecked.Id == i ){
                      int index = sList.indexOf(schecked);
                      sList.set(index,new Student(i,schecked.Roll,schecked.Name,1,0));
                      break;
                  }
              }
              attdList.add(new Attendance(i, date, "P"));
          }
    } // end of submit()

    // When checkbox with tag checkbox2 gets selected
    public void submit2(View view) {

        int status = 0;

        int checkboxId = view.getId();
        String strcheckboxId = Integer.toString(checkboxId);
        int idLength = strcheckboxId.length()-1;
        String stri = strcheckboxId.substring(0, idLength);
        int  i = Integer.parseInt(stri);

        Iterator itr = attdList.iterator();
        while (itr.hasNext()) {
            Attendance atd = (Attendance) itr.next();
            int inds = attdList.indexOf(atd);
            int aid = atd.a_id;
            String attnd = atd.a_attd;
            if (aid == i && attnd == "A") {
                attdList.remove(inds);
                for(Student schecked:sList){
                    if(schecked.Id == i ){
                        int index = sList.indexOf(schecked);
                        sList.set(index,new Student(i,schecked.Roll,schecked.Name,0,0));
                        break;
                    }
                }
                status = 1;
                break;
            }else if(aid == i && attnd == "P"){
                status = 2;
               // attdList.add(new Attendance(i, date, "a"));
                CheckBox checkbox = (CheckBox)findViewById(checkboxId);
                checkbox.setChecked(false);
                Toast.makeText(context,"Choose only one checkbox",Toast.LENGTH_LONG).show();
                break;
            }
        }
            if(status == 0){
               // selected.add(checkboxId);
                for(Student schecked:sList){
                    if(schecked.Id == i ){
                        int index = sList.indexOf(schecked);
                        sList.set(index,new Student(i,schecked.Roll,schecked.Name,0,1));
                        break;
                    }
                }
                attdList.add(new Attendance(i, date, "A"));
            }

        }// end of submit2()



    public void senddata(View view) {
        if (attdList.size() == sList.size()) {
            if (isNetworkAvailable()) {

                new Senddata(context, attdList).execute();
            } else{
                DataBaseHandler database = new DataBaseHandler(context, n_email, c_location);
                database.insertRecord(attdList);
                Toast.makeText(context,"Attendance Taken Successfully",Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(context,"Check your selections",Toast.LENGTH_LONG).show();
        }
    }

}


