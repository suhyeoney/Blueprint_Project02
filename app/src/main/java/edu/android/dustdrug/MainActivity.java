package edu.android.dustdrug;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "edu.android";
    private static final int REQUEST_ENABLE_BLUETOOTH = 3;
    private BluetoothAdapter bluetoothAdapter;
    private FirstFragment firstFragment;
    private MainFragment mainFragment;
    private long lastTimeBackPressed = 0;
    Geocoder geocoder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "MainActivity - onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(this);
//        FragmentManager manager = getSupportFragmentManager();
//        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
//        if (fragment == null) {
//            Log.i(TAG, "fragment == null");
//            FragmentTransaction transaction = manager.beginTransaction();
//            firstFragment = FirstFragment.newInstance();
//            transaction.replace(R.id.fragment_container, firstFragment);
//            transaction.commit();
//            Log.i(TAG, "first fragment call");
//        }

//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            Toast.makeText(this, "블루투스를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
//
//            /**  에뮬레이터 죽음  그래서 아래코드 주석처리함*/
//            finish();
//            return;
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "MainActivity - onStart");
        // 필요없음.........;;;;
        // public void blueToothPairing(View view) 쓰면 권한 주는 단계 뛰어 넘어짐
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if(bluetoothAdapter == null) {
//            Toast.makeText(this, "블루투스를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
//        }
//
//        if(!bluetoothAdapter.isEnabled()) { // insert ! in front of bluetoothadapter.isENnabled
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
//        }
    }

    /* ↓ Back 버튼 누를 시 앱 종료 기능 */
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > lastTimeBackPressed + 2000) {
            lastTimeBackPressed = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 버튼 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
//            return;

        } else { // back 키 2번 누르면 앱 종료
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);

        }

    }

    // 블루투스 승인 요청 코드
    // fragment_main에 btn_onclick 사용
    public void blueToothPairing(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }


    public void geoConvert(View view) {
        Log.i(TAG, "MainActivity - geoConvert");
        //GeoCoding geoCoding = new GeoCoding();
//        GeoCoding geoCoding = GeoCoding.newInstance();
        GeoCoding geoCoding = GeoCoding.getInstance();
        geoCoding.getGeocoder(geocoder);
        geoCoding.test();
        String str = geoCoding.setText();
        Log.i(TAG, "Main str : " + str);
        mainFragment.txtGeo.setText(str);
        //txtGeo.setText(str);
//        Coding.ConvertLatiLongToAddress();
    }


    public void addressConvert(View view) {
        Log.i(TAG, "MainActivity - addressConvert");
    }

    public Object getMainfragment() {
        Log.i(TAG, "MainActivity - getMainfragment");
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment != mainFragment) {
            FragmentTransaction transaction = manager.beginTransaction();
            mainFragment = MainFragment.newInstance();
            transaction.replace(R.id.fragment_container, mainFragment);
            transaction.commit();
            Log.i(TAG, "main fragment call");
        }
        return mainFragment;
    }

    // TODO: 블루투스 페어링
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        Log.i(TAG, "onActivityResult " + resultCode);
//
//        switch (requestCode) {
//            case REQUEST_CONNECT_DEVICE:
//                if (resultCode == Activity.RESULT_OK) {
//                    btService.getDeviceInfo(data);
//                }
//                break;
//            case REQUEST_ENABLE_BT:
//                if (resultCode == Activity.RESULT_OK) {
//                    btService.scanDevice();
//                } else {
//                    Log.i(TAG, "Bluetooth is not enabled");
//                }
//                break;
//        }
//    }
}
