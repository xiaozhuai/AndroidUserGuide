package xiaozhuai.github.io.userguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaozhuai
 * @date : 16/9/12
 */
public class UserGuide {


    private Context mContext;
    private FrameLayout mHolder;
    private int mHolderWidth;
    private int mHolderHeight;
    private ImageView mImageView;
    private FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(0, 0);
    private ArrayList<GuideBinder> mGuideBinderList = new ArrayList<>();

    private static final int MASK_COLOR = 0xC0000000;
    private int mCurrentStep = -1;

    private Rect mHolderRect;

    public interface UserGuideListener{
        void onStep(int step);
        void onBoundViewTouched(View v, PointF begin, PointF end);
        void onEnd();
        void onBackgroundTouched();// 整个背景的点击事件
    }

    private UserGuideListener mUserGuideListener;

    public UserGuide(Context context, FrameLayout holder){
        mContext = context;
        mHolder = holder;
        mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mHolder.addView(mImageView);
        mHolder.setOnTouchListener(mHolderOnTouchListener);
        reset();
    }

    private void updateWidthHeight(){
        mHolderWidth = mHolder.getMeasuredWidth();
        mHolderHeight = mHolder.getMeasuredHeight();
    }

    public void setUserGuideListener(UserGuideListener l){
        mUserGuideListener = l;
    }

