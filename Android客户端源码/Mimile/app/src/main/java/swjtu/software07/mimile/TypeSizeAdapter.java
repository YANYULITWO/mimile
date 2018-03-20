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
 * Created by zhuye on 2017/7/16.
 */

public class TypeSizeAdapter extends ArrayAdapter<TypeSizeItem> {
    private int resourceId;

    public TypeSizeAdapter(Context context, int textViewResourceId, List<TypeSizeItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TypeSizeItem typeSizeItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView typeSizeName = (TextView) view.findViewById(R.id.type_size_name);
        TextView typeSizeDescription = (TextView) view.findViewById(R.id.type_size_description);
        TextView typeSizeImage = (TextView) view.findViewById(R.id.type_size_image);
        typeSizeName.setText(typeSizeItem.getSizeName());
        typeSizeDescription.setText(typeSizeItem.getSizeDescription());
        typeSizeImage.setText(typeSizeItem.getSizeImage());

        return view;
    }
}
