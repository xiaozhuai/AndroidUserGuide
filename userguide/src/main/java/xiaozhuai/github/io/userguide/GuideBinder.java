package xiaozhuai.github.io.userguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaozhuai
 * @date : 16/9/12
 */
//TODO 添加BinderSet
public class GuideBinder {

    public static class Img{
        public static final int TYPE_RES_ID     = 0;
        public static final int TYPE_DRAWABLE   = 1;
        public static final int TYPE_BITMAP     = 2;
        public int type = 0;
        public int resId;
        public Drawable drawable;
        public Bitmap bitmap;
        public void setResId(int id){
            type = TYPE_RES_ID;
            resId = id;
        }
        public void setDrawable(Drawable d){
            type = TYPE_DRAWABLE;
            drawable = d;
        }
        public void setBitmap(Bitmap b){
            type = TYPE_BITMAP;
            bitmap = b;
        }
    }

    private View mView;
    private Img mImg = new Img();
    private int mWidth;
    private int mHeight;
    private Rect mViewRect = new Rect();
    private int mShadowType;

    private List<Rect> mListViewRect = new ArrayList<>();

    public static final int SHADOW_TYPE_FULL = 0;
    public static final int SHADOW_TYPE_CIRCLE = 1;
    public static final int SHADOW_TYPE_RECT = 2;
    public static final int SHADOW_TYPE_FANTASY = 3;

    private GuideBinder(){

    }

    public static GuideBinder build(){
        GuideBinder guideBinder = new GuideBinder();
        return guideBinder;
    }

    public GuideBinder bind(View view){
        mView = view;
//        updateRect();
//        mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                updateRect();
//            }
//        });

        return this;
    }

    public void updateRect(Rect parentRect){
        if (mView == null) {
            return;
        }

        int[] location = new int[2];
        mView.getLocationOnScreen(location);
        mViewRect.left = location[0];
        mViewRect.top = location[1];
        mViewRect.left = mViewRect.left - parentRect.left;
        mViewRect.top = mViewRect.top - parentRect.top;
        mViewRect.right = mViewRect.left+mView.getMeasuredWidth();
        mViewRect.bottom = mViewRect.top+mView.getMeasuredHeight();
    }


    public GuideBinder img(int resId){
        mImg.setResId(resId);
        return this;
    }

    public GuideBinder img(Drawable drawable){
        mImg.setDrawable(drawable);
        return this;
    }

    public GuideBinder img(Bitmap bitmap){
        mImg.setBitmap(bitmap);
        return this;
    }

    public GuideBinder size(int width, int height){
        mWidth = width;
        mHeight = height;
        return this;
    }

    public GuideBinder size(float width, float height){
        mWidth = (int) width;
        mHeight = (int) height;
        return this;
    }

    public GuideBinder shadowType(int shadowType){
        mShadowType = shadowType;
        return this;
    }

    public Rect getViewRect(){
        return mViewRect;
    }

    public View getView(){
        return mView;
    }

    public int getImageWidth(){
        return mWidth;
    }

    public int getImageHeight(){
        return mHeight;
    }

    public Img getImage(){
        return mImg;
    }

    public int getShadowType(){
        return mShadowType;
    }

    public List<Rect> getHighlightViewRecs(){
        return mListViewRect;
    }

}