    private View.OnTouchListener mHolderOnTouchListener = new View.OnTouchListener(){
        private PointF begin = new PointF();
        private PointF end = new PointF();
        private float x,y;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            x = event.getX();
            y = event.getY();
//            if(isPointInRect(x, y)){
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        begin.x = x;
                        begin.y = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        end.x = x;
                        end.y = y;
                        if (mUserGuideListener != null) {
                            final int curStep = mCurrentStep;
                            if (isPointInRect(x, y) && stepIsCorrect(curStep)) {
                                mUserGuideListener.onBoundViewTouched(
                                        mGuideBinderList.get(curStep).getView(),
                                        begin,
                                        end
                                );
                            } else {
                                mUserGuideListener.onBackgroundTouched();
                            }
                        }
                        break;

                }

//            }
            return true;
        }
    };

    private boolean isPointInRect(float x, float y){
        final int curStep = mCurrentStep;
        if (stepIsCorrect(curStep)) {
            Rect rect = mGuideBinderList.get(curStep).getViewRect();
//        D.i("slack",rect.left+","+x+","+rect.right+"----"+rect.top+","+y+","+rect.bottom);
            if ((int) x < rect.right && (int) x > rect.left && (int) y < rect.bottom && (int) y > rect.top) {
                return true;
            }
        }
        return false;
    }

    public void clear(){
        for (int i = 0; i < mGuideBinderList.size(); i++) {
            if(mGuideBinderList.get(i).getImage().bitmap != null) {
                mGuideBinderList.get(i).getImage().bitmap.recycle();
            }
        }
        mGuideBinderList.clear();
    }

    public void add(GuideBinder guideBinder){
        mGuideBinderList.add(guideBinder);
    }

    public void add(GuideBinder[] guideBinders){
        for(int i=0; i<guideBinders.length; i++){
            if (guideBinders[i].getView() != null) {
                mGuideBinderList.add(guideBinders[i]);
            }
        }
    }

    public void add(List<GuideBinder> guideBinderList){
        add((GuideBinder[]) guideBinderList.toArray());
    }

    public int size(){
        return mGuideBinderList.size();
    }

    public void reset(){
        mCurrentStep = -1;
        mImageView.setImageBitmap(null);
        mHolder.setBackground(null);
        mHolder.setVisibility(View.GONE);
    }

    /**
     *
     * @return
     */
    private boolean stepIsCorrect(int step) {
        return step >= 0 && step < mGuideBinderList.size();
    }

    private PointF calcImagePoint(GuideBinder binder){
        PointF viewCenterPoint = new PointF();
        PointF imagePoint = new PointF();
        Rect rect = binder.getViewRect();
        viewCenterPoint.x = ((float)rect.right + (float)rect.left)/2f;
        viewCenterPoint.y = ((float)rect.bottom + (float)rect.top)/2f;

        imagePoint.x = viewCenterPoint.x - (float)binder.getImageWidth() / 2f;
        imagePoint.y = viewCenterPoint.y - (float)binder.getImageHeight() / 2f;
        return imagePoint;
    }

    private BitmapDrawable genBackgroundBitmapDrawable(){

        Bitmap bitmap  = Bitmap.createBitmap(mHolderWidth, mHolderHeight, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(MASK_COLOR);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(MASK_COLOR);

        final int curStep = mCurrentStep;
        if (stepIsCorrect(curStep)) {
            GuideBinder binder = mGuideBinderList.get(curStep);
            Rect rect = binder.getViewRect();
            switch (binder.getShadowType()) {
                case GuideBinder.SHADOW_TYPE_FANTASY:
                    View view = binder.getView();
                    if (view != null) {
                        view.setDrawingCacheEnabled(true);
                        view.buildDrawingCache();
                        Bitmap tmp = view.getDrawingCache();
                        canvas.drawBitmap(tmp, rect.left, rect.top, paint);
                        view.setDrawingCacheEnabled(false);
                    }
                    break;

                case GuideBinder.SHADOW_TYPE_CIRCLE:
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                    float cx = ((float) rect.left + (float) rect.right) / 2.0f;
                    float cy = ((float) rect.top + (float) rect.bottom) / 2.0f;
                    float r = Math.max((float) rect.right - (float) rect.left, (float) rect.bottom - (float) rect.top) / 2.0f;
                    canvas.drawCircle(cx, cy, r, paint);
                    break;

                case GuideBinder.SHADOW_TYPE_RECT:
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                    canvas.drawRect(rect, paint);

                    break;
                case GuideBinder.SHADOW_TYPE_FULL:
                default:
                    break;
            }

            // 高亮 部分
            List<Rect> recs = binder.getHighlightViewRecs();

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            for (int i = 0; i < recs.size(); i++) {
                RectF rectF = new RectF();
                rectF.set(recs.get(i));
                canvas.drawRoundRect(rectF,16,16 ,paint);
//            canvas.drawRect(recs.get(i), paint);
            }
        }

        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    public void next(){
        mCurrentStep += 1;
        if(stepIsCorrect(mCurrentStep)){

            mHolder.setVisibility(View.VISIBLE);

            //这里必须使用view.post来跳到下一步的教程
            //因为这里会需要得到view的真实宽高和相对于屏幕的绝对坐标
            //而要获取这些参数必须是在测量完成之后,如果不用post,直接执行
            //则会出现执行时测量尚未完成,得到的参数不对,造成坐标计算的错误
            final int nextStep = mCurrentStep;
            mGuideBinderList.get(nextStep).getView().post(new Runnable() {
                @Override
                public void run() {

                    updateWidthHeight();
                    mHolderRect = new Rect();
                    int[] location = new int[2];
                    mHolder.getLocationOnScreen(location);
                    mHolderRect.left = location[0];
                    mHolderRect.top = location[1];
                    mHolderRect.right = mHolderRect.left+mHolderWidth;
                    mHolderRect.bottom = mHolderRect.top+mHolderHeight;

                    if (!stepIsCorrect(nextStep)) {
                        return;
                    }
                    GuideBinder binder = mGuideBinderList.get(nextStep);
                    binder.updateRect(mHolderRect);
                    mLayoutParams.width  = binder.getImageWidth();
                    mLayoutParams.height = binder.getImageHeight();
                    //mLayoutParams.setMargins((int)imagePoint.x, (int)imagePoint.y, 0 ,0);
                    mImageView.setLayoutParams(mLayoutParams);
                    PointF imagePoint = calcImagePoint(binder);
                    mImageView.setX(imagePoint.x);
                    mImageView.setY(imagePoint.y);
                    GuideBinder.Img img = binder.getImage();
                    switch (img.type){
                        case GuideBinder.Img.TYPE_RES_ID:
                            mImageView.setImageResource(img.resId);
                            break;
                        case GuideBinder.Img.TYPE_DRAWABLE:
                            mImageView.setImageDrawable(img.drawable);
                            break;
                        case GuideBinder.Img.TYPE_BITMAP:
                            mImageView.setImageBitmap(img.bitmap);
                            break;
                        default:
                            break;
                    }
                    if(mUserGuideListener!=null){
                        mUserGuideListener.onStep(mCurrentStep);
                    }
                }
            });

            mHolder.post(new Runnable() {
                @Override
                public void run() {
                    mHolder.setBackground(genBackgroundBitmapDrawable());
                }
            });
        }else{
            reset();
            if(mUserGuideListener!=null){
                mUserGuideListener.onEnd();
            }
        }
    }


}
