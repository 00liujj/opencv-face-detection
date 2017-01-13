package com.example.camera;

import android.app.Activity;
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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String TAG = "CallCamera";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;

    Uri fileUri = null;
    ImageView photoImage = null;

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

        return new File(directory.getPath() + File.separator + "IMG_"
                + timeStamp + ".jpg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoImage = (ImageView) findViewById(R.id.photo_image);

        Button callCameraButton = (Button) findViewById(R.id.button_callcamera);

        InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);

//        byte[] contents = new byte[is.available()];
//        is.read(contents);
//        FileOutputStream fos = new FileOutputStream("/sdcard/xxxx.xml");
//        fos.write(contents);

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
            FaceDetection.detectFace(imageFile.getAbsolutePath(), bitmap, bitmapOut);
            photoImage.setImageBitmap(bitmapOut);
        }
    }

}
