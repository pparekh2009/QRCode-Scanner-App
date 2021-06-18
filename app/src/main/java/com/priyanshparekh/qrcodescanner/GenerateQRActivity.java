package com.priyanshparekh.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateQRActivity extends AppCompatActivity {

    EditText qrValue;
    Button generate_btn, save_btn, share_btn;
    ImageView qrImage;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        qrValue = findViewById(R.id.qrValue);
        generate_btn = findViewById(R.id.generate_btn);
        qrImage = findViewById(R.id.qrImage);
        save_btn = findViewById(R.id.save_btn);
        share_btn = findViewById(R.id.share_btn);

        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = qrValue.getText().toString();
                generateQR(data);
//                QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 10);
//                Bitmap qrgBits = qrgEncoder.encodeAsBitmap();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) qrImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                try {
                    saveQRImage(bitmap);
                } catch (IOException e) {
                    Toast.makeText(GenerateQRActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(GenerateQRActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) qrImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                try {
                    shareQRImage(bitmap);
                } catch (IOException e) {
                    Toast.makeText(GenerateQRActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(GenerateQRActivity.this, "Sharing Image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateQR(String data) {
        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 200);
        Bitmap bitmap = qrgEncoder.getBitmap();
        qrImage.setImageBitmap(bitmap);
    }

    public void saveQRImage(Bitmap bitmap) throws IOException {
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getDir("imgDir", Context.MODE_PRIVATE);
//        File file = new File(directory, "UniqueFileName" + ".png");
//        if (!file.exists()) {
//            Log.d("path", file.toString());
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                fos.flush();
//                fos.close();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


//        FileOutputStream outStream = null;
//        File sdCard = Environment.getExternalStorageDirectory();
//        File dir = new File(sdCard.getAbsolutePath() + "/QRCodeScanner");
//        dir.mkdirs();
//
//        String fileName = String.format("%d.png", System.currentTimeMillis());
//        File outFile = new File(dir, fileName);
//        outStream = new FileOutputStream(outFile);
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//        outStream.flush();
//        outStream.close();
        String ImagePath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "qrCode", "qrCode");
        Uri uri = Uri.parse(ImagePath);
    }

    public Uri getImageToShare(Bitmap bitmap) throws IOException {
        File imageFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder,"sharedImage.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            uri = FileProvider.getUriForFile(this, "com.google.android.fileprovider", file);
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    public void shareQRImage(Bitmap bitmap) throws IOException {
        Uri uri = getImageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}