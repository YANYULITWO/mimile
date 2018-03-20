package swjtu.software07.mimile;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.design.widget.TabLayout;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangj.baselibrary.activity.BaseActivity;
import com.wangj.baselibrary.http.Constant;
import com.wangj.baselibrary.http.HttpPostTask;
import com.wangj.baselibrary.http.bean.CommonRequest;
import com.wangj.baselibrary.http.bean.CommonResponse;
import com.wangj.baselibrary.http.interf.ResponseHandler;
import com.wangj.baselibrary.util.*;
import com.wangj.baselibrary.util.LoadingDialogUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * 主活动，用于控制首页/分类/购物车/我的四个视图，包括底部菜单栏的点击事件和各个子视图的内部事件
 */

public class MainActivity extends Activity {

    private String SERVLET = "http://119.29.205.70:8080/";
    private String URL_HOMESHOW = SERVLET + "MML/homePageServlet";
    private String URL_DETAIL = SERVLET + "MML/returnDetailInfoServlet";
    private String URL_CARTLIST = SERVLET + "MML/returnShoppingCartInfoServlet";
    private String URL_DELETE = SERVLET + "MML/addIntoShoppingCartServlet";

    private ArrayList<View> pageView;           // 包括首页/分类/购物车/我的四个视图集
    private ArrayList<View> typeListPageView;   // 包括品牌/屏幕尺寸/机身颜色/系统四个视图集

    // 分类各子视图中List的子项集
    private List<TypeBrandItem> brandTypes = new ArrayList<>();
    private List<TypeColorItem> colorTypes = new ArrayList<>();
    private List<TypeOSItem> osTypes = new ArrayList<>();
    private List<TypeSizeItem> sizeTypes = new ArrayList<>();

    SpecialViewPager viewPager;               // 主界面左右视图切换控制
    SpecialViewPager typeListViewPager;       // 分类界面左右视图控制

    // 主界面的四个子视图
    View homeView;
    View typeListView;
    View cartView;
    View userView;

    // 分类界面的四个子视图
    View typeListBrandView;
    View typeListScreenSizeView;
    View typeListColorView;
    View typeListOSView;


    private SharedPreferences pref;     // 用于区分是否已登录
    private String userPhone = "";

    EditText searchText;

    Vector<String> idList;
    Vector<String> cartList;

