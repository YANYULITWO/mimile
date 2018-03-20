package swjtu.software07.mimile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhuye on 2017/7/14.
 */

public class TypeAdapter extends ArrayAdapter<TypeItem> {
    private int resourceId;

    public TypeAdapter(Context context, int textViewResourceId, List<TypeItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TypeItem typeItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView typeImage = (ImageView) view.findViewById(R.id.type_image);
        TextView typeText = (TextView) view.findViewById(R.id.type_name);
        typeImage.setImageResource(typeItem.getImageId());
        typeText.setText(typeItem.getName());
        return view;
    }
}
