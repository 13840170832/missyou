package com.lin.missyou.service;

import com.lin.missyou.model.Banner;
import com.lin.missyou.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl implements BannerService{

    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public Banner getByName(String name) {
        return bannerRepository.findOneByName(name);
    }
}
