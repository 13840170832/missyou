package com.lin.missyou.api.v1;

import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.model.Category;
import com.lin.missyou.model.GridCategory;
import com.lin.missyou.service.CategoryService;
import com.lin.missyou.service.GridCategoryService;
import com.lin.missyou.vo.CategoriesAllVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GridCategoryService gridCategoryService;

    @RequestMapping("/all")
    public CategoriesAllVO getAll(){
        Map<Integer, List<Category>> categories = categoryService.getAll();
        return new CategoriesAllVO(categories);
    }

    @RequestMapping("/grid/all")
    public List<GridCategory> getGridCategoryList(){
        List<GridCategory> list = gridCategoryService.getGridCategoryList();
        if(list.isEmpty()){
            throw new NotFoundException(30009);
        }
        return list;
    }
}
