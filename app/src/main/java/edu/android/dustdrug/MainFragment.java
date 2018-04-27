package edu.android.dustdrug;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    public static final String TAG = "edu.android";
    private static final int REQ_CODE_PERMISSION = 1;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    public double longtitude;
    public double latitude;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    private FirstFragment firstFragment;
    private LocationManager locationManager;
    private Location location;
    private LineChart lineChart; // 그래프(jar 파일 사용) private LineChart lineChart; // 그래프(jar 파일 사용)

    public TextView textView;
    public TextView txtGeo;
    public Button btnGeo;
    public Button btnAddress;
    public Button btnblue;
    public BluetoothService btService = null;
    private Handler mHandler = null;


    public MainFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        lineChart = view.findViewById(R.id.chartValueEveryHour);
        btnblue = view.findViewById(R.id.btnPairing);

        btnblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                    }

                };
                if(btService == null){
                    btService = new BluetoothService(getActivity(), mHandler);
                }
                if(btService.getDeviceState()) {
                    // 블루투스가 지원 가능한 기기일 때
                    btService.enableBluetooth();
                } else {
                    //finish();
                    return;
                }
            }
        });


        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(40f, 0));
        entries.add(new Entry(80f, 1));
        entries.add(new Entry(60f, 2));
        entries.add(new Entry(20f, 3));
        entries.add(new Entry(180f, 4));
        entries.add(new Entry(90f, 5));
        entries.add(new Entry(160f, 6));
        entries.add(new Entry(50f, 7));
        entries.add(new Entry(30f, 8));
        entries.add(new Entry(70f, 10));
        entries.add(new Entry(90f, 11));
        entries.add(new Entry(40f, 12));
        entries.add(new Entry(80f, 13));
        entries.add(new Entry(60f, 14));
        entries.add(new Entry(20f, 15));
        entries.add(new Entry(180f, 16));
        entries.add(new Entry(90f, 17));
        entries.add(new Entry(160f, 18));
        entries.add(new Entry(50f, 19));
        entries.add(new Entry(30f, 20));
        entries.add(new Entry(70f, 21));
        entries.add(new Entry(90f, 22));
        entries.add(new Entry(70f, 23));


        LineDataSet dataset = new LineDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < 10; i++)
            labels.add("0" + i + "시");
        for (int i = 10; i < 24; i++)
            labels.add(i + "시");


        LineData data = new LineData(labels, dataset);
        dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠

        lineChart.setData(data);
        lineChart.animateY(5000);
        lineChart.setScaleEnabled(false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        Log.i(TAG,"MainFragment - lineChart 생성");

        textView = view.findViewById(R.id.textLocation);
        btnGeo = view.findViewById(R.id.btnGeo);
        txtGeo = view.findViewById(R.id.txtViewAddress);
        textView.setText("Location");

//        if(hasPermissions(permissions)) { // 위치가 꺼져있을 경우 앱을 실행시키자마자 바로 위치 권한 수락여부 다이얼로그를 띄우게 함
//            showLocationInfo();
//        } else {
//            if (ActivityCompat
//                    .shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
//                Toast.makeText(getContext(), "아래로 끌어 새로고침이 필요합니다.", Toast.LENGTH_LONG).show();
//            } else if (ActivityCompat
//                    .shouldShowRequestPermissionRationale(getActivity(), permissions[1])) {
//                Toast.makeText(getContext(), "GPS가 안되서 근접한 거리라도...", Toast.LENGTH_LONG).show();
//            }
//            ActivityCompat.requestPermissions(getActivity(), permissions, REQ_CODE_PERMISSION);
//        }

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mainFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (hasPermissions(permissions)) {
                        showLocationInfo();
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        if (ActivityCompat
                                .shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
                            Toast.makeText(getContext(), "아래로 끌어 새로고침이 필요합니다.", Toast.LENGTH_LONG).show();
                        } else if (ActivityCompat
                                .shouldShowRequestPermissionRationale(getActivity(), permissions[1])) {
                            Toast.makeText(getContext(), "GPS가 안되서 근접한 거리라도...", Toast.LENGTH_LONG).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        ActivityCompat.requestPermissions(getActivity(), permissions, REQ_CODE_PERMISSION);
                    }
                } catch (NullPointerException e) {
                    e.getMessage();
                    Toast.makeText(getContext(), "위치정보를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        startLocationService();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    public void startLocationService() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "MainFragment - onLocationChanged, location : " + location);
            longtitude = location.getLongitude();
            latitude = location.getLatitude();
            //TODO : 저장;
            Log.i(TAG,"MainFragment - onLocationChanged");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "MainFragment - onRequestPermissionsResult start");
        if (requestCode == REQ_CODE_PERMISSION) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(getContext(), "Make the authorization get allowed", Toast.LENGTH_SHORT).show();
            }
        }
        Log.i(TAG, "MainFragment - onRequestPermissionsResult end");

    }

    private boolean hasPermissions(String[] permissions) {
        boolean result = true;
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(getContext(), p) != PackageManager.PERMISSION_GRANTED) {
                result = false;
                break;
            }
        }
        return result;
    }

    public void showLocationInfo() {

        startLocationService();
        longtitude = location.getLongitude();
        latitude = location.getLatitude();
        GeoCoding geoCoding = GeoCoding.getInstance();
        geoCoding.getlatitude(latitude,longtitude);
        textView.setText("경도 : " + longtitude + "\n" + "위도 : " + latitude);
        Log.i(TAG,"MainFragment - showLocationInfo");
    }


    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {

            /** 추가된 부분 시작 **/
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    btService.getDeviceInfo(data);
                }
                break;
            /** 추가된 부분 끝 **/
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Next Step
                    btService.scanDevice();
                } else {

                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }
    class asdasdasd extends Activity{
        public void onActivityResult (int requestCode, int resultCode, Intent data) {
            Log.d(TAG, "onActivityResult " + resultCode);

            switch (requestCode) {

                /** 추가된 부분 시작 **/
                case REQUEST_CONNECT_DEVICE:
                    // When DeviceListActivity returns with a device to connect
                    if (resultCode == Activity.RESULT_OK) {
                        btService.getDeviceInfo(data);
                    }
                    break;
                /** 추가된 부분 끝 **/
                case REQUEST_ENABLE_BT:
                    // When the request to enable Bluetooth returns
                    if (resultCode == Activity.RESULT_OK) {
                        // Next Step
                        btService.scanDevice();
                    } else {

                        Log.d(TAG, "Bluetooth is not enabled");
                    }
                    break;
            }
        }
    }
}

