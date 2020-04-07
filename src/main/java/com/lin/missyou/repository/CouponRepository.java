package com.lin.missyou.repository;

import com.lin.missyou.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    @Query("SELECT c from Coupon c\n" +
            "join c.categoryList ca\n" +
            "join Activity a on a.id = c.activityId\n" +
            "WHERE ca.id = :cid\n" +
            "AND a.startTime < : now\n" +
            "AND a.endTime > :now")
    List<Coupon> findByCatory(Long cid, Date now);


    @Query("select c\n" +
            "from Coupon c\n" +
            "join Activity a on a.id = c.activityId\n" +
            "where c.wholeStore = :isWholeStore\n" +
            "and a.startTime < :now\n" +
            "and a.endTime > :now")
    List<Coupon> findByWholeStore(Boolean isWholeStore,Date now);

    @Query("select c\n" +
            "from Coupon c join UserCoupon uc on c.id=uc.couponId\n" +
            "join User u on u.id=uc.userId\n" +
            "where uc.status=1 and u.id=:uid\n" +
            "and c.startTime < :now and c.endTime > :now\n" +
            "and uc.orderId is null")
    List<Coupon> findMyAvailable(Long uid,Date now);

    @Query("select c from Coupon c \n" +
            "join UserCoupon uc on c.id=uc.couponId\n" +
            "join User u on u.id=uc.userId\n" +
            "where uc.status=2 and u.id=:uid\n" +
            "and c.startTime < :now \n" +
            "and uc.orderId is not null ")
    List<Coupon> findMyUsed(Long uid,Date now);

    @Query("select c from Coupon c \n" +
            "join UserCoupon uc on c.id=uc.couponId\n" +
            "join User u on u.id=uc.userId\n" +
            "where uc.status<>2 and u.id=:uid\n" +
            "and c.endTime < :now\n" +
            "and uc.orderId is null")
    List<Coupon> findMyExpired(Long uid,Date now);
}


