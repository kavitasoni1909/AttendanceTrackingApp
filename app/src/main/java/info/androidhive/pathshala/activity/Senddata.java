package info.androidhive.pathshala.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Senddata extends AsyncTask<String,String,String> {

    ArrayList<Attendance> attd_list = new ArrayList<>();
    Context context;
    String id ;
    String date;
    String attd;

    ProgressDialog PD;


    public Senddata(Context ctx, ArrayList<Attendance> attdList) {
        attd_list = attdList;
        context = ctx;



    }


    protected String doInBackground(String[] params) {
        String responseBody="";
        OkHttpClient client = new OkHttpClient();

        Iterator itr = attd_list.iterator();
        while (itr.hasNext()) {
            Attendance atd =(Attendance)itr.next();
            id = Integer.toString(atd.a_id);

            try {
                //(changes)date = atd.a_date;
                date = atd.a_date.toString();
            }catch (Exception e){}

            attd = atd.a_attd;

            RequestBody formBody = new FormBody.Builder()
                    .add("id",id )
                    .add("date", date)
                    .add("attendance",attd)
                    .build();

            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/sendattendance.php").post(formBody)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    responseBody=response.body().string();
                } else {
                    throw new IOException("Unexpected code " + response);

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return responseBody;
    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        PD = new ProgressDialog(context);
        PD.setTitle("Please Wait..");
        PD.setMessage("Sending data...");
        PD.setCancelable(false);
        PD.show();
    }


    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        PD.dismiss();
        AlertDialog.Builder myAlert=new AlertDialog.Builder(context);
        myAlert.setMessage("Successfully sent")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
          public void onClick(DialogInterface dialog,int which){
              dialog.dismiss();
          }
        });
        myAlert.show();

        if (str.contains("$date out of range")) {
            Toast.makeText(context, "Date selected out of course duration", Toast.LENGTH_LONG).show();
        } else if (str.contains("$data inserted")) {
            Toast.makeText(context, "Attendance Taken Successfully", Toast.LENGTH_LONG).show();
        } else if (str.contains("$not successfull")) {
            Toast.makeText(context,"Opps something went wrong", Toast.LENGTH_LONG).show();

        } else if(str.contains("$Already attendance taken")){
            Toast.makeText(context,"Attendance Already Taken", Toast.LENGTH_LONG).show();
        }
    }

}


