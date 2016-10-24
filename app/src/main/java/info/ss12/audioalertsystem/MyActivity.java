package info.ss12.audioalertsystem;

import android.app.*;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Process;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import info.ss12.audioalertsystem.roundknob.RoundKnobButton;
import info.ss12.audioalertsystem.roundknob.Singleton;
import info.ss12.audioalertsystem.scrollview.LockablescrollView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements OnSignalsDetectedListener {
    public static final int DETECT_NONE = 0;
    protected static final int SELECT_TONE = 1;
    public static int interAd;
    public static int interAdone;
    public static MyActivity mainApp;
    private static MediaPlayer mediaPlayer;
    public static int selectedDetection;
    static int vibn1;
    static int vibn2;
    static int vibn3;
    static int vibn4;
    private LinearLayout f60a;
    private AudioManager am;
    Animation animFadeIn;
    Animation animFadeOut;
    private CountDownTimer f61c;
    private String chooseMusic;
    private TextView chooseTone;
    private TextView chosenTone;
    private List<String> countries;
    private int curVolume;
    private boolean detectingOnOff;
    private LinearLayout layout;
    private boolean loopTrack;
    private CheckBox looping;
    Singleton m_Inst;
    private MyActivity main;
    private int maxVolume;
    private int numWhistleDetected;
    private String path;
    private ImageView preview_sound;
    private boolean preview_sound_playing;
    private RadioButton radioClap;
    private RadioButton radioWhistle;
    RoundKnobButton rv;
    private RadioButton sensitivity1;
    private RadioButton sensitivity2;
    private RadioButton sensitivity3;
    private RadioGroup sensitivityType;
    private int settedVolume;
    private boolean showsensitivity;
    private ToggleButton togbut;
    private Vibrator vib;
    private RadioButton vib1;
    private RadioButton vib2;
    private RadioButton vib3;
    private RadioButton vib4;
    private RadioGroup vibType;
    private RadioGroup rgServiceToggle;
    private RadioButton rbServiceToggleOn;
    private RadioButton rbServiceToggleOff;
    private ToggleButton tbVibrate;
    private ImageView fbShareButton;
    private int volumeBefore;
    private SeekBar seekbarVolume;

//    class C04911 implements OnClickListener {
//        private final /* synthetic */ TextView f51val$istsoundAugewhlt;
//
//        C04911(TextView textView) {
//            this.f51val$istsoundAugewhlt = textView;
//        }
//
//        public void onClick(View v) {
//            if (this.f51val$istsoundAugewhlt.getText().toString().equals(MyActivity.this.chooseMusic)) {
//                MyActivity.this.giveHint();
//                MyActivity.this.togbut.setChecked(false);
//            } else if (MyActivity.this.detectingOnOff) {
//                MyActivity.this.detectingOnOff = false;
//                MyActivity.this.togbut.setChecked(false);
//                MyActivity.this.savePref("DETECTION", MyActivity.DETECT_NONE);
//               /* if (!(MyActivity.this.recorderThread == null || MyActivity.this.detectorThread == null)) {
//                    MyActivity.this.recorderThread.stopRecording();
//                    Log.i("stop on click", "stopped recording");
//                }*/
//                Intent intent1 = new Intent(MyActivity.this, Myservice.class);
//                stopService(intent1);
////                MyActivity.this.displayInterstitial();
//            } else if (!MyActivity.this.detectingOnOff) {
//                MyActivity.this.detectingOnOff = true;
//                MyActivity.this.togbut.setChecked(true);
////                startDetecting();
//                Intent intent = new Intent(MyActivity.this, Myservice.class);
//                MyActivity.this.startService(intent);
//            }
//        }
//    }

    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.2 */
    class C04922 implements OnClickListener {
        C04922() {
        }

        public void onClick(View v) {
            MyActivity.this.startActivityForResult(Intent.createChooser(new Intent(
                    "android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI),
                    MyActivity.this.getResources().getText(R.string.selectAudio)),
                    MyActivity.SELECT_TONE);
//            MyActivity.this.displayInterstitial();
        }
    }

    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.3 */
    class C04933 extends CountDownTimer {
        C04933(long $anonymous0, long $anonymous1) {
            super($anonymous0, $anonymous1);
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            MyActivity.this.stopPlaying();
            MyActivity.this.releasePlayer();
        }
    }

    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.4 */
    class C04944 implements OnClickListener {
        C04944() {
        }

        public void onClick(View v) {
            if (MyActivity.this.preview_sound_playing) {
                MyActivity.this.preview_sound_playing = false;
                MyActivity.this.preview_sound.setBackgroundResource(R.drawable.preview_off_bg);
                MyActivity.this.stopPreview();
                MyActivity.this.f61c.cancel();
                MyActivity.this.f61c.onFinish();
            } else {
                MyActivity.this.preview_sound_playing = true;
                MyActivity.this.preview_sound.setBackgroundResource(R.drawable.preview_on_bg);
                MyActivity.this.ringPreview();
                MyActivity.this.f61c.start();
            }
//            MyActivity.this.displayInterstitial();
        }
    }

    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.5 */
    class C04955 implements OnClickListener {
        C04955() {
        }

        public void onClick(View v) {
            if (MyActivity.this.looping.isChecked()) {
                MyActivity.this.loopTrack = true;
                MyActivity.this.savePref("WDH", MyActivity.DETECT_NONE);
            } else {
                MyActivity.this.loopTrack = false;
                MyActivity.this.savePref("WDH", MyActivity.SELECT_TONE);
            }
//            MyActivity.this.displayInterstitial();
        }
    }
//
//    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.6 */
//    class C04966 implements OnClickListener {
//        C04966() {
//        }
//
//        public void onClick(View v) {
//            if (MyActivity.this.vibrate.isChecked()) {
//                MyActivity.this.savePref("VIB", MyActivity.DETECT_NONE);
//            } else {
//                MyActivity.this.savePref("VIB", MyActivity.SELECT_TONE);
//            }
////            MyActivity.this.displayInterstitial();
//        }
//    }

    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.7 */
    class C04977 implements OnClickListener {
        C04977() {
        }

        public void onClick(View v) {
            if (MyActivity.this.vib1.isChecked()) {
                MyActivity.this.savePref("VIBTYPE", MyActivity.SELECT_TONE);
                if (MyActivity.vibn1 == 0) {
                    MyActivity.vibn1 = MyActivity.SELECT_TONE;
                    long[] pattern = new long[5];
                    pattern[MyActivity.SELECT_TONE] = 200;
                    pattern[2] = 200;
                    pattern[3] = 200;
                    pattern[4] = 1000;
                    MyActivity.this.vib.vibrate(pattern, -1);
                } else if (MyActivity.vibn1 == MyActivity.SELECT_TONE) {
                    MyActivity.this.vib.cancel();
                    MyActivity.vibn1 = MyActivity.DETECT_NONE;
                }
            }
//            MyActivity.this.displayInterstitial();
        }
    }

    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.8 */
    class C04988 implements OnClickListener {
        C04988() {
        }

        public void onClick(View v) {
            if (MyActivity.this.vib2.isChecked()) {
                MyActivity.this.savePref("VIBTYPE", 2);
                if (MyActivity.vibn2 == 0) {
                    MyActivity.vibn2 = MyActivity.SELECT_TONE;
                    long[] pattern = new long[5];
                    pattern[MyActivity.SELECT_TONE] = 500;
                    pattern[2] = 200;
                    pattern[3] = 500;
                    pattern[4] = 1000;
                    MyActivity.this.vib.vibrate(pattern, -1);
                } else if (MyActivity.vibn2 == MyActivity.SELECT_TONE) {
                    MyActivity.this.vib.cancel();
                    MyActivity.vibn2 = MyActivity.DETECT_NONE;
                }
            }
//            MyActivity.this.displayInterstitial();
        }
    }

    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.9 */
    class C04999 implements OnClickListener {
        C04999() {
        }

        public void onClick(View v) {
            if (MyActivity.this.vib3.isChecked()) {
                MyActivity.this.savePref("VIBTYPE", 3);
                if (MyActivity.vibn3 == 0) {
                    MyActivity.vibn3 = MyActivity.SELECT_TONE;
                    long[] pattern = new long[5];
                    pattern[MyActivity.SELECT_TONE] = 200;
                    pattern[2] = 1000;
                    pattern[3] = 500;
                    pattern[4] = 1000;
                    MyActivity.this.vib.vibrate(pattern, -1);
                } else if (MyActivity.vibn3 == MyActivity.SELECT_TONE) {
                    MyActivity.this.vib.cancel();
                    MyActivity.vibn3 = MyActivity.DETECT_NONE;
                }
            }
//            MyActivity.this.displayInterstitial();
        }
    }

    /* renamed from: de.blenyApp.musicg.android.phonefinder.MyActivity.21 */
//    class AnonymousClass21 implements RoundKnobButton.RoundKnobButtonListener {
//        private final /* synthetic */ Toast val$to;
//
//        AnonymousClass21(Toast toast) {
//            this.val$to = toast;
//        }
//
//        public void onStateChange(boolean newstate) {
////            if (newstate) {
////                MyActivity.this.disablescrolling();
////            } else {
////                MyActivity.this.enablescrolling();
////            }
//        }
////
////        public void onRotate(int percentage) {
//////            MyActivity.this.disablescrolling();
////            MyActivity.this.savePref("VOLPERCENT", percentage);
////            int j = MyActivity.this.am.getStreamMaxVolume(3);
////            Log.i("j", "J: " + j);
////            double u = ((double) j) / 100.0d;
////            Log.i("u", "u: " + u);
////            double z = u * ((double) percentage);
//////            Log.i(ModelFields.CACHE_BUSTER, "z: " + z);
////            MyActivity.this.curVolume = (int) Math.round(z);
////            Log.i("rounded", "curVol: " + MyActivity.this.curVolume);
////            MyActivity.this.settedVolume = MyActivity.this.curVolume;
////            MyActivity.this.am.setStreamVolume(3, MyActivity.this.settedVolume, MyActivity.DETECT_NONE);
////            MyActivity.this.saveVolume("VOLUME", MyActivity.this.settedVolume);
////            MyActivity.this.am.setStreamVolume(3, MyActivity.this.volumeBefore, MyActivity.DETECT_NONE);
////            this.val$to.setText(percentage + " %");
////            this.val$to.show();
////        }
//    }

    public void onRotate(int percentage) {
//            MyActivity.this.disablescrolling();
        MyActivity.this.savePref("VOLPERCENT", percentage);
        int j = MyActivity.this.am.getStreamMaxVolume(3);
        Log.i("j", "J: " + j);
        double u = ((double) j) / 100.0d;
        Log.i("u", "u: " + u);
        double z = u * ((double) percentage);
//            Log.i(ModelFields.CACHE_BUSTER, "z: " + z);
        MyActivity.this.curVolume = (int) Math.round(z);
        Log.i("rounded", "curVol: " + MyActivity.this.curVolume);
        MyActivity.this.settedVolume = MyActivity.this.curVolume;
        MyActivity.this.am.setStreamVolume(3, MyActivity.this.settedVolume, MyActivity.DETECT_NONE);
        MyActivity.this.saveVolume("VOLUME", MyActivity.this.settedVolume);
        MyActivity.this.am.setStreamVolume(3, MyActivity.this.volumeBefore, MyActivity.DETECT_NONE);
//        this.val$to.setText(percentage + " %");
//        this.val$to.show();
    }

    static {
        interAd = 3;
        interAdone = SELECT_TONE;
        selectedDetection = DETECT_NONE;
        vibn1 = DETECT_NONE;
        vibn2 = DETECT_NONE;
        vibn3 = DETECT_NONE;
        vibn4 = DETECT_NONE;
    }

    public MyActivity() {
        this.numWhistleDetected = DETECT_NONE;
        this.detectingOnOff = false;
        this.preview_sound_playing = false;
        this.m_Inst = Singleton.getInstance();
        this.main = this;
    }

    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        MyActivity.this.loopTrack = true;
        MyActivity.this.savePref("WDH", MyActivity.DETECT_NONE);
//        stopService(new Intent(getBaseContext(), Myservice.class));
        if (VERSION.SDK_INT >= 11) {
            super.setTheme(16973931);
        } else {
            super.setTheme(16973832);
        }
        setContentView(R.layout.new_design);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(SELECT_TONE);
        } else if (getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(DETECT_NONE);
        }
        this.chooseMusic = getResources().getString(R.string.choose_music);
//        intersitialAd();
//        adLoading2();
        loadPref();
//        loadTrackLoop();
        stopPreview();
        mainApp = this;
        mediaPlayer = null;
        final TextView istsoundAugewählt = (TextView) findViewById(R.id.textView2);
//        this.togbut = (ToggleButton) findViewById(R.id.chkState);
//        this.togbut.setOnClickListener(new C04911(istsoundAugewählt));

        this.rgServiceToggle = (RadioGroup) findViewById(R.id.rg_service_toggle);
        this.rbServiceToggleOn = (RadioButton) findViewById(R.id.rb_service_toggle_on);
        this.rbServiceToggleOff = (RadioButton) findViewById(R.id.rb_service_toggle_off);
        this.fbShareButton = (ImageView) findViewById(R.id.fb_share);

        this.fbShareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MyActivity.this);
                alertDialogBuilder.setTitle("Facebook");
                alertDialogBuilder
                        .setMessage("Want to share this app on Facebook?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                shareAppLinkViaFacebook();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        this.rbServiceToggleOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (istsoundAugewählt.getText().toString().equals(MyActivity.this.chooseMusic)) {
                    MyActivity.this.giveHint();
                    MyActivity.this.rbServiceToggleOff.setChecked(true);
                } else if (!MyActivity.this.detectingOnOff) {
                    MyActivity.this.detectingOnOff = true;
                    MyActivity.this.rbServiceToggleOn.setChecked(true);
//                startDetecting();
                    Intent intent = new Intent(MyActivity.this, Myservice.class);
                    MyActivity.this.startService(intent);
                }
            }
        });

        this.rbServiceToggleOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyActivity.this.detectingOnOff) {
                    MyActivity.this.detectingOnOff = false;
                    MyActivity.this.rbServiceToggleOff.setChecked(true);
                    MyActivity.this.savePref("DETECTION", MyActivity.DETECT_NONE);
               /* if (!(MyActivity.this.recorderThread == null || MyActivity.this.detectorThread == null)) {
                    MyActivity.this.recorderThread.stopRecording();
                    Log.i("stop on click", "stopped recording");
                }*/
                    Intent intent1 = new Intent(MyActivity.this, Myservice.class);
                    stopService(intent1);
//                MyActivity.this.displayInterstitial();
                }
            }
        });

        this.seekbarVolume = (SeekBar) findViewById(R.id.seekBar1);
        seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onRotate(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        checkDetectionOnOff();
//        addRoundknobButton();
        this.am = (AudioManager) getSystemService("audio");
        this.volumeBefore = this.am.getStreamVolume(3);
        saveVolBefore("VBEFORE", this.volumeBefore);
        this.curVolume = this.am.getStreamVolume(3);
        this.maxVolume = this.am.getStreamMaxVolume(3);
        this.am.setStreamVolume(3, this.curVolume, DETECT_NONE);
        this.seekbarVolume.setProgress(this.curVolume * 10);
        this.chooseTone = (TextView) findViewById(R.id.textView1);
        this.chosenTone = (TextView) findViewById(R.id.textView2);
        RelativeLayout toneLayout = (RelativeLayout) findViewById(R.id.RelativeLayout1);
        OnClickListener toneChoosing = new C04922();
        this.chooseTone.setOnClickListener(toneChoosing);
        this.chosenTone.setOnClickListener(toneChoosing);
        this.f61c = new C04933(10000, 100);
//        this.preview_sound = (ImageView) findViewById(R.id.imageView1);
//        this.preview_sound.setBackgroundResource(R.drawable.preview_off_bg);
        OnClickListener onOrOff = new C04944();
//        this.preview_sound.setOnClickListener(onOrOff);
//        ((TextView) findViewById(R.id.textView4)).setOnClickListener(onOrOff);
//        this.looping = (CheckBox) findViewById(R.id.checkBox1);
//        this.looping.setOnClickListener(new C04955());
        this.tbVibrate = (ToggleButton) findViewById(R.id.switch_vibration);
        this.tbVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        MyActivity.this.savePref("VIB", MyActivity.DETECT_NONE);
                    } else {
                        MyActivity.this.savePref("VIB", MyActivity.SELECT_TONE);
                    }
            }
        });
        this.vib = (Vibrator) getSystemService("vibrator");
