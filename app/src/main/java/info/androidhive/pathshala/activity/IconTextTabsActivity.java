package info.androidhive.pathshala.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import info.androidhive.pathshala.R;
import info.androidhive.pathshala.fragments.OneFragment;
import info.androidhive.pathshala.fragments.TwoFragment;

public class IconTextTabsActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList<Info> infolist = new ArrayList<>();
    Context context;
    EditText editText3,editText4;
    String n_email , c_location ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_text_tabs);
        context = this;


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void login1(View view){
       String et1,et2;
      EditText  editText1 = ( EditText)findViewById(R.id.editText1);
        et1 = editText1.getText().toString();
       EditText editText2 = ( EditText)findViewById(R.id.editText2);
        et2 = editText2.getText().toString();
       new BackgroundNgo(et1, et2).execute();
    }

    public void login2(View view){
        String et3,et4;
          editText3 = ( EditText)findViewById(R.id.editText3);
        et3 = editText3.getText().toString();
         editText4 = ( EditText)findViewById(R.id.editText4);
        et4 = editText4.getText().toString();

          new BackgroundCenter(et3,et4).execute();
    }

    class BackgroundNgo extends AsyncTask<String,String,String>{
        String et1 , et2 ;

        public BackgroundNgo(String str1 , String str2){
            et1 = str1;
            et2 = str2;
        }
        protected String doInBackground(String[] params) {
            String responseBody="";
            OkHttpClient client=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", et1)
                    .add("ngo_password", et2)
                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/ngoLogin.php").post(formBody)
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
                JSONObject obj1 = new JSONObject(str);
                JSONArray ngologin = obj1.getJSONArray("ngologin");

                JSONObject login = ngologin.getJSONObject(0);
                String ngo_login = login.optString("ngo_login");
                if(ngo_login.equals("true"))
                {
                    Intent intent = new Intent(context,NgoLogin.class);
                    Bundle extras = new Bundle();
                    extras.putString("ngoemail",et1);
                    intent.putExtras(extras);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(new Application().getApplicationContext(),"login unsuccess",Toast.LENGTH_LONG).show();

                }




            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    } // End of BackgroundNgo

    class BackgroundCenter extends AsyncTask<String,String,String>{

        String et3 , et4;
        public BackgroundCenter(String str3 , String str4){
            et3 = str3;
            et4 = str4;
        }
        @Override
        protected String doInBackground(String[] params) {
            String responseBody="";
            OkHttpClient client=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", et3)
                    .add("center_location", et4)
                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/centerLogin.php").post(formBody)
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

                e.printStackTrace();
            }
            return responseBody;

        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            try {
                JSONObject obj1 = new JSONObject(str);
                JSONArray centerlogin = obj1.getJSONArray("centerlogin");

                JSONObject login = centerlogin.getJSONObject(0);
                String ngo_login = login.optString("center_login");
                if(ngo_login.equals("true"))
                {
                    CenterLoginState.setUserName(context,"true");
                  //  Toast.makeText(new Application().getApplicationContext(),"login successful",Toast.LENGTH_LONG).show();

                    new BackgroundInfo(et3,et4).execute();
                    Intent intent = new Intent(context,Centerlogin.class);
                    /*Bundle extras = new Bundle();
                    extras.putString("ngoemail",et3);
                    extras.putString("centerlocation",et4);
                    intent.putExtras(extras);*/
                    CenterLoginState cls = new CenterLoginState();
                    cls.setUserName(context,"true");
                    cls.setNgoEmail(context,et3);
                    cls.setCenterLocation(context,et4);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(new Application().getApplicationContext(),"login unsuccessful",Toast.LENGTH_LONG).show();
                }




            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //retrieving student list for sqlite
    class BackgroundInfo extends AsyncTask<String, String, String> {
         String n_email, c_location;
        public BackgroundInfo(String nemail ,String clocation){
            n_email = nemail;
            c_location = clocation;
        }
        @Override
        protected String doInBackground(String[] params) {

            String responseBody = "";


            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("ngo_email", n_email)
                    .add("center_location", c_location)

                    .build();
            Request request = new Request.Builder()
                    .url("http://pathshala4.comxa.com/php/sql.php").post(formBody)
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

                JSONObject obj = new JSONObject(str);
                JSONArray details = obj.getJSONArray("details");

                for (int i = 0; i < details.length(); i++) {
                    JSONObject student = details.getJSONObject(i);

                    String coursename = student.optString("course_name");
                    String name = student.optString("s_name");
                    int id = student.optInt("id");
                    int roll = student.optInt("s_roll");
                    infolist.add(new Info(id,roll,coursename,name));

                }
                 DataBaseHandler db = new DataBaseHandler(context,n_email,c_location);
                 db.addStudents(infolist);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "NGO");
        adapter.addFrag(new TwoFragment(), "CENTER");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
