package swjtu.software07.mimile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangj.baselibrary.activity.BaseActivity;
import com.wangj.baselibrary.http.bean.CommonRequest;
import com.wangj.baselibrary.http.bean.CommonResponse;
import com.wangj.baselibrary.http.interf.ResponseHandler;
import com.wangj.baselibrary.util.*;

import java.net.URLDecoder;

public class VerifyIdentityActivity extends BaseActivity {

    private String SERVLET = "http://119.29.205.70:8080/";
    private String URL_LOGIN = SERVLET + "MML/loginServlet";

    private FingerprintManagerCompat manager;
    private CancellationSignal mCancellationSignal = new CancellationSignal();

    ImageView fingerIcon;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 去掉标题栏
        setContentView(R.layout.activity_verify_identity);
        Intent lastIntent = getIntent();
        phone = lastIntent.getStringExtra("phone");

        // 获取一个FingerPrintManagerCompat的实例
        manager = FingerprintManagerCompat.from(this);
        if (manager.isHardwareDetected()) {
            if (manager.hasEnrolledFingerprints()) {
                // 可以指纹验证
            } else {
                Toast.makeText(VerifyIdentityActivity.this, "该设备尚未录入指纹", Toast.LENGTH_SHORT).show();
                Button fingerVerifyButton = (Button) findViewById(R.id.finger_print_verify);
                fingerVerifyButton.setVisibility(View.GONE);
                Button quit = (Button) findViewById(R.id.quit_verify_identify);
                quit.setVisibility(View.GONE);
            }
        } else {
            Button fingerVerifyButton = (Button) findViewById(R.id.finger_print_verify);
            fingerVerifyButton.setVisibility(View.GONE);
            Button quit = (Button) findViewById(R.id.quit_verify_identify);
            quit.setVisibility(View.GONE);
        }

        Button confirmPass = (Button) findViewById(R.id.verify_identify);
        confirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText password_text = (EditText) findViewById(R.id.verify_identify_edit_text);
                String password = password_text.getText().toString();

                final CommonRequest request = new CommonRequest();
                request.addRequestParam("user_phone", phone);
                request.addRequestParam("user_password", MD5Creator.md5Password(password));
                sendHttpPostRequest(URL_LOGIN, request, new ResponseHandler() {
                    @Override
                    public void success(CommonResponse response) {
                        com.wangj.baselibrary.util.LoadingDialogUtil.cancelLoading();
                        // tvRequest.setText(request.getJsonStr());
                        // tvResponse.setText(response.getResCode() + "\n" + response.getResMsg());

                    }

                    @Override
                    public void fail(String failCode, String failMsg) {
                        // tvRequest.setText(request.getJsonStr());
                        // tvResponse.setText(failCode + "\n" + failMsg);
                        try {
                            Toast.makeText(VerifyIdentityActivity.this, URLDecoder.decode(failMsg, "UTF-8"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("Decode Error", e.toString());
                        }
                        DialogUtil.showHintDialog(VerifyIdentityActivity.this, true, "登陆失败", URLDecoder.decode(failMsg), "关闭对话框", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                com.wangj.baselibrary.util.LoadingDialogUtil.cancelLoading();
                                DialogUtil.dismissDialog();
                            }
                        });
                    }
                }, true);


            }
        });

        fingerIcon = (ImageView) findViewById(R.id.finger_print_icon);
        fingerIcon.setVisibility(View.INVISIBLE);
        Button check = (Button) findViewById(R.id.finger_print_verify);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerIcon.setVisibility(View.VISIBLE);
                manager.authenticate(null, 0, mCancellationSignal, new MyCallBack(), null);
            }
        });

        Button quit = (Button) findViewById(R.id.quit_verify_identify);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCancellationSignal.cancel();
                finish();
            }
        });


    }

    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        private static final String TAG = "MyCallBack";

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.d(TAG, "onAuthenticationError: " + errString);
            Toast.makeText(VerifyIdentityActivity.this, errString.toString(), Toast.LENGTH_SHORT).show();
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            Log.d(TAG, "onAuthenticationFailed: " + "验证失败");
            Toast.makeText(VerifyIdentityActivity.this, "验证失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.d(TAG, "onAuthenticationHelp: " + helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult
                                                      result) {
            Log.d(TAG, "onAuthenticationSucceeded: " + "验证成功");
            Toast.makeText(VerifyIdentityActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
            fingerIcon.setVisibility(View.INVISIBLE);
        }
    }


}
