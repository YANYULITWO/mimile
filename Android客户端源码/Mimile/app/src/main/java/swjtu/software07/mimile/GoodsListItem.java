package swjtu.software07.mimile;

/**
 * Created by zhuye on 2017/7/20.
 */

public class GoodsListItem {
    private int imageId;
    private String goodsName;
    private String goodsSale;
    private String goodsPrice;

    public GoodsListItem(String name, String sale, String price) {
        this.goodsName = name;
        this.goodsPrice = price;
        this.goodsSale = sale;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getGoodsSale() {
        return goodsSale;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public int getImageId() {
        return imageId;
    }
}
