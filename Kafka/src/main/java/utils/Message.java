package utils;

import java.io.File;

/**
 * Created by Administrator on 2018/6/8 0008.
 */
public class Message {

    public static final String ERROR_ENCODING = "错误的编码类型";

    public static final String UNFIND_FILE = "无法加载配置文件";

    public static final String CSV_HEADER = "plantform_name,channel_name,shop_kdtId,shop_name,product_alias," +
            "product_name,product_category_name,product_price,product_origin_price,product_brand,product_total_stock,"+
            "product_url,product_create_time,product_update_time,product_image_url,order_nickname,order_item_num," +
            "order_total_price,order_origin_time,order_time,create_time,run_time";


    public static final String ERROR_JSON = "解析Json出错";

//    用于存放数据的文件夹
    public static final String DIRNAME = "data" + File.separator + "src";

    public static final String SUFFIX = ".csv";
}
