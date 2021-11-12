package com.example.welcome.jobpost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by welcome on 4/5/2017.
 */

public class MyCompanyGridAdapter extends BaseAdapter {

    public String[] companyData={"Profile","Jobs","Applied Candidate","LOG OUT"};
    public  static int[] img={R.drawable.profile,
            R.drawable.jobs,
            R.drawable.selectedc,R.drawable.logout_icon};
    private Context mContext;
    public MyCompanyGridAdapter(Context c){
        mContext=c;

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* TextView tv;
        if(convertView==null)
        {
            tv=new TextView(mContext);
            tv.setLayoutParams(new GridView.LayoutParams(200,200));

        }
        else
        {
            tv=(TextView)convertView;

        }
        tv.setText(companyData[position]);

        return tv;*/
        View grid;
        LayoutInflater layoutInflater =(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            grid=new GridView(mContext);
            grid= layoutInflater.inflate(R.layout.job_gridview,null);
            TextView tv = (TextView) grid.findViewById(R.id.txt_text);
            ImageView imageView=(ImageView)grid.findViewById(R.id.gridViewImage);
            tv.setText(companyData[position]);
            imageView.setImageResource(img[position]);

        }
        else
        {
            grid=(View) convertView;

        }
        //  grid.setText(MyData[position]);

        return grid;
    }
}

