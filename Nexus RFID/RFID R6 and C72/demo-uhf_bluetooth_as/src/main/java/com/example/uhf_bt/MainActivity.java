package com.example.uhf_bt;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf_bt.fragment.BTRenameFragment;
import com.example.uhf_bt.fragment.BarcodeFragment;
import com.example.uhf_bt.fragment.UHFEraseFragment;
import com.example.uhf_bt.fragment.UHFKillFragment;
import com.example.uhf_bt.fragment.UHFLockFragment;
import com.example.uhf_bt.fragment.UHFNewReadTagFragment;
import com.example.uhf_bt.fragment.UHFReadFragment;
import com.example.uhf_bt.fragment.UHFReadTagFragment;
import com.example.uhf_bt.fragment.UHFSetFragment;
import com.example.uhf_bt.fragment.UHFUpdataFragment;
import com.example.uhf_bt.fragment.UHFWriteFragment;
import com.rscja.deviceapi.RFIDWithUHFBluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    public boolean scaning = false;
    public String remoteBTName = "";
    public String remoteBTAdd = "";
    private final static String TAG = "MainActivity111";
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_SELECT_DEVICE = 1;

    public BluetoothDevice mDevice = null;
    private FragmentTabHost mTabHost;
    private FragmentManager fm;
    private Button btn_connect, btn_search, btn_mac;
    private TextView tvAddress;
    public BluetoothAdapter mBtAdapter = null;
    public RFIDWithUHFBluetooth uhf = RFIDWithUHFBluetooth.getInstance();
    BTStatus btStatus = new BTStatus();

    private boolean mIsActiveDisconnect = true; // 是否主动断开连接
    private static final int RECONNECT_NUM = 3; // 重连次数
    private int mReConnectCount = RECONNECT_NUM; // 重新连接次数

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initUI();
        uhf.init(this);
        Utils.initSound(this);
        checkLocationEnable();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onDestroy() {
        uhf.free();
        Utils.freeSound();
        super.onDestroy();
        android.os.Process.killProcess(Process.myPid());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                if (btn_connect.getText().equals(this.getString(R.string.Connect))) {
                    scanBluetoothDevice();
                } else {
                    mIsActiveDisconnect = true; // 主动断开为true
                    uhf.disconnect();
                }
                break;
            case R.id.btn_search:
                if (scaning) {
                    Toast.makeText(this, getString(R.string.title_stop_read_card), Toast.LENGTH_SHORT).show();
                    return;
                }
                scanBluetoothDevice();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (!scaning) {
            if (item.getItemId() == R.id.UHF_ver) {
                String ver = uhf.getR2000Version();
                Utils.alert(MainActivity.this, R.string.action_uhf_ver, ver, R.drawable.webtext);
            } else if (item.getItemId() == R.id.UHF_Battery) {
                String ver = getString(R.string.action_uhf_bat) + ":" + uhf.getBattery() + "%";
                Utils.alert(MainActivity.this, R.string.action_uhf_bat, ver, R.drawable.webtext);
            } else if (item.getItemId() == R.id.UHF_T) {
                String temp = getString(R.string.title_about_Temperature) + ":" + uhf.getR2000Temperature() + "℃";
                Utils.alert(MainActivity.this, R.string.title_about_Temperature, temp, R.drawable.webtext);
            }
        } else {
            Toast.makeText(this, getString(R.string.title_stop_read_card), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                    tvAddress.setText(mDevice.getName() + "(" + deviceAddress + ")");
                    connect(deviceAddress);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //-------------------------------------------------------------------
    private void scanBluetoothDevice() {
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onClick - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (btn_connect.getText().equals(this.getString(R.string.disConnect))) {
                uhf.disconnect();
            }
            Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);
            startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
        }
    }

    public void connect(String deviceAddress) {
        if (uhf.getConnectStatus() == RFIDWithUHFBluetooth.StatusEnum.CONNECTING) {
            Toast.makeText(this, getString(R.string.connecting), Toast.LENGTH_SHORT).show();
        } else {
            uhf.connect(deviceAddress, btStatus);
        }
    }

    /**
     * 重新连接
     *
     * @param deviceAddress
     */
    private void reConnect(String deviceAddress) {
        if (!mIsActiveDisconnect && mReConnectCount > 0) {
            connect(deviceAddress);
            mReConnectCount--;
        }
    }

    /**
     * 应该提示未连接状态
     * @return
     */
    private boolean shouldShowDisconnected() {
        return mIsActiveDisconnect || mReConnectCount == 0;
    }

    class BTStatus implements RFIDWithUHFBluetooth.BTStatusCallback {
        @Override
        public void getStatus(final RFIDWithUHFBluetooth.StatusEnum statusEnum, final BluetoothDevice device) {
            runOnUiThread(new Runnable() {
                public void run() {
                    remoteBTName = "";
                    remoteBTAdd = "";
                    if (statusEnum == RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
                        SystemClock.sleep(500);
                        btn_connect.setText(MainActivity.this.getString(R.string.disConnect));
                        remoteBTName = device.getName();
                        remoteBTAdd = device.getAddress();
                        tvAddress.setText(remoteBTName + "(" + remoteBTAdd + ")" + "-connected");
                        if (shouldShowDisconnected())
                            Toast.makeText(MainActivity.this, getString(R.string.connect_success), Toast.LENGTH_SHORT).show();

                        mIsActiveDisconnect = false;
                        mReConnectCount = RECONNECT_NUM;
                    } else if (statusEnum == RFIDWithUHFBluetooth.StatusEnum.DISCONNECTED) {
                        btn_connect.setText(MainActivity.this.getString(R.string.Connect));
                        if (device != null) {
                            remoteBTName = device.getName();
                            remoteBTAdd = device.getAddress();
                            if (shouldShowDisconnected())
                                tvAddress.setText("(" + remoteBTAdd + ")" + "-not connected");
                        } else {
                            if (shouldShowDisconnected())
                                tvAddress.setText("-not connected");
                        }
                        if (shouldShowDisconnected())
                            Toast.makeText(MainActivity.this, getString(R.string.disconnect), Toast.LENGTH_SHORT).show();

                        if (mDevice != null) {
//                            reConnect(mDevice.getAddress()); // 重连
                        }
                    }

                    if (iConnectStatus != null) {
                        iConnectStatus.getStatus(statusEnum);
                    }
                }
            });
        }
    }

    protected void initUI() {
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_connect.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        fm = getSupportFragmentManager();
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, fm, R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.title_inventory2)).setIndicator(getString(R.string.title_inventory2)),
                UHFNewReadTagFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.title_inventory)).setIndicator(getString(R.string.title_inventory)),
                UHFReadTagFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.title_2d_Scan)).setIndicator(getString(R.string.title_2d_Scan)),
                BarcodeFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.uhf_msg_tab_set)).setIndicator(getString(R.string.uhf_msg_tab_set)),
                UHFSetFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.uhf_msg_tab_read)).setIndicator(getString(R.string.uhf_msg_tab_read)),
                UHFReadFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.uhf_msg_tab_write)).setIndicator(getString(R.string.uhf_msg_tab_write)),
                UHFWriteFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.uhf_msg_tab_lock)).setIndicator(getString(R.string.uhf_msg_tab_lock)),
                UHFLockFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.uhf_msg_tab_kill)).setIndicator(getString(R.string.uhf_msg_tab_kill)),
                UHFKillFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.uhf_msg_tab_erase)).setIndicator(getString(R.string.uhf_msg_tab_erase)),
                UHFEraseFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.title_update)).setIndicator(getString(R.string.title_update)),
                UHFUpdataFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.title_bt_rename)).setIndicator(getString(R.string.title_bt_rename)),
                BTRenameFragment.class, null);
    }

    public void updateUI(String oldName, String newName) {
        if (!TextUtils.isEmpty(oldName) && !TextUtils.isEmpty(newName)) {
            tvAddress.setText(tvAddress.getText().toString().replace(oldName, newName));
            remoteBTName = newName;
        }
    }

    //---------------------------------------------------------
    IConnectStatus iConnectStatus = null;

    public void setConnectStatusNotice(IConnectStatus iConnectStatus) {
        this.iConnectStatus = iConnectStatus;
    }

    public interface IConnectStatus {
        void getStatus(RFIDWithUHFBluetooth.StatusEnum statusEnum);
    }

    //------------------获取定位权限--------------------------------
    private static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST = 100;
    private static final int REQUEST_ACTION_LOCATION_SETTINGS = 3;

    private void checkLocationEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION_REQUEST);
            }
        }
        if (!isLocationEnabled()) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_ACTION_LOCATION_SETTINGS);
        }
    }

    private boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private Toast toast;

    public void showToast(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }
}


