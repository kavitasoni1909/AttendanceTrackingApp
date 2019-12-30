package info.androidhive.pathshala.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class DashBoard extends AppCompatActivity {

    String n_email,c_location,todate,fromdate,coursename,percentage;
    Context context;
    ArrayList<GetDefaulters> dList=new ArrayList<>();
    CustomAdapterDefaulters defaultercustomAdapter;
    ListView lvDefaulters;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        context=this;
        lvDefaulters=(ListView)findViewById(R.id.defaulters_list);

        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");
        c_location = bundle.getString("centerlocation");
        percentage = bundle.getString("percentage");
        coursename = bundle.getString("coursename");
        todate = bundle.getString("todate");
        fromdate = bundle.getString("fromdate");

        new Background().execute();

    }

    private class Background extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String[] params) {
            String responseBody = "";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", n_email)
                    .add("center_location", c_location)
                    .add("course_name", coursename)
                    .add("date1", fromdate)
                    .add("date2", todate)
                    .add("per", percentage)


                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/defaulters.php").post(formBody)
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

            if (str.contains("$cod")) {
                Toast.makeText(context, "Date Selected is out of Course Duration", Toast.LENGTH_LONG).show();
            } else if (str.contains("$nd")) {
                Toast.makeText(context, "No Defaulters", Toast.LENGTH_LONG).show();
            } else if (str.contains("$nlc")) {
                Toast.makeText(context, "No Lectures Conducted", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject obj1 = new JSONObject(str);
                    JSONArray defaulters = obj1.getJSONArray("defaulters");
                    for (int i = 0; i < defaulters.length(); i++) {
                        JSONObject defaulter = defaulters.getJSONObject(i);
                        int sid = defaulter.optInt("id");
                        String sroll = defaulter.optString("s_roll");
                        String sname = defaulter.optString("s_name");
                        String percentage = defaulter.optString("percentage");

                        dList.add(new GetDefaulters(sid, sroll, sname, percentage));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            defaultercustomAdapter = new CustomAdapterDefaulters(context, dList);
            lvDefaulters.setAdapter(defaultercustomAdapter);


        }
    }
}
