package swjtu.software07.mimile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wangj.baselibrary.activity.BaseActivity;
import com.wangj.baselibrary.http.Constant;
import com.wangj.baselibrary.http.HttpPostTask;
import com.wangj.baselibrary.http.bean.CommonRequest;
import com.wangj.baselibrary.http.bean.CommonResponse;
import com.wangj.baselibrary.http.interf.ResponseHandler;
import com.wangj.baselibrary.util.DialogUtil;
import com.wangj.baselibrary.util.LoadingDialogUtil;
import com.wangj.baselibrary.util.LogUtil;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Vector;

public class ProductDetailActivity extends Activity {

    private String SERVLET = "http://119.29.205.70:8080/";
    private String URL_COLLECT = SERVLET + "MML/addIntoFavoriteServlet";
    private String URL_ADD_CART = SERVLET + "MML/addIntoShoppingCartServlet";

    Intent lastIntent;
    private SharedPreferences pref;

    String userPhone;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 去掉标题栏
        setContentView(R.layout.activity_product_detail);
        lastIntent = getIntent();
        pref = getSharedPreferences("user_data", MODE_PRIVATE);
        userPhone = pref.getString("currentUser", "");
        // Click buttons


        Button collect = (Button) findViewById(R.id.collect_product);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhone = pref.getString("currentUser", "");
                if (userPhone.equals("")) {
                    Toast.makeText(ProductDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivityForResult(loginIntent, 102);
                } else {
                    CommonRequest request = new CommonRequest();
                    request.addRequestParam("type", "1");
                    request.addRequestParam("sp_id", lastIntent.getStringExtra("id"));
                    request.addRequestParam("user_phone", pref.getString("currentUser", ""));
                    new HttpPostTask(request, mHandler, new ResponseHandler() {
                        @Override
                        public void success(CommonResponse response) {
                            Toast.makeText(ProductDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                            LoadingDialogUtil.cancelLoading();
                        }

                        @Override
                        public void fail(String failCode, String failMsg) {
                            Toast.makeText(ProductDetailActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                            LoadingDialogUtil.cancelLoading();
                        }
                    }).execute(URL_COLLECT);
                }
            }
        });
        Button addCart = (Button) findViewById(R.id.add_cart);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhone = pref.getString("currentUser", "");
                if (userPhone.equals("")) {
                    Toast.makeText(ProductDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivityForResult(loginIntent, 102);
                } else {
                    final AlertDialog.Builder amountDialog = new AlertDialog.Builder(ProductDetailActivity.this);
                    final View dialogView = LayoutInflater.from(ProductDetailActivity.this)
                            .inflate(R.layout.cart_amount_dialog,null);
                    amountDialog.setTitle("请选择数量");
                    amountDialog.setView(dialogView);
                    final Button changeMinus = (Button) dialogView.findViewById(R.id.change_minus);
                    changeMinus.setEnabled(false);
                    changeMinus.setBackgroundResource(R.drawable.minus_gray);
                    final Button changePlus = (Button) dialogView.findViewById(R.id.change_plus);
                    changeMinus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText changeAmount = (EditText) dialogView.findViewById(R.id.change_amount_text);
                            int c = Integer.parseInt(changeAmount.getText().toString());
                            c -= 1;
                            if (c == 1) {
                                changeMinus.setEnabled(false);
                                changeMinus.setBackgroundResource(R.drawable.minus_gray);
                            } else {
                                changeMinus.setEnabled(true);
                                changeMinus.setBackgroundResource(R.drawable.minus);
                            }
                            changeAmount.setText(""+ c);
                        }
                    });
                    changePlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText changeAmount = (EditText) dialogView.findViewById(R.id.change_amount_text);
                            int c = Integer.parseInt(changeAmount.getText().toString());
                            c += 1;
                            changeMinus.setEnabled(true);
                            changeMinus.setBackgroundResource(R.drawable.minus);
                            changeAmount.setText(""+ c);
                        }
                    });
                    amountDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText changeAmount = (EditText) dialogView.findViewById(R.id.change_amount_text);
                                    amount = changeAmount.getText().toString();
                                    CommonRequest request = new CommonRequest();
                                    request.addRequestParam("type", "1");
                                    request.addRequestParam("sp_id", lastIntent.getStringExtra("id"));
                                    request.addRequestParam("user_phone", pref.getString("currentUser", ""));
                                    request.addRequestParam("sp_amount", amount);
                                    new HttpPostTask(request, mHandler, new ResponseHandler() {
                                        @Override
                                        public void success(CommonResponse response) {
                                            Toast.makeText(ProductDetailActivity.this, "已添加到购物车", Toast.LENGTH_SHORT).show();
                                            LoadingDialogUtil.cancelLoading();
                                        }

                                        @Override
                                        public void fail(String failCode, String failMsg) {
                                            Toast.makeText(ProductDetailActivity.this, "添加购物车失败", Toast.LENGTH_SHORT).show();
                                            LoadingDialogUtil.cancelLoading();
                                        }
                                    }).execute(URL_ADD_CART);
                                }
                            });

                    amountDialog.show();


                }
            }
        });
        Button buyNow = (Button) findViewById(R.id.buy_now);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetailActivity.this, "老子现在就要买", Toast.LENGTH_SHORT).show();
            }
        });


        // Fill info
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(lastIntent.getStringExtra("name"));
        TextView price = (TextView) findViewById(R.id.price);
        price.setText("￥" + lastIntent.getStringExtra("price") + ".00");
        ImageView img = (ImageView) findViewById(R.id.img);
        Glide.with(ProductDetailActivity.this).load(lastIntent.getStringExtra("img")).into(img);
        TextView pp = (TextView) findViewById(R.id.pp);
        String ppStr = lastIntent.getStringExtra("pp");
        ppStr = (ppStr.equals("null"))?"不详":ppStr;
        pp.setText(ppStr);
        TextView xh = (TextView) findViewById(R.id.xh);
        String xhStr = lastIntent.getStringExtra("xh");
        xhStr = (xhStr.equals("null"))?"不详":xhStr;
        xh.setText(xhStr);
        TextView ssnf = (TextView) findViewById(R.id.ssnf);
        String ssnfStr = lastIntent.getStringExtra("ssnf");
        ssnfStr = (ssnfStr.equals("null"))?"不详":ssnfStr;
        ssnf.setText(ssnfStr);
        TextView ssyf = (TextView) findViewById(R.id.ssyf);
        String ssyfStr = lastIntent.getStringExtra("ssyf");
        ssyfStr = (ssyfStr.equals("null"))?"不详":ssyfStr;
        ssyf.setText(ssyfStr);
        TextView jsys = (TextView) findViewById(R.id.jsys);
        String jsysStr = lastIntent.getStringExtra("jsys");
        jsysStr = (jsysStr.equals("null"))?"不详":jsysStr;
        jsys.setText(jsysStr);
        TextView jscd = (TextView) findViewById(R.id.jscd);
        String jscdStr = lastIntent.getStringExtra("jscd");
        jscdStr = (jscdStr.equals("null"))?"不详":jscdStr;
        jscd.setText(jscdStr);
        TextView jskd = (TextView) findViewById(R.id.jskd);
        String jskdStr = lastIntent.getStringExtra("jskd");
        jskdStr = (jskdStr.equals("null"))?"不详":jskdStr;
        jskd.setText(jskdStr);
        TextView jshd = (TextView) findViewById(R.id.jshd);
        String jshdStr = lastIntent.getStringExtra("jshd");
        jshdStr = (jshdStr.equals("null"))?"不详":jshdStr;
        jshd.setText(jshdStr);
        TextView jszl = (TextView) findViewById(R.id.jszl);
        String jszlStr = lastIntent.getStringExtra("jszl");
        jszlStr = (jszlStr.equals("null"))?"不详":jszlStr;
        jszl.setText(jszlStr);
        TextView srfs = (TextView) findViewById(R.id.srfs);
        String srfsStr = lastIntent.getStringExtra("srfs");
        srfsStr = (srfsStr.equals("null"))?"不详":srfsStr;
        srfs.setText(srfsStr);
        TextView jscz = (TextView) findViewById(R.id.jscz);
        String jsczStr = lastIntent.getStringExtra("jscz");
        jsczStr = (jsczStr.equals("null"))?"不详":jsczStr;
        jscz.setText(jsczStr);
        TextView czxt = (TextView) findViewById(R.id.czxt);
        String czxtStr = lastIntent.getStringExtra("czxt");
        czxtStr = (czxtStr.equals("null"))?"不详":czxtStr;
        czxt.setText(czxtStr);
        TextView czxtbb = (TextView) findViewById(R.id.czxtbb);
        String czxtbbStr = lastIntent.getStringExtra("czxtbb");
        czxtbbStr = (czxtbbStr.equals("null"))?"不详":czxtbbStr;
        czxtbb.setText(czxtbbStr);
        TextView cpupp = (TextView) findViewById(R.id.cpupp);
        String cpuppStr = lastIntent.getStringExtra("cpupp");
        cpuppStr = (cpuppStr.equals("null"))?"不详":cpuppStr;
        cpupp.setText(cpuppStr);
        TextView cpupl = (TextView) findViewById(R.id.cpupl);
        String cpuplStr = lastIntent.getStringExtra("cpupl");
        cpuplStr = (cpuplStr.equals("null"))?"不详":cpuplStr;
        cpupl.setText(cpuplStr);
        TextView cpuhs = (TextView) findViewById(R.id.cpuhs);
        String cpuhsStr = lastIntent.getStringExtra("cpuhs");
        cpuhsStr = (cpuhsStr.equals("null"))?"不详":cpuhsStr;
        cpuhs.setText(cpuhsStr);
        TextView cpuxh = (TextView) findViewById(R.id.cpuxh);
        String cpuxhStr = lastIntent.getStringExtra("cpuxh");
        cpuxhStr = (cpuxhStr.equals("null"))?"不详":cpuxhStr;
        cpuxh.setText(cpuxhStr);
        TextView skjlx = (TextView) findViewById(R.id.skjlx);
        String skjlxStr = lastIntent.getStringExtra("skjlx");
        skjlxStr = (skjlxStr.equals("null"))?"不详":skjlxStr;
        skjlx.setText(skjlxStr);
        TextView zdzcsimksl = (TextView) findViewById(R.id.zdzcsimksl);
        String zdzcsimkslStr = lastIntent.getStringExtra("zdzcsimksl");
        zdzcsimkslStr = (zdzcsimkslStr.equals("null"))?"不详":zdzcsimkslStr;
        zdzcsimksl.setText(zdzcsimkslStr);
        TextView simklx = (TextView) findViewById(R.id.simklx);
        String simklxStr = lastIntent.getStringExtra("simklx");
        simklxStr = (simklxStr.equals("null"))?"不详":simklxStr;
        simklx.setText(simklxStr);
        TextView fourgwl = (TextView) findViewById(R.id.fourgwl);
        String fourgwlStr = lastIntent.getStringExtra("fourgwl");
        fourgwlStr = (fourgwlStr.equals("null"))?"不详":fourgwlStr;
        fourgwl.setText(fourgwlStr);
        TextView threegtwogwl = (TextView) findViewById(R.id.threegtwogwl);
        String threegtwogwlStr = lastIntent.getStringExtra("threegtwogwl");
        threegtwogwlStr = (threegtwogwlStr.equals("null"))?"不详":threegtwogwlStr;
        threegtwogwl.setText(threegtwogwlStr);
        TextView rom = (TextView) findViewById(R.id.rom);
        String romStr = lastIntent.getStringExtra("rom");
        romStr = (romStr.equals("null"))?"不详":romStr;
        rom.setText(romStr);
        TextView ram = (TextView) findViewById(R.id.ram);
        String ramStr = lastIntent.getStringExtra("ram");
        ramStr = (ramStr.equals("null"))?"不详":ramStr;
        ram.setText(ramStr);
        TextView cck = (TextView) findViewById(R.id.cck);
        String cckStr = lastIntent.getStringExtra("cck");
        cckStr = (cckStr.equals("null"))?"不详":cckStr;
        cck.setText(cckStr);
        TextView zdcckzrl = (TextView) findViewById(R.id.zdcckzrl);
        String zdcckzrlStr = lastIntent.getStringExtra("zdcckzrl");
        zdcckzrlStr = (zdcckzrlStr.equals("null"))?"不详":zdzcsimkslStr;
        zdcckzrl.setText(zdcckzrlStr);
        TextView zpmcc = (TextView) findViewById(R.id.zpmcc);
        String zpmccStr = lastIntent.getStringExtra("zpmcc");
        zpmccStr = (zpmccStr.equals("null"))?"不详":zpmccStr;
        zpmcc.setText(zpmccStr);
        TextView fbl = (TextView) findViewById(R.id.fbl);
        String fblStr = lastIntent.getStringExtra("fbl");
        fblStr = (fblStr.equals("null"))?"不详":fblStr;
        fbl.setText(fblStr);
        TextView pmxsmd = (TextView) findViewById(R.id.pmxsmd);
        String pmxsmdStr = lastIntent.getStringExtra("pmxsmd");
        pmxsmdStr = (pmxsmdStr.equals("null"))?"不详":pmxsmdStr;
        pmxsmd.setText(pmxsmdStr);
        TextView pmczlx = (TextView) findViewById(R.id.pmczlx);
        String pmczlxStr = lastIntent.getStringExtra("pmczlx");
        pmczlxStr = (pmczlxStr.equals("null"))?"不详":pmczlxStr;
        pmczlx.setText(pmczlxStr);
        TextView qzsxt = (TextView) findViewById(R.id.qzsxt);
        String qzsxtStr = lastIntent.getStringExtra("qzsxt");
        qzsxtStr = (qzsxtStr.equals("null"))?"不详":qzsxtStr;
        qzsxt.setText(qzsxtStr);
        TextView sxtsl = (TextView) findViewById(R.id.sxtsl);
        String sxtslStr = lastIntent.getStringExtra("sxtsl");
        sxtslStr = (sxtslStr.equals("null"))?"不详":sxtslStr;
        sxtsl.setText(sxtslStr);
        TextView hzsxt = (TextView) findViewById(R.id.hzsxt);
        String hzsxtStr = lastIntent.getStringExtra("hzsxt");
        hzsxtStr = (hzsxtStr.equals("null"))?"不详":hzsxtStr;
        hzsxt.setText(hzsxtStr);
        TextView dcrl = (TextView) findViewById(R.id.dcrl);
        String dcrlStr = lastIntent.getStringExtra("dcrl");
        dcrlStr = (dcrlStr.equals("null"))?"不详":dcrlStr;
        dcrl.setText(dcrlStr);
        TextView dcsfkcx = (TextView) findViewById(R.id.dcsfkcx);
        String dcsfkcxStr = lastIntent.getStringExtra("dcsfkcx");
        dcsfkcxStr = (dcsfkcxStr.equals("null"))?"不详":dcsfkcxStr;
        dcsfkcx.setText(dcsfkcxStr);
        TextView cdq = (TextView) findViewById(R.id.cdq);
        String cdqStr = lastIntent.getStringExtra("cdq");
        cdqStr = (cdqStr.equals("null"))?"不详":cdqStr;
        cdq.setText(cdqStr);
        TextView sjcsjk = (TextView) findViewById(R.id.sjcsjk);
        String sjcsjkStr = lastIntent.getStringExtra("sjcsjk");
        sjcsjkStr = (sjcsjkStr.equals("null"))?"不详":sjcsjkStr;
        sjcsjk.setText(sjcsjkStr);
        TextView nfc = (TextView) findViewById(R.id.nfc);
        String nfcStr = lastIntent.getStringExtra("nfc");
        nfcStr = (nfcStr.equals("null"))?"不详":nfcStr;
        nfc.setText(nfcStr);
        TextView ejjklx = (TextView) findViewById(R.id.ejjklx);
        String ejjklxStr = lastIntent.getStringExtra("ejjklx");
        ejjklxStr = (ejjklxStr.equals("null"))?"不详":ejjklxStr;
        ejjklx.setText(ejjklxStr);
        TextView cdjklx = (TextView) findViewById(R.id.cdjklx);
        String cdjklxStr = lastIntent.getStringExtra("cdjklx");
        cdjklxStr = (cdjklxStr.equals("null"))?"不详":cdjklxStr;
        cdjklx.setText(cdjklxStr);
        TextView zwsb = (TextView) findViewById(R.id.zwsb);
        String zwsbStr = lastIntent.getStringExtra("zwsb");
        zwsbStr = (zwsbStr.equals("null"))?"不详":zwsbStr;
        zwsb.setText(zwsbStr);
        TextView gps = (TextView) findViewById(R.id.gps);
        String gpsStr = lastIntent.getStringExtra("gps");
        gpsStr = (gpsStr.equals("null"))?"不详":gpsStr;
        gps.setText(gpsStr);
        TextView tly = (TextView) findViewById(R.id.tly);
        String tlyStr = lastIntent.getStringExtra("tly");
        tlyStr = (tlyStr.equals("null"))?"不详":tlyStr;
        tly.setText(tlyStr);
        TextView cygn = (TextView) findViewById(R.id.cygn);
        String cygnStr = lastIntent.getStringExtra("cygn");
        cygnStr = (cygnStr.equals("null"))?"不详":cygnStr;
        cygn.setText(cygnStr);

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == Constant.HANDLER_HTTP_SEND_FAIL) {
                LogUtil.logErr(msg.obj.toString());

                LoadingDialogUtil.cancelLoading();
                DialogUtil.showHintDialog(ProductDetailActivity.this, "请求发送失败，请重试", true);
            } else if (msg.what == Constant.HANDLER_HTTP_RECEIVE_FAIL) {
                LogUtil.logErr(msg.obj.toString());

                LoadingDialogUtil.cancelLoading();
                DialogUtil.showHintDialog(ProductDetailActivity.this, "请求接受失败，请重试", true);
            }
        }
    };

    private void showCartAmountDialog() {

    }
}
