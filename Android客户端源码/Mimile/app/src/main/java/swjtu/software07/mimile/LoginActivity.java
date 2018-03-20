package swjtu.software07.mimile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wangj.baselibrary.activity.BaseActivity;
import com.wangj.baselibrary.http.bean.CommonRequest;
import com.wangj.baselibrary.http.bean.CommonResponse;
import com.wangj.baselibrary.http.interf.ResponseHandler;
import com.wangj.baselibrary.util.DialogUtil;
import com.wangj.baselibrary.util.LoadingDialogUtil;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LoginActivity extends BaseActivity {

    private String phoneNumber = "";

    private SharedPreferences pref;

    private String SERVLET = "http://119.29.205.70:8080/";
    private String URL_LOGIN = SERVLET + "MML/loginServlet";
    private String URL_VERIFY = SERVLET + "MML/signUpServlet";
    private String URL_FORGET = SERVLET + "MML/forgetPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        final EditText loginPhone = (EditText) findViewById(R.id.login_phone_edit_text);
        final EditText loginKeyword = (EditText) findViewById(R.id.login_keyword_edit_text);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        CheckBox rememberPass = (CheckBox) findViewById(R.id.remember_password_checkbox);
        boolean isRemember =  pref.getBoolean("remember_password", false);
        if (isRemember) {
            String phoneNumber = pref.getString("phoneNumber", "");
            String password = pref.getString("password", "");
            loginPhone.setText(phoneNumber);
            loginPhone.setText(password);
            rememberPass.setChecked(true);
        }

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(loginPhone.getText().toString(), loginKeyword.getText().toString());
            }
        });

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");

                            phoneNumber = phone;
                            try {
                                signUp();
                            } catch (Exception e) {
                                Log.e("Sign Up Error", e.toString());
                            }
                        }
                    }
                });
                registerPage.show(LoginActivity.this);
            }
        });

        Button forgetPassButton = (Button) findViewById(R.id.forget_password_button);
        forgetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");

                            phoneNumber = phone;
                            try {
                                forgetPassword();
                            } catch (Exception e) {
                                Log.e("Sign Up Error", e.toString());
                            }
                        }
                    }
                });
                registerPage.show(LoginActivity.this);
            }
        });
    }

    private void login(String phone, final String password){
        // final TextView tvRequest = (TextView) findViewById(R.id.tv_request);
        // final TextView tvResponse = (TextView) findViewById(R.id.tv_response);
        final CommonRequest request = new CommonRequest();
        final String phoneNum = phone;
        request.addRequestParam("user_phone", phoneNum);
        request.addRequestParam("user_password", MD5Creator.md5Password(password));
        sendHttpPostRequest(URL_LOGIN, request, new ResponseHandler() {
            @Override
            public void success(CommonResponse response) {
                LoadingDialogUtil.cancelLoading();
                // tvRequest.setText(request.getJsonStr());
                // tvResponse.setText(response.getResCode() + "\n" + response.getResMsg());
                Intent mainIntent = new Intent();
                mainIntent.putExtra("phone", phoneNum);
                mainIntent.putExtra("password", password);
                setResult(RESULT_OK, mainIntent);
                CheckBox rememberPass = (CheckBox) findViewById(R.id.remember_password_checkbox);
                SharedPreferences.Editor editor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
                if (rememberPass.isChecked()) {
                    editor.putString("phoneNumber", phoneNum);
                    editor.putString("password", password);
                    editor.putBoolean("remember_password", true);
                    Toast.makeText(LoginActivity.this, "账户添加到本地啦！", Toast.LENGTH_SHORT).show();
                }
                editor.putString("currentUser", phoneNum);
                editor.apply();
                finish();
            }

            @Override
            public void fail(String failCode, String failMsg) {
                // tvRequest.setText(request.getJsonStr());
                // tvResponse.setText(failCode + "\n" + failMsg);
                try {
                    Toast.makeText(LoginActivity.this, URLDecoder.decode(failMsg, "UTF-8"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Decode Error", e.toString());
                }
                DialogUtil.showHintDialog(LoginActivity.this, true, "登陆失败", URLDecoder.decode(failMsg), "关闭对话框", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadingDialogUtil.cancelLoading();
                        DialogUtil.dismissDialog();
                    }
                });
            }
        }, true);

    }

    private void signUp() throws Exception{
        final CommonRequest request = new CommonRequest();
        request.addRequestParam("user_phone", URLEncoder.encode(phoneNumber, "UTF-8"));
        sendHttpPostRequest(URL_VERIFY, request, new ResponseHandler() {
            @Override
            public void success(CommonResponse response) {
                LoadingDialogUtil.cancelLoading();
                Log.e("response", "Message:" + response.getResMsg());
                try {
                    String resultCode = response.getResCode();
                    if (resultCode.equals("0")) {
                        // 手机号可以注册
                        Intent initPwdIntent = new Intent(LoginActivity.this, InitPasswordActivity.class);
                        initPwdIntent.putExtra("phone", phoneNumber);
                        startActivityForResult(initPwdIntent, 100);
                    } else if (resultCode.equals("101")){
                        // 已注册用户,跳转登录界面
                        Toast.makeText(LoginActivity.this, "已注册用户,请直接登录", Toast.LENGTH_SHORT);
                    } else {
                        String resultStr = URLDecoder.decode(response.getResMsg(), "UTF-8");
                        showNormalDialog(resultStr);
                    }
                } catch (Exception e) {
                    Log.e("Sign Up Error", e.toString());
                }
                // tvRequest.setText(request.getJsonStr());
                // tvResponse.setText(response.getResCode() + "\n" + response.getResMsg());
                // DialogUtil.showHintDialog(LoginActivity.this, "注册成功啦！", false);
            }

            @Override
            public void fail(String failCode, String failMsg) {
                // tvRequest.setText(request.getJsonStr());
                // tvResponse.setText(failCode + "\n" + failMsg);
                DialogUtil.showHintDialog(LoginActivity.this, true, "注册失败", URLDecoder.decode(failMsg), "关闭对话框", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadingDialogUtil.cancelLoading();
                        DialogUtil.dismissDialog();
                    }
                });
            }
        }, true);
    }

    private void forgetPassword() throws Exception {
        final CommonRequest request = new CommonRequest();
        request.addRequestParam("user_phone", URLEncoder.encode(phoneNumber, "UTF-8"));
        Toast.makeText(LoginActivity.this, "已经发送了数据", Toast.LENGTH_SHORT).show();
        Log.d("Send message", "Phone: " + URLEncoder.encode(phoneNumber, "UTF-8"));
        sendHttpPostRequest(URL_FORGET, request, new ResponseHandler() {
            @Override
            public void success(CommonResponse response) {
                LoadingDialogUtil.cancelLoading();
                Log.e("response", "Message:" + response.getResMsg());
                try {
                    String resultCode = response.getResCode().toString();
                    if (resultCode.equals("0")) {
                        // 手机号已经注册, 可以修改密码
                        Toast.makeText(LoginActivity.this, "可以修改密码", Toast.LENGTH_SHORT).show();
                        Intent changePwdIntent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                        changePwdIntent.putExtra("phone", phoneNumber);
                        startActivityForResult(changePwdIntent, 100);
                    } else if (resultCode.equals("101")){
                        // 手机号未注册, 调用注册
                        Toast.makeText(LoginActivity.this, "未注册用户,请先注册", Toast.LENGTH_SHORT);
                    } else {
                        String resultStr = URLDecoder.decode(response.getResMsg(), "UTF-8");
                        showNormalDialog(resultStr);
                    }
                } catch (Exception e) {
                    Log.e("Sign Up Error", e.toString());
                }
                // tvRequest.setText(request.getJsonStr());
                // tvResponse.setText(response.getResCode() + "\n" + response.getResMsg());
                // DialogUtil.showHintDialog(LoginActivity.this, "注册成功啦！", false);
            }

            @Override
            public void fail(String failCode, String failMsg) {
                // tvRequest.setText(request.getJsonStr());
                // tvResponse.setText(failCode + "\n" + failMsg);
                DialogUtil.showHintDialog(LoginActivity.this, true, "注册失败", URLDecoder.decode(failMsg), "关闭对话框", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadingDialogUtil.cancelLoading();
                        DialogUtil.dismissDialog();
                    }
                });
            }
        }, true);
    }

    private void showNormalDialog(String detailInfo){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(LoginActivity.this);
        normalDialog.setTitle("注册失败");
        normalDialog.setMessage(detailInfo);
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // 显示
        normalDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
                String returnedPhone = data.getStringExtra("phone");
                String returnedPassword = data.getStringExtra("password");
                EditText loginPhone = (EditText) findViewById(R.id.login_phone_edit_text);
                EditText loginKeyword = (EditText) findViewById(R.id.login_keyword_edit_text);
                loginPhone.setText(returnedPhone);
                loginKeyword.setText(returnedPassword);
                break;
            default:
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent mainIntent = new Intent();
            mainIntent.putExtra("phone", "");
            setResult(RESULT_OK, mainIntent);
            finish();
        }

        return false;
    }
}
