package info.androidhive.pathshala.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class OverallAnalysis extends AppCompatActivity {

    Context context;
    ArrayList<Center> cList=new ArrayList<>();
    CustomAdapterAnalysis customAdapterAnalysis;
    ListView lvCenters;
    String n_email,todate,fromdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_analysis);
        context=this;
        lvCenters=(ListView)findViewById(R.id.center_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");
        todate = bundle.getString("todate");
        fromdate = bundle.getString("fromdate");

        new Background().execute();
    }



    private class Background extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String[] params) {
            String responseBody="";
            OkHttpClient client=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", n_email)
                    .add("date1", fromdate)
                    .add("date2", todate)


                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/graph.php").post(formBody)
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
            if(str=="$0"){
                Toast.makeText(context,"No Lectures Conducted",Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    String sarray[] = str.split("%");
                    JSONObject obj1 = new JSONObject(sarray[0]);
                    JSONObject obj2 = new JSONObject(sarray[1]);
                    JSONArray centers_total = obj1.getJSONArray("centers_total");
                    JSONArray centers_present = obj2.getJSONArray("centers_present");
                    for (int i = 0; i < centers_total.length(); i++) {
                        JSONObject center_total = centers_total.getJSONObject(i);
                        JSONObject center_present = centers_present.getJSONObject(i);
                        String center_location = center_total.optString("center_location");
                        int tcount = center_total.optInt("count_total");
                        int pcount = center_present.optInt("count_present");
                        double percentage = pcount * 100 / tcount;
                        cList.add(new Center(center_location, percentage));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            customAdapterAnalysis=new CustomAdapterAnalysis(context, cList);
            lvCenters.setAdapter(customAdapterAnalysis);


        }
    }

}
