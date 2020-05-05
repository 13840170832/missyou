package com.lin.missyou.service;

import com.lin.missyou.model.Sku;
import com.lin.missyou.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkuService {

    @Autowired
    private SkuRepository skuRepository;

    public List<Sku> getSkuListByIds(List<Long> ids){
        return skuRepository.findAllByIdIn(ids);
    }

    public List<Sku> getSkusByIds(String ids){
        List<Long> idsList = Arrays.asList(ids.split(","))
                .stream()
                .map(s->Long.parseLong(s))
                .collect(Collectors.toList());
        return skuRepository.findAllByIdIn(idsList);
    }
}
