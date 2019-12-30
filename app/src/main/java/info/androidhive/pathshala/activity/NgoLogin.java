package info.androidhive.pathshala.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import info.androidhive.pathshala.R;

public class NgoLogin extends AppCompatActivity {

    String n_email;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");
    }

    public void ngoviewstart(View view){

        Intent intent = new Intent(context,Ngoviewstart.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail",n_email);
        intent.putExtras(extras);
        startActivity(intent);

    }

    public void dashboardstart(View view){

        Intent intent = new Intent(context,DashBoardStart.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail",n_email);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void overallanalysisstart(View view){
        Intent intent = new Intent(context,OverallAnalysisStart.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail",n_email);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void ngocenteranalysis(View view){
        Intent intent = new Intent(context,NgoCAnalysis.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail",n_email);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void zoneanalysisbutton(View view){
        Intent intent = new Intent(context,ZoneAnalysisStart.class);
        Bundle extras = new Bundle();
        extras.putString("ngoemail",n_email);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void logoutlogout(View view){
        startActivity(new Intent(context,IconTextTabsActivity.class));
    }
}
