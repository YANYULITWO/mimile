package swjtu.software07.mimile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by zhuye on 2017/7/20.
 */

public class GoodsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> list;

    public GoodsListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.goods_list_item, parent, false);
            holder = new ViewHolder();
            holder.goodsName = (TextView) convertView.findViewById(R.id.goods_list_item_name);
            holder.goodsSale = (TextView) convertView.findViewById(R.id.goods_list_item_sale);
            holder.goodsPrice = (TextView) convertView.findViewById(R.id.goods_list_item_price);
            holder.goodsImage = (ImageView) convertView.findViewById(R.id.goods_list_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.goodsName.setText(map.get("name"));
        String zpmccTemp = map.get("zpmcc");
        String pattern = "[0-9]";
        boolean isMatch = Pattern.matches(pattern, zpmccTemp);
        if (zpmccTemp.equals("null")) {
            zpmccTemp = "不详";
        }
        if (isMatch) {
            zpmccTemp = zpmccTemp + ".0";
        }
        holder.goodsSale.setText(zpmccTemp + "英寸");
        holder.goodsPrice.setText("￥"+((map.get("price").equals("-1"))?"暂无报价":map.get("price")+".00"));
        Glide.with(context).load(map.get("img")).into(holder.goodsImage);

        return convertView;
    }

    private static class ViewHolder {
        private TextView goodsName;
        private TextView goodsSale;
        private TextView goodsPrice;
        private ImageView goodsImage;
    }
}
