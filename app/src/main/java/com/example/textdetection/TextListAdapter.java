package com.example.textdetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TextListAdapter extends BaseAdapter {

    private Context context;
    private int layout ;
    private ArrayList<text> textlist;

    public TextListAdapter(Context context, int layout, ArrayList<text> textlist) {
        this.context = context;
        this.layout = layout;
        this.textlist = textlist;
    }

    @Override
    public int getCount() {
        return textlist.size()  ;
    }

    @Override
    public Object getItem(int position) {
        return textlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
       View row= view;
       ViewHolder holder= new ViewHolder();

       if (row==null){
           LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           row= inflater.inflate(layout,null);
           holder.textView=row.findViewById(R.id.textView);
           holder.imageView=row.findViewById(R.id.imageView);
           row.setTag(holder);
       }
       else {
           holder =(ViewHolder) row.getTag();
       }
       text t=textlist.get(position);
       holder.textView.setText(t.getText());
       byte[] textimage=t.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(textimage,0,textimage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }
}

