package com.example.uhf_bt.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.R;
import com.example.uhf_bt.Utils;
import com.rscja.deviceapi.RFIDWithUHFBluetooth;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.fragment.app.Fragment;


public class UHFNewReadTagFragment extends Fragment implements View.OnClickListener{

    private boolean loopFlag = false;
    private ListView LvTags;
    private Button InventoryLoop,btInventory,btStop;//
    private  Button btClear;
    private TextView tv_count,tv_total;
    private boolean isExit=false;
    private long total=0;
    private MainActivity mContext;
    private SimpleAdapter adapter;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> tagList;
    private RadioButton rbEPC_,rbEPCTID,rbEPCTIDUSER;
    private Button btGetMode,btSetMode;
    private EditText etUserPtr,etUserLen;
    private String TAG="DeviceAPI_UHFReadTag";

    //--------------------------------------获取 解析数据-------------------------------------------------
    final int FLAG_START=0;//开始
    final int FLAG_STOP=1;//停止
    final int FLAG_UHFINFO=3;
    final int FLAG_SUCCESS=10;//成功
    final int FLAG_FAIL=11;//失败

    boolean isRuning=false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FLAG_STOP:
                    if(msg.arg1==FLAG_SUCCESS) {
                        //停止成功
                        btClear.setEnabled(true);
                        btStop.setEnabled(false);
                        InventoryLoop.setEnabled(true);
                        btInventory.setEnabled(true);
                    }else{
                        //停止失败
                        Utils.playSound(2);
                        Toast.makeText(mContext, R.string.uhf_msg_inventory_stop_fail,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FLAG_START:
                    if(msg.arg1==FLAG_SUCCESS){
                        //开始读取标签成功
                        btClear.setEnabled(false);
                        btStop.setEnabled(true);
                        InventoryLoop.setEnabled(false);
                        btInventory.setEnabled(false);
                    }else{
                        //开始读取标签失败
                        Utils.playSound(2);
                    }
                    break;
                case FLAG_UHFINFO:
                    UHFTAGInfo info =(UHFTAGInfo)msg.obj;
                    addEPCToList(info);
                    Utils.playSound(1);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uhf_new_read_tag, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "UHFReadTagFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopInventory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isExit=true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.btClear:
                clearData();
                break;
            case  R.id.InventoryLoop:
                startThread(false);
                break;
            case R.id.btInventory:
                inventory();
                break;
            case  R.id.btStop:
                if(mContext.uhf.getConnectStatus()==RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
                    startThread(true);
                }
                break;
            case R.id.btGetMode:
                getMode(true);
                break;
            case R.id.btSetMode:
                setMode();
            case R.id.rbEPCTIDUSER:
                if(!TextUtils.isEmpty(etUserLen.getText().toString())){
                    if(Integer.parseInt(etUserLen.getText().toString())==0){
                        etUserLen.setText("6");
                    }
                }else{
                    etUserLen.setText("6");
                }
                break;
        }
    }
    private  void init(){
        isExit=false;
        mContext.setConnectStatusNotice(new ConnectStatus());
        LvTags=(ListView)mContext.findViewById(R.id.LvTags);
        btInventory=(Button)mContext.findViewById(R.id.btInventory);
        InventoryLoop=(Button)mContext.findViewById(R.id.InventoryLoop);
        btStop=(Button)mContext.findViewById(R.id.btStop);
        btStop.setEnabled(false);
        btClear=(Button)mContext.findViewById(R.id.btClear);
        tv_count=(TextView)mContext.findViewById(R.id.tv_count);
        tv_total=(TextView)mContext.findViewById(R.id.tv_total);

        etUserPtr=(EditText)mContext.findViewById(R.id.etUserPtr);
        etUserLen=(EditText)mContext.findViewById(R.id.etUserLen);
        rbEPC_=(RadioButton)mContext.findViewById(R.id.rbEPC);
        rbEPCTID=(RadioButton)mContext.findViewById(R.id.rbEPCTID);
        rbEPCTIDUSER=(RadioButton)mContext.findViewById(R.id.rbEPCTIDUSER);
        btGetMode=(Button)mContext.findViewById(R.id.btGetMode);
        btSetMode=(Button)mContext.findViewById(R.id.btSetMode);

        rbEPCTIDUSER.setOnClickListener(this);
        btSetMode.setOnClickListener(this);
        btGetMode.setOnClickListener(this);

        InventoryLoop.setOnClickListener(this);
        btInventory.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btStop.setOnClickListener(this);
        tagList = new ArrayList<HashMap<String, String>>();
        adapter = new SimpleAdapter(mContext, tagList, R.layout.listtag_items,
                new String[] { "tagData", "tagLen", "tagCount", "tagRssi" },
                new int[] { R.id.TvTagUii, R.id.TvTagLen, R.id.TvTagCount,
                        R.id.TvTagRssi });
        LvTags.setAdapter(adapter);
        mContext.uhf.setKeyEventCallback(new RFIDWithUHFBluetooth.KeyEventCallback() {
            @Override
            public void getKeyEvent(int keycode) {
                Log.d("DeviceAPI_ReadTAG","  keycode ="+keycode  +"   ,isExit="+isExit);
               if(!isExit && mContext.uhf.getConnectStatus()==RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
                   startThread(loopFlag);
               }
            }
        });
        clearData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mContext.uhf.getConnectStatus() == RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
            InventoryLoop.setEnabled(true);
            btInventory.setEnabled(true);
            getMode(false);
        }else{
            InventoryLoop.setEnabled(false);
            btInventory.setEnabled(false);
        }
    }

    private void clearData() {
        tv_count.setText("0");
        tagList.clear();
        total=0;
        tv_total.setText("0");
        adapter.notifyDataSetChanged();
    }

    /**
     * 停止识别
     */
      private void stopInventory() {
            loopFlag = false;
            RFIDWithUHFBluetooth.StatusEnum statusEnum=mContext.uhf.getConnectStatus();
            Message msg=handler.obtainMessage(FLAG_STOP);
            boolean result=mContext.uhf.stopInventoryTag();
            if (result || statusEnum==RFIDWithUHFBluetooth.StatusEnum.DISCONNECTED) {
                msg.arg1=FLAG_SUCCESS;
            } else {
                msg.arg1=FLAG_FAIL;
            }
            mContext.scaning=false;
            handler.sendMessage(msg);
    }
        class ConnectStatus implements MainActivity.IConnectStatus {
           @Override
           public void getStatus(RFIDWithUHFBluetooth.StatusEnum statusEnum) {
               if(statusEnum== RFIDWithUHFBluetooth.StatusEnum.CONNECTED){
                   if(!loopFlag) {
                       try {
                           Thread.sleep(500);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       getMode(false);
                       InventoryLoop.setEnabled(true);
                       btInventory.setEnabled(true);
                   }
               }else if(statusEnum== RFIDWithUHFBluetooth.StatusEnum.DISCONNECTED){
                   loopFlag = false;
                   mContext.scaning=false;
                   btClear.setEnabled(true);
                   btStop.setEnabled(false);
                   InventoryLoop.setEnabled(false);
                   btInventory.setEnabled(false);
               }
           }
       }
       private void getMode(boolean isFlag) {
           if (mContext.uhf.getConnectStatus() == RFIDWithUHFBluetooth.StatusEnum.CONNECTED) {
               int[] outMode = new int[1];
               int[] outUserStart = new int[1];
               int[] outUserLen = new int[1];
               if (mContext.uhf.getEpcTidUserMode(outMode, outUserStart, outUserLen)) {
                   if (outMode[0] == 0) {
                       rbEPC_.setChecked(true);
                   } else if (outMode[0] == 1) {
                       rbEPCTID.setChecked(true);
                   } else if (outMode[0] == 2) {
                       rbEPCTIDUSER.setChecked(true);
                   } else {
                       rbEPC_.setChecked(false);
                       rbEPCTID.setChecked(false);
                       rbEPCTIDUSER.setChecked(false);
                   }
                   etUserPtr.setText(outUserStart[0] + "");
                   etUserLen.setText(outUserLen[0] + "");
                   if (isFlag) Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
               } else {
                   if (isFlag) Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
                   rbEPC_.setChecked(false);
                   rbEPCTID.setChecked(false);
                   rbEPCTIDUSER.setChecked(false);
               }
           }
       }
        private void setMode(){
            String strUserPtr=etUserPtr.getText().toString();
            String strUserLen=etUserLen.getText().toString();
            int userPtr=0;
            int userLen=0;
            int mode=0;
            if(strUserPtr==null || strUserLen.isEmpty()){
                userPtr=0;
            }else{
                userPtr=Integer.parseInt(strUserPtr.toString());
            }
            if(strUserLen==null || strUserLen.isEmpty()){
                userLen=0;
            }else{
                userLen=Integer.parseInt(strUserLen.toString());
            }
            if(rbEPC_.isChecked()){
                mode=0;
            }else if(rbEPCTID.isChecked()){
                mode=1;
            }else if(rbEPCTIDUSER.isChecked()){
                mode=2;
            }else{
                mode=-1;
            }
            if(mContext.uhf.setEpcTidUserMode(mode,userPtr,userLen)){
                Toast.makeText(mContext,"success",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext,"fail",Toast.LENGTH_SHORT).show();
            }
        }

    public synchronized void startThread(boolean isStop){
        if(isRuning) {
            return;
        }
        isRuning=true;
        new TagThread(isStop).start();
    }

    class TagThread extends Thread {
        boolean isStop=false;
        public TagThread(boolean isStop){
            this.isStop=isStop;
        }
        public void run() {
            if(isStop){
                stopInventory();
                isRuning=false;//执行完成设置成false
            }else{
                Message msg=handler.obtainMessage(FLAG_START);
                if (mContext.uhf.startInventoryTag()) {
                    loopFlag = true;
                    mContext.scaning=true;
                    msg.arg1=FLAG_SUCCESS;
                } else {
                    msg.arg1=FLAG_FAIL;
                }
                handler.sendMessage(msg);
                isRuning=false;//执行完成设置成false
                while (loopFlag) {
                    getUHFInfo();
                }
            }
        }
    }

    private synchronized  boolean getUHFInfo(){
            ArrayList<UHFTAGInfo> list = mContext.uhf.readTagFromBuffer_EpcTidUser();
            if (list != null) {
                for (int k = 0; k < list.size(); k++) {
                    UHFTAGInfo info = list.get(k);
                    Message msg = handler.obtainMessage(FLAG_UHFINFO);
                    msg.obj =info;
                    handler.sendMessage(msg);
                }
                if (list.size() > 0)
                    return true;
            } else {
                return false;
            }
            return false;
    }
    /**
     * 添加EPC到列表中
     *
     * @param uhftagInfo
     */
    private void addEPCToList(UHFTAGInfo uhftagInfo) {
        if (!TextUtils.isEmpty(uhftagInfo.getEPC())) {
            int index = checkIsExist(uhftagInfo.getEPC());

            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("EPC:");
            stringBuilder.append(uhftagInfo.getEPC());
            if(!TextUtils.isEmpty(uhftagInfo.getTid())){
                stringBuilder.append("\r\nTID:");
                stringBuilder.append(uhftagInfo.getTid());
            }
            if(!TextUtils.isEmpty(uhftagInfo.getUser())){
                stringBuilder.append("\r\nUSER:");
                stringBuilder.append(uhftagInfo.getUser());
            }

            map = new HashMap<String, String>();
            map.put("tagUii", uhftagInfo.getEPC());
            map.put("tagData", stringBuilder.toString());
            map.put("tagCount", String.valueOf(1));
            map.put("tagRssi", uhftagInfo.getRssi());
            // mContext.getAppContext().uhfQueue.offer(epc + "\t 1");
            if (index == -1) {
                tagList.add(map);
                LvTags.setAdapter(adapter);
                tv_count.setText("" + adapter.getCount());
            } else {
                int tagcount = Integer.parseInt(tagList.get(index).get("tagCount"), 10) + 1;
                map.put("tagCount", String.valueOf(tagcount));
                tagList.set(index, map);
            }
            total=total+1;
            tv_total.setText(String.valueOf(total));
            adapter.notifyDataSetChanged();
        }
    }
    public int checkIsExist(String strEPC) {
        int existFlag = -1;
        if (strEPC==null || strEPC.isEmpty()) {
            return existFlag;
        }
        String tempStr = "";
        for (int i = 0; i < tagList.size(); i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp = tagList.get(i);
            tempStr = temp.get("tagUii");
            if (strEPC.equals(tempStr)) {
                existFlag = i;
                break;
            }
        }
        return existFlag;
    }

    private void inventory(){
        UHFTAGInfo uhftagInfo= mContext.uhf.inventorySingleTag_EpcTidUser();
        if(uhftagInfo!=null){
            Message msg = handler.obtainMessage(FLAG_UHFINFO);
            msg.obj = uhftagInfo;
            handler.sendMessage(msg);
        }
    }
}
