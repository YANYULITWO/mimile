package swjtu.software07.mimile;

/**
 * Created by zhuye on 2017/7/16.
 */

public class TypeOSItem {
    private String OSName;
    private String OSsummary;
    private int imageId;

    public TypeOSItem(String name, String summary, int imageId) {
        this.OSName = name;
        this.OSsummary = summary;
        this.imageId = imageId;
    }

    public String getOSName() {
        return OSName;
    }

    public String getOSsummary() {
        return OSsummary;
    }

    public int getImageId() {
        return imageId;
    }
}
