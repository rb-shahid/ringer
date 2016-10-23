package info.ss12.audioalertsystem.roundknob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class RoundKnobButton extends RelativeLayout implements OnGestureListener {
    private Bitmap bmpRotorOff;
    private Bitmap bmpRotorOn;
    private GestureDetector gestureDetector;
    private ImageView ivRotor;
    private float mAngleDown;
    private float mAngleUp;
    private OnTouchListener mGestureListener;
    private boolean mState;
    private RoundKnobButtonListener m_listener;
    private int m_nHeight;
    private int m_nWidth;
    private boolean onDown;

    /* renamed from: de.btn.roundknob.RoundKnobButton.1 */
    class C05161 extends SimpleOnGestureListener {
        C05161() {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float rotDegrees = RoundKnobButton.this.cartesianToPolar(1 - (e2.getX() / ((float) RoundKnobButton.this.getWidth())), 1 - (e2.getY() / ((float) RoundKnobButton.this.getHeight())));
            if (Float.isNaN(rotDegrees)) {
                return false;
            }
            float posDegrees = rotDegrees;
            if (rotDegrees < 0.0f) {
                posDegrees = 360.0f + rotDegrees;
            }
            if (posDegrees <= 210 && posDegrees >= 150.0f) {
                return false;
            }
            RoundKnobButton.this.setRotorPosAngle(posDegrees);
            int percent = Math.round((rotDegrees + 150.0f) / 3.0f);
            if (RoundKnobButton.this.m_listener != null) {
                RoundKnobButton.this.m_listener.onRotate(percent);
            }
            return true;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        public boolean onDown(MotionEvent e) {
            Log.i("ONDOWN", "GestureDetector --> onDown");
            RoundKnobButton.this.onDown = true;
            if (RoundKnobButton.this.m_listener != null) {
                RoundKnobButton.this.m_listener.onStateChange(RoundKnobButton.this.onDown);
            }
            RoundKnobButton.this.mAngleDown = RoundKnobButton.this.cartesianToPolar(1 - (e.getX() / ((float) RoundKnobButton.this.getWidth())), 1 - (e.getY() / ((float) RoundKnobButton.this.getHeight())));
            return true;
        }
    }

    /* renamed from: de.btn.roundknob.RoundKnobButton.2 */
    class C05172 implements OnTouchListener {
        C05172() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (RoundKnobButton.this.gestureDetector.onTouchEvent(event)) {
                return true;
            }
            if (event.getAction() == 0 && RoundKnobButton.this.onDown) {
                Log.i("ACTIONUP", "OnTouchListener --> onTouch DOWN");
                RoundKnobButton.this.onDown = true;
                if (RoundKnobButton.this.m_listener == null) {
                    return true;
                }
                RoundKnobButton.this.m_listener.onStateChange(RoundKnobButton.this.onDown);
                return true;
            } else if (event.getAction() == 3 && RoundKnobButton.this.onDown) {
                Log.i("ACTIONUP", "OnTouchListener --> onTouch ACTION_CANCEL");
                RoundKnobButton.this.onDown = false;
                if (RoundKnobButton.this.m_listener == null) {
                    return true;
                }
                RoundKnobButton.this.m_listener.onStateChange(RoundKnobButton.this.onDown);
                return true;
            } else if (event.getAction() != 1 || !RoundKnobButton.this.onDown) {
                return false;
            } else {
                Log.i("ACTIONUP", "OnTouchListener --> onTouch ACTION_UP");
                RoundKnobButton.this.onDown = false;
                if (RoundKnobButton.this.m_listener == null) {
                    return true;
                }
                RoundKnobButton.this.m_listener.onStateChange(RoundKnobButton.this.onDown);
                return true;
            }
        }
    }

    public interface RoundKnobButtonListener {
        void onRotate(int i);

        void onStateChange(boolean z);
    }

    public void SetListener(RoundKnobButtonListener l) {
        this.m_listener = l;
    }

    public void SetState(boolean state) {
        this.mState = state;
        this.ivRotor.setImageBitmap(state ? this.bmpRotorOn : this.bmpRotorOff);
    }

    public RoundKnobButton(Context context, int back, int rotoron, int w, int h) {
        super(context);
        this.mState = true;
        this.m_nWidth = 0;
        this.m_nHeight = 0;
        this.m_nWidth = w;
        this.m_nHeight = h;
        ImageView ivBack = new ImageView(context);
        ivBack.setImageResource(back);
        LayoutParams lp_ivBack = new LayoutParams(w, h);
        lp_ivBack.addRule(13);
        addView(ivBack, lp_ivBack);
        Bitmap srcon = BitmapFactory.decodeResource(context.getResources(), rotoron);
        float scaleWidth = ((float) w) / ((float) srcon.getWidth());
        float scaleHeight = ((float) h) / ((float) srcon.getHeight());
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        this.bmpRotorOn = Bitmap.createBitmap(srcon, 0, 0, srcon.getWidth(), srcon.getHeight(), matrix, true);
        this.ivRotor = new ImageView(context);
        this.ivRotor.setImageBitmap(this.bmpRotorOn);
        LayoutParams lp_ivKnob = new LayoutParams(w, h);
        lp_ivKnob.addRule(13);
        addView(this.ivRotor, lp_ivKnob);
        SetState(this.mState);
        initGestureDetection();
    }

    private float cartesianToPolar(float x, float y) {
        return (float) (-Math.toDegrees(Math.atan2((double) (x - 0.5f), (double) (y - 0.5f))));
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent event) {
        this.mAngleDown = cartesianToPolar(1 - (event.getX() / ((float) getWidth())), 1 - (event.getY() / ((float) getHeight())));
        return true;
    }

    public boolean onSingleTapUp(MotionEvent e) {
        float x = e.getX() / ((float) getWidth());
        float y = e.getY() / ((float) getHeight());
        this.mAngleUp = cartesianToPolar(1 - x, 1 - y);
        float rotDegrees = cartesianToPolar(1 - x, 1 - y);
        if (!Float.isNaN(rotDegrees)) {
            float posDegrees = rotDegrees;
            if (rotDegrees < 0.0f) {
                posDegrees = 360.0f + rotDegrees;
            }
            if (posDegrees > 210 || posDegrees < 150.0f) {
                setRotorPosAngle(posDegrees);
                int percent = Math.round((rotDegrees + 150.0f) / 3.0f);
                if (this.m_listener != null) {
                    this.m_listener.onRotate(percent);
                }
            }
        }
        return true;
    }

    public void setRotorPosAngle(float deg) {
        if (deg >= 210 || deg <= 150.0f) {
            if (deg > 180) {
                deg -= 360.0f;
            }
            Matrix matrix = new Matrix();
            this.ivRotor.setScaleType(ScaleType.MATRIX);
            matrix.postRotate(deg, (float) (this.m_nWidth / 2), (float) (this.m_nHeight / 2));
            this.ivRotor.setImageMatrix(matrix);
        }
    }

    public void setRotorPercentage(int percentage) {
        int posDegree = (percentage * 3) - 150;
        if (posDegree < 0) {
            posDegree += 360;
        }
        setRotorPosAngle((float) posDegree);
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float rotDegrees = cartesianToPolar(1 - (e2.getX() / ((float) getWidth())), 1 - (e2.getY() / ((float) getHeight())));
        if (Float.isNaN(rotDegrees)) {
            return false;
        }
        float posDegrees = rotDegrees;
        if (rotDegrees < 0.0f) {
            posDegrees = 360.0f + rotDegrees;
        }
        if (posDegrees <= 210 && posDegrees >= 150.0f) {
            return false;
        }
        setRotorPosAngle(posDegrees);
        int percent = Math.round((rotDegrees + 150.0f) / 3.0f);
        if (this.m_listener != null) {
            this.m_listener.onRotate(percent);
        }
        return true;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    public void initGestureDetection() {
        this.gestureDetector = new GestureDetector(getContext(), new C05161());
        this.mGestureListener = new C05172();
        setOnTouchListener(this.mGestureListener);
    }
}
