package com.example.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class Scene3 extends AppCompatActivity {

    TextView timeView;
    TextView barcodetv;
    String barcode_st = "";
    ImageView barcode;
    String url = "http://192.168.0.20:8000/";
    ContentValues info = new ContentValues();
    String storenum = "";
    String bar ="";
    String name = "";
    TextView nameView;
    TextView waitingView;
    Button btnDelay, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene3);

        initControls();
        Intent getin = getIntent();
        storenum = getIntent().getStringExtra("num");
        info.put("waiting", storenum);
        Scene3.NetworkTask networkTask = new Scene3.NetworkTask(url, info);
        networkTask.execute();
    }

    private void initControls() {
        if (barcodetv == null){
            barcodetv = (TextView) findViewById(R.id.barcodetv);
        }
        if (barcode == null){
            barcode = (ImageView) findViewById(R.id.barcode);
        }
        if (waitingView == null){
            waitingView = (TextView) findViewById(R.id.waitingView);
        }
        if (nameView == null){
            nameView = (TextView) findViewById(R.id.nameView);
        }
        if (btnDelay ==null){
            btnDelay = (Button) findViewById(R.id.btnDelay);
        }
        if(btnCancel == null){
            btnCancel = (Button) findViewById(R.id.btnCancel);
        }
        if (timeView == null){
            timeView = (TextView) findViewById(R.id.timeView);
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... voids) {

            String result;          // 요청 결과를 저장할 변수
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request3(url,values);

            return result;
        }

        protected  void onPostExecute(String s) {
            super.onPostExecute(s);
//            doInBackground 로 부터 리턴된 값이 매개변수로 넘어오므로 s를 추력.
//            tv.setText(s);
            name =
            bar = s.substring(0,10);
            String text = s.substring(12);
            barcodetv.setText(bar);
            barcode_st = bar;
            waitingView.setText(text);
            Bitmap barcodes = createBarcode(barcode_st);
            barcode.setImageBitmap(barcodes);
            barcode.invalidate();
        }

        public Bitmap createBarcode(String code) {
            Bitmap bitmap = null;
            MultiFormatWriter gen = new MultiFormatWriter();
            try{
                final int WIDTH = 840;
                final int HEIGHT = 320;
                BitMatrix bytemap = gen.encode(code, BarcodeFormat.CODE_128, WIDTH,HEIGHT);
                bitmap = Bitmap.createBitmap(WIDTH,HEIGHT,Bitmap.Config.ARGB_8888);
                for(int i = 0 ; i < WIDTH ; ++i){
                    for(int j = 0 ; j < HEIGHT ; ++j){
                        bitmap.setPixel(i,j,bytemap.get(i,j)? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    public void Listener() {
        btnDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initControls();
                info.put("waiting",storenum);
                info.put(null,bar);
                Scene3.NetworkTask networkTask = new Scene3.NetworkTask(url, info);
                networkTask.execute();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initControls();
                info.put("account/cancel", bar);
                Scene3.NetworkTask networkTask = new Scene3.NetworkTask(url, info);
                networkTask.execute();
            }
        });
    }
}
