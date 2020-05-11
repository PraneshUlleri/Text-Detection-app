package com.example.textdetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Button capture,detect,save,view;
ImageView iv;
TextView tv;
public static SQLiteHelper sqLiteHelper;
    Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    capture=findViewById(R.id.capture);
    detect=findViewById(R.id.detect);
    save=findViewById(R.id.btnsave);
    view=findViewById(R.id.btnview);
    iv=findViewById(R.id.iv);
    tv=findViewById(R.id.text);

    sqLiteHelper= new SQLiteHelper(this,"TextDB",null,1);
    sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS TEXT(ID INTEGER PRIMARY KEY AUTOINCREMENT,text VARCHAR, image BLOG)");

    capture.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent();
            tv.setText("");
        }
    });

    detect.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            detectTextFromImage();
        }
    });

    save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try{
                sqLiteHelper.insertData(
                        tv.getText().toString(),
                        ivtoByte(iv));
                Toast.makeText(getApplicationContext(), "added ", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    });

    view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i= new Intent (getApplicationContext(), textList.class);
            startActivity(i);
        }
    });
    }

    private byte[] ivtoByte(ImageView iv) {
        Bitmap bitmap= ((BitmapDrawable)iv.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }


    ///here i will code for camera

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            iv.setImageBitmap(imageBitmap);
        }
    }

    private void detectTextFromImage()
    {

        final FirebaseVisionImage firebaseVisionImage= FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector= FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"ouch "+e.getMessage()+" has occured",Toast.LENGTH_SHORT).show();
               Log.d("Error : ",e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block>blockList=firebaseVisionText.getBlocks();
        if (blockList.size()==0){ Toast.makeText(getApplicationContext(),"no text found",Toast.LENGTH_SHORT).show();}
        else{

            for(FirebaseVisionText.Block block : firebaseVisionText.getBlocks())
            {
                String text = block.getText();
                tv.setText(text);
            }
        }
    }

}
