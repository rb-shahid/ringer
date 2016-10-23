package info.ss12.audioalertsystem;

import android.app.*;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.*;
import android.os.Build.VERSION;
import android.app.Service;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Myservice extends Service implements OnSignalsDetectedListener {
    public static final int DETECT_NONE = 0;
    public static final int DETECT_WHISTLE = 1;
    public static Myservice f62m;
    private static MediaPlayer mediaPlayer;
//    public static int selectedDetection;
    private static Vibrator vib;
    private AudioManager am;
    private CallStateListener callStateListener;
    private CountDownTimer cdt5;
    private Context context;
    private DetectorThread detectorThread;
    private String filePath;
//    private GoogleAnalytics mGaInstance;
//    private Tracker mGaTracker;
    private int numWhistleDetected;
    private RecorderThread recorderThread;
    private TelephonyManager tm;
    private String tone;
    private boolean whitle;

    /* renamed from: de.blenyApp.phonefinder.Myservice.1 */
    class C05111 implements Runnable {
        C05111() {
        }

        public void run() {
            Log.i("RUN_ONWHITLE", "RUNNING");
            Myservice myservice = Myservice.this;
            myservice.numWhistleDetected = myservice.numWhistleDetected + Myservice.DETECT_WHISTLE;
            Log.i("ANZAHL", "numWhistle:" + Myservice.this.numWhistleDetected);
            int i = Myservice.this.loadsensitivity();
            int j = Myservice.DETECT_NONE;
            if (Myservice.this.whitle) {
                if (i == Myservice.DETECT_WHISTLE) {
                    j = 14;
                } else if (i == 2) {
                    j = 8;
                } else if (i == 3) {
                    j = Myservice.DETECT_NONE;
                }
            }
            Myservice.this.timerAction(Myservice.this.numWhistleDetected);
            if (Myservice.this.numWhistleDetected >= j && Myservice.mediaPlayer == null) {
                Log.i("RINGING", "BeforeRINGING");
                Myservice.this.numWhistleDetected = Myservice.DETECT_NONE;
                Myservice.this.checkForeground();
                Log.i("RINGING", "AfterRINGING");
            }
        }
    }

    /* renamed from: de.blenyApp.phonefinder.Myservice.2 */
    class C05132 implements Runnable {

        /* renamed from: de.blenyApp.phonefinder.Myservice.2.1 */
        class C05121 extends CountDownTimer {
            C05121(long $anonymous0, long $anonymous1) {
                super($anonymous0, $anonymous1);
            }

            public void onTick(long millisUntilFinished) {
                Log.i("TIMER", "tickt: " + (millisUntilFinished / 1000));
            }

            public void onFinish() {
                Myservice.this.numWhistleDetected = Myservice.DETECT_NONE;
                Log.i("TIMER", "abgelaufen und whistles= " + Myservice.this.numWhistleDetected);
            }
        }

        C05132() {
        }

        public void run() {
            if (Myservice.this.cdt5 != null) {
                Myservice.this.cdt5.cancel();
                Log.i("TIMER", "CANCELED");
            }
            Myservice.this.cdt5 = new C05121(3500, 1000).start();
        }
    }

    class CallStateListener extends PhoneStateListener {
        CallStateListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
/*
            switch (state) {
                case Myservice.DETECT_NONE */
/*0*//*
:
                    Myservice.this.recorderThread.startRecording();
                case Myservice.DETECT_WHISTLE */
/*1*//*
:
                    Myservice.this.recorderThread.stopRecording();
                case 2 */
/*2*//*
:
                    Myservice.this.recorderThread.stopRecording();
                default:
            }
*/
        }
    }

    public Myservice() {
        this.numWhistleDetected = DETECT_NONE;
    }

/*
    static {
        selectedDetection = DETECT_NONE;
    }
*/

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        goHomeView();
        stopPlaying();
        releasePlayer();
        Log.i("TAG", "onDestroy");
        this.tm.listen(this.callStateListener, DETECT_NONE);
//        this.mGaTracker.sendView("ServiceIsStopped");
//        this.mGaTracker.sendEvent("Service", "Stopped", "app", null);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG", "onstart");
        this.tone = loadPref();
        this.filePath = this.tone;
        this.am = (AudioManager) getSystemService("audio");
        f62m = this;
        f62m.notifyForeground2();

        vib = (Vibrator) getSystemService("vibrator");
        this.context = getApplicationContext();
        this.callStateListener = new CallStateListener();
        this.tm = (TelephonyManager) this.context.getSystemService("phone");
        this.tm.listen(this.callStateListener, 32);
        Log.i("TAG", "onCreate");
        loadTypeDec();
        int status1 = whitle ? DETECT_NONE : DETECT_WHISTLE;
        recorderThread = new RecorderThread();
        recorderThread.start();
        detectorThread = new DetectorThread(recorderThread, status1, getApplicationContext(), this);
        detectorThread.start();


