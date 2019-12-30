package info.androidhive.pathshala.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

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

public class Viewattendance extends AppCompatActivity {
    Context context;
    ArrayList<AttendanceView> aList = new ArrayList<>();
    CustomAdapterView customAdapterview;
    ListView lvAttendance;
    String n_email,c_location,coursename,todate,fromdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewattendance);

        context = this;
        Bundle bundle = getIntent().getExtras();
        n_email = bundle.getString("ngoemail");
        c_location = bundle.getString("centerlocation");
        todate = bundle.getString("todate");
        fromdate = bundle.getString("fromdate");
        coursename = bundle.getString("coursename");

        lvAttendance = (ListView) findViewById(R.id.attendance_list);
        new Background().execute();

    }

    private class Background extends AsyncTask<String, String, String> {

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


                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/viewAttendance.php").post(formBody)
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
            try {
                String sarray[] = str.split("%");
                JSONObject obj1 = new JSONObject(sarray[0]);
                JSONObject obj2 = new JSONObject(sarray[1]);
                JSONObject obj3 = new JSONObject(sarray[2]);
                JSONArray view_total = obj1.getJSONArray("viewtotal");
                JSONArray view_present = obj2.getJSONArray("viewpresent");
                JSONArray view_absent = obj3.getJSONArray("viewabsent");
                int view_total_length = view_total.length();
                int view_present_length = view_present.length();
                int view_absent_length = view_absent.length();
                int i = 0;
                int j = 0;
                int k = 0;
                while (i < view_total_length) {
                    int acount;
                    int pcount;
                    JSONObject vt = view_total.getJSONObject(i);
                    int s_roll_total = vt.optInt("s_roll");
                    String s_name = vt.optString("s_name");
                    int tcount = vt.optInt("tcount");
                    i++;
                    if (view_present_length > 0) {
                        JSONObject vp = view_present.getJSONObject(j);
                        int s_roll_present = vp.optInt("s_roll");
                        if (s_roll_present == s_roll_total) {
                            pcount = vp.optInt("pcount");
                            j++;
                            view_present_length--;
                        } else {
                            pcount = 0;
                        }
                    } else {
                        pcount = 0;
                    }
                    if (view_absent_length > 0) {
                        JSONObject va = view_absent.getJSONObject(k);
                        int s_roll_absent = va.optInt("s_roll");
                        if (s_roll_absent == s_roll_total) {
                            acount = va.optInt("acount");
                            k++;
                            view_absent_length--;
                        } else {
                            acount = 0;
                        }
                    } else {
                        acount = 0;
                    }
                    String sroll = Integer.toString(s_roll_total);
                    aList.add(new AttendanceView(sroll, s_name, tcount, pcount, acount));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            customAdapterview = new CustomAdapterView(context, aList);
            lvAttendance.setAdapter(customAdapterview);


        }

    }
}
