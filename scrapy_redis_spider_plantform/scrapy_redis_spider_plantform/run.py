from scrapy.cmdline import execute

# redis.Redis().lpush('youzanSpider:start_urls', 'https://h5.youzan.com/v2/showcase/goods/allgoods?kdt_id=12286&p=1&shop_name=国馆')
execute('scrapy crawl youzanSpider'.split())