//        this.mGaTracker.sendView("ServiceStarted");
//        this.mGaTracker.sendEvent("Service", "Started", "app", null);
        return super.onStartCommand(intent, flags, startId);
    }

    private void starten() {

//        selectedDetection = DETECT_WHISTLE;

        /*if (this.whitle) {
            detectorThread = new DetectorThread(this.recorderThread, DETECT_NONE, this);
            detectorThread.start();
//            detectorThread.setOnSignalsDetectedListener(this);
        } else {*/
//            detectorThread.setOnSignalsDetectedListener(this);
//        }
    }

    private void loadTypeDec() {
        int i = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("TYPDEC", DETECT_NONE);
        if (i == 0) {
            this.whitle = true;
        } else if (i == DETECT_WHISTLE) {
            this.whitle = false;
        }
    }

    private void notifyForeground2() {
        Notification notification = new Notification(R.drawable.logophonefinderneuestenew, "Started...", System.currentTimeMillis());
        notification.setLatestEventInfo(this, getResources().getString(R.string.app_name), "is running...", PendingIntent.getActivity(this, DETECT_NONE, new Intent(this, MyActivity.class), DETECT_NONE));
        startForeground(3, notification);
    }

    private int loadVib() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VIB", DETECT_WHISTLE);
    }

    private int loadVibType() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VIBTYPE", DETECT_WHISTLE);
    }

    private void goHomeView() {
        if (this.recorderThread != null) {
            this.recorderThread.stopRecording();
        }
        if (this.detectorThread != null) {
            this.detectorThread.stopDetection();
        }
//        selectedDetection = DETECT_NONE;
    }

    public void onWhistleDetected() {
        new C05111().run();
    }

    private void timerAction(int i) {
        new Handler(Looper.getMainLooper()).post(new C05132());
    }

    private int loadsensitivity() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("sensTYPE", 2);
    }

    private long[] vibCreation() {
        long[] pattern = new long[5];
        pattern[DETECT_WHISTLE] = (long) 200;
        pattern[2] = (long) 200;
        pattern[3] = (long) 200;
        pattern[4] = (long) 1000;
        long[] pattern2 = new long[5];
        pattern2[DETECT_WHISTLE] = (long) 500;
        pattern2[2] = (long) 200;
        pattern2[3] = (long) 500;
        pattern2[4] = (long) 1000;
        long[] pattern3 = new long[5];
        pattern3[DETECT_WHISTLE] = (long) 200;
        pattern3[2] = (long) 1000;
        pattern3[3] = (long) 500;
        pattern3[4] = (long) 1000;
        long[] pattern4 = new long[5];
        pattern4[DETECT_WHISTLE] = (long) 200;
        pattern4[2] = (long) 1000;
        pattern4[3] = (long) 1000;
        pattern4[4] = (long) 500;
        int i = loadVibType();
        if (i == DETECT_WHISTLE) {
            return pattern;
        }
        if (i == 2) {
            return pattern2;
        }
        if (i == 3) {
            return pattern3;
        }
        if (i == 4) {
            return pattern4;
        }
        return null;
    }

    public void ring() {
        mediaPlayer = new MediaPlayer();
        this.tone = loadPref();
        this.filePath = this.tone;
        try {
            if (this.filePath.equals("No one selected")) {
                Toast.makeText(this, getString(R.string.choose_music), DETECT_NONE).show();
                return;
            }
            FileInputStream inputStream = new FileInputStream(new File(this.filePath));
            mediaPlayer.setDataSource(inputStream.getFD());
            inputStream.close();
            this.am.setStreamVolume(3, getVolume(), DETECT_NONE);
            if (loadTrackLoop() == 0) {
                mediaPlayer.setLooping(true);
            } else {
                mediaPlayer.setLooping(false);
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
            if (loadVib() == 0 && mediaPlayer.isPlaying()) {
                vib.vibrate(vibCreation(), DETECT_NONE);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e2) {
            e2.printStackTrace();
        } catch (IllegalStateException e3) {
            e3.printStackTrace();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    private int getVolume() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VOLUME", 8);
    }

    public String loadPref() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("TONE", "No one selected");
    }

    private int loadVolumeBefore() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VBEFORE", 5);
    }

    public void stopPlaying() {
        if (mediaPlayer != null) {
            this.am.setStreamVolume(3, loadVolumeBefore(), DETECT_NONE);
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        if (vib != null) {
            vib.cancel();
        }
    }

    public void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void staticstopreale() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (vib != null) {
            vib.cancel();
        }
    }

    private void typeNotification() {
        if (VERSION.SDK_INT >= 11) {
            notifyUser();
        } else {
            notifyUserUnder();
        }
    }

    private void notifyUser() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService("notification");
        Notification notification = new Notification(R.drawable.logophonefinderneuestenew, getResources().getString(R.string.notification_text), System.currentTimeMillis());
        notification.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), getResources().getString(R.string.notification_text), PendingIntent.getActivity(this, DETECT_NONE, new Intent(getApplicationContext(), MyActivity.class), 335544320));
        notification.flags = 16;
        mNotificationManager.notify(DETECT_WHISTLE, notification);
    }

    private void notifyUserUnder() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService("notification");
        Notification notification = new Notification(R.drawable.logophonefinderneuestenew, getResources().getString(R.string.notification_text), System.currentTimeMillis());
        notification.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), getResources().getString(R.string.notification_text), PendingIntent.getActivity(this, DETECT_NONE, new Intent(getApplicationContext(), MyActivity.class), 335544320));
        notification.flags = 16;
        mNotificationManager.notify(DETECT_WHISTLE, notification);
    }

    private void checkForeground() {
        boolean isActivityFound = false;
        if (((RunningTaskInfo) ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningTasks(Integer.MAX_VALUE).get(DETECT_NONE)).topActivity.getPackageName().toString().equalsIgnoreCase(getPackageName().toString())) {
            isActivityFound = true;
        }
        if (isActivityFound) {
            ring();
            Log.i("ACTIVITY", "g\u00f6ffnet! DIALOG muss kommen!");
            Intent intent = new Intent(getApplicationContext(), StopSoundDialog.class);
            intent.addFlags(268435456);
            startActivity(intent);
            return;
        }
        ring();
        typeNotification();
    }

    private int loadTrackLoop() {
        int loop = PreferenceManager.getDefaultSharedPreferences(this).getInt("WDH", DETECT_WHISTLE);
        if (loop != DETECT_WHISTLE && loop == 0) {
            return DETECT_NONE;
        }
        return DETECT_WHISTLE;
    }
}
