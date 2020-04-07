package com.lin.missyou.repository;

import com.lin.missyou.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity,Long> {

    Optional<Activity> findByName(String name);

    Optional<Activity> findByCouponListId(Long couponId);
}
