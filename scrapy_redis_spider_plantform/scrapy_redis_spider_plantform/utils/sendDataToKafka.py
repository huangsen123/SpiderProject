# -*- coding: utf-8 -*-
from kafka import KafkaProducer
import scrapy_redis_spider_plantform.settings as settings

def sendData(value):
    producer = KafkaProducer(bootstrap_servers=[settings.KAFKA_ADDRESS])  # 此处ip可以是多个['0.0.0.1:9092','0.0.0.2:9092','0.0.0.3:9092' ]
    producer.send(settings.TOPIC_NAME, value)
    producer.flush()
    producer.close()