package info.androidhive.pathshala.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import info.androidhive.pathshala.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ZoneAnalysis extends AppCompatActivity {

    Context context;
    ArrayList<Center> cList = new ArrayList<>();
    CustomAdapterZone customAdapterzone;
    ListView lvCenters;
    String n_email, todate, fromdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_analysis);
        context = this;
        lvCenters = (ListView) findViewById(R.id.zone_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");
        todate = bundle.getString("todate");
        fromdate = bundle.getString("fromdate");

        new BackgroundZone().execute();
    }

    private class BackgroundZone extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            String responseBody = "";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", n_email)
                    .add("date1", fromdate)
                    .add("date2", todate)


                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/zonegraph.php").post(formBody)
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
            if (str == "$0") {
                Toast.makeText(context, "No Lectures Conducted", Toast.LENGTH_LONG).show();
            } else {
                try {
                    String sarray[] = str.split("%");
                    JSONObject obj1 = new JSONObject(sarray[0]);
                    JSONObject obj2 = new JSONObject(sarray[1]);
                    JSONArray zones_total = obj1.getJSONArray("zones_total");
                    JSONArray zones_present = obj2.getJSONArray("zones_present");
                    for (int i = 0; i < zones_total.length(); i++) {
                        JSONObject zone_total = zones_total.getJSONObject(i);
                        JSONObject zone_present = zones_present.getJSONObject(i);
                        String zone_name = zone_total.optString("zone_name");
                        int tcount = zone_total.optInt("count_total");
                        int pcount = zone_present.optInt("count_present");
                        double percentage = pcount * 100 / tcount;
                        cList.add(new Center(zone_name, percentage));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            customAdapterzone = new CustomAdapterZone(context, cList);
            lvCenters.setAdapter(customAdapterzone);


        }

    }
}