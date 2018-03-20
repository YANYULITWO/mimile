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

public class TypeOSAdapter extends ArrayAdapter<TypeOSItem>{
    private int resourceId;

    public TypeOSAdapter(Context context, int textViewResourceId, List<TypeOSItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TypeOSItem typeOSItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView typeOSImage = (ImageView) view.findViewById(R.id.type_os_image);
        TextView typeOSName = (TextView) view.findViewById(R.id.type_os_name);
        TextView typeOSSummary = (TextView) view.findViewById(R.id.type_os_summary);
        typeOSName.setText(typeOSItem.getOSName());
        typeOSSummary.setText(typeOSItem.getOSsummary());
        typeOSImage.setImageResource(typeOSItem.getImageId());

        return view;
    }
}