//        this.vibType = (RadioGroup) findViewById(R.id.radioGroup1);
//        this.vib1 = (RadioButton) findViewById(R.id.radio11);
//        this.vib2 = (RadioButton) findViewById(R.id.radio12);
//        this.vib3 = (RadioButton) findViewById(R.id.radio13);
//        this.vib4 = (RadioButton) findViewById(R.id.radio14);
//        this.vib1.setOnClickListener(new C04977());
//        this.vib2.setOnClickListener(new C04988());
//        this.vib3.setOnClickListener(new C04999());
//        this.vib4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (MyActivity.this.vib4.isChecked()) {
//                    MyActivity.this.savePref("VIBTYPE", 4);
//                    if (MyActivity.vibn4 == 0) {
//                        MyActivity.vibn4 = MyActivity.SELECT_TONE;
//                        long[] pattern = new long[5];
//                        pattern[MyActivity.SELECT_TONE] = 200;
//                        pattern[2] = 1000;
//                        pattern[3] = 1000;
//                        pattern[4] = 500;
//                        MyActivity.this.vib.vibrate(pattern, -1);
//                    } else if (MyActivity.vibn4 == MyActivity.SELECT_TONE) {
//                        MyActivity.this.vib.cancel();
//                        MyActivity.vibn4 = MyActivity.DETECT_NONE;
//                    }
//                }
////                MyActivity.this.displayInterstitial();
//            }
//        });
        this.sensitivityType = (RadioGroup) findViewById(R.id.radioGroup2);
        this.sensitivity1 = (RadioButton) findViewById(R.id.radio21);
        this.sensitivity2 = (RadioButton) findViewById(R.id.radio22);
        this.sensitivity3 = (RadioButton) findViewById(R.id.radio23);
        this.sensitivity1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyActivity.this.sensitivity1.isChecked()) {
                    MyActivity.this.savePref("sensTYPE", MyActivity.SELECT_TONE);
                }
