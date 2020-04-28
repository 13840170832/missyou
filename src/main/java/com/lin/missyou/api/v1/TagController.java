package com.lin.missyou.api.v1;

import com.lin.missyou.model.Tag;
import com.lin.missyou.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/tag")
@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/type/{type}")
    public List<Tag> getTagsByType(@PathVariable Integer type){
        return tagService.getTagsByType(type);
    }

}
