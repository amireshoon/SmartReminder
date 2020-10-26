package ap.behrouzi.smartr.utils;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by igor.malytsky on 9/10/15.
 */
public class ResizeCallbackImageView extends androidx.appcompat.widget.AppCompatImageView {

    public interface OnSizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener mSizeChangedListener) {
        this.mSizeChangedListener = mSizeChangedListener;
    }

    private OnSizeChangedListener mSizeChangedListener;

    public ResizeCallbackImageView(Context context) {
        super(context);
    }

    public ResizeCallbackImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeCallbackImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mSizeChangedListener != null) {
            mSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
        }
    }
}
