package swjtu.software07.mimile;

/**
 * Created by zhuye on 2017/7/14.
 */

public class TypeItem {
    private String name;
    private int imageId;

    public TypeItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }
}
