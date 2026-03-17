package com.example.uhf_bt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uhf_bt.FileUtils;
import com.example.uhf_bt.MainActivity;
import com.example.uhf_bt.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class BTRenameFragment extends Fragment {
    static boolean isExit_=false;
    MainActivity mContext;
    EditText etNewName;
    EditText etOldName;
    Button btSet;

    public BTRenameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_btrename, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isExit_=false;
        mContext=(MainActivity)getActivity();
        etNewName=(EditText)getActivity().findViewById(R.id.etNewName);
        etOldName=(EditText)getActivity().findViewById(R.id.etOldName);
        etOldName.setEnabled(false);
        etOldName.setVisibility(View.GONE);
        btSet=(Button)getActivity().findViewById(R.id.btSet);
        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName=etNewName.getText().toString();
                if(newName!=null && newName.length()==0){
                    Toast.makeText(mContext,"设置失败",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                   boolean result= mContext.uhf.setRemoteBluetoothName(newName);
                    if(result){
                        mContext.updateUI(mContext.remoteBTName,newName);
                        Toast.makeText(mContext,"设置成功",Toast.LENGTH_SHORT).show();

                        ///---------------------------------------
                        List<String[]> list= FileUtils.readxml();
                        for(int k=0;k<list.size();k++){
                            if(mContext.remoteBTAdd.equals(list.get(k)[0])){
                                list.get(k)[1]=newName;
                                FileUtils.savexml(list);
                                break;
                            }
                        }

                        //---------------------------------------


                    }else{
                        Toast.makeText(mContext,"设置失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
