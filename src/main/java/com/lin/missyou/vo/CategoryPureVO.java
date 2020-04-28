package com.lin.missyou.vo;

import com.lin.missyou.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CategoryPureVO {
    private Long id;
    private String name;
    private Boolean isRoot;
    private Integer parentId;
    private String img;
    private Integer index;

    public CategoryPureVO(Category category) {
        BeanUtils.copyProperties(category,this);
    }
}
