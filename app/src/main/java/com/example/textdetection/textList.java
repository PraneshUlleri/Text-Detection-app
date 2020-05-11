package com.example.textdetection;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class textList extends AppCompatActivity {
    GridView gridView;
    ArrayList<text> textlist;
    TextListAdapter adapter=  null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_list_layout);
        gridView=findViewById(R.id.gridView);
        textlist = new ArrayList<>();
        adapter = new TextListAdapter(this,R.layout.text_list,textlist);
        gridView.setAdapter(adapter);

        final Cursor cursor=MainActivity.sqLiteHelper.getData("SELECT  * FROM TEXT");
        textlist.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String text =cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            textlist.add(new text(id,text,image));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = { "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(textList.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item==0){

                            Cursor c= MainActivity.sqLiteHelper.getData("SELECT id from TEXT");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));

                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();

            }
        });

    }


    private void showDialogDelete(final int i){
        AlertDialog.Builder dialogdelete = new AlertDialog.Builder(textList.this);
        dialogdelete.setTitle("Caution");
        dialogdelete.setMessage("are you sure?");
        dialogdelete.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              try{  MainActivity.sqLiteHelper.deleteData(i);}
              catch (Exception e ){e.printStackTrace();}
                updateList();
            }

        });

        dialogdelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogdelete.show();
    }

    private void updateList(){
        final Cursor cursor=MainActivity.sqLiteHelper.getData("SELECT  * FROM TEXT");
        textlist.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String text =cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            textlist.add(new text(id,text,image));
        }
        adapter.notifyDataSetChanged();
    }



}
