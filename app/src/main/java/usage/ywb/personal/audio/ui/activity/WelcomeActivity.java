package usage.ywb.personal.audio.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import usage.ywb.personal.audio.R;
import usage.ywb.personal.audio.utils.StatusBar;

/**
 * @author yuwenbo
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setNavigationBar(this);
        setContentView(R.layout.activity_welcome);

        new Thread(){
            @Override
            public void run() {
                final Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                try {
                    sleep(5000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }.start();

    }
}
