package swjtu.software07.mimile;

/**
 * Created by zhuye on 2017/7/16.
 */

public class TypeSizeItem {
    private String sizeName;
    private String sizeDescription;
    private String sizeImage;

    public TypeSizeItem(String name, String description, String image) {
        this.sizeName = name;
        this.sizeDescription = description;
        this.sizeImage = image;
    }

    public String getSizeName() {
        return sizeName;
    }

    public String getSizeDescription() {
        return sizeDescription;
    }

    public String getSizeImage() {
        return sizeImage;
    }
}
