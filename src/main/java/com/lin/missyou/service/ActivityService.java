package com.lin.missyou.service;

import com.lin.missyou.model.Activity;
import com.lin.missyou.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public Optional<Activity> getByName(String name){
        Optional<Activity> activityOptional = activityRepository.findByName(name);
        return activityOptional;
    }
}
