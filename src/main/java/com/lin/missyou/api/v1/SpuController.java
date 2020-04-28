package com.lin.missyou.api.v1;

import com.lin.missyou.bo.PageCounter;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.model.Spu;
import com.lin.missyou.service.SpuService;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.vo.PagingDozer;
import com.lin.missyou.vo.SpuSimplifyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {

    @Autowired
    private SpuService spuService;

    @GetMapping("/id/{id}/detail")
    public Spu getSpu(@PathVariable @Positive Long id){
        Spu spu = this.spuService.getSpu(id);
        if(null == spu){
            throw new NotFoundException(30003);
        }
        return spu;
    }

    @GetMapping("/id/{id}/simplify")
    public SpuSimplifyVO getSimplifySpu(@PathVariable @Positive Long id){
        Spu spu = this.spuService.getSpu(id);
        if(null == spu){
            throw new NotFoundException(30003);
        }
        SpuSimplifyVO simplifyVO = new SpuSimplifyVO();
        BeanUtils.copyProperties(spu,simplifyVO);
        return simplifyVO;
    }

    @GetMapping("/latest")
    public PagingDozer<Spu,SpuSimplifyVO> getLatestSpuList(@RequestParam(defaultValue = "0") Integer start,
                                                @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start,count);
        Page<Spu> spuPage = spuService.getLatestPagingSpu(pageCounter.getPage(),pageCounter.getCount());
        return new PagingDozer(spuPage,SpuSimplifyVO.class);
    }

    @RequestMapping("/by/category/{id}")
    public PagingDozer<Spu,SpuSimplifyVO> getByCategoryId(@PathVariable @Positive(message = "{id.positive}") Long id,
                                                             @RequestParam(name="is_root",defaultValue = "false") Boolean isRoot,
                                                             @RequestParam(defaultValue = "0") Integer start,
                                                             @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start,count);
        Page<Spu> spuPage = spuService.getCategory(id,isRoot,pageCounter.getPage(),pageCounter.getCount());
        return new PagingDozer(spuPage,SpuSimplifyVO.class);
    }

    @RequestMapping("/search")
    public PagingDozer<Spu,SpuSimplifyVO> search(@RequestParam(name="q") String keyword,
                                                 @RequestParam(defaultValue = "0") Integer start,
                                                 @RequestParam(defaultValue = "10") Integer count){
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start,count);
        Page<Spu> spuPage = spuService.search(keyword,pageCounter.getPage(),pageCounter.getCount());
        return new PagingDozer(spuPage,SpuSimplifyVO.class);
    }














//    @GetMapping("/latest")
//    public List<SpuSimplifyVO> getLatestSpuList(){
//        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
//        List<Spu> spuList = this.spuService.getLatestPagingSpu();
//        List<SpuSimplifyVO> voList = new ArrayList<>();
//        spuList.forEach(s->{
//            SpuSimplifyVO vo = mapper.map(s, SpuSimplifyVO.class);
//            voList.add(vo);
//        });
//        return voList;
//    }
}
