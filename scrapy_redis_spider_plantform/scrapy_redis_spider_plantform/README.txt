1.这是一个分布式爬虫项目，负责从网页中爬取数据存到kafka中
2.实现多个爬虫同时爬取只需要多复制几个同时启动
3.在utils文件夹下有一个定时任务，每2个小时往redis中添加起始url触发爬虫工作，若同时启动多个爬虫，定时任务只要启动一个
4.店铺的信息保存在shop.csv中，格式：店铺ID,店铺名称
5.添加店铺时只要在shop.csv中按照指定格式添加一行数据，注意不要有空行
6.关于redis和kafka的配置在settings.py中