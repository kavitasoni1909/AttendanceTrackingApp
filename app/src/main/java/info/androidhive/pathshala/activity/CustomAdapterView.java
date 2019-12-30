package info.androidhive.pathshala.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import info.androidhive.pathshala.R;

/**
 * Created by lenovo on 3/24/16.
 */
public class CustomAdapterView extends BaseAdapter {
    ArrayList<AttendanceView> attendanceList=new ArrayList<>();
    Context context;
    LayoutInflater inflater=null;


    public  CustomAdapterView(Context context, ArrayList records){
        attendanceList=records;
        this.context=context;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return attendanceList.size();
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
        View view=inflater.inflate(R.layout.list_attendance,null);
        TextView tvsr=(TextView)view.findViewById(R.id.tvsr);
        TextView tvsn = (TextView)view.findViewById(R.id.tvsn);
        TextView tvtc = (TextView)view.findViewById(R.id.tvtc);
        TextView tvpc = (TextView)view.findViewById(R.id.tvpc);
        TextView tvac = (TextView)view.findViewById(R.id.tvac);
        tvsr.setText(attendanceList.get(position).StudentRoll);
        //tvsr.setText("1");
        tvsn.setText(""+attendanceList.get(position).StudentName);
        tvtc.setText(""+attendanceList.get(position).TCount);
        tvpc.setText(""+attendanceList.get(position).PCount);
        tvac.setText(""+attendanceList.get(position).ACount);

        return view;
    }
}


