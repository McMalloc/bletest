package edu.mid.robert.bletest;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class ScanBeacons extends ActionBarActivity {
    private LEScan scanner;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            scanner = ((LEScan.LocalBinder)service).getService();

            // Tell the user about this for our demo.
            Toast.makeText(Binding.this, R.string.local_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
            Toast.makeText(Binding.this, R.string.local_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(Binding.this,
                LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


}

//import android.app.AlertDialog;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.text.method.ScrollingMovementMethod;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class ScanBeacons extends AppCompatActivity {
//
//    BluetoothManager btManager;
//    BluetoothAdapter btAdapter;
//    Button startScanningButton;
//    Button stopScanningButton;
//    TextView peripheralTextView;
//
//    Handler h = new Handler();
//    int delay = 1000; //milliseconds
//
//    private final static int REQUEST_ENABLE_BT = 1;
//    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_beacons);
//
//        peripheralTextView = (TextView) findViewById(R.id.PeripheralTextView);
//        peripheralTextView.setMovementMethod(new ScrollingMovementMethod());
//
//        startScanningButton = (Button) findViewById(R.id.StartScanButton);
//        startScanningButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                startScanning();
//            }
//        });
//
//        stopScanningButton = (Button) findViewById(R.id.StopScanButton);
//        stopScanningButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                stopScanning();
//            }
//        });
//        stopScanningButton.setVisibility(View.INVISIBLE);
//
//        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
//        btAdapter = btManager.getAdapter();
//
//        if (btAdapter != null && !btAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
//        }
//    }
//
//    // Device scan callback.
//    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
//        @Override
//        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//            final int rssi_f = rssi;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String display = device.getName() + ": " + rssi_f;
//                    peripheralTextView.setText(display);
//                    System.out.println(display);
//                    startScanning();
//                }
//            });
//        }
//    };
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_COARSE_LOCATION: {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    System.out.println("coarse location permission granted");
//                } else {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Functionality limited");
//                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
//                    builder.setPositiveButton(android.R.string.ok, null);
//                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                        }
//
//                    });
//                    builder.show();
//                }
//                return;
//            }
//        }
//    }
//
//    public void startScanning() {
//        System.out.println("start scanning");
//        peripheralTextView.setText("");
//        startScanningButton.setVisibility(View.INVISIBLE);
//        stopScanningButton.setVisibility(View.VISIBLE);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btAdapter.startLeScan(mLeScanCallback);
//            }
//        });
//    }
//
//    public void stopScanning() {
//        System.out.println("stopping scanning");
//        peripheralTextView.append("Stopped Scanning");
//        startScanningButton.setVisibility(View.VISIBLE);
//        stopScanningButton.setVisibility(View.INVISIBLE);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                btAdapter.stopLeScan(mLeScanCallback);
//            }
//        });
//    }
//}