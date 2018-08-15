# -*- coding: utf-8 -*-

# Scrapy settings for scrapy_redis_spider_plantform project
#
# For simplicity, this file contains only settings considered important or
# commonly used. You can find more settings consulting the documentation:
#
#     https://doc.scrapy.org/en/latest/topics/settings.html
#     https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
#     https://doc.scrapy.org/en/latest/topics/spider-middleware.html

BOT_NAME = 'scrapy_redis_spider_plantform'

SPIDER_MODULES = ['scrapy_redis_spider_plantform.spiders']
NEWSPIDER_MODULE = 'scrapy_redis_spider_plantform.spiders'

# redis配置
# 使用scrapy-redis里的调度器组件，不使用默认的调度器
SCHEDULER = 'scrapy_redis.scheduler.Scheduler'
# 使用scrapy-redis里的去重组件，不使用scrapy默认的去重方式
DUPEFILTER_CLASS = 'scrapy_redis.dupefilter.RFPDupeFilter'

# 允许暂停，redis请求记录不丢失
SCHEDULER_PERSIST = True
# 默认的scrapy-redis请求队列形式（按优先级）
SCHEDULER_QUEUE_CLASS = 'scrapy_redis.queue.SpiderPriorityQueue'
# 队列形式，请求先进先出
#SCHEDULER_QUEUE_CLASS = 'scrapy_redis.queue.SpiderQueue'
# 栈形式，请求先进后出
#SCHEDULER_QUEUE_CLASS = 'scrapy_redis.queue.SpiderStack'
# REDIS_URL = 'redis://@127.0.0.1:6379'
# 指定数据库的主机IP
REDIS_HOST = "192.168.3.210"
# 指定数据库的端口号
REDIS_PORT = 5008

# 起始的url,触发爬虫
START_URLS = 'youzanSpider:start_urls'
# redis中url存放指纹信息的key
FINGERPRINT_KEY = 'youzanSpider:dupefilter'

# kafka配置
# kafka的iP+port
KAFKA_ADDRESS = '192.168.3.200:6667'
# kafka主题
TOPIC_NAME = 'send'

# log配置
LOG_ENABLED = True
# LOG_ENCODING = 'utf-8'
# LOG_FILE = "./log/info.log"

# Crawl responsibly by identifying yourself (and your website) on the user-agent
#USER_AGENT = 'scrapy_redis_spider_plantform (+http://www.yourdomain.com)'

# Obey robots.txt rules
ROBOTSTXT_OBEY = False

# Configure maximum concurrent requests performed by Scrapy (default: 16)
#CONCURRENT_REQUESTS = 32

# Configure a delay for requests for the same website (default: 0)
# See https://doc.scrapy.org/en/latest/topics/settings.html#download-delay
# See also autothrottle settings and docs
DOWNLOAD_DELAY = 1
# The download delay setting will honor only one of:
#CONCURRENT_REQUESTS_PER_DOMAIN = 16
#CONCURRENT_REQUESTS_PER_IP = 16

# Disable cookies (enabled by default)
#COOKIES_ENABLED = False

# Disable Telnet Console (enabled by default)
#TELNETCONSOLE_ENABLED = False

# Override the default request headers:
#DEFAULT_REQUEST_HEADERS = {
#   'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
#   'Accept-Language': 'en',
#}

# Enable or disable spider middlewares
# See https://doc.scrapy.org/en/latest/topics/spider-middleware.html
#SPIDER_MIDDLEWARES = {
#    'scrapy_redis_spider_plantform.middlewares.ScrapyRedisSpiderPlantformSpiderMiddleware': 543,
#}

# Enable or disable downloader middlewares
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
#DOWNLOADER_MIDDLEWARES = {
#    'scrapy_redis_spider_plantform.middlewares.ScrapyRedisSpiderPlantformDownloaderMiddleware': 543,
#}

# Enable or disable extensions
# See https://doc.scrapy.org/en/latest/topics/extensions.html
#EXTENSIONS = {
#    'scrapy.extensions.telnet.TelnetConsole': None,
#}

# Configure item pipelines
# See https://doc.scrapy.org/en/latest/topics/item-pipeline.html
ITEM_PIPELINES = {
   'scrapy_redis_spider_plantform.pipelines.ScrapyRedisSpiderPlantformPipeline': 300,
   # 'scrapy_redis.pipelines.RedisPipeline': 400
}

# Enable and configure the AutoThrottle extension (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/autothrottle.html
AUTOTHROTTLE_ENABLED = True
# The initial download delay
#AUTOTHROTTLE_START_DELAY = 5
# The maximum download delay to be set in case of high latencies
#AUTOTHROTTLE_MAX_DELAY = 60
# The average number of requests Scrapy should be sending in parallel to
# each remote server
#AUTOTHROTTLE_TARGET_CONCURRENCY = 1.0
# Enable showing throttling stats for every response received:
#AUTOTHROTTLE_DEBUG = False

# Enable and configure HTTP caching (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html#httpcache-middleware-settings
#HTTPCACHE_ENABLED = True
#HTTPCACHE_EXPIRATION_SECS = 0
#HTTPCACHE_DIR = 'httpcache'
#HTTPCACHE_IGNORE_HTTP_CODES = []
#HTTPCACHE_STORAGE = 'scrapy.extensions.httpcache.FilesystemCacheStorage'
