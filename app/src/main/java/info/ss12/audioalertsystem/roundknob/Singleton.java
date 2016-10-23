package info.ss12.audioalertsystem.roundknob;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class Singleton extends Application {
    private static final String LOG_TAG = "Singleton";
    static final boolean SET_DEBUG = true;
    private static Singleton m_Instance;
    public float m_fFrameS;
    public int m_nFrameH;
    public int m_nFrameW;
    public int m_nPaddingX;
    public int m_nPaddingY;
    public int m_nTotalH;
    public int m_nTotalW;

    public Singleton() {
        this.m_fFrameS = 0.0f;
        this.m_nFrameW = 0;
        this.m_nFrameH = 0;
        this.m_nTotalW = 0;
        this.m_nTotalH = 0;
        this.m_nPaddingX = 0;
        this.m_nPaddingY = 0;
        m_Instance = this;
    }

    public static Singleton getInstance() {
        if (m_Instance == null) {
            synchronized (Singleton.class) {
                if (m_Instance == null) {
                    Singleton singleton = new Singleton();
                }
            }
        }
        return m_Instance;
    }

    public void onCreate() {
        super.onCreate();
    }

    public static void Debug(String tag, String message) {
        Log.d(tag, message);
    }

    public void InitGUIFrame(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.m_nTotalW = dm.widthPixels;
        this.m_nTotalH = dm.heightPixels;
        this.m_fFrameS = ((float) this.m_nTotalW) / 640.0f;
        this.m_nFrameW = this.m_nTotalW;
        this.m_nFrameH = (int) (960.0f * this.m_fFrameS);
        this.m_nPaddingY = 0;
        this.m_nPaddingX = (this.m_nTotalW - this.m_nFrameW) / 2;
        Debug(LOG_TAG, "InitGUIFrame: frame:" + this.m_nFrameW + "x" + this.m_nFrameH + " Scale:" + this.m_fFrameS);
    }

    public int Px2DIP(int value) {
        return (int) (((float) value) * getResources().getDisplayMetrics().density);
    }

    public int Scale(int v) {
        float s = ((float) v) * this.m_fFrameS;
        return ((double) (s - ((float) ((int) s)))) >= 0.5d ? ((int) s) + 1 : (int) s;
    }

    public Bitmap getScaledBitmap(Context context, float scalex, float scaley, int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        Matrix matrix = new Matrix();
        matrix.postScale(scalex, scaley);
        matrix.postRotate(0.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, SET_DEBUG);
    }

    public Drawable getScaledDrawable(Context context, float scalex, float scaley, int id) {
        return new BitmapDrawable(context.getResources(), getScaledBitmap(context, scalex, scaley, id));
    }

    public int GetPercent(int value, int percent) {
        return (percent * value) / 100;
    }

    public int getMediumTextSize() {
        return Scale(16);
    }
}