    /**
     * 底部菜单栏事件响应函数
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_typeList:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_cart:
                    initCartShow();
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_user:
                    ScrollView userScrollView = (ScrollView) userView.findViewById(R.id.user_scroll_layout);
                    userScrollView.fullScroll(View.FOCUS_UP);
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }

    };

    /**
     * 主活动创建函数
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                 // 调用父类方法
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 去掉标题栏
        setContentView(R.layout.activity_main);             // 使用布局构建视图

        // 创建底部菜单栏对象实例并设置监听事件响应函数
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 初始化主界面左右滑动视图控制器
        initViewPager();

        searchText = (EditText) homeView.findViewById(R.id.search_edit_text);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    Intent searchIntent = new Intent(MainActivity.this, GoodsListActivity.class);
                    String keyword = searchText.getText().toString();
                    searchIntent.putExtra("chosen_code", "0");
                    searchIntent.putExtra("keyword", keyword);
                    startActivity(searchIntent);
                    return true;
                }
                return false;
            }
        });

        // 创建Home界面搜索按钮对象实例并设置监听事件，跳转到搜索活动
        final Button searchButton = (Button) homeView.findViewById(R.id.new_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(MainActivity.this, GoodsListActivity.class);
                String keyword = searchText.getText().toString();
                searchIntent.putExtra("chosen_code", "0");
                searchIntent.putExtra("keyword", keyword);
                startActivity(searchIntent);
            }
        });

        // 初始化用户名(如果有)
        initUserTitle();
        // 初始化typeList界面上部菜单栏
        initTypeListTopbar();
        // 创建并初始化typeList界面的四个子视图
        initBrandList();
        initSizeList();
        initColorList();
        initOSList();
        // 创建并初始化cart界面的商品列表视图
        initCartList();
        // 主界面商品随机显示
        initHomeShowList();

        // 初始化user界面用户头像按钮并设置监听事件, 跳转到登录界面/用户详情
        Button userAvatarButton = (Button) userView.findViewById(R.id.user_avatar_image);
        userAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhone = pref.getString("currentUser", "");
                if (userPhone.equals("")) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(loginIntent, 102);
                }
            }
        });

        // 初始化user界面用户名Text(未登录时提示登录/已登录显示用户名)并设置监听事件, 跳转到登录界面/用户详情
        TextView userAvatarTextView = (TextView) userView.findViewById(R.id.user_name_text_view);
        userAvatarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhone = pref.getString("currentUser", "");
                if (userPhone.equals("")) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(loginIntent, 102);
                }
            }
        });

        // 初始化退出登录按钮并设置监听事件, 弹出提示对话框进行确认
        TextView logoutText = (TextView) userView.findViewById(R.id.logout_text);
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhone = "";
                SharedPreferences.Editor editor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
                editor.remove("phoneNumber");
                editor.remove("password");
                editor.remove("remember_password");
                editor.remove("currentUser");
                editor.apply();
                TextView userTitle = (TextView) userView.findViewById(R.id.user_name_text_view);
                userTitle.setText("点击登录");
                LinearLayout logoutLayout = (LinearLayout) userView.findViewById(R.id.logout_layout);
                logoutLayout.setVisibility(View.GONE);
            }
        });

        // 创建user界面上排四个按钮的实例并设置响应事件
        final LinearLayout userPoints = (LinearLayout) userView.findViewById(R.id.user_points);
        LinearLayout userAddress = (LinearLayout) userView.findViewById(R.id.user_address);
        LinearLayout userCollection = (LinearLayout) userView.findViewById(R.id.user_collection);
        LinearLayout userRecord = (LinearLayout) userView.findViewById(R.id.user_record);
        userPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pointsIntent = new Intent(MainActivity.this, UserPointsActivity.class);
                startActivity(pointsIntent);
            }
        });
        userAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addressIntent = new Intent(MainActivity.this, UserAddressActivity.class);
                startActivity(addressIntent);
            }
        });
        // 我的收藏
        userCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goodsListIntent = new Intent(MainActivity.this, GoodsListActivity.class);
                goodsListIntent.putExtra("chosen_code", "11");
                startActivity(goodsListIntent);

            }
        });
        userRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goodsListIntent = new Intent(MainActivity.this, GoodsListActivity.class);
                goodsListIntent.putExtra("chosen_code", "12");
                startActivity(goodsListIntent);
            }
        });

        // 创建订单四个按钮的实例并设置响应事件
        LinearLayout ordersUnpayed = (LinearLayout) userView.findViewById(R.id.user_order_unPayed);
        LinearLayout ordersUnReceived = (LinearLayout) userView.findViewById(R.id.user_order_unReceived);
        LinearLayout ordersUnRated = (LinearLayout) userView.findViewById(R.id.user_order_unrated);
        LinearLayout ordersAll = (LinearLayout) userView.findViewById(R.id.user_order_all);
        ordersUnpayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ordersIntent = new Intent(MainActivity.this, OrdersActivity.class);
                ordersIntent.putExtra("title", "未付款");
                startActivity(ordersIntent);
            }
        });
        ordersUnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ordersIntent = new Intent(MainActivity.this, OrdersActivity.class);
                ordersIntent.putExtra("title", "在途中");
                startActivity(ordersIntent);
            }
        });
        ordersUnRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ordersIntent = new Intent(MainActivity.this, OrdersActivity.class);
                ordersIntent.putExtra("title", "未评价");
                startActivity(ordersIntent);
            }
        });
        ordersAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ordersIntent = new Intent(MainActivity.this, OrdersActivity.class);
                ordersIntent.putExtra("title", "全部订单");
                startActivity(ordersIntent);
            }
        });

        // 账户安全
        LinearLayout userSafety = (LinearLayout) userView.findViewById(R.id.user_safety);
        userSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent safetyIntent = new Intent(MainActivity.this, UserSafetyActivity.class);
                startActivity(safetyIntent);
            }
        });

        // 设置
        LinearLayout settings = (LinearLayout) userView.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(MainActivity.this, UserSettingsActivity.class);
                startActivity(settings);
            }
        });

        ListView cartListView = (ListView) cartView.findViewById(R.id.cart_list_view);
        cartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommonRequest request = new CommonRequest();
                final String idStr = cartList.get(i).toString();
                pref = getSharedPreferences("user_data", MODE_PRIVATE);
                userPhone = pref.getString("currentUser", "");
                request.addRequestParam("sp_id", idStr);
                request.addRequestParam("user_phone", userPhone);
                new HttpPostTask(request, mHandler, new ResponseHandler() {
                    @Override
                    public void success(CommonResponse response) {
                        if (response.getDataList().size() > 0) {
                            Intent detailIntent = new Intent(MainActivity.this, ProductDetailActivity.class);
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
                            DialogUtil.showHintDialog(MainActivity.this, "列表数据为空", true);
                        }
                        com.wangj.baselibrary.util.LoadingDialogUtil.cancelLoading();
                    }

                    @Override
                    public void fail(String failCode, String failMsg) {
                        LoadingDialogUtil.cancelLoading();
                    }
                }).execute(URL_DETAIL);
            }
        });
        cartListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String idStr = cartList.get(i).toString();
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setIcon(R.drawable.delete);
                normalDialog.setTitle("删除确认");
                normalDialog.setMessage("您确定要删除吗？");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommonRequest request = new CommonRequest();
                                pref = getSharedPreferences("user_data", MODE_PRIVATE);
                                userPhone = pref.getString("currentUser", "");
                                request.addRequestParam("type", "2");
                                request.addRequestParam("sp_id", idStr);
                                request.addRequestParam("user_phone", userPhone);
                                request.addRequestParam("sp_amount", "");
                                new HttpPostTask(request, mHandler, new ResponseHandler() {
                                    @Override
                                    public void success(CommonResponse response) {
                                        Toast.makeText(MainActivity.this, "已成功删除", Toast.LENGTH_SHORT).show();
                                        LoadingDialogUtil.cancelLoading();
                                    }

                                    @Override
                                    public void fail(String failCode, String failMsg) {
                                        Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                        LoadingDialogUtil.cancelLoading();
                                    }
                                }).execute(URL_DELETE);
                                initCartShow();
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

        GridView showGoods = (GridView) homeView.findViewById(R.id.show_home_list_view);
        showGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // System.out.println(idList.get(i).toString());
                CommonRequest request = new CommonRequest();
                final String idStr = idList.get(i).toString();
                pref = getSharedPreferences("user_data", MODE_PRIVATE);
                userPhone = pref.getString("currentUser", "");
                request.addRequestParam("sp_id", idStr);
                request.addRequestParam("user_phone", userPhone);
                new HttpPostTask(request, mHandler, new ResponseHandler() {
                    @Override
                    public void success(CommonResponse response) {
                        if (response.getDataList().size() > 0) {
                            Intent detailIntent = new Intent(MainActivity.this, ProductDetailActivity.class);
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
                            DialogUtil.showHintDialog(MainActivity.this, "列表数据为空", true);
                        }
                        com.wangj.baselibrary.util.LoadingDialogUtil.cancelLoading();

                    }

                    @Override
                    public void fail(String failCode, String failMsg) {
                        LoadingDialogUtil.cancelLoading();
                    }
                }).execute(URL_DETAIL);
            }
        });
    }

    /**
     * 初始化ViewPager，建立四个对应的View并设置适配器
     */
    private void initViewPager() {
        viewPager = (SpecialViewPager) findViewById(R.id.viewPager);    // 创建主界面ViewPager对象实例
        viewPager.setNoScroll(true);                // 禁止滑动
        LayoutInflater inflater = getLayoutInflater();

        // 使用主界面各子视图的布局创建各视图的对象实例
        homeView = inflater.inflate(R.layout.show_home, null);
        typeListView = inflater.inflate(R.layout.show_typelist, null);
        cartView = inflater.inflate(R.layout.show_cart, null);
        userView = inflater.inflate(R.layout.show_user, null);

        // 将主界面各子视图添加到视图集中
        pageView =new ArrayList<>();
        pageView.add(homeView);
        pageView.add(typeListView);
        pageView.add(cartView);
        pageView.add(userView);

        // 添加主界面ViewPager的适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pageView.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(pageView.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(pageView.get(position));
                return pageView.get(position);
            }
        };

        // 绑定主界面ViewPager适配器
        viewPager.setAdapter(mPagerAdapter);
        // 设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
    }


    private void initTypeListTopbar() {
        final TabLayout typeListTabLayout = (TabLayout) typeListView.findViewById(R.id.type_tab);
        typeListViewPager = (SpecialViewPager) typeListView.findViewById(R.id.type_list_view_pager);
        typeListTabLayout.addTab(typeListTabLayout.newTab().setText("常见品牌"));
        typeListTabLayout.addTab(typeListTabLayout.newTab().setText("屏幕尺寸"));
        typeListTabLayout.addTab(typeListTabLayout.newTab().setText("机身颜色"));
        typeListTabLayout.addTab(typeListTabLayout.newTab().setText("价格区间"));
        typeListTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                typeListViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });


        //
        LayoutInflater inflater = getLayoutInflater();
        typeListBrandView = inflater.inflate(R.layout.typelist_brand, null);
        typeListScreenSizeView = inflater.inflate(R.layout.typelist_screensize, null);
        typeListColorView = inflater.inflate(R.layout.typelist_color, null);
        typeListOSView = inflater.inflate(R.layout.typelist_os, null);

        typeListPageView =new ArrayList<>();
        //添加想要切换的界面
        typeListPageView.add(typeListBrandView);
        typeListPageView.add(typeListScreenSizeView);
        typeListPageView.add(typeListColorView);
        typeListPageView.add(typeListOSView);

        PagerAdapter mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return typeListPageView.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(typeListPageView.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(typeListPageView.get(position));
                return typeListPageView.get(position);
            }
        };

        // 绑定适配器
        typeListViewPager.setAdapter(mPagerAdapter);
        // 设置viewPager的初始界面为第一个界面
        typeListViewPager.setCurrentItem(0);
        // 为typeList添加响应事件, 使得上部导航栏可以与ViewPager实现同步
        typeListViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                typeListTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });



    }

    private void initUserTitle() {
        SharedPreferences.Editor editor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
        editor.remove("currentUser");
        editor.apply();
        pref = getSharedPreferences("user_data", MODE_PRIVATE);
        final boolean isRemember =  pref.getBoolean("remember_password", false);
        LinearLayout logoutLayout = (LinearLayout) userView.findViewById(R.id.logout_layout);

        if (isRemember) {
            String phoneNumber = pref.getString("phoneNumber", "");
            String userTitleString = "用户" + phoneNumber;
            TextView userTitle = (TextView) userView.findViewById(R.id.user_name_text_view);
            userTitle.setText(userTitleString);
            editor.putString("currentUser", phoneNumber);
            editor.apply();
            userPhone = phoneNumber;
            Log.d("MainActivity", "用户名：" + phoneNumber);
            logoutLayout.setVisibility(View.VISIBLE);
        } else {
            logoutLayout.setVisibility(View.GONE);
        }
    }


    /**
     * 初始化主界面随机显示
     */

    private void initHomeShowList() {
        CommonRequest request = new CommonRequest();
        new HttpPostTask(request, mHandler, new ResponseHandler() {
            @Override
            public void success(CommonResponse response) {
                if (response.getDataList().size() > 0) {
                    idList = new Vector<>();
                    idList.clear();
                    for (HashMap<String, String> map : response.getDataList()) {
                        idList.add(map.get("id"));
                    }
                    System.out.print(idList.toString());
                    GridView homeShowList = (GridView) homeView.findViewById(R.id.show_home_list_view);
                    GoodsShowAdapter adapter = new GoodsShowAdapter(MainActivity.this, response.getDataList());
                    homeShowList.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "列表数据为空", Toast.LENGTH_SHORT).show();
                }
                com.wangj.baselibrary.util.LoadingDialogUtil.cancelLoading();

            }

            @Override
            public void fail(String failCode, String failMsg) {
                LoadingDialogUtil.cancelLoading();
            }
        }).execute(URL_HOMESHOW);

    }

    private void initCartShow() {
        pref = getSharedPreferences("user_data", MODE_PRIVATE);
        userPhone = pref.getString("currentUser", "");
        if (userPhone.equals("")) {
            initCartList();
        } else {
            CommonRequest request = new CommonRequest();
            request.addRequestParam("user_phone", userPhone);
            new HttpPostTask(request, mHandler, new ResponseHandler() {
                @Override
                public void success(CommonResponse response) {
                    if (response.getDataList().size() > 0) {
                        ImageView blankCartIcon = (ImageView) cartView.findViewById(R.id.cart_blank_icon);
                        blankCartIcon.setVisibility(View.GONE);
                        TextView blankCartText = (TextView) cartView.findViewById(R.id.cart_blank_text);
                        blankCartText.setVisibility(View.GONE);
                        ListView cartListView = (ListView) cartView.findViewById(R.id.cart_list_view);
                        cartListView.setVisibility(View.VISIBLE);
                        cartList = new Vector<>();
                        cartList.clear();
                        for (HashMap<String, String> map : response.getDataList()) {
                            cartList.add(map.get("id"));
                        }
                        System.out.print(cartList.toString());
                        CartItemAdapter adapter = new CartItemAdapter(MainActivity.this, response.getDataList());
                        cartListView.setAdapter(adapter);

                    } else {
                        ImageView blankCartIcon = (ImageView) cartView.findViewById(R.id.cart_blank_icon);
                        blankCartIcon.setVisibility(View.VISIBLE);
                        TextView blankCartText = (TextView) cartView.findViewById(R.id.cart_blank_text);
                        blankCartText.setText("购物车为空");
                        blankCartText.setVisibility(View.VISIBLE);
                        ListView cartListView = (ListView) cartView.findViewById(R.id.cart_list_view);
                        cartListView.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "列表数据为空", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void fail(String failCode, String failMsg) {
                    LoadingDialogUtil.cancelLoading();
                }
            }).execute(URL_CARTLIST);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == Constant.HANDLER_HTTP_SEND_FAIL) {
                LogUtil.logErr(msg.obj.toString());

                LoadingDialogUtil.cancelLoading();
                DialogUtil.showHintDialog(MainActivity.this, "请求发送失败，请重试", true);
            } else if (msg.what == Constant.HANDLER_HTTP_RECEIVE_FAIL) {
                LogUtil.logErr(msg.obj.toString());

                LoadingDialogUtil.cancelLoading();
                DialogUtil.showHintDialog(MainActivity.this, "请求接受失败，请重试", true);
            }
        }
    };



    /**
     * 初始化商品按品牌分类列表
     */
    private void initBrandTypes() {
        brandTypes.add(new TypeBrandItem("Apple", "不同凡想, 如7而至", R.drawable.brand_apple));
        brandTypes.add(new TypeBrandItem("华为", "革命性的安卓体验", R.drawable.brand_huawei));
        brandTypes.add(new TypeBrandItem("小米", "为发烧而生", R.drawable.brand_xiaomi));
        brandTypes.add(new TypeBrandItem("魅族", "为梦想而立", R.drawable.brand_meizu));
        brandTypes.add(new TypeBrandItem("OPPO", "拍你想拍的", R.drawable.brand_oppo));
        brandTypes.add(new TypeBrandItem("VIVO", "照亮你的美", R.drawable.brand_vivo));
        brandTypes.add(new TypeBrandItem("三星", "突破所限, 大有可能",R.drawable.brand_samsung));
        brandTypes.add(new TypeBrandItem("HTC", "大热爱, 小自在",R.drawable.brand_htc));
        brandTypes.add(new TypeBrandItem("锤子", "漂亮的不像实力派",R.drawable.brand_chuizi));
        brandTypes.add(new TypeBrandItem("中兴", "快意弹指间",R.drawable.brand_zhongxing));
        brandTypes.add(new TypeBrandItem("努比亚", "个性,是一种生活态度", R.drawable.brand_nubia));
        brandTypes.add(new TypeBrandItem("摩托罗拉", "HELLO MOTO", R.drawable.brand_motorola));
    }

    private void initBrandList() {
        initBrandTypes();
        TypeBrandAdapter brandAdapter = new TypeBrandAdapter(MainActivity.this, R.layout.type_brand_item, brandTypes);
        final ListView brandListView = (ListView) typeListBrandView.findViewById(R.id.brand_list_view);
        brandListView.setAdapter(brandAdapter);
        brandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goodsListIntent = new Intent(MainActivity.this, GoodsListActivity.class);
                TypeBrandItem tempItem = brandTypes.get(i);
                goodsListIntent.putExtra("chosen_code", "1");
                goodsListIntent.putExtra("chosen_type", tempItem.getBrandName());
                startActivity(goodsListIntent);

            }
        });
    }


    private void initSizeTypes() {
        sizeTypes.add(new TypeSizeItem("3.5英寸及以下", "诺基亚3310  飞利浦E106 ", "XS"));
        sizeTypes.add(new TypeSizeItem("3.6 - 4.5英寸", "iPhone SE  iPhone 5s", "S"));
        sizeTypes.add(new TypeSizeItem("4.6 - 4.9英寸", "iPhone 7 华为nova2 小米4A", "M"));
        sizeTypes.add(new TypeSizeItem("5.0 - 5.4英寸", "小米6  荣耀9  魅族PRO6s", "L"));
        sizeTypes.add(new TypeSizeItem("5.5 - 5.9英寸", "iPhone 7Plus  荣耀V9  魅族PRO6 Plus", "XL"));
        sizeTypes.add(new TypeSizeItem("6.0英寸及以上", "小米Max2 三星S8+", "XXL"));
    }

    private void initSizeList() {
        initSizeTypes();
        TypeSizeAdapter sizeAdapter = new TypeSizeAdapter(MainActivity.this, R.layout.type_size_item, sizeTypes);
        ListView sizeListView = (ListView) typeListScreenSizeView.findViewById(R.id.size_list_view);
        sizeListView.setAdapter(sizeAdapter);
        sizeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goodsListIntent = new Intent(MainActivity.this, GoodsListActivity.class);
                TypeSizeItem tempItem = sizeTypes.get(i);

                goodsListIntent.putExtra("chosen_code", "2");
                String minSize = new String();
                String maxSize = new String();
                if (i == 0) {
                    minSize = "0.0";
                    maxSize = "3.5";
                } else if (i == 1) {
                    minSize = "3.5";
                    maxSize = "4.5";
                } else if (i == 2) {
                    minSize = "4.5";
                    maxSize = "5.0";
                } else if (i == 3) {
                    minSize = "5.0";
                    maxSize = "5.5";
                } else if (i == 4) {
                    minSize = "5.5";
                    maxSize = "6.0";
                } else if (i == 5) {
                    minSize = "6.0";
                    maxSize = "10.0";
                } else {
                    minSize = "0.0";
                    maxSize = "10.0";
                }
                goodsListIntent.putExtra("min_size", minSize);
                goodsListIntent.putExtra("max_size", maxSize);
                startActivity(goodsListIntent);
            }
        });
    }

    /**
     * 初始化
     */
    private void initColorTypes() {
        colorTypes.add(new TypeColorItem("黑色系", "亮黑 星空黑 曜石黑 迷夜黑 玄金黑 慕斯黑", R.drawable.pure_color_block_black));
        colorTypes.add(new TypeColorItem("灰色系", "星空灰 银钻灰 暗夜灰 苍穹灰", R.drawable.pure_color_block_gray));
        colorTypes.add(new TypeColorItem("银色系", "月光银 珍珠银 炫酷银 耀然银", R.drawable.pure_color_block_silver));
        colorTypes.add(new TypeColorItem("白色系", "珠光白 雪晶白 陶瓷白 月光白", R.drawable.pure_color_block_white));
        colorTypes.add(new TypeColorItem("金色系", "铂光金 香槟金 流光金 原力金", R.drawable.pure_color_block_gold));
        colorTypes.add(new TypeColorItem("粉色系", "樱语粉 爱慕粉 樱花粉", R.drawable.pure_color_block_pink));
        colorTypes.add(new TypeColorItem("蓝色系", "魅海蓝 极光蓝 活力蓝 魅惑蓝 深海蓝", R.drawable.pure_color_block_blue));
        colorTypes.add(new TypeColorItem("红色系", "酒红 中国红 玛瑙红", R.drawable.pure_color_block_red));
        colorTypes.add(new TypeColorItem("其他颜色", "草木绿 军绿 幻紫", R.drawable.color_circle_rainbow));
    }

    private void initColorList() {
        initColorTypes();
        TypeColorAdapter colorAdapter = new TypeColorAdapter(MainActivity.this, R.layout.type_color_item, colorTypes);
        ListView colorListView = (ListView) typeListColorView.findViewById(R.id.color_list_view);
        colorListView.setAdapter(colorAdapter);
        colorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goodsListIntent = new Intent(MainActivity.this, GoodsListActivity.class);
                TypeColorItem tempItem = colorTypes.get(i);
                String colorName = new String();
                if (i == 0) {
                    colorName = "黑";
                } else if (i == 1) {
                    colorName = "灰";
                } else if (i == 2) {
                    colorName = "银";
                } else if (i == 3) {
                    colorName = "白";
                } else if (i == 4) {
                    colorName = "金";
                } else if (i == 5) {
                    colorName = "粉";
                } else if (i == 6) {
                    colorName = "蓝";
                } else if (i == 7) {
                    colorName = "红";
                } else if (i == 8) {
                    colorName = "其他";
                }
                goodsListIntent.putExtra("chosen_code", "3");
                goodsListIntent.putExtra("chosen_type", colorName);
                startActivity(goodsListIntent);
            }
        });
    }

    private void initOSTypes() {
        osTypes.add(new TypeOSItem("￥0 - ￥999", "便宜好用", R.drawable.price_1));
        osTypes.add(new TypeOSItem("￥1000 - ￥1999", "性价比之选", R.drawable.price_2));
        osTypes.add(new TypeOSItem("￥2000 - ￥2999", "明智之选", R.drawable.price_3));
        osTypes.add(new TypeOSItem("￥3000 - ￥3999", "性能怪兽", R.drawable.price_4));
        osTypes.add(new TypeOSItem("￥4000 - ￥4999", "高端旗舰", R.drawable.price_5));
        osTypes.add(new TypeOSItem("￥5000以上", "土豪之选", R.drawable.price_6));
    }

    private void initOSList() {
        initOSTypes();
        TypeOSAdapter osAdapter = new TypeOSAdapter(MainActivity.this, R.layout.type_os_item, osTypes);
        ListView osListView = (ListView) typeListOSView.findViewById(R.id.os_list_view);
        osListView.setAdapter(osAdapter);
        osListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goodsListIntent = new Intent(MainActivity.this, GoodsListActivity.class);
                TypeOSItem tempItem = osTypes.get(i);
                String minPrice = new String();
                String maxPrice = new String();
                if (i == 0) {
                    minPrice = "0";
                    maxPrice = "999";
                } else if (i == 1) {
                    minPrice = "1000";
                    maxPrice = "1999";
                } else if (i == 2) {
                    minPrice = "2000";
                    maxPrice = "2999";
                } else if (i == 3) {
                    minPrice = "3000";
                    maxPrice = "3999";
                } else if (i == 4) {
                    minPrice = "4000";
                    maxPrice = "4999";
                } else if (i == 5) {
                    minPrice = "5000";
                    maxPrice = "10000";
                } else {
                    minPrice = "0";
                    maxPrice = "10000";
                }
                goodsListIntent.putExtra("chosen_code", "4");
                goodsListIntent.putExtra("min_size", minPrice);
                goodsListIntent.putExtra("max_size", maxPrice);
                startActivity(goodsListIntent);
            }
        });
    }



    private void initCartList() {
        // 初始化购物车界面列表
        ImageView blankCartIcon = (ImageView) cartView.findViewById(R.id.cart_blank_icon);
        blankCartIcon.setVisibility(View.VISIBLE);
        TextView blankCartText = (TextView) cartView.findViewById(R.id.cart_blank_text);
        blankCartText.setText("请先登录");
        blankCartText.setVisibility(View.VISIBLE);
        ListView cartListView = (ListView) cartView.findViewById(R.id.cart_list_view);
        cartListView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:
                pref = getSharedPreferences("user_data", MODE_PRIVATE);
                LinearLayout logoutLayout = (LinearLayout) userView.findViewById(R.id.logout_layout);
                TextView userTitle = (TextView) userView.findViewById(R.id.user_name_text_view);

                userPhone = pref.getString("currentUser", "");
                if (userPhone.equals("")) {
                    userTitle.setText("点击登录");
                    logoutLayout.setVisibility(View.GONE);
                } else {
                    String userTitleString = "用户" + userPhone;
                    userTitle.setText(userTitleString);
                    logoutLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(R.drawable.dialog_note)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pref = getSharedPreferences("user_data", MODE_PRIVATE);
                        final boolean isRemember =  pref.getBoolean("remember_password", false);
                        if (isRemember) {

                        } else {
                            SharedPreferences.Editor editor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
                            editor.remove("phoneNumber");
                            editor.remove("password");
                            editor.remove("remember_password");
                            editor.apply();
                        }
                        finish();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }
}
