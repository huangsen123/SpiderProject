# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class YouZanItem(scrapy.Item):
    #平台名称
    plantform_name = scrapy.Field()
    # 渠道名称
    channel_name = scrapy.Field()
    # 商店id
    shop_kdtId = scrapy.Field()
    # 商店名称
    shop_name = scrapy.Field()
    # 商品唯一标识
    product_alias = scrapy.Field()
    # 商品名称
    product_name = scrapy.Field()
    # 商品品类
    product_category_name = scrapy.Field()
    # 商品价格
    product_price = scrapy.Field()
    # 商品原始价格
    product_origin_price = scrapy.Field()
    # 商品品牌
    product_brand = scrapy.Field()
    # 商品的库存量
    product_total_stock = scrapy.Field()
    # 商品的url地址
    product_url = scrapy.Field()
    # 商品的创建时间
    product_create_time = scrapy.Field()
    # 商品的更新时间
    product_update_time = scrapy.Field()
    # 商品图片地址
    product_image_url = scrapy.Field()
    # 订单用户的昵称
    order_nickname = scrapy.Field()
    # 订单的数量
    order_item_num = scrapy.Field()
    # 订单的总价格（订单的数量*单价）
    order_total_price = scrapy.Field()
    # 订单原始的时间（05-06）
    order_origin_time = scrapy.Field()
    # 订单时间 （2018-05-06）
    order_time = scrapy.Field()
    # 数据创建时间
    create_time = scrapy.Field()
    # 爬虫的运行时间（数据的更新时间）
    run_time = scrapy.Field()
    pass
