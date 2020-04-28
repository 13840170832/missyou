package com.lin.missyou.api.v1;

//import com.lin.missyou.exception.http.ForbiddenException;
//import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.model.Banner;
import com.lin.missyou.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/banner")
@Validated
public class BannerController {

//    @Autowired
//    private ISkill iSkill;

    @Autowired
    private BannerService bannerService;

    @GetMapping("/name/{name}")
    public Banner getByName(@PathVariable String name){
        Banner banner = bannerService.getByName(name);
        if(null == banner){
            throw new NotFoundException(30005);
        }
        return banner;
    }

//    @PostMapping(value = "/test/{id1}")
//    public PersonDTO test(@PathVariable(name="id1") @Range(min = 1,max = 10,message = "不能超过10噢") Integer id,
//                          @RequestParam(name="name2") @Length(min = 3) String name,
//                          @RequestBody @Validated PersonDTO personDTO){
//        iSkill.r();
//        PersonDTO dto = new PersonDTO();
//        dto.setName("雪");
//        dto.setAge(18);
////        PersonDTO dto = PersonDTO.builder()
////                .name("")
////                .age(18)
////                .build();
//        return dto;
//    }


}
