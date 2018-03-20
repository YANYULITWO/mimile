package swjtu.software07.mimile;

/**
 * Created by zhuye on 2017/7/16.
 */

public class TypeBrandItem {
    private String brandName;
    private String slogan;
    private int imageId;

    public TypeBrandItem(String brand, String slogan, int imageId) {
        this.brandName = brand;
        this.slogan = slogan;
        this.imageId = imageId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getSlogan() {
        return slogan;
    }

    public int getImageId() {
        return imageId;
    }
}
