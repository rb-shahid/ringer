package info.ss12.audioalertsystem.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import info.ss12.audioalertsystem.Myservice;

public class LockablescrollView extends ScrollView {
    private boolean scrollable;

    public LockablescrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.scrollable = true;
    }

    public void setScrollingEnabled(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case Myservice.DETECT_NONE /*0*/:
                if (this.scrollable) {
                    return super.onTouchEvent(ev);
                }
                return this.scrollable;
            default:
                return super.onTouchEvent(ev);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.scrollable) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }
}
