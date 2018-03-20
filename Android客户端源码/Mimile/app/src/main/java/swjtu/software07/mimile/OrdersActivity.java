package swjtu.software07.mimile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class OrdersActivity extends Activity {

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 去掉标题栏
        setContentView(R.layout.activity_orders);
        Intent getIntent = getIntent();
        title = getIntent.getStringExtra("title");
        TextView ordersTitle = (TextView) findViewById(R.id.orders_title_name);
        ordersTitle.setText(title);

        if (title.equals("未付款")) {

        } else if (title.equals("在途中")) {

        } else if (title.equals("未评价")) {

        } else if (title.equals("全部订单")) {

        } else {

        }

    }
}
