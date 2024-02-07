USE travel_reservation;

# 确保数据一致性：表RESERVATIONS中所有预订该航班的条目数加上该航班的剩余座位数必须等于该航班上总的座位数。这个条件对于表BUS和表HOTELS同样适用。
-- 检查航班数据一致性
DELIMITER //
CREATE PROCEDURE checkFlightConsistency (IN resvKey VARCHAR(50), OUT FLAG INT)
    BEGIN
        DECLARE totalSeats INT;
        DECLARE availSeats INT;
        DECLARE bookedSeats INT;
        SET FLAG = 0;
        SELECT numSeats INTO totalSeats FROM FLIGHTS WHERE flightNum = resvKey;
        SELECT numAvail INTO availSeats FROM FLIGHTS WHERE flightNum = resvKey;
        SELECT COUNT(*) INTO bookedSeats FROM RESERVATIONS r WHERE r.resvType = 1 AND r.resvKey = resvKey;
        IF availSeats + bookedSeats != totalSeats THEN
            SET FLAG = 1;
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Data inconsistency detected in FLIGHTS table.';
        END IF;
    END; //
DELIMITER ;
-- 检查酒店数据一致性
DELIMITER //
CREATE PROCEDURE checkHotelConsistency (resvKey VARCHAR(50), OUT FLAG INT)
    BEGIN
        DECLARE totalRooms INT;
        DECLARE availRooms INT;
        DECLARE bookedRooms INT;
        SET FLAG = 0;
        SELECT numRooms INTO totalRooms FROM HOTELS WHERE location = resvKey;
        SELECT numAvail INTO availRooms FROM HOTELS WHERE location = resvKey;
        SELECT COUNT(*) INTO bookedRooms FROM RESERVATIONS r WHERE r.resvType = 2 AND r.resvKey = resvKey;
        IF availRooms + bookedRooms != totalRooms THEN
            SET FLAG = 1;
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Data inconsistency detected in HOTELS table.';
        END IF;
    END; //
DELIMITER ;
-- 检查大巴车数据一致性
DELIMITER //
CREATE PROCEDURE checkBusConsistency (resvKey VARCHAR(50), OUT FLAG INT)
    BEGIN
        DECLARE totalSeats INT;
        DECLARE availSeats INT;
        DECLARE bookedSeats INT;
        SET FLAG = 0;
        SELECT numBus INTO totalSeats FROM BUS WHERE location = resvKey;
        SELECT numAvail INTO availSeats FROM BUS WHERE location = resvKey;
        SELECT COUNT(*) INTO bookedSeats FROM RESERVATIONS r WHERE r.resvType = 3 AND r.resvKey = resvKey;
        IF availSeats + bookedSeats != totalSeats THEN
            SET FLAG = 1;
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Data inconsistency detected in BUS table.';
        END IF;
    END; //
DELIMITER ;

# 设置触发器，保证添加、删除、更新预定数据后，航班/酒店/大巴车的空闲数改变
-- 插入预定数据后，更新航班空闲座位数/酒店空闲房间数/大巴车空闲座位数
DELIMITER //
CREATE TRIGGER updateAvailability_afterInsert AFTER INSERT ON RESERVATIONS
    FOR EACH ROW
    BEGIN
        DECLARE checkConsistency INT DEFAULT 0;
        CASE
            WHEN NEW.resvType = 1 THEN
                UPDATE FLIGHTS SET numAvail = numAvail - 1 WHERE flightNum = NEW.resvKey;
                CALL checkFlightConsistency(NEW.resvKey, checkConsistency);
            WHEN NEW.resvType = 2 THEN
                UPDATE HOTELS SET numAvail = numAvail - 1 WHERE location = NEW.resvKey;
                CALL checkHotelConsistency(NEW.resvKey, checkConsistency);
            WHEN NEW.resvType = 3 THEN
                UPDATE BUS SET numAvail = numAvail - 1 WHERE location = NEW.resvKey;
                CALL checkBusConsistency(NEW.resvKey, checkConsistency);
        END CASE;
    END; //
DELIMITER ;
-- 删除预定数据后，更新航班空闲座位数/酒店空闲房间数/大巴车空闲座位数
DELIMITER //
CREATE TRIGGER updateAvailability_afterDelete AFTER DELETE ON RESERVATIONS
    FOR EACH ROW
    BEGIN
        DECLARE checkConsistency INT DEFAULT 0;
        CASE
            WHEN OLD.resvType = 1 THEN
                UPDATE FLIGHTS SET numAvail = numAvail + 1 WHERE flightNum = OLD.resvKey;
                CALL checkFlightConsistency(OLD.resvKey, checkConsistency);
            WHEN OLD.resvType = 2 THEN
                UPDATE HOTELS SET numAvail = numAvail + 1 WHERE location = OLD.resvKey;
                CALL checkHotelConsistency(OLD.resvKey, checkConsistency);
            WHEN OLD.resvType = 3 THEN
                UPDATE BUS SET numAvail = numAvail + 1 WHERE location = OLD.resvKey;
                CALL checkBusConsistency(OLD.resvKey, checkConsistency);
        END CASE;
    END; //
DELIMITER ;
-- 更新预定数据后，更新航班空闲座位数/酒店空闲房间数/大巴车空闲座位数
DELIMITER //
CREATE TRIGGER updateAvailability_afterUpdate AFTER UPDATE ON RESERVATIONS
    FOR EACH ROW
    BEGIN
        DECLARE checkConsistency INT DEFAULT 0;
        CASE
            WHEN NEW.resvType = 1 THEN
                UPDATE FLIGHTS SET numAvail = numAvail + 1 WHERE flightNum = OLD.resvKey;
                UPDATE FLIGHTS SET numAvail = numAvail - 1 WHERE flightNum = NEW.resvKey;
                CALL checkFlightConsistency(OLD.resvKey, checkConsistency);
                IF NEW.resvKey != OLD.resvKey THEN
                    CALL checkFlightConsistency(NEW.resvKey, checkConsistency);
                END IF;
            WHEN NEW.resvType = 2 THEN
                UPDATE HOTELS SET numAvail = numAvail + 1 WHERE location = OLD.resvKey;
                UPDATE HOTELS SET numAvail = numAvail - 1 WHERE location = NEW.resvKey;
                CALL checkHotelConsistency(OLD.resvKey, checkConsistency);
                IF NEW.resvKey != OLD.resvKey THEN
                    CALL checkHotelConsistency(NEW.resvKey, checkConsistency);
                END IF;
            WHEN NEW.resvType = 3 THEN
                UPDATE BUS SET numAvail = numAvail + 1 WHERE location = OLD.resvKey;
                UPDATE BUS SET numAvail = numAvail - 1 WHERE location = NEW.resvKey;
                CALL checkBusConsistency(OLD.resvKey, checkConsistency);
                IF NEW.resvKey != OLD.resvKey THEN
                    CALL checkBusConsistency(NEW.resvKey, checkConsistency);
                END IF;
        END CASE;
    END; //
DELIMITER ;