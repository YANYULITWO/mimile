package swjtu.software07.mimile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wangj.baselibrary.activity.BaseActivity;
import com.wangj.baselibrary.http.bean.CommonRequest;
import com.wangj.baselibrary.http.bean.CommonResponse;
import com.wangj.baselibrary.http.interf.ResponseHandler;
import com.wangj.baselibrary.util.DialogUtil;
import com.wangj.baselibrary.util.LoadingDialogUtil;

public class InitPasswordActivity extends BaseActivity {

    private String SERVLET = "http://119.29.205.70:8080/";
    private String URL_SIGNUP = SERVLET + "MML/signWithPasswordServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_init_password);

        Button confirmPwd = (Button) findViewById(R.id.confirm_password);
        confirmPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText passwordEditText01 = (EditText) findViewById(R.id.password_set_edit_text);
                EditText passwordEditText02 = (EditText) findViewById(R.id.password_confirm_edit_text);
                final String password01 = passwordEditText01.getText().toString();
                String password02 = passwordEditText02.getText().toString();
                if (password01 != null && password02 != null && password01.equals(password02)) {
                    Intent intent = getIntent();
                    final String phoneNum = intent.getStringExtra("phone");
                    final CommonRequest request = new CommonRequest();
                    request.addRequestParam("user_phone", phoneNum);
                    request.addRequestParam("user_password", MD5Creator.md5Password(password01));
                    sendHttpPostRequest(URL_SIGNUP, request, new ResponseHandler() {
                        @Override
                        public void success(CommonResponse response) {
                            LoadingDialogUtil.cancelLoading();
                            // tvRequest.setText(request.getJsonStr());
                            // tvResponse.setText(response.getResCode() + "\n" + response.getResMsg());
                            DialogUtil.showHintDialog(InitPasswordActivity.this, "注册成功啦！", false);
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("phone", phoneNum);
                            returnIntent.putExtra("password", password01);
                            setResult(100, returnIntent);
                            finish();
                        }

                        @Override
                        public void fail(String failCode, String failMsg) {
                            // tvRequest.setText(request.getJsonStr());
                            // tvResponse.setText(failCode + "\n" + failMsg);
                            DialogUtil.showHintDialog(InitPasswordActivity.this, true, "注册失败", failCode + " : " + failMsg, "关闭对话框", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LoadingDialogUtil.cancelLoading();
                                    DialogUtil.dismissDialog();
                                }
                            });
                        }
                    }, true);
                } else {
                    Toast.makeText(InitPasswordActivity.this, "密码有误", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
