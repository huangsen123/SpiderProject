import redis
import scrapy_redis_spider_plantform.settings as settings

class OperationRedis(object):

    def __init__(self):
        pool = redis.ConnectionPool(host=settings.REDIS_HOST, port=settings.REDIS_PORT)
        self.redis = redis.Redis(connection_pool=pool)

    def addMaxTime(self, key, value):
        self.redis.set(key, value)

    def getMaxTime(self, key):
        if self.redis.exists(key):
            return str(self.redis.get(key), encoding='utf-8')
        else:
            return '0000-00-00 00:00:00'

    def addStartUrl(self, url):
        self.redis.lpush(settings.START_URLS, url)

    def clearFingerprint(self):
        self.redis.delete(settings.FINGERPRINT_KEY)
