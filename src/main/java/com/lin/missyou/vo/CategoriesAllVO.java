package com.lin.missyou.vo;

import com.lin.missyou.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoriesAllVO {
    private List<CategoryPureVO> roots;
    private List<CategoryPureVO> subs;

    public CategoriesAllVO(Map<Integer,List<Category>> map) {
        this.roots = map.get(1).stream()
                .map(r-> {
                    return new CategoryPureVO(r);
                })
                .collect(Collectors.toList());
        this.subs = map.get(2).stream()
                .map(CategoryPureVO::new)
                .collect(Collectors.toList());
    }
}
