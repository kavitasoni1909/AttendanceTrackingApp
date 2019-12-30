package info.androidhive.pathshala.activity;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import info.androidhive.pathshala.R;

/**
 * Created by Dimpi on 07/06/2016.
 */
public class CustomAdapterZone extends BaseAdapter {
    ArrayList<Center> centersList=new ArrayList<>();
    Context context;
    LayoutInflater inflater=null;
    int count;

    public  CustomAdapterZone(Context context, ArrayList records){
        centersList=records;
        this.context=context;
        this.count=count;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return centersList.size();
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
        View view=inflater.inflate(R.layout.list_center,null);
        TextView tvcl=(TextView)view.findViewById(R.id.tvcl);
        TextView tvcount = (TextView)view.findViewById(R.id.tvcount);
        tvcl.setText(centersList.get(position).CenterLocation);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) tvcl.getLayoutParams();
        double percentage=centersList.get(position).Percentage;
        int cal=(int) percentage;
        lp1.width = cal*4 ;
        tvcl.setLayoutParams(lp1);
        tvcount.setText(""+centersList.get(position).Percentage+"%");

        return view;
    }


}
