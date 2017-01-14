package com.example.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String TAG = "FaceDetection";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;

    Uri fileUri = null;
    ImageView photoImage = null;
    File cascadeFile = null;

    private File getOutputPhotoFile() {

        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getPackageName());

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        String imagefn = directory.getPath() + File.separator + "IMG_0000" + ".jpg";

        File photoFile = new File(imagefn);
        Log.d(TAG, String.format("the image file is %s", photoFile.getAbsolutePath()));
        return photoFile;
    }

    private File getCascadeFile() {
        File cascadeFile = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            byte[] contents = new byte[is.available()];
            is.read(contents);
            cascadeFile = new File(getDir("cascade", Context.MODE_PRIVATE), "haarcascade_frontalface.xml");
            FileOutputStream fos = new FileOutputStream(cascadeFile);
            fos.write(contents);
            cascadeFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, String.format("the cascade file is %s", cascadeFile.getAbsolutePath()));
        return cascadeFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoImage = (ImageView) findViewById(R.id.photo_image);
        cascadeFile = getCascadeFile();

        Button callCameraButton = (Button) findViewById(R.id.button_callcamera);


        callCameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(getOutputPhotoFile());
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = null;
                if (data == null) {
                    // A known bug here! The image should have saved in fileUri
                    Toast.makeText(this, "Image saved successfully", Toast.LENGTH_LONG).show();
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Toast.makeText(this, "Image saved successfully in: " + data.getData(),
                            Toast.LENGTH_LONG).show();
                }
                showPhoto(photoUri.getPath());
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Callout for image capture failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showPhoto(String photoUri) {
        File imageFile = new File(photoUri);
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            double factor = 480.0 / bitmap.getWidth();
            int width = (int)(bitmap.getWidth()*factor);
            int height = (int)(bitmap.getHeight()*factor);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            //BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
            //photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //photoImage.setImageDrawable(drawable);
            Log.d(TAG, String.format("in java, the size is w=%d, h=%d",
                    bitmap.getWidth(), bitmap.getHeight()));
            Bitmap bitmapOut =
                    Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            FaceDetection.detectFace(cascadeFile.getAbsolutePath(), bitmap, bitmapOut);
            photoImage.setImageBitmap(bitmapOut);
        }
    }

}
