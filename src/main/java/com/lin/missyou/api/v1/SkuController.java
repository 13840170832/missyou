package com.lin.missyou.api.v1;

import com.lin.missyou.model.Sku;
import com.lin.missyou.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/sku")
@RestController
public class SkuController {

    @Autowired
    private SkuService skuService;

    @GetMapping
    public List<Sku> GetSkuList(@RequestParam String ids){
         return skuService.getSkusByIds(ids);
    }
}
