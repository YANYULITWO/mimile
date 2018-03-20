package swjtu.software07.mimile;

/**
 * Created by zhuye on 2017/7/16.
 */

public class TypeColorItem {
    private String colorName;
    private String colorExamples;
    private int imageId;

    public TypeColorItem(String name, String examples, int imageId) {
        this.colorName = name;
        this.colorExamples = examples;
        this.imageId = imageId;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorExamples() {
        return colorExamples;
    }

    public int getImageId() {
        return imageId;
    }
}
