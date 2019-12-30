package info.androidhive.pathshala.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import info.androidhive.pathshala.R;

/**
 * Created by lenovo on 3/24/16.
 */
public class CustomAdapter extends BaseAdapter {
    ArrayList<Student> studentsList=new ArrayList<>();
    Context context;
    LayoutInflater inflater=null;
    int id = 1;
  //  ArrayList<Integer> checks = new ArrayList<>();

    // ArrayList<CheckBox>  checks = new ArrayList<>();


    public  CustomAdapter(Context context, ArrayList records){
        studentsList=records;
        this.context=context;

        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return studentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.list_student,null);
        TextView tvName=(TextView)view.findViewById(R.id.tvName);
        TextView tvId = (TextView)view.findViewById(R.id.tvId);
        TextView tvRoll = (TextView)view.findViewById(R.id.tvRoll);
        //CheckBox checkBox1 = (CheckBox)view.findViewById(R.id.checkBox1);
        //CheckBox checkBox2 = (CheckBox)view.findViewById(R.id.checkBox2);

        CheckBox cb1 = (CheckBox)view.findViewWithTag("checkbox1");
        CheckBox cb2 = (CheckBox)view.findViewWithTag("checkbox2");



        tvName.setText(studentsList.get(position).Name);
        tvRoll.setText(""+studentsList.get(position).Roll);
        tvId.setText("" + studentsList.get(position).Id);
        String strcb1 = tvId.getText().toString() + "1";
        cb1.setId(Integer.parseInt(strcb1));
        String strcb2 = tvId.getText().toString() + "2";
        cb2.setId(Integer.parseInt(strcb2));

        if(studentsList.get(position).value1==1){
            cb1.setChecked(true);
        }
        else if(studentsList.get(position).value2==1){
            cb2.setChecked(true);
        }

        return view;
    }


}


