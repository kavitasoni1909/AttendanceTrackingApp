package info.androidhive.pathshala.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import info.androidhive.pathshala.R;

public class OverallAnalysisStart extends AppCompatActivity {

    Context context;

    String n_email;

    /** Private members of the class */
    private TextView tvfDate;
    private TextView tvtDate;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_analysis_start);
        context = this;
        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");

        tvtDate = (TextView)findViewById(R.id.todatetV);
        tvfDate =(TextView)findViewById(R.id.fromdatetV);
        buttonfromdate = (Button)findViewById(R.id.fromdateb);
        buttontodate = (Button)findViewById(R.id.todateb);

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
    }

    /** Updates the date in the TextView */
    private void updateDisplay() {
        tvfDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pYear).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pDay).append(" "));
    }

    private void updateDisplay2() {
        tvtDate.setText(
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

    public void overallanalysis(View view){


        String a="0";

        String fromdate= tvfDate.getText().toString();
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

        String todate = tvtDate.getText().toString();
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

        Intent intent = new Intent(context,OverallAnalysis.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail",n_email);
        extras.putString("todate",todate);
        extras.putString("fromdate",fromdate);
        intent.putExtras(extras);
        startActivity(intent);

    }

}
