# -*- coding: utf-8 -*-
import scrapy
from scrapy_redis.spiders import RedisSpider
from faker import Factory
from logging import getLogger
from scrapy import signals
from scrapy.xlib.pydispatch import dispatcher
import json
import sys
from scrapy_redis_spider_plantform.items import YouZanItem
from scrapy_redis_spider_plantform.utils.spiderUtils import getKdtId, dealOrderTime, getNowTime, getNowTimeFormat, writeError
import scrapy_redis_spider_plantform.utils.redisOperation as r

f = Factory.create()
logger = getLogger('YouzanSpider')

class YouzanSpider(RedisSpider):
    name = 'youzanSpider'
    allowed_domains = ['youzan.com']
    redis_key = 'youzanSpider:start_urls'
    # start_urls = ['http://youzan.com/']

    # 订单url的请求头
    headers_order = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'Accept-Encoding': 'gzip, deflate, br',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        'Cache-Control': 'max-age=0',
        'Connection': 'keep-alive',
        'Content-Type': 'application/json',
        'DNT': 1,
        'Host': 'shop.youzan.com',
        'X-Requested-With': 'XMLHttpRequest',
        'User-Agent': f.user_agent()
    }

    # 商品url的请求头
    headers_shop = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'Accept-Encoding': 'gzip, deflate, br',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        'Cache-Control': 'max-age=0',
        'Connection': 'keep-alive',
        'Content-Type': 'application/json',
        'DNT': 1,
        'Host': 'h5.youzan.com',
        'X-Requested-With': 'XMLHttpRequest',
        'User-Agent': f.user_agent()
    }

    # 初始化
    def __init__(self):
        self.re = r.OperationRedis()
        dispatcher.connect(self.spider_opened, signals.spider_opened)
        dispatcher.connect(self.spider_closed, signals.spider_closed)

    # 爬虫的开启监控
    def spider_opened(self):
        nowTime = getNowTimeFormat()
        print(nowTime+":有赞爬虫开始了")
        logger.info(nowTime+":有赞爬虫开始了")

    # 爬虫的关闭监控
    def spider_closed(self):
        nowTime = getNowTimeFormat()
        print(nowTime+":有赞爬虫关闭了")
        logger.info(nowTime+":有赞爬虫关闭了")

    # 开始处理start_urls的请求
    # def start_requests(self):
    #     kdt_id = '12286'
    #     yield scrapy.Request('https://h5.youzan.com/v2/showcase/goods/allgoods?kdt_id=' + kdt_id
    #                          + '&p=1', method='GET', headers=self.headers_shop, meta={'kdtId': kdt_id},
    #                          callback=self.get_shop_all_product)
    # https://h5.youzan.com/v2/showcase/goods/allgoods?kdt_id=12286&p=1

    # 处理start_urls的请求
    def parse(self, response):
        params = getKdtId(response.url)
        kdtId = params['kdtId']
        shopName = params['shopName']
        runTime = params['runTime']
        if '' != kdtId and '' != shopName and '' != runTime:
            print('店铺名称：{}，店铺ID：{}，运行开始时间：{}'.format(shopName, kdtId, runTime))
            yield scrapy.Request('https://h5.youzan.com/v2/showcase/goods/allgoods?kdt_id=' + kdtId + '&p=1',
                                 method='GET', headers=self.headers_shop, callback=self.get_shop_all_product,
                                 meta={'kdtId': kdtId, 'shopName': shopName, 'runTime': runTime})
        else:
            logger.warning('start_urls中没有店铺id(kdt_id)或者店铺名称(shop_name)')


    # 处理商店的商品信息
    def get_shop_all_product(self, response):
        kdtId = response.meta['kdtId']
        shopName = response.meta['shopName']
        runTime = response.meta['runTime']

        cookies = response.request.headers.getlist('Cookie')
        cookie = {'DO_CHECK_YOU_VERSION': '1', 'KDTSESSIONID': 'YZ464893424865128448YZfEh5mv5r',
                  'nobody_sign': 'YZ464893424865128448YZfEh5mv5r', '_kdt_id_': kdtId,
                  'yz_ep_page_type_track': 'iDJ3GNJDHbhHtOl6W3j3ZA%3D%3D'}
        if len(cookies) > 0:
            cookies = cookies[0].decode('utf-8')
            cookie = {i.split('=')[0]: i.split('=')[1] for i in cookies.split('; ')}

        # shop_name = response.xpath('//p[@class="shop-name"]/text()').extract_first()
        # if shop_name is not None:
        #     shop_name = shop_name.strip()
        # else:
        #     shop_name = shopName
        try:
            get_product = response.xpath('//div[@class="content-body js-page-content"]/script/text()').extract_first()
            products = get_product.split('var _showcase_components = ')[1].split(';        } else {')[0]
            dump_products = json.dumps(eval(products, type('Dummy', (dict,), dict(__getitem__=lambda s, n: n))()))
            dict_products = json.loads(dump_products)[0]
        except Exception as e:
            writeError(False, shopName, kdtId, response.url, response.text)
            print(response.text)
            logger.error('解析商品异常：' + response.text + " 错误内容：" + str(e))
            sys.exit(0)
            return

        print(dict_products)

        if "next_url" in dict_products:  # 还有下一页
            next_page = dict_products['next_url']
            next_page = next_page.replace('\\', '')
            yield scrapy.Request(next_page, method='GET', headers=self.headers_shop,
                                 callback=self.get_shop_all_product,
                                 meta={'kdtId': kdtId, 'shopName': shopName, 'runTime': runTime})
        else:
            print("=====================商品已到达最后一页======================")

        page = 1
        for product in dict_products['goods']:
            alias = str(product['alias'])
            maxTime = self.re.getMaxTime(alias)
            yield scrapy.Request('https://shop15653306.youzan.com/v2/trade/order/orderitemlist.json?alias=' +
                                 alias + '&page=' + str(page) + '&perpage=10', method='GET', cookies=cookie,
                                 headers=self.headers_order, callback=self.get_product_order,
                                 meta={'product': product, 'page': page, 'shopName': shopName, 'kdtId': kdtId,
                                       'runTime': runTime, 'maxTime': maxTime, 'cookies': cookie})

    # 处理商品的订单信息
    def get_product_order(self, response):
        page = response.meta['page']
        product = response.meta['product']
        shopName = response.meta['shopName']
        kdtId = response.meta['kdtId']
        runTime = response.meta['runTime']
        maxTime = response.meta['maxTime']
        cookie = response.meta['cookies']
        alias = product['alias']

        rep = response.text
        try:
            order_list = json.loads(rep)
        except Exception:
            try:
                dump = json.dumps(eval(rep, type('Dummy', (dict,), dict(__getitem__=lambda s, n: n))()))
                order_list = json.loads(dump)
            except Exception as e:
                writeError(True, shopName, kdtId, response.url, rep)
                print(rep)
                logger.error('解析订单异常：' + rep + " 错误内容：" + str(e))
                sys.exit(0)
                return
        # try:
        #     dump = json.dumps(eval(rep, type('Dummy', (dict,), dict(__getitem__=lambda s, n: n))()))
        #     order_list = json.loads(dump)
        # except Exception as e:
        #     logger.error('解析订单异常：' + response.text + " 错误内容：" + str(e))
        #     print(e)
        #     return
        if '0' == str(order_list['code']):
            data_lists = order_list['data']['list']
            if len(data_lists) > 0:
                if page == 1:
                    self.re.addMaxTime(alias, dealOrderTime(data_lists[0]['update_time']))

                now = getNowTime()
                year = int(str(now.year))
                month = int(str(now.month))
                day = int(str(now.day))
                hour = int(str(now.hour))
                minute = int(str(now.minute))

                for order in data_lists:

                    update_time_ = str(order['update_time'])
                    month_ = int(update_time_[0:2])
                    day_ = int(update_time_[3:5])
                    hour_ = int(update_time_[6:8])
                    min_ = int(update_time_[9:11])
                    if month_ > month \
                            or (month_ == month and day_ > day) \
                            or (month_ == month and day_ == day and hour_ > hour) \
                            or (month_ == month and day_ == day and hour_ == hour and min_ > minute):
                        year = year - 1
                    month = month_
                    day = day_
                    hour = hour_
                    minute = min_
                    order_time = str(year) + '-' + update_time_

                    if maxTime == order_time:
                        return

                    item = YouZanItem()
                    item['plantform_name'] = '电商'
                    item['channel_name'] = '有赞'
                    item['shop_kdtId'] = kdtId
                    item['shop_name'] = shopName.replace(',', '，')
                    item['product_alias'] = alias
                    item['product_name'] = product['title'].replace(',', '，')
                    item['product_category_name'] = ''
                    item['product_price'] = product['price']
                    item['product_origin_price'] = product['origin']
                    item['product_brand'] = ''
                    item['product_total_stock'] = product['total_stock']
                    item['product_url'] = product['url']
                    item['product_create_time'] = product['created_time']
                    item['product_update_time'] = product['update_time']
                    item['product_image_url'] = product['image_url']
                    item['order_nickname'] = order['nickname'].replace(',', '，')
                    item['order_item_num'] = order['item_num']
                    item['order_total_price'] = float(order['item_price']) * float(order['item_num'])
                    item['order_origin_time'] = order['update_time']
                    item['order_time'] = order_time
                    item['create_time'] = getNowTimeFormat()
                    item['run_time'] = runTime
                    yield item

                next_page = str(order_list['data']['has_next']).replace('T', 't')
                if 'true' == next_page:
                    yield scrapy.Request(
                        'https://shop.youzan.com/v2/trade/order/orderitemlist.json?alias=' + str(product['alias']) +
                        '&page=' + str(page + 1) + '&perpage=10', method='GET', cookies=cookie,
                        headers=self.headers_order, callback=self.get_product_order,
                        meta={"product": product, "page": page + 1, 'shopName': shopName, 'kdtId': kdtId,
                              'runTime': runTime, 'maxTime': maxTime, 'cookies': cookie})
            else:
                item = YouZanItem()
                item['plantform_name'] = '电商'
                item['channel_name'] = '有赞'
                item['shop_kdtId'] = kdtId
                item['shop_name'] = shopName.replace(',', '，')
                item['product_alias'] = alias
                item['product_name'] = product['title'].replace(',', '，')
                item['product_category_name'] = ''
                item['product_price'] = product['price']
                item['product_origin_price'] = product['origin']
                item['product_brand'] = ''
                item['product_total_stock'] = product['total_stock']
                item['product_url'] = product['url']
                item['product_create_time'] = product['created_time']
                item['product_update_time'] = product['update_time']
                item['product_image_url'] = product['image_url']
                item['order_nickname'] = ''
                item['order_item_num'] = 0
                item['order_total_price'] = 0
                item['order_origin_time'] = ''
                item['order_time'] = ''
                item['create_time'] = getNowTimeFormat()
                item['run_time'] = runTime
                yield item

