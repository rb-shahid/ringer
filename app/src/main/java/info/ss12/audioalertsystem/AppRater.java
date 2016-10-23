package info.ss12.audioalertsystem;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;

public class AppRater {
    private static final int DAYS_UNTIL_PROMPT = 0;
    private static final int LAUNCHES_UNTIL_PROMPT = 5;
//    private static GoogleAnalytics mGaInstance;
//    private static Tracker mGaTracker;

    /* renamed from: de.blenyApp.phonefinder.AppRater.1 */
    static class C05071 implements OnClickListener {
        private final /* synthetic */ Dialog val$dialog;
        private final /* synthetic */ Editor val$editor;
        private final /* synthetic */ Context val$mContext;

        C05071(Context context, Editor editor, Dialog dialog) {
            this.val$mContext = context;
            this.val$editor = editor;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            this.val$mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=de.blenyApp.phonefinder")));
            //todo send Event
//            AppRater.mGaTracker.sendEvent("Rated", "true", "app", Long.valueOf(1));
            if (this.val$editor != null) {
                this.val$editor.putBoolean("dontshowagain", true);
                this.val$editor.commit();
            }
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AppRater.2 */
    static class C05082 implements OnClickListener {
        private final /* synthetic */ Dialog val$dialog;

        C05082(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            //todo send event
//            AppRater.mGaTracker.sendEvent("Rated", "later", "app", Long.valueOf(1));
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AppRater.3 */
    static class C05093 implements OnClickListener {
        private final /* synthetic */ Dialog val$dialog;
        private final /* synthetic */ Editor val$editor;

        C05093(Editor editor, Dialog dialog) {
            this.val$editor = editor;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            if (this.val$editor != null) {
                this.val$editor.putBoolean("dontshowagain", true);
                this.val$editor.commit();
            }
            //todo send event
//            AppRater.mGaTracker.sendEvent("Rated", "false", "app", Long.valueOf(1));
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AppRater.4 */
    static class C05104 implements OnClickListener {
        private final /* synthetic */ Dialog val$dialog;
        private final /* synthetic */ Editor val$editor;
        private final /* synthetic */ Context val$mContext;

        C05104(Context context, Editor editor, Dialog dialog) {
            this.val$mContext = context;
            this.val$editor = editor;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            this.val$mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=de.blenyApp.phonefinderpro")));
            //todo send event
//            AppRater.mGaTracker.sendEvent("BUY", "Pro", "app", Long.valueOf(1));
            if (this.val$editor != null) {
                this.val$editor.putBoolean("dontshowagain", true);
                this.val$editor.commit();
            }
            this.val$dialog.dismiss();
        }
    }

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", DAYS_UNTIL_PROMPT);
        if (!prefs.getBoolean("dontshowagain", false)) {
//            mGaInstance = GoogleAnalytics.getInstance(mContext);
//            mGaTracker = mGaInstance.newTracker("UA-42091686-1");
//            mGaTracker.setAnonymizeIp(true);
            Editor editor = prefs.edit();
            long launch_count = prefs.getLong("launch_count", 0) + 1;
            editor.putLong("launch_count", launch_count);
            Long date_firstLaunch = Long.valueOf(prefs.getLong("date_firstlaunch", 0));
            if (date_firstLaunch.longValue() == 0) {
                date_firstLaunch = Long.valueOf(System.currentTimeMillis());
                editor.putLong("date_firstlaunch", date_firstLaunch.longValue());
            }
            if (launch_count >= 5 && System.currentTimeMillis() >= date_firstLaunch.longValue() + 0) {
                showRateDialog(mContext, editor);
                //todo send view
//                mGaTracker.sendView("RateMyAppView");
            }
            editor.apply();
        }
    }

    public static void showRateDialog(Context mContext, Editor editor) {
        Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + mContext.getResources().getString(R.string.app_name));
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(mContext);
        tv.setText(new StringBuilder(String.valueOf(mContext.getResources().getString(R.string.rate1))).append(" ").append(mContext.getResources().getString(R.string.app_name)).append(" ").append(mContext.getResources().getString(R.string.rate2)).toString());
        tv.setWidth(240);
        tv.setPadding(4, DAYS_UNTIL_PROMPT, 4, 10);
        ll.addView(tv);
        Button b1 = new Button(mContext);
        b1.setText(mContext.getResources().getString(R.string.but1));
        b1.setOnClickListener(new C05071(mContext, editor, dialog));
        ll.addView(b1);
        Button b2 = new Button(mContext);
        b2.setText(mContext.getResources().getString(R.string.but2));
        b2.setOnClickListener(new C05082(dialog));
        ll.addView(b2);
        Button b3 = new Button(mContext);
        b3.setText(mContext.getResources().getString(R.string.but3));
        b3.setOnClickListener(new C05093(editor, dialog));
        ll.addView(b3);
        Button b4 = new Button(mContext);
        b4.setText(mContext.getResources().getString(R.string.but4));
        b4.setOnClickListener(new C05104(mContext, editor, dialog));
        ll.addView(b4);
        dialog.setContentView(ll);
        dialog.show();
    }
}