//                MyActivity.this.displayInterstitial();
            }
        });
        this.sensitivity2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyActivity.this.sensitivity2.isChecked()) {
                    MyActivity.this.savePref("sensTYPE", 2);
                }
//                MyActivity.this.displayInterstitial();
            }
        });
        this.sensitivity3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyActivity.this.sensitivity3.isChecked()) {
                    MyActivity.this.savePref("sensTYPE", 3);
                }
//                MyActivity.this.displayInterstitial();
            }
        });
        this.radioWhistle = (RadioButton) findViewById(R.id.radio31);
        this.radioClap = (RadioButton) findViewById(R.id.radio32);
        this.animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        this.animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        this.radioWhistle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyActivity.this.radioWhistle.isChecked()) {
                    MyActivity.this.savePref("TYPDEC", MyActivity.DETECT_NONE);
//                    MyActivity.this.giveHintOffOn();
                    LinearLayout r = (LinearLayout) MyActivity.this.findViewById(R.id.LinearLayout5);
                    if (!MyActivity.this.showsensitivity) {
                        r.startAnimation(MyActivity.this.animFadeIn);
                    }
                    MyActivity.this.showsensitivity = true;
                }
//                MyActivity.this.displayInterstitial();
            }
        });
        this.radioClap.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyActivity.this.radioClap.isChecked()) {
                    MyActivity.this.savePref("TYPDEC", MyActivity.SELECT_TONE);
//                    MyActivity.this.giveHintOffOn();
                    LinearLayout r = (LinearLayout) MyActivity.this.findViewById(R.id.LinearLayout5);
                    if (MyActivity.this.showsensitivity) {
                        r.startAnimation(MyActivity.this.animFadeOut);
                    }
                    MyActivity.this.showsensitivity = false;
                }
