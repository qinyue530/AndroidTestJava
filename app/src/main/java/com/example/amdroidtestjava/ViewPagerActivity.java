package com.example.amdroidtestjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.amdroidtestjava.adapter.ViewPagerAdapter;
import com.example.amdroidtestjava.enity.MyBaseEntity;
import com.example.amdroidtestjava.fragement.StaticFragement;
import com.example.amdroidtestjava.receiver.MyBroadcastReceiver;
import com.example.amdroidtestjava.receiver.OrderAReceiver;
import com.example.amdroidtestjava.receiver.OrderBReceiver;
import com.example.amdroidtestjava.receiver.OrderCReceiver;
import com.example.amdroidtestjava.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String[] item = new String[]{"AAA","BBB","CCC","DDD","EEE","FFF","GGG","HHH","III","JJJ","KKK","LLL"};
    ViewPager myViewPager;
    PagerTabStrip myPagerTabStrip;

    Button broadcastReceiverButton;
    List<MyBaseEntity> myBaseEntityList = new ArrayList<>();

    MyBroadcastReceiver myBroadcastReceiver;
    Button broadcastReceiverOrderButton;

    OrderAReceiver orderAReceiver;
    OrderBReceiver orderBReceiver;
    OrderCReceiver orderCReceiver;

    View fragment;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_pager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.myViewPagerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initViewOnListener();
        for(String str : item){

            MyBaseEntity myBaseEntity = new MyBaseEntity();
            myBaseEntity.setIcon(R.drawable.tigger);
            myBaseEntity.setName(str);
            myBaseEntity.setDesc("获取的是:"+str);
            myBaseEntityList.add(myBaseEntity);
        }
        ViewPagerAdapter  viewPagerAdapter = new ViewPagerAdapter(this , myBaseEntityList);
        myViewPager.setAdapter(viewPagerAdapter);
        //设置默认选择展示的图片,如不设置默认为第0个
        myViewPager.setCurrentItem(3);
        initPagerStrip();

        StaticFragement fragment = new StaticFragement();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragementStatic, fragment);
        fragmentTransaction.commit();

        broadcastReceiverButton = findViewById(R.id.broadcastReceiverButton);
        broadcastReceiverButton.setOnClickListener(this);

        broadcastReceiverOrderButton = findViewById(R.id.broadcastReceiverOrderButton);
        broadcastReceiverOrderButton.setOnClickListener(this);


    }

    //初始化翻页标签栏
    private void initPagerStrip() {
        //设置翻页标签栏的文本大小
        myPagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        myPagerTabStrip.setTextColor(Color.BLACK);
    }

    private void initView() {
        myViewPager = findViewById(R.id.myViewPager);
        myPagerTabStrip = findViewById(R.id.myPagerTabStrip);
    }

    private void initViewOnListener() {
        myViewPager.addOnPageChangeListener(this);

    }

    /**
     *  翻页过程中触发
     * @param position 示当前页面的序号
     * @param positionOffset 页面偏移的百分比，取值为0到1
     * @param positionOffsetPixels 页面的偏移距离
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 在翻页结束后触发。
     * @param position 表示当前滑到了哪一个页画
     */
    @Override
    public void onPageSelected(int position) {
        Utils.toastShow(this,"当前为第"+position+"个图片:"+myBaseEntityList.get(position).getName());
    }

    /**
     *  翻页状态改变时触发  翻页过程中 状态值变化顺序为 正在滑动 ->滑动完毕 ->静止
     * @param state The new scroll state. 0 静止 1 正在滑动 2 滑动完毕
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.broadcastReceiverButton){
            Intent intent = new Intent(MyBroadcastReceiver.MBR_ACTION);
            sendBroadcast(intent);
        }else if(view.getId() == R.id.broadcastReceiverOrderButton){
            Intent intent = new Intent(MyBroadcastReceiver.MBR_ORDER_ACTION);
            //发送有序广播
            //receiverPermission 权限
            sendOrderedBroadcast(intent,null);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册广播监听
        myBroadcastReceiver = new MyBroadcastReceiver();
        //过滤器
        // 创建一个意图过滤器，只处理对应action的广播
        IntentFilter filter = new IntentFilter(MyBroadcastReceiver.MBR_ACTION);
        //注册接收器，注册之后才能正常接收广播
        registerReceiver (myBroadcastReceiver, filter,RECEIVER_EXPORTED);


        //多个接收器处理有序广播的顺序规则为：
        //1、优先级越大的接收器，越早收到有序广播；
        //2、犹先级相同的时候，越早注册的接收器越早收到有序广播
        orderAReceiver = new OrderAReceiver();
        IntentFilter filterA = new IntentFilter(MyBroadcastReceiver.MBR_ORDER_ACTION);
        filterA.setPriority(-999);
        registerReceiver (orderAReceiver, filterA,RECEIVER_EXPORTED);

        orderBReceiver = new OrderBReceiver();
        IntentFilter filterB = new IntentFilter(MyBroadcastReceiver.MBR_ORDER_ACTION);
        filterA.setPriority(-800);
        registerReceiver (orderBReceiver, filterB,RECEIVER_EXPORTED);

        orderCReceiver = new OrderCReceiver();
        IntentFilter filterC = new IntentFilter(MyBroadcastReceiver.MBR_ORDER_ACTION);
        filterA.setPriority(888);
        registerReceiver (orderCReceiver, filterC,RECEIVER_EXPORTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //注销接收器，法销之后就不再接收广播
        unregisterReceiver(myBroadcastReceiver);
        unregisterReceiver(orderAReceiver);
        unregisterReceiver(orderBReceiver);
        unregisterReceiver(orderCReceiver);
    }
}