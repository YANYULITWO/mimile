package swjtu.software07.mimile;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangj.baselibrary.activity.BaseActivity;
import com.wangj.baselibrary.http.HttpPostTask;
import com.wangj.baselibrary.http.bean.CommonRequest;
import com.wangj.baselibrary.http.bean.CommonResponse;
import com.wangj.baselibrary.http.interf.ResponseHandler;
import com.wangj.baselibrary.util.DialogUtil;
import com.wangj.baselibrary.util.LoadingDialogUtil;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class GoodsListActivity extends BaseActivity {

    ListView goodsList;

    private String SERVLET = "http://119.29.205.70:8080/";
    private String URL_GOODSLIST01 = SERVLET + "MML/returnProductInfoServlet";
    private String URL_GOODSLIST02 = SERVLET + "MML/returnProductPriceServlet";
    private String URL_SEARCH = SERVLET + "MML/searchWholeTableServlet";
    private String URL_DETAIL = SERVLET + "MML/returnDetailInfoServlet";
    private String URL_USER_COLLECTION = SERVLET + "MML/returnFavoriteInfoServlet";
    private String URL_USER_RECORD = SERVLET + "MML/returnBrowsingHistoryServlet";
    private String URL_DELETE = SERVLET + "MML/addIntoFavoriteServlet";

    private String typeCode;
    private String title;
    private String minSize;
    private String maxSize;

    Intent dataIntent;

    Vector<String> idList;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setTitle("商品列表");
        setContentView(R.layout.activity_goods_list);
        dataIntent = getIntent();
        idList = new Vector<>();
        typeCode = dataIntent.getStringExtra("chosen_code");
        goodsList = (ListView) findViewById(R.id.goods_list_view);
        goodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // HashMap<String, String> map = list.get(i);
                CommonRequest request = new CommonRequest();
                // String idNum = map.get("id");
                final String idStr = idList.get(i).toString();
                request.addRequestParam("sp_id", idStr);
                pref = getSharedPreferences("user_data", MODE_PRIVATE);
                request.addRequestParam("user_phone", pref.getString("currentUser", ""));
                sendHttpPostRequest(URL_DETAIL, request, new ResponseHandler() {
                    @Override
                    public void success(CommonResponse response) {
                        if (response.getDataList().size() > 0) {
                            Intent detailIntent = new Intent(GoodsListActivity.this, ProductDetailActivity.class);
                            HashMap<String, String> detailMap = response.getDataList().get(0);
                            detailIntent.putExtra("id", idStr);
                            detailIntent.putExtra("name", detailMap.get("name"));
                            detailIntent.putExtra("img", detailMap.get("img"));
                            detailIntent.putExtra("price", detailMap.get("price"));
                            detailIntent.putExtra("pp", detailMap.get("pp"));
                            detailIntent.putExtra("xh", detailMap.get("xh"));
                            detailIntent.putExtra("ssnf", detailMap.get("ssnf"));
                            detailIntent.putExtra("ssyf", detailMap.get("ssyf"));
                            detailIntent.putExtra("jsys", detailMap.get("jsys"));
                            detailIntent.putExtra("jscd", detailMap.get("jscd"));
                            detailIntent.putExtra("jskd", detailMap.get("jskd"));
                            detailIntent.putExtra("jshd", detailMap.get("jshd"));
                            detailIntent.putExtra("jszl", detailMap.get("jszl"));
                            detailIntent.putExtra("srfs", detailMap.get("srfs"));
                            detailIntent.putExtra("jscz", detailMap.get("jscz"));
                            detailIntent.putExtra("czxt", detailMap.get("czxt"));
                            detailIntent.putExtra("czxtbb", detailMap.get("czxtbb"));
                            detailIntent.putExtra("cpupp", detailMap.get("cpupp"));
                            detailIntent.putExtra("cpupl", detailMap.get("cpupl"));
                            detailIntent.putExtra("cpuhs", detailMap.get("cpuhs"));
                            detailIntent.putExtra("cpuxh", detailMap.get("cpuxh"));
                            detailIntent.putExtra("skjlx", detailMap.get("skjlx"));
                            detailIntent.putExtra("zdzcsimksl", detailMap.get("zdzcsimksl"));
                            detailIntent.putExtra("simklx", detailMap.get("simklx"));
                            detailIntent.putExtra("fourgwl", detailMap.get("fourgwl"));
                            detailIntent.putExtra("threegtwogwl", detailMap.get("threegtwogwl"));
                            detailIntent.putExtra("rom", detailMap.get("rom"));
                            detailIntent.putExtra("ram", detailMap.get("ram"));
                            detailIntent.putExtra("cck", detailMap.get("cck"));
                            detailIntent.putExtra("zdcckzrl", detailMap.get("zdcckzrl"));
                            detailIntent.putExtra("zpmcc", detailMap.get("zpmcc"));
                            detailIntent.putExtra("fbl", detailMap.get("fbl"));
                            detailIntent.putExtra("pmxsmd", detailMap.get("pmxsmd"));
                            detailIntent.putExtra("pmczlx", detailMap.get("pmczlx"));
                            detailIntent.putExtra("qzsxt", detailMap.get("qzsxt"));
                            detailIntent.putExtra("sxtsl", detailMap.get("sxtsl"));
                            detailIntent.putExtra("hzsxt", detailMap.get("hzsxt"));
                            detailIntent.putExtra("dcrl", detailMap.get("dcrl"));
                            detailIntent.putExtra("dcsfkcx", detailMap.get("dcsfkcx"));
                            detailIntent.putExtra("cdq", detailMap.get("cdq"));
                            detailIntent.putExtra("sjcsjk", detailMap.get("sjcsjk"));
                            detailIntent.putExtra("nfc", detailMap.get("nfc"));
                            detailIntent.putExtra("ejjklx", detailMap.get("ejjklx"));
                            detailIntent.putExtra("cdjklx", detailMap.get("cdjklx"));
                            detailIntent.putExtra("zwsb", detailMap.get("zwsb"));
                            detailIntent.putExtra("gps", detailMap.get("gps"));
                            detailIntent.putExtra("tly", detailMap.get("tly"));
                            detailIntent.putExtra("cygn", detailMap.get("cygn"));
                            startActivity(detailIntent);
                        } else {
                            Toast.makeText(GoodsListActivity.this, "列表数据为空", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void fail(String failCode, String failMsg) {
                        // tvRequest.setText(request.getJsonStr());
                        // tvResponse.setText(failCode + "\n" + failMsg);
                        System.out.println("Fail:" + failMsg.toString());
                        DialogUtil.showHintDialog(GoodsListActivity.this, true, "登陆失败", URLDecoder.decode(failMsg), "关闭对话框", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LoadingDialogUtil.cancelLoading();
                                DialogUtil.dismissDialog();
                            }
                        });
                    }
                }, true);
                // list中已经变为存放详细信息
            }
        });
        try {
            initGoodsList();
        } catch (Exception e) {
            Log.e("Encode Error", e.toString());
        }
    }

    private void initGoodsList() throws Exception{
        CommonRequest request = new CommonRequest();
        if (typeCode.equals("1") || typeCode.equals("3")){
            // 品牌和颜色
            title = dataIntent.getStringExtra("chosen_type");
            request.addRequestParam("type", URLEncoder.encode(typeCode, "UTF-8"));
            request.addRequestParam("selectType", URLEncoder.encode(title, "UTF-8"));
            sendHttpPostRequest(URL_GOODSLIST01, request, new ResponseHandler() {
                @Override
                public void success(CommonResponse response) {
                    if (response.getDataList().size() > 0) {

                        idList.clear();
                        for (HashMap<String, String> map : response.getDataList()) {
                            idList.add(map.get("id"));
                        }

                        GoodsListAdapter adapter = new GoodsListAdapter(GoodsListActivity.this, response.getDataList());
                        goodsList.setAdapter(adapter);
                    } else {
                        Toast.makeText(GoodsListActivity.this, "无相关产品信息", Toast.LENGTH_SHORT).show();
                    }
                    LoadingDialogUtil.cancelLoading();

                }

                @Override
                public void fail(String failCode, String failMsg) {
                    LoadingDialogUtil.cancelLoading();
                }
            }, true);
        } else if (typeCode.equals("2") || typeCode.equals("4")) {
            // 屏幕尺寸和价格
            minSize = dataIntent.getStringExtra("min_size");
            maxSize = dataIntent.getStringExtra("max_size");
            request.addRequestParam("type", URLEncoder.encode(typeCode, "UTF-8"));
            request.addRequestParam("min", URLEncoder.encode(minSize, "UTF-8"));
            request.addRequestParam("max", URLEncoder.encode(maxSize, "UTF-8"));
            sendHttpPostRequest(URL_GOODSLIST02, request, new ResponseHandler() {
                @Override
                public void success(CommonResponse response) {
                    if (response.getDataList().size() > 0) {

                        idList.clear();
                        for (HashMap<String, String> map : response.getDataList()) {
                            idList.add(map.get("id"));
                        }
                        GoodsListAdapter adapter = new GoodsListAdapter(GoodsListActivity.this, response.getDataList());
                        goodsList.setAdapter(adapter);
                    } else {
                        Toast.makeText(GoodsListActivity.this, "无相关产品信息", Toast.LENGTH_SHORT).show();
                    }
                    LoadingDialogUtil.cancelLoading();
                }

                @Override
                public void fail(String failCode, String failMsg) {
                    LoadingDialogUtil.cancelLoading();
                }
            }, true);
        } else if (typeCode.equals("11")) {
            // 我的收藏
            TextView windowTitle = (TextView) findViewById(R.id.title_name);
            windowTitle.setText("我的收藏");
            pref = getSharedPreferences("user_data", MODE_PRIVATE);
            request.addRequestParam("user_phone", pref.getString("currentUser", ""));
            sendHttpPostRequest(URL_USER_COLLECTION, request, new ResponseHandler() {
                @Override
                public void success(CommonResponse response) {
                    if (response.getDataList().size() > 0) {
                        idList.clear();
                        for (HashMap<String, String> map : response.getDataList()) {
                            idList.add(map.get("id"));
                        }
                        GoodsListAdapter adapter = new GoodsListAdapter(GoodsListActivity.this, response.getDataList());
                        goodsList.setAdapter(adapter);
                    } else {
                        Toast.makeText(GoodsListActivity.this, "我还没有收藏呢", Toast.LENGTH_SHORT).show();
                    }
                    LoadingDialogUtil.cancelLoading();
                }

                @Override
                public void fail(String failCode, String failMsg) {
                    LoadingDialogUtil.cancelLoading();
                }
            }, true);
            goodsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final String idStr = idList.get(i).toString();
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(GoodsListActivity.this);
                    normalDialog.setIcon(R.drawable.delete);
                    normalDialog.setTitle("删除确认");
                    normalDialog.setMessage("您确定要删除吗？");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CommonRequest request = new CommonRequest();
                                    pref = getSharedPreferences("user_data", MODE_PRIVATE);
                                    String userPhone = pref.getString("currentUser", "");
                                    request.addRequestParam("type", "2");
                                    request.addRequestParam("sp_id", idStr);
                                    request.addRequestParam("user_phone", userPhone);
                                    new HttpPostTask(request, mHandler, new ResponseHandler() {
                                        @Override
                                        public void success(CommonResponse response) {
                                            Toast.makeText(GoodsListActivity.this, "已成功删除", Toast.LENGTH_SHORT).show();
                                            LoadingDialogUtil.cancelLoading();
                                        }

                                        @Override
                                        public void fail(String failCode, String failMsg) {
                                            Toast.makeText(GoodsListActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                            LoadingDialogUtil.cancelLoading();
                                        }
                                    }).execute(URL_DELETE);
                                    pref = getSharedPreferences("user_data", MODE_PRIVATE);
                                    request.addRequestParam("user_phone", pref.getString("currentUser", ""));
                                    sendHttpPostRequest(URL_USER_COLLECTION, request, new ResponseHandler() {
                                        @Override
                                        public void success(CommonResponse response) {
                                            if (response.getDataList().size() > 0) {
                                                idList.clear();
                                                for (HashMap<String, String> map : response.getDataList()) {
                                                    idList.add(map.get("id"));
                                                }
                                                GoodsListAdapter adapter = new GoodsListAdapter(GoodsListActivity.this, response.getDataList());
                                                goodsList.setAdapter(adapter);
                                            } else {
                                                Toast.makeText(GoodsListActivity.this, "列表数据为空", Toast.LENGTH_SHORT).show();
                                            }
                                            LoadingDialogUtil.cancelLoading();
                                        }

                                        @Override
                                        public void fail(String failCode, String failMsg) {
                                            LoadingDialogUtil.cancelLoading();
                                        }
                                    }, true);
                                }
                            });
                    normalDialog.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                }
                            });
                    // 显示
                    normalDialog.show();

                    return true;
                }
            });


        } else if (typeCode.equals("12")) {
            // 浏览记录
            TextView windowTitle = (TextView) findViewById(R.id.title_name);
            windowTitle.setText("浏览记录");
            pref = getSharedPreferences("user_data", MODE_PRIVATE);
            request.addRequestParam("user_phone", pref.getString("currentUser", ""));
            sendHttpPostRequest(URL_USER_RECORD, request, new ResponseHandler() {
                @Override
                public void success(CommonResponse response) {
                    if (response.getDataList().size() > 0) {
                        idList.clear();
                        for (HashMap<String, String> map : response.getDataList()) {
                            idList.add(map.get("id"));
                        }
                        GoodsListAdapter adapter = new GoodsListAdapter(GoodsListActivity.this, response.getDataList());
                        goodsList.setAdapter(adapter);
                    } else {
                        Toast.makeText(GoodsListActivity.this, "浏览记录为空", Toast.LENGTH_SHORT).show();
                    }
                    LoadingDialogUtil.cancelLoading();
                }

                @Override
                public void fail(String failCode, String failMsg) {
                    LoadingDialogUtil.cancelLoading();
                }
            }, true);
        } else {
            // 搜索结果
            Intent lastIntent = getIntent();
            String keyword = lastIntent.getStringExtra("keyword");
            request.addRequestParam("search", URLEncoder.encode(keyword, "UTF-8"));
            sendHttpPostRequest(URL_SEARCH, request, new ResponseHandler() {
                @Override
                public void success(CommonResponse response) {
                    if (response.getDataList().size() > 0) {

                        idList.clear();
                        for (HashMap<String, String> map : response.getDataList()) {
                            idList.add(map.get("id"));
                        }

                        GoodsListAdapter adapter = new GoodsListAdapter(GoodsListActivity.this, response.getDataList());
                        goodsList.setAdapter(adapter);
                    } else {
                        DialogUtil.showHintDialog(GoodsListActivity.this, "无相关产品信息", true);
                    }
                    LoadingDialogUtil.cancelLoading();
                }

                @Override
                public void fail(String failCode, String failMsg) {
                    LoadingDialogUtil.cancelLoading();
                }
            }, true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}
