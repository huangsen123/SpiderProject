# -*- coding: utf-8 -*-
from datetime import datetime
from urllib import parse
import csv

# 处理订单时间，增加年份
def dealOrderTime(time):
    year = int(str(datetime.now().year))
    month = int(str(datetime.now().month))
    day = int(str(datetime.now().day))
    hour = int(str(datetime.now().hour))
    minute = int(str(datetime.now().minute))

    month_ = int(time[0:2])
    day_ = int(time[3:5])
    hour_ = int(time[6:8])
    min_ = int(time[9:11])

    if month_ > month \
            or (month_ == month and day_ > day) \
            or (month_ == month and day_ == day and hour_ > hour) \
            or (month_ == month and day_ == day and hour_ == hour and min_ > minute):
        year = year - 1
    return str(year) + "-" + time

# 从url中获取kdt_id
def getKdtId(url):
    param = {'kdtId': '', 'shopName': '', 'runTime': ''}
    query = parse.urlparse(url).query
    params = parse.parse_qs(query)
    if len(params) != 0:
        if 'kdt_id' in params:
            param['kdtId'] = params['kdt_id'][0]
        if 'shop_name' in params:
            param['shopName'] = params['shop_name'][0]
        if 'run_time' in params:
            param['runTime'] = params['run_time'][0]
    return param

# 获取格式化后的当前时间
def getNowTimeFormat():
    return datetime.now().strftime('%Y-%m-%d %H:%M:%S')

# 获取当前时间
def getNowTime():
    return datetime.now()

# 获取文件名称，flag为true时订单，false商品
def getFileName(flag):
    fileName = 'goods_' + datetime.now().strftime('%Y-%m-%d')+'.csv'
    if flag:
        fileName = 'order_' + datetime.now().strftime('%Y-%m-%d')+'.csv'
    return fileName

# 将解析失败的商品或者订单放入文件中
def writeError(flag, shopName, kdtId, key, value):
    with open('./error/'+getFileName(flag), 'a', encoding='utf-8', newline='') as f:
        write = csv.writer(f)
        write.writerow([shopName, kdtId, key, value])