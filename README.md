# Android UserGuide

[[English README](README.md)] [[中文说明](README_CN.md)]

Android User Guide is a framework to build user guide steps easily for android application.

Under [MIT LICENSE](LICENSE.md)

author: xiaozhuai - [xiaozhuai7@gmail.com](xiaozhuai7@gmail.com)

# Demo

Here is a simple demo

```java
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
                        .img(R.drawable.touch_me_2)
                        .size(400*density, 400*density)
                        .shadowType(GuideBinder.SHADOW_TYPE_CIRCLE)
        );
        mUserGuide.add(
                GuideBinder.build()
                        .bind(findViewById(R.id.btn_3))
                        .img(R.drawable.touch_me_1)
                        .size(400*density, 400*density)
                        .shadowType(GuideBinder.SHADOW_TYPE_RECT)
        );
        mUserGuide.add(
                GuideBinder.build()
                        .bind(findViewById(R.id.btn_4))
                        .img(R.drawable.touch_me_2)
                        .size(400*density, 400*density)
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

```

# Imgs

![preview](imgs/preview.png)



# Finally

Forget my poor ps skill, I mean that red arrow. : )