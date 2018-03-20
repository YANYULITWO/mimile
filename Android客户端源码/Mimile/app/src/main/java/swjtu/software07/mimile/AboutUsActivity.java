package swjtu.software07.mimile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 去掉标题栏
        setContentView(R.layout.activity_about_us);
    }
}
