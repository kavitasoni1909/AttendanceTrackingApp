package info.androidhive.pathshala.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import info.androidhive.pathshala.R;

public class Centerlogin extends AppCompatActivity {
    Context context;
    String n_email, c_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centerlogin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        if(CenterLoginState.getUserName(Centerlogin.this).length() == 0 || !CenterLoginState.getUserName(Centerlogin.this).equals("true") )
        {
            Intent intent = new Intent(context, IconTextTabsActivity.class);
            startActivity(intent);
        }
        else
        {
            // Stay at the current activity.
            CenterLoginState c = new CenterLoginState();
            n_email = c.getNgoEmail(context);
            c_location = c.getCenterLocation(context);
            /*Bundle bundle = getIntent().getExtras();
            n_email = bundle.getString("ngoemail");
            c_location = bundle.getString("centerlocation");*/
        }

    }


    public void takeattendance(View view) {
        Intent intent = new Intent(context, TakeAttendanceStart.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail", n_email);
        extras.putString("centerlocation", c_location);
        intent.putExtras(extras);
        startActivity(intent);

    }

    public void viewattendance(View view) {
        Intent intent = new Intent(context, ViewAttendanceStart.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail", n_email);
        extras.putString("centerlocation", c_location);
        intent.putExtras(extras);
        startActivity(intent);

    }

    public void getdefaulters(View view) {
        Intent intent = new Intent(context, DefaulterStart.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail", n_email);
        extras.putString("centerlocation", c_location);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void getanalysis(View view) {
        Intent intent = new Intent(context, AnalysisStart.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail", n_email);
        extras.putString("centerlocation", c_location);
        intent.putExtras(extras);
        startActivity(intent);

    }

    public void sendoffline(View view) {
        ArrayList<Attendance> attdList = new ArrayList<>();
        DataBaseHandler database = new DataBaseHandler(context, n_email, c_location);
        attdList=database.getAllAtt();
        new Senddata(context, attdList).execute();

    }

    public void logout(View view) {
        startActivity(new Intent(context, IconTextTabsActivity.class));
    }
}

