package xiaozhuai.github.io.userguidedemo;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;


import xiaozhuai.github.io.userguide.GuideBinder;
import xiaozhuai.github.io.userguide.UserGuide;

public class MainActivity extends AppCompatActivity {

    private UserGuide mUserGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserGuide = new UserGuide(this, (FrameLayout) findViewById(R.id.user_guide_frame));
        mUserGuide.setUserGuideListener(mUserGuideListener);

        float density = getResources().getDisplayMetrics().density;

        mUserGuide.add(
                GuideBinder.build()
                        .bind(findViewById(R.id.btn_1))
                        .img(R.drawable.touch_me_1)
                        .size(400*density, 400*density)
                        .shadowType(GuideBinder.SHADOW_TYPE_FULL)
        );
        mUserGuide.add(
                GuideBinder.build()
                        .bind(findViewById(R.id.btn_2))
                        .shadowType(GuideBinder.SHADOW_TYPE_CIRCLE)
        );
        mUserGuide.add(
                GuideBinder.build()
                        .bind(findViewById(R.id.btn_3))
                        .shadowType(GuideBinder.SHADOW_TYPE_RECT)
        );
        mUserGuide.add(
                GuideBinder.build()
                        .bind(findViewById(R.id.btn_4))
                        .shadowType(GuideBinder.SHADOW_TYPE_FANTASY)
        );

        mUserGuide.next();

    }

    private UserGuide.UserGuideListener mUserGuideListener = new UserGuide.UserGuideListener() {
        @Override
        public void onStep(int step) {
            Toast.makeText(MainActivity.this, "on step "+step, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBoundViewTouched(View v, PointF begin, PointF end) {
            Toast.makeText(MainActivity.this, "bound view touched", Toast.LENGTH_SHORT).show();
            mUserGuide.next();
        }

        @Override
        public void onEnd() {
            Toast.makeText(MainActivity.this, "guide end", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBackgroundTouched() {
            Toast.makeText(MainActivity.this, "background touched", Toast.LENGTH_SHORT).show();
        }
    };
}
