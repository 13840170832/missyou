package com.lin.missyou.service;

import com.lin.missyou.model.Tag;
import com.lin.missyou.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getTagsByType(Integer type){
        return tagRepository.findAllByType(type);
    }
}