//                MyActivity.this.displayInterstitial();
            }
        });
        loadVibCheckbox();
//        loadVibTypeBox();
        loadTypeDec();
        loadsensitivity();
        loadVolume();
//        AppRater.app_launched(this);
        this.countries = new ArrayList();
        this.countries.add("BE");
        this.countries.add("BG");
        this.countries.add("DK");
        this.countries.add("DE");
        this.countries.add("EE");
        this.countries.add("FI");
        this.countries.add("FR");
        this.countries.add("GR");
        this.countries.add("IE");
        this.countries.add("IT");
        this.countries.add("HR");
        this.countries.add("LV");
        this.countries.add("LT");
        this.countries.add("LU");
        this.countries.add("MT");
        this.countries.add("NL");
        this.countries.add("AT");
        this.countries.add("PL");
        this.countries.add("PT");
        this.countries.add("RO");
        this.countries.add("SE");
        this.countries.add("SK");
        this.countries.add("SI");
        this.countries.add("ES");
        this.countries.add("CZ");
        this.countries.add("HU");
        this.countries.add("GB");
        this.countries.add("CY");
        checkCountry();
    }

    private void checkCountry() {
        String locale = getResources().getConfiguration().locale.getCountry();
        if (locale == null) {
            return;
        }
        if (this.countries.contains(locale)) {
//            cookieDialog();
            Log.i("DIALOG", "wird aufgerufen");
            return;
        }
        Log.i("DIALOG", "nicht aufgerufen");
    }

    protected void giveHint() {
        Toast.makeText(this, getString(R.string.choose_music_file), SELECT_TONE).show();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    private void saveVolBefore(String key, int l) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putInt(key, l);
        edit.commit();
    }

    private void loadVibCheckbox() {
        int i = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VIB", SELECT_TONE);
        if (i == 0) {
            this.tbVibrate.setChecked(true);
        } else if (i == SELECT_TONE) {
            this.tbVibrate.setChecked(false);
        }
    }

