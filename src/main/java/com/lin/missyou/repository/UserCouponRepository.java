package com.lin.missyou.repository;

import com.lin.missyou.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {

    Optional<UserCoupon> findFirstByUserIdAndAndCouponId(Long uid,Long couponId);

    Optional<UserCoupon> findFirstByUserIdAndAndCouponIdAndStatus(Long uid,Long couponId,Integer status);

    @Modifying
    @Query("update UserCoupon uc \n" +
            "set uc.orderId = :oid,uc.status = 2 \n" +
            "where uc.couponId = :couponId and uc.userId = :uid\n" +
            "and uc.status = 1 and uc.orderId is null")
    int writeOffCoupon(Long couponId,Long oid,Long uid);

    @Modifying
    @Query("update UserCoupon c set c.status = 1,c.orderId = null \n" +
            "where c.couponId = :couponId and c.userId = :uid\n" +
            "and c.status=2 and c.orderId is not null\n")
    int returnBack(Long couponId,Long uid);

}
