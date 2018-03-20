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

public class TypeColorAdapter extends ArrayAdapter<TypeColorItem> {
    private int resourceId;

    public TypeColorAdapter(Context context, int textViewResourceId, List<TypeColorItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TypeColorItem typeColorItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView typeColorImage = (ImageView) view.findViewById(R.id.type_color_image);
        TextView typeColorName = (TextView) view.findViewById(R.id.type_color_name);
        TextView typeColorExamples = (TextView) view.findViewById(R.id.type_color_examples);
        typeColorName.setText(typeColorItem.getColorName());
        typeColorExamples.setText(typeColorItem.getColorExamples());
        typeColorImage.setImageResource(typeColorItem.getImageId());
        return view;
    }
}