//    private void loadVibTypeBox() {
//        int i = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VIBTYPE", DETECT_NONE);
//        if (i == 0) {
//            this.vib1.setChecked(true);
//        } else if (i == SELECT_TONE) {
//            this.vib1.setChecked(true);
//        } else if (i == 2) {
//            this.vib2.setChecked(true);
//        } else if (i == 3) {
//            this.vib3.setChecked(true);
//        }
//    }

    private void loadsensitivity() {
        int i = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("sensTYPE", 2);
        if (i == SELECT_TONE) {
            this.sensitivity1.setChecked(true);
        } else if (i == 2) {
            this.sensitivity2.setChecked(true);
        } else if (i == 3) {
            this.sensitivity3.setChecked(true);
        }
    }

    private void loadTypeDec() {
        int i = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("TYPDEC", DETECT_NONE);
        LinearLayout r = (LinearLayout) findViewById(R.id.LinearLayout5);
        this.animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        this.animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        if (i == 0) {
            this.radioWhistle.setChecked(true);
            r.setVisibility(DETECT_NONE);
            this.showsensitivity = true;
        } else if (i == SELECT_TONE) {
            this.radioClap.setChecked(true);
            this.showsensitivity = false;
            if (!this.showsensitivity) {
                r.startAnimation(this.animFadeOut);
            }
        }
    }

    private void savePref(String key, int l) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putInt(key, l);
        edit.commit();
    }

    private void saveVolume(String key, int l) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putInt(key, l);
        edit.commit();
    }

    private void shareAppLinkViaFacebook() {
        String urlToShare = "http://www.coucouring.com/";

        try {
            Intent intent1 = new Intent();
            intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
            intent1.setAction("android.intent.action.SEND");
            intent1.setType("text/plain");
            intent1.putExtra("android.intent.extra.TEXT", urlToShare);
            startActivity(intent1);
        } catch (Exception e) {
            // If we failed (not native FB app installed), try share through SEND
            Intent intent = new Intent(Intent.ACTION_SEND);
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(intent);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_TONE /*1*/:
                if (resultCode == -1) {
                    Uri uri = data.getData();
                    new File(String.valueOf(uri)).getName();
                    this.path = getRealPathFromURI2(this, uri);
                    saveRingtone("TONE", this.path);
                    this.chosenTone.setText(getFileNameFromURI2(this, Uri.parse(this.path)));
                    break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI2(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = new String[SELECT_TONE];
            proj[DETECT_NONE] = "_data";
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            String string = cursor.getString(column_index);
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getFileNameFromURI2(Context context, Uri contentUri) {
        Ringtone ringtone = RingtoneManager.getRingtone(context, contentUri);
        if (ringtone != null) {
            return ringtone.getTitle(context);
        }
        return this.chooseMusic;
    }

    private void saveRingtone(String key, String l) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString(key, l);
        edit.commit();
    }

    public void loadPref() {
        this.chosenTone = (TextView) findViewById(R.id.textView2);
        String tone = PreferenceManager.getDefaultSharedPreferences(this).getString("TONE", this.chooseMusic);
        if (!tone.equals(this.chooseMusic)) {
            tone = getFileNameFromURI2(this, Uri.parse(tone));
            this.chosenTone.setText(tone);
        }
        this.chosenTone.setText(tone);
    }

    public String loadPrefPlay() {
        this.chosenTone = (TextView) findViewById(R.id.textView2);
        return PreferenceManager.getDefaultSharedPreferences(this).getString("TONE", this.chooseMusic);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(DETECT_NONE, DETECT_NONE, DETECT_NONE, getString(R.string.title_activity_about)).setIcon(R.drawable.actionabout);
        menu.add(DETECT_NONE, SELECT_TONE, SELECT_TONE, getString(R.string.quit)).setIcon(R.drawable.contentremov);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DETECT_NONE /*0*/:
                startActivity(new Intent(getApplicationContext(), AboutActivityNew.class));
//                displayInterstitial();
                Log.i("ABOUT", "kkkkkkkkkkkk");
                break;
            case SELECT_TONE /*1*/:
                stopService(new Intent(getBaseContext(), Myservice.class));
                savePref("DETECTION", DETECT_NONE);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.rbServiceToggleOn.isChecked()) {
            moveTaskToBack(true);
            this.am.setStreamVolume(3, loadVolumeBefore2(), DETECT_NONE);
            return true;
        }
        stopService(new Intent(getBaseContext(), Myservice.class));
        this.am.setStreamVolume(3, loadVolumeBefore2(), DETECT_NONE);
        savePref("DETECTION", DETECT_NONE);
        finish();
        return true;
    }

    private int loadVolumeBefore2() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VBEFORE", this.volumeBefore);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.am.setStreamVolume(3, this.volumeBefore, DETECT_NONE);
        if (this.layout != null) {
            this.layout.removeAllViews();
        }
        Process.killProcess(Process.myPid());
    }

    public void finish() {
        super.finish();
    }

    public void onWhistleDetected() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (MyActivity.this.numWhistleDetected >= 0 && MyActivity.mediaPlayer == null) {
                    Log.i("vvvvvvvv", "rjwpwjvpiw");
                }
            }
        });
    }

    private void loadVolume() {
        SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        int i = prf.getInt("VOLUME", this.curVolume) * 10;
        this.seekbarVolume.setProgress(prf.getInt("VOLPERCENT", 50));
    }

    private int getVolume() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VOLUME", this.curVolume);
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            this.am.setStreamVolume(3, this.volumeBefore, DETECT_NONE);
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        this.preview_sound = (ImageView) findViewById(R.id.imageView1);
        if (this.preview_sound_playing) {
            this.preview_sound_playing = false;
            this.preview_sound.setBackgroundResource(R.drawable.preview_off_bg);
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void loadTrackLoop() {
        this.looping = (CheckBox) findViewById(R.id.checkBox1);
        int loop = PreferenceManager.getDefaultSharedPreferences(this).getInt("WDH", SELECT_TONE);
        if (loop == SELECT_TONE) {
            this.loopTrack = false;
            this.looping.setChecked(false);
        } else if (loop == 0) {
            this.loopTrack = true;
            this.looping.setChecked(true);
        } else {
            this.loopTrack = false;
            this.looping.setChecked(false);
        }
    }

    private void ringPreview() {
        if (mediaPlayer == null) {
            try {
                this.chosenTone = (TextView) findViewById(R.id.textView2);
                String filePath = loadPrefPlay();
                mediaPlayer = new MediaPlayer();
                if (filePath.equals(this.chooseMusic)) {
                    Toast.makeText(this, getString(R.string.choose_music_file), DETECT_NONE).show();
                    return;
                }
                FileInputStream inputStream = new FileInputStream(new File(filePath));
                mediaPlayer.setDataSource(inputStream.getFD());
                inputStream.close();
                this.am.setStreamVolume(3, getVolume(), DETECT_NONE);
                checkForeground();
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
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
    }

    private void stopPreview() {
        stopPlaying();
        releasePlayer();
    }

    private void checkDetectionOnOff() {
        if (this.rbServiceToggleOn.isChecked()) {
            this.detectingOnOff = true;
        } else {
            this.detectingOnOff = false;
        }
    }

    private void typeNotification() {
        if (VERSION.SDK_INT < 16) {
            tetNoti();
        }
    }

    private void tetNoti() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.logophonefinderneuestenew).setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.notification_text)).setTicker(getString(R.string.notification_text));
        Intent resultIntent = new Intent(this, MyActivity.class);
        resultIntent.setFlags(603979776);
        resultIntent.putExtra("HE", "method");
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(PendingIntent.getActivity(this, DETECT_NONE, resultIntent, 268435456));
        ((NotificationManager) getSystemService("notification")).notify(23119, mBuilder.build());
    }

    private void checkForeground() {
        boolean isActivityFound = false;
        if (((RunningTaskInfo) ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningTasks(Integer.MAX_VALUE).get(DETECT_NONE)).topActivity.getPackageName().toString().equalsIgnoreCase(getPackageName().toString())) {
            isActivityFound = true;
        }
        if (!isActivityFound) {
            typeNotification();
        }
    }

    protected void onPause() {
        super.onPause();
        this.am.setStreamVolume(3, loadVolumeBefore2(), DETECT_NONE);
        checkDetectionOnOff();
        Log.i("OnPaue", "paued");
    }

    protected void onStop() {
        Log.i("OnTOP", "getoppt");
        if (this.layout != null) {
            this.layout.removeAllViews();
        }
        this.am.setStreamVolume(3, loadVolumeBefore2(), DETECT_NONE);
        checkDetectionOnOff();
        super.onStop();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Myservice.staticstopreale();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
    }

