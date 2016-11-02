package info.ss12.audioalertsystem;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class StopSoundDialog extends Activity {
    private static MediaPlayer mediaPlayer;
    private static Vibrator vib;
    private AudioManager am;
    private String filePath;
    private String tone;

    /* renamed from: de.blenyApp.phonefinder.StopSoundDialog.1 */
    class C05151 implements OnClickListener {
        C05151() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Myservice.staticstopreale();
            StopSoundDialog.this.stopPlaying();
            StopSoundDialog.this.releasePlayer();
            StopSoundDialog.vib.cancel();
            dialog.dismiss();
            StopSoundDialog.this.finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.am = (AudioManager) getSystemService(AUDIO_SERVICE);
        this.am.setStreamVolume(3, getVolume(), 0);
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        new Builder(this).setCancelable(false).setTitle(getResources().getText(R.string.dia_title)).setMessage(getResources().getText(R.string.dia_message)).setPositiveButton(17039379, new C05151()).setIcon(R.drawable.ic_dialog_preview_gold).show();
    }

    public void ring() {
        mediaPlayer = new MediaPlayer();
        this.tone = loadPref();
        this.filePath = this.tone;
        try {
            if (this.filePath.equals("No one selected")) {
                Toast.makeText(this, getString(R.string.choose_music), Toast.LENGTH_SHORT).show();
                return;
            }
            FileInputStream inputStream = new FileInputStream(new File(this.filePath));
            mediaPlayer.setDataSource(inputStream.getFD());
            inputStream.close();
            this.am.setStreamVolume(3, getVolume(), 0);
            if (loadTrackLoop() == 0) {
                mediaPlayer.setLooping(true);
            } else {
                mediaPlayer.setLooping(false);
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
            if (loadVib() == 0 && mediaPlayer.isPlaying()) {
                vib.vibrate(vibCreation(), 0);
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

    private int loadVib() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VIB", 1);
    }

    private long[] vibCreation() {
        long[] pattern = new long[4];
        pattern[1] = (long) 200;
        pattern[2] = (long) 200;
        pattern[3] = (long) 200;
        long[] pattern2 = new long[4];
        pattern2[1] = (long) 500;
        pattern2[2] = (long) 200;
        pattern2[3] = (long) 500;
        long[] pattern3 = new long[4];
        pattern3[1] = (long) 200;
        pattern3[2] = (long) 1000;
        pattern3[3] = (long) 500;
        long[] pattern4 = new long[5];
        pattern4[1] = (long) 200;
        pattern4[2] = (long) 1000;
        pattern4[3] = (long) 1000;
        pattern4[4] = (long) 500;
        int i = loadVibType();
        if (i == 1) {
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

    private int loadVibType() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VIBTYPE", 1);
    }

    private int loadTrackLoop() {
        int loop = PreferenceManager.getDefaultSharedPreferences(this).getInt("WDH", 1);
        if (loop != 1 && loop == 0) {
            return 0;
        }
        return 1;
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            this.am.setStreamVolume(3, loadVolumeBefore2(), 0);
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private int loadVolumeBefore2() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("VBEFORE", 8);
    }
}
