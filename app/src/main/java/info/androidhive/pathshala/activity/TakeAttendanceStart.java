package info.androidhive.pathshala.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import info.androidhive.pathshala.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TakeAttendanceStart extends AppCompatActivity {

    Context context;
    String n_email,c_location;

    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner sp;

    /** Private members of the class */
    private TextView dateselecttV;
    private Button dateselectbutton;

    private int pYear;
    private int pMonth;
    private int pDay;
    /** This integer will uniquely define the dialog to be used for displaying date picker.*/
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID_ID= 1;
    /** Callback received when the user "picks" a date in the dialog */
    private DatePickerDialog.OnDateSetListener pDateSetListener1 =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay();

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance_start);
        context = this;

        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");
        c_location = bundle.getString("centerlocation");

        dateselecttV = (TextView)findViewById(R.id.datetV);
        dateselectbutton = (Button)findViewById(R.id.datebutton);


        /** Listener for click event of the button */
        dateselectbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });



        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        /** Display the current date in the TextView */
        updateDisplay();



        //SPINNER

        sp=(Spinner)findViewById(R.id.spinner);
        adapter=new ArrayAdapter<String>(this,R.layout.spinnerlayout,R.id.txt,listItems);
       sp.setAdapter(adapter);
        if (isNetworkAvailable()) {

            new BackgroundSpinner().execute();
        } else{
            DataBaseHandler database = new DataBaseHandler(context, n_email, c_location);
            listItems = database.getAllCourses();

            adapter=new ArrayAdapter<String>(this,R.layout.spinnerlayout,R.id.txt,listItems);
            sp.setAdapter(adapter);




       }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /** Updates the date in the TextView */
    private void updateDisplay() {
        dateselecttV.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pYear).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pDay).append(" "));
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        pDateSetListener1,
                        pYear, pMonth, pDay);

        }
        return null;
    }

    private class BackgroundSpinner extends AsyncTask<String,String,String> {

        ArrayList<String> list;

        @Override
        protected String doInBackground(String[] params) {

            String responseBody="";
            OkHttpClient client=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", n_email)
                    .add("center_location",c_location)

                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/spinner_takeAttendance.php").post(formBody)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();

                if (response.isSuccessful()){
                    responseBody=response.body().string();
                }else{
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
            try {
                JSONObject obj=new JSONObject(str);
                JSONArray students=obj.getJSONArray("course_total");
                for(int i=0;i<students.length();i++){
                    JSONObject student=students.getJSONObject(i);
                    String name=student.optString("course_name");

                    listItems.add(name);
                    adapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //listItems.addAll(list);
            // adapter.notifyDataSetChanged();
        }
    }

    public void gogo(View view){

        String spintext = sp.getSelectedItem().toString();

        String a="0";

        String date= dateselecttV.getText().toString();
        String asplit[]=date.split("-");
        if(asplit[1].length()==1)
        {
            asplit[1]=a.concat(asplit[1]);
        }if(asplit[2].length()<=2)
        {
            asplit[2]=a.concat(asplit[2]);
        }
        date=asplit[0].concat("-");
        date=date.concat(asplit[1]);
        date=date.concat("-");
        date=date.concat(asplit[2]);

        Intent intent = new Intent(context,Takeattendance.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail",n_email);
        extras.putString("centerlocation", c_location);
        extras.putString("coursename", spintext);
        extras.putString("selecteddate",date);
        intent.putExtras(extras);
        startActivity(intent);
    }


}
