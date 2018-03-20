package swjtu.software07.mimile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhuye on 2017/7/16.
 */

public class TypeBrandAdapter extends ArrayAdapter<TypeBrandItem> {
    private int resourceId;

    public TypeBrandAdapter(Context context, int textViewResourceId, List<TypeBrandItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TypeBrandItem typeBrandItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView typeBrandImage = (ImageView) view.findViewById(R.id.type_brand_image);
        TextView typeBrandName = (TextView) view.findViewById(R.id.type_brand_name);
        TextView typeBrandSlogan = (TextView) view.findViewById(R.id.type_brand_slogan);
        typeBrandName.setText(typeBrandItem.getBrandName());
        typeBrandSlogan.setText(typeBrandItem.getSlogan());
        typeBrandImage.setImageResource(typeBrandItem.getImageId());
        return view;
    }
}
