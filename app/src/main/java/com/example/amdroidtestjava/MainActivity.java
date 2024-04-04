package com.example.amdroidtestjava;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amdroidtestjava.util.DateUtils;
import com.example.amdroidtestjava.util.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView helloWorld = findViewById(R.id.helloWorld);
        helloWorld.setText("你好,世界!");
        //Java设置字体大小默认为sp 会随着系统设置的字体大小改变
        helloWorld.setTextSize(30);
        //设置字体颜色
        helloWorld.setTextColor(Color.WHITE);
        //xml中颜色配置 如果为6位数字 默认前两位为FF可以看见,java中默认为00透明的看不到
        helloWorld.setTextColor(0xFFFFFFFF);
        //设置背景颜色
        helloWorld.setBackgroundColor(Color.RED);
        //获取按钮
        Button button = findViewById(R.id.button);
        //获取视图的参数
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        //重新赋值视图的高度
        layoutParams.height = Utils.dp2px(this,50);
        //重新赋值视图的宽度
        layoutParams.width = Utils.dp2px(this,80);
        //重新赋值视图的参数
        button.setLayoutParams(layoutParams);
        //按钮点击事件
        view_click(button , new NextActivity());
        //文本框点击事件
        view_click(helloWorld,new NextActivity() );
        //线性布局
        Button linearActivityButton = findViewById(R.id.linearActivity);

        view_click(linearActivityButton,new LinearActivity());
        //相对布局
        Button relativeActivityButton = findViewById(R.id.relativeActivity);

        view_click(relativeActivityButton,new RelativeActivity());
        //网格布局
        Button gridActivityButton = findViewById(R.id.gridActivity);

        view_click(gridActivityButton,new GridActivity());
        //垂直滚动布局
        Button scrollViewActivityButton = findViewById(R.id.scrollViewActivity);

        view_click(scrollViewActivityButton,new ScrollViewActivity());
    }

    //view 点击事件
    public void view_click(View view , AppCompatActivity appCompatActivity){

        //添加按钮点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //由那个页面,跳转到另一个页面
                intent.setClass(MainActivity.this,appCompatActivity.getClass());
                //开始跳转
                startActivity(intent);
            }
        });
    }

    //button 的 onClick的属性 直接调用方法,已过时 但可用方便以后阅读他人代码
    public void doClick(View view) {
        //获取当前view的ID
        Button doClickButton = findViewById(view.getId());

        doClickButton.setText(DateUtils.getNowTime());

    }
}
