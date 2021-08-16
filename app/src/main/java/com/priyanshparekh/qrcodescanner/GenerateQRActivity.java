package com.priyanshparekh.qrcodescanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
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

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E83DC"));
        actionBar.setBackgroundDrawable(colorDrawable);

        save_btn.setVisibility(View.INVISIBLE);
        share_btn.setVisibility(View.INVISIBLE);


        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = qrValue.getText().toString();
                generateQR(data);

                save_btn.setVisibility(View.VISIBLE);
                share_btn.setVisibility(View.VISIBLE);
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
        intent.putExtra(Intent.EXTRA_SUBJECT, "QR Code");
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}