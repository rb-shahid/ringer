package info.ss12.audioalertsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.ads.AdRequest.Builder;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;

public class AboutActivityNew extends Activity {

//    private AdView ad;
//    private GoogleAnalytics mGaInstance;
//    private Tracker mGaTracker;

    /* renamed from: de.blenyApp.phonefinder.AboutActivityNew.1 */
    class C05001 implements OnClickListener {
        C05001() {
        }

        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(Uri.parse("http://www.apache.org/licenses/LICENSE-2.0"));
            AboutActivityNew.this.startActivity(intent);
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AboutActivityNew.2 */
    class C05012 implements OnClickListener {
        C05012() {
        }

        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(Uri.parse("http://code.google.com/p/musicg/"));
            AboutActivityNew.this.startActivity(intent);
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AboutActivityNew.3 */
    class C05023 implements OnClickListener {
        C05023() {
        }

        public void onClick(View v) {
            //todo send event
//            AboutActivityNew.this.mGaTracker.sendEvent("Bleny App Store", "Link clicked", "app", null);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Bleny+App"));
            AboutActivityNew.this.startActivity(intent);
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AboutActivityNew.4 */
    class C05034 implements OnClickListener {
        C05034() {
        }

        public void onClick(View v) {
            //todo send event
//            AboutActivityNew.this.mGaTracker.sendEvent("Facebook", "Link clicked", "app", null);
            AboutActivityNew.this.startActivity(AboutActivityNew.getOpenFacebookIntent(AboutActivityNew.this.getBaseContext()));
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AboutActivityNew.5 */
    class C05045 implements OnClickListener {
        C05045() {
        }

        public void onClick(View v) {
            //todo send event
//            AboutActivityNew.this.mGaTracker.sendEvent("Twitter", "Link clicked", "app", null);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(Uri.parse("https://twitter.com/AppBleny"));
            AboutActivityNew.this.startActivity(intent);
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AboutActivityNew.6 */
    class C05056 implements OnClickListener {
        C05056() {
        }

        public void onClick(View v) {
            //todo send Event
//            AboutActivityNew.this.mGaTracker.sendEvent("Bleny App Website", "Link clicked", "app", null);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(Uri.parse("https://blenyapp.com"));
            AboutActivityNew.this.startActivity(intent);
        }
    }

    /* renamed from: de.blenyApp.phonefinder.AboutActivityNew.7 */
    class C05067 implements OnClickListener {
        C05067() {
        }

        public void onClick(View v) {
            //todo send Event
//            AboutActivityNew.this.mGaTracker.sendEvent("Bleny App Website", "Link clicked", "app", null);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(Uri.parse("https://blenyapp.com"));
            AboutActivityNew.this.startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (VERSION.SDK_INT >= 11) {
            super.setTheme(16973931);
        } else {
            super.setTheme(16973832);
        }*/
        setContentView(R.layout.activity_about_new);
//        adLoading();
//        this.mGaInstance = GoogleAnalytics.getInstance(this);
//        this.mGaTracker = this.mGaInstance.newTracker("UA-42091686-1");
//        this.mGaTracker.setAnonymizeIp(true);
        TextView musicgWeb = (TextView) findViewById(R.id.textView1046);
        ImageView bleny = (ImageView) findViewById(R.id.playstore1);
        ((TextView) findViewById(R.id.textView7)).setOnClickListener(new C05001());
        musicgWeb.setOnClickListener(new C05012());
        try {
            ((TextView) findViewById(R.id.textView8)).setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
            Toast.makeText(this, "NO Version found", Toast.LENGTH_SHORT).show();
        }
        bleny.setOnClickListener(new C05023());
        ImageView twitter = (ImageView) findViewById(R.id.twitter);
        ((ImageView) findViewById(R.id.face)).setOnClickListener(new C05034());
        twitter.setOnClickListener(new C05045());
        ((TextView) findViewById(R.id.textView90)).setOnClickListener(new C05056());
        ((TextView) findViewById(R.id.textView9)).setOnClickListener(new C05067());
    }

    public static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent("android.intent.action.VIEW", Uri.parse("fb://page/381614908615384"));
        } catch (Exception e) {
            return new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/blenyapp"));
        }
    }

    protected void onStart() {
        super.onStart();
        //todo send easy track
//        EasyTracker.getInstance().activityStart(this);
//        EasyTracker.getTracker().setAnonymizeIp(true);
    }

    protected void onStop() {
        super.onStop();
        //todo send easy track
//        EasyTracker.getInstance().activityStop(this);
//        EasyTracker.getTracker().setAnonymizeIp(true);
    }

//    private void adLoading() {
//        this.ad = new AdView(this);
//        this.ad.setAdUnitId("ca-app-pub-2217667320333373/3802239845");
//        this.ad.setAdSize(AdSize.SMART_BANNER);
//        ((LinearLayout) findViewById(R.id.adView)).addView(this.ad);
//        this.ad.loadAd(new Builder().build());
//    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
