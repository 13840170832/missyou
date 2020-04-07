package com.lin.missyou.api.v1;

import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.model.Activity;
import com.lin.missyou.service.ActivityService;
import com.lin.missyou.vo.ActivityCouponVO;
import com.lin.missyou.vo.ActivityPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/name/{name}")
    public ActivityPureVO getHomeActivity(@PathVariable String name){
        Optional<Activity> activityOptional = activityService.getByName(name);
        activityOptional.orElseThrow(() -> new NotFoundException(40001));
        return new ActivityPureVO(activityOptional.get());
    }

    @GetMapping("/name/{name}/with_coupon")
    public ActivityCouponVO getActivityWithCoupons(@PathVariable String name){
        Optional<Activity> activityOptional = activityService.getByName(name);
        activityOptional.orElseThrow(()-> new NotFoundException(40001));
        return new ActivityCouponVO(activityOptional.get());
    }
}
