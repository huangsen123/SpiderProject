# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
import json
from scrapy_redis_spider_plantform.utils.sendDataToKafka import sendData

class ScrapyRedisSpiderPlantformPipeline(object):
    def process_item(self, item, spider):
        print('*********************************************************************************')
        data = json.dumps(dict(item), ensure_ascii=False)
        print(data)
        sendData(data.encode('utf-8'))
        return item
