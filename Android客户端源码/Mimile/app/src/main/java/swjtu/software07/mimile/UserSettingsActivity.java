package swjtu.software07.mimile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class UserSettingsActivity extends Activity {

    TextView cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 去掉标题栏
        setContentView(R.layout.activity_user_settings);

        cache = (TextView) findViewById(R.id.current_cache);
        cache.setText(GlideCatchUtil.getInstance().getCacheSize(getApplicationContext()));

        LinearLayout clearCacheLayout = (LinearLayout) findViewById(R.id.clear_cache);
        clearCacheLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CleanMessageUtil.clearAllCache(getApplicationContext());
                // GlideCatchUtil.getInstance().clearImageAllCache(getApplicationContext());
                Toast.makeText(UserSettingsActivity.this, "缓存已清除", Toast.LENGTH_SHORT).show();
                cache.setText(GlideCatchUtil.getInstance().getCacheSize(getApplicationContext()));

            }
        });

        // 关于我们
        LinearLayout aboutLayout = (LinearLayout) findViewById(R.id.about_us);
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutIntent = new Intent(UserSettingsActivity.this, AboutUsActivity.class);
                startActivity(aboutIntent);
            }
        });

        // 开发者
        final LinearLayout developerLayout = (LinearLayout) findViewById(R.id.developer);
        developerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent developerIntent = new Intent(UserSettingsActivity.this, DeveloperActivity.class);
                startActivity(developerIntent);
            }
        });

        // 联系我们
        LinearLayout contactLayout = (LinearLayout) findViewById(R.id.contact_us);
        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:mimile07@126.com");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra(Intent.EXTRA_SUBJECT, "米米乐APP反馈");
                startActivity(it);
            }
        });
    }
}
