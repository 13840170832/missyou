package com.lin.missyou.service;

import com.lin.missyou.model.SaleExplain;
import com.lin.missyou.repository.SaleExplainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleExplainService {

    @Autowired
    private SaleExplainRepository saleExplainRepository;

    public List<SaleExplain> getAll(){
        return saleExplainRepository.findAll();
    }
}
