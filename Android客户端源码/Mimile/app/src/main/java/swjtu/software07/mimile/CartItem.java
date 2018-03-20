package swjtu.software07.mimile;

/**
 * Created by zhuye on 2017/7/23.
 */

public class CartItem {
    private String name;
    private int imageId;
    private int amount;
    private String price;

    public CartItem(String name, String price, int amount, int imageId) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public int getImageId() {
        return imageId;
    }
}
