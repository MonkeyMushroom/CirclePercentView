package com.monkey.circlepercentview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CirclePercentView mCirclePercentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCirclePercentView = (CirclePercentView) findViewById(R.id.circlePercentView);
        mCirclePercentView.setOnCircleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float percent = (float) (Math.random() * 99 + 1);
                mCirclePercentView.setCurPercent(percent);
            }
        });
    }
}
