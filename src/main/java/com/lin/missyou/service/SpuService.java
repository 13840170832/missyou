package com.lin.missyou.service;

import com.lin.missyou.model.Spu;
import com.lin.missyou.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuService {

    @Autowired
    private SpuRepository spuRepository ;

    public Spu getSpu(Long id){
        return spuRepository.findOneById(id);
    }

    public Page<Spu> getLatestPagingSpu(Integer pageNum, Integer size){
        Pageable pageable = PageRequest.of(pageNum,size, Sort.by("createTime").descending());
        return  this.spuRepository.findAll(pageable);
    }

    public Page<Spu> getCategory(Long categoryId,Boolean isRoot,Integer pageNum,Integer size){
        Pageable pageable = PageRequest.of(pageNum,size);
        Page<Spu> page = null;
        if(isRoot){
            page = spuRepository.findByRootCategoryIdOrderByCreateTimeDesc(categoryId,pageable);
        }else{
            page = spuRepository.findByCategoryIdOrderByCreateTimeDesc(categoryId,pageable);
        }
        return page;
    }

    public Page<Spu> search(String keyword,Integer pageNum,Integer size){
        Pageable pageable = PageRequest.of(pageNum,size);
        return spuRepository.findByTitleContainingOrSubtitleContainingOrTagsContaining(keyword,keyword,keyword,pageable);
    }
}
