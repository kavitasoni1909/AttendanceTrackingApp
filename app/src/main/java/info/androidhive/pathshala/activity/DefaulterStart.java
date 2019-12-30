package info.androidhive.pathshala.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

public class DefaulterStart extends AppCompatActivity {

    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner sp;

    String n_email,c_location,spinnertext;
   Button proceed;
    Context context;
   // EditText coursename;
    EditText  percentage;
    /** Private members of the class */
    private TextView textViewfromdate;
    private TextView textViewtodate;
    private Button buttonfromdate;
    private Button buttontodate;
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

    private DatePickerDialog.OnDateSetListener pDateSetListener2 =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay2();

                }
            };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaulter_start);
         context = this;
         proceed = (Button)findViewById(R.id.buttonproceed);
        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");
        c_location = bundle.getString("centerlocation");

        /** Capture our View elements */
        textViewfromdate = (TextView) findViewById(R.id.textViewfromdate);
        textViewtodate = (TextView) findViewById(R.id.textViewtodate);
        buttonfromdate = (Button) findViewById(R.id.buttonfromdate);
        buttontodate = (Button) findViewById(R.id.buttontodate);

        /** Listener for click event of the button */
        buttonfromdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        buttontodate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID_ID);
            }
        });

        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        /** Display the current date in the TextView */
        updateDisplay();

        sp=(Spinner)findViewById(R.id.spinnercrsname);
        adapter=new ArrayAdapter<String>(this,R.layout.spinnerlayout,R.id.txt,listItems);
        sp.setAdapter(adapter);

        new BackgroundSpinner().execute();

}

    private class BackgroundSpinner extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String[] params) {

            String responseBody="";
            OkHttpClient client=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", n_email)
                    .add("center_location", c_location)

                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/spinner.php").post(formBody)
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


        }
    }

    /** Updates the date in the TextView */
    private void updateDisplay() {
        textViewfromdate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pYear).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pDay).append(" "));
    }

    private void updateDisplay2() {
        textViewtodate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pYear).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pDay).append(" "));
    }

    /** Create a new dialog for date picker */

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        pDateSetListener1,
                        pYear, pMonth, pDay);
            case DATE_DIALOG_ID_ID:
                return new DatePickerDialog(this,
                        pDateSetListener2,
                        pYear, pMonth, pDay);
        }
        return null;
    }

    public void proceedproceed(View view){

        spinnertext = sp.getSelectedItem().toString();


        //coursename = (EditText)findViewById(R.id.editTextcoursename);
       //String strcoursename = coursename.getText().toString();

        percentage = (EditText)findViewById(R.id.editTextpercentage);
        String strpercentage = percentage.getText().toString();

        String a="0";

       String fromdate= textViewfromdate.getText().toString();
        String asplit[]=fromdate.split("-");
        if(asplit[1].length()==1)
        {
            asplit[1]=a.concat(asplit[1]);
        }if(asplit[2].length()<=2)
        {
            asplit[2]=a.concat(asplit[2]);
        }
        fromdate=asplit[0].concat("-");
        fromdate=fromdate.concat(asplit[1]);
        fromdate=fromdate.concat("-");
        fromdate=fromdate.concat(asplit[2]);

        String todate = textViewtodate.getText().toString();
        String bsplit[]=todate.split("-");
        if(bsplit[1].length()==1)
        {
            bsplit[1]=a.concat(bsplit[1]);
        }if(bsplit[2].length()<=2)
        {
            bsplit[2]=a.concat(bsplit[2]);
        }
        todate=bsplit[0].concat("-");
        todate=todate.concat(bsplit[1]);
        todate=todate.concat("-");
        todate=todate.concat(bsplit[2]);

        Intent intent = new Intent(context,Defaulters.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail",n_email);
        extras.putString("centerlocation",c_location);
        extras.putString("todate",todate);
        extras.putString("fromdate",fromdate);
        extras.putString("coursename",spinnertext);
        extras.putString("percentage",strpercentage);
        intent.putExtras(extras);
        startActivity(intent);
    }

}
