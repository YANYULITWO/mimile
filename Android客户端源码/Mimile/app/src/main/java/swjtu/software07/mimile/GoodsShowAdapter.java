package swjtu.software07.mimile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhuye on 2017/7/22.
 */

public class GoodsShowAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> list;

    public GoodsShowAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
        GoodsShowAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.goods_show_item, parent, false);
            holder = new GoodsShowAdapter.ViewHolder();
            holder.goodsName = (TextView) convertView.findViewById(R.id.goods_list_item_name);
            holder.goodsSale = (TextView) convertView.findViewById(R.id.goods_list_item_sale);
            holder.goodsPrice = (TextView) convertView.findViewById(R.id.goods_list_item_price);
            holder.goodsImage = (ImageView) convertView.findViewById(R.id.goods_list_item_image);
            convertView.setTag(holder);
        } else {
            holder = (GoodsShowAdapter.ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.goodsName.setText(map.get("name"));
        holder.goodsSale.setText((map.get("zpmcc").equals("null"))?"不详":(map.get("zpmcc") + "英寸"));
        holder.goodsPrice.setText("￥"+((map.get("price").equals("-1"))?"暂无报价":map.get("price"))+".00");
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
