# 创建旅行预订系统数据库
CREATE DATABASE travel_reservation;
USE travel_reservation;

# 创建数据库表
-- 航班表
CREATE TABLE FLIGHTS
(
    flightNum VARCHAR(50) PRIMARY KEY,
    price INT,
    numSeats INT,
    numAvail INT,
    FromCity VARCHAR(50),
    ArivCity VARCHAR(50)
);
-- 酒店表
CREATE TABLE HOTELS (
    location VARCHAR(50) PRIMARY KEY,
    price INT,
    numRooms INT,
    numAvail INT
);
-- 大巴车表
CREATE TABLE BUS (
    location VARCHAR(50) PRIMARY KEY,
    price INT,
    numBus INT,
    numAvail INT
);
-- 客户表
CREATE TABLE CUSTOMERS (
    custName VARCHAR(50) PRIMARY KEY,
    custID INT
);
-- 预定订单表
CREATE TABLE RESERVATIONS (
    resvID INT AUTO_INCREMENT PRIMARY KEY,
    custName VARCHAR(50),
    resvType INT,
    resvKey VARCHAR(50)
);

# 创建视图查看预定的详细信息
CREATE VIEW reservation_details AS
SELECT
    r.resvID AS resvID,
    r.custName AS custName,
    r.resvType AS resvType,
    r.resvKey AS resvKey,
    f.numSeats AS quantity,
    f.numAvail AS availability,
    f.FromCity AS locationFrom,
    f.ArivCity AS locationTo,
    f.price AS price
FROM
    RESERVATIONS r
        LEFT JOIN
    FLIGHTS f ON r.resvType = 1 AND r.resvKey = f.flightNum
WHERE
        r.resvType = 1
UNION
SELECT
    r.resvID AS resvID,
    r.custName AS custName,
    r.resvType AS resvType,
    r.resvKey AS resvKey,
    h.numRooms AS quantity,
    h.numAvail AS availability,
    NULL AS locationFrom,
    NULL AS locationTo,
    h.price AS price
FROM
    RESERVATIONS r
        LEFT JOIN
    HOTELS h ON r.resvType = 2 AND r.resvKey = h.location
WHERE
        r.resvType = 2
UNION
SELECT
    r.resvID AS resvID,
    r.custName AS custName,
    r.resvType AS resvType,
    r.resvKey AS resvKey,
    b.numBus AS quantity,
    b.numAvail AS availability,
    NULL AS locationFrom,
    NULL AS locationTo,
    b.price AS price
FROM
    RESERVATIONS r
        LEFT JOIN
    BUS b ON r.resvType = 3 AND r.resvKey = b.location
WHERE
        r.resvType = 3;