package com.example.beacon;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private BeaconManager beaconManager;

    private Region region;

    private TextView tvId;
    Boolean isConnected=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvId=findViewById(R.id.tvId);
        isConnected=false;
        beaconManager= new BeaconManager(this);





        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if(!list.isEmpty()){
                    Beacon nearestBeacon =list.get(0);
                    Log.d("Airport","nearest places"+nearestBeacon.getRssi());
                    tvId.setText(nearestBeacon.getRssi()+"");


                    if(!isConnected && nearestBeacon.getRssi() >-70){

                        isConnected=true;

                        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);

                        dialog.setTitle("알림")
                                .setMessage("비콘이 연결되었습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create().show();
                    }
                    else if(isConnected && nearestBeacon.getRssi()<-70){
                        Toast.makeText(MainActivity.this,"연결종료",Toast.LENGTH_SHORT).show();
                        isConnected=false;
                    }

                }
            }
        });

        region = new Region("ranged region", UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"),40001,25446);



    }


    @Override
    protected void onResume(){
        super.onResume();



        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

    }


    @Override
    protected void onPause(){
        super.onPause();
//        beaconManager.stopRanging(region);
    }
}
