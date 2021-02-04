package com.example.recyclerview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recyclerview.entity.Item;
import com.example.recyclerview.init.ActivityInit;
import com.example.recyclerview.thread.MyThread;
import com.example.recyclerview.utils.HandleDataUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Du
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<Item> allData;
    private List<Item> selData;

    private final List<String> scrollTab = new ArrayList<>();
    private HandleDataUtils handleDataUtils;
    private static Boolean isQuit = false;

    private ActivityInit activityInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handleDataUtils.saveSelectFunctionItem(selData);
        handleDataUtils.saveAllFunctionWithState(allData);
    }

    @Override
    public void onBackPressed() {

        if (!isQuit) {
            isQuit = true;
            activityInit.btnViewExit(allData,selData);
            MyThread myThread = new MyThread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        isQuit = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            Thread thread = new Thread(myThread);
            thread.start();

        } else {
            handleDataUtils.saveSelectFunctionItem(selData);
            handleDataUtils.saveAllFunctionWithState(allData);
            finish();
        }
    }

    public void init() {
        getSupportActionBar().hide();

        handleDataUtils = new HandleDataUtils(this);
        allData = handleDataUtils.getAllFunctionWithState();
        selData = handleDataUtils.getSelectFunctionItem();

        activityInit = new ActivityInit(this);
        activityInit.initRecyclerViewBlockAdapter(R.id.recyclerViewExist, allData, selData);
        activityInit.initRecyclerViewItemAdapter(R.id.recyclerViewAll, allData, selData);
        activityInit.initView(R.id.horizonLScrollView, R.id.rg_tab, allData, selData);
        activityInit.initTab(handleDataUtils);
    }
}