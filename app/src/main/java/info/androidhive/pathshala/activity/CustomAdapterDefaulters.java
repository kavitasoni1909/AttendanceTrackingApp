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
public class CustomAdapterDefaulters extends BaseAdapter {
    ArrayList<GetDefaulters> defaultersList=new ArrayList<>();
    Context context;
    LayoutInflater inflater=null;

    public  CustomAdapterDefaulters(Context context, ArrayList records){
        defaultersList=records;
        this.context=context;

        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return defaultersList.size();
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
        View view=inflater.inflate(R.layout.list_defaulters,null);
        //TextView tvId = (TextView)view.findViewById(R.id.defaulterId);
        TextView tvsr=(TextView)view.findViewById(R.id.tvsr);
         TextView tvsn = (TextView)view.findViewById(R.id.tvsn);
        TextView tvp = (TextView)view.findViewById(R.id.tvp);
        tvsr.setText(defaultersList.get(position).Roll);
        //tvId.setText(defaultersList.get(position).Id);
        tvsn.setText(defaultersList.get(position).Name);
        tvp.setText(defaultersList.get(position).Percentage);


        return view;
    }
}