//    private void enablescrolling() {
//        ((LockablescrollView) findViewById(R.id.scrol)).setScrollingEnabled(true);
//    }
//
//    private void disablescrolling() {
//        ((LockablescrollView) findViewById(R.id.scrol)).setScrollingEnabled(false);
//    }

//    private void addRoundknobButton() {
//        this.m_Inst.InitGUIFrame(this);
//        RelativeLayout ll = (RelativeLayout) findViewById(R.id.RelativeLayout2);
//        SeekBar sb = (SeekBar) findViewById(R.id.seekBar1);
//        if (sb != null) {
//            ll.removeView(sb);
//        }
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
//        this.rv = new RoundKnobButton(this, R.drawable.volume_stator, R.drawable.volume_rotor, this.m_Inst.Scale(150), this.m_Inst.Scale(150));
//        lp = new RelativeLayout.LayoutParams(-2, -2);
//        lp.addRule(14);
//        lp.addRule(3, R.id.textView3);
//        lp.setMargins(DETECT_NONE, 5, DETECT_NONE, DETECT_NONE);
//        ll.addView(this.rv, lp);
//        RelativeLayout lt = (RelativeLayout) findViewById(R.id.relTop);
//        TextView tv2 = new TextView(this);
//        tv2.setText("");
//        lp = new RelativeLayout.LayoutParams(-2, -2);
//        lp.addRule(14);
//        lp.addRule(12);
//        lt.addView(tv2, lp);
//        Toast to = Toast.makeText(this, "", SELECT_TONE);
//        this.rv.setRotorPercentage(100);
//        this.rv.SetListener(new AnonymousClass21(to));
//    }
}
