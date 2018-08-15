import csv
import scrapy_redis_spider_plantform.utils.redisOperation as r
from scrapy_redis_spider_plantform.utils.spiderUtils import getNowTimeFormat
import time

class timeTask(object):

    base_url = 'https://h5.youzan.com/v2/showcase/goods/allgoods?p=1'

    # 初始化
    def __init__(self):
        self.redis = r.OperationRedis()

    # 获取店铺信息
    def getShops(self):
        file = open('../shop.csv', encoding='utf-8')
        return csv.reader(file)

    # 组织起始url, 放入redis
    def addUrlToRedis(self):
        shops = self.getShops()
        for shop in shops:
            now = getNowTimeFormat()
            url = self.base_url + '&kdt_id=' + shop[0] + '&shop_name=' + shop[1] + '&run_time=' + now
            self.redis.addStartUrl(url)

    # 组织任务
    def runTask(self):
        self.redis.clearFingerprint()
        self.addUrlToRedis()

    # 每2个小时执行一次
    def main(self):
        while True:
            print('定时任务启动[' + getNowTimeFormat() + ']：每12小时触发爬虫运行')
            self.runTask()
            time.sleep(60*60*12)


if __name__ == '__main__':
    t = timeTask()
    t.main()