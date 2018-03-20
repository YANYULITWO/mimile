package swjtu.software07.mimile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class UserSafetyActivity extends Activity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 去掉标题栏
        setContentView(R.layout.activity_user_safety);
        pref = getSharedPreferences("user_data", MODE_PRIVATE);
        LinearLayout changePass = (LinearLayout) findViewById(R.id.change_password);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePassIntent = new Intent(UserSafetyActivity.this, VerifyIdentityActivity.class);
                changePassIntent.putExtra("phone", pref.getString("currentUser", ""));
                startActivity(changePassIntent);
            }
        });


        String phone = pref.getString("currentUser", "");
        if (phone.equals("")) {
            changePass.setVisibility(View.GONE);
        }

        LinearLayout accountAppeal = (LinearLayout) findViewById(R.id.account_appeal);
        accountAppeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:10086"));
                startActivity(dialIntent);
            }
        });

        LinearLayout tips = (LinearLayout) findViewById(R.id.safety_tips);
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder tip = new AlertDialog.Builder(UserSafetyActivity.this);
                tip.setTitle("安全提示");
                tip.setMessage("请不要将密码告诉陌生人");
                tip.setPositiveButton("确定", null);
                tip.show();
            }
        });
    }
}
