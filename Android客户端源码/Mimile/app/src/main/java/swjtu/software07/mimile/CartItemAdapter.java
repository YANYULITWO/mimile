package swjtu.software07.mimile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by zhuye on 2017/7/24.
 */

public class CartItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> list;

    public CartItemAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartItemAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);
            holder = new CartItemAdapter.ViewHolder();
            holder.cartItemName = (TextView) convertView.findViewById(R.id.cart_list_item_name);
            holder.cartItemPrice = (TextView) convertView.findViewById(R.id.cart_list_item_price);
            holder.cartItemAmount = (TextView) convertView.findViewById(R.id.cart_list_item_amount);
            holder.cartItemImage = (ImageView) convertView.findViewById(R.id.cart_list_item_image);
            convertView.setTag(holder);
        } else {
            holder = (CartItemAdapter.ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.cartItemName.setText(map.get("name"));
        holder.cartItemPrice.setText("￥"+((map.get("price").equals("-1"))?"暂无报价":map.get("price")+".00"));
        holder.cartItemAmount.setText(map.get("amount"));
        Glide.with(context).load(map.get("img")).into(holder.cartItemImage);

        return convertView;
    }

    private static class ViewHolder {
        private TextView cartItemName;
        private TextView cartItemAmount;
        private TextView cartItemPrice;
        private ImageView cartItemImage;
    }
}
