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
 * Created by welcome on 4/4/2017.
 */

public class MyGridAdapter extends BaseAdapter {
    public String[] text={"Profile","Education","Experince","Training","CV","JOBS","Log Out"};
    public  static int[] img={R.drawable.profile,
            R.drawable.education2,
            R.drawable.experience3,
            R.drawable.training,
            R.drawable.cv,
            R.drawable.jobs,R.drawable.logout_icon};

   // public String[] companyData={"Profile","Jobs","Selected Candidate"};

    private Context mContext;
   // private  String [] text;
   // private int [] img;


    public MyGridAdapter(Context c ){
        mContext=c;

    }

    @Override
    public int getCount() {
        return 7;
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
        View grid;
        LayoutInflater layoutInflater =(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            grid=new GridView(mContext);
            grid= layoutInflater.inflate(R.layout.job_gridview,null);
            TextView tv = (TextView) grid.findViewById(R.id.txt_text);
            ImageView imageView=(ImageView)grid.findViewById(R.id.gridViewImage);
            tv.setText(text[position]);
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
