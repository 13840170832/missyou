package com.lin.missyou.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Builder
public class PageCounter {
    private Integer page;
    private Integer count;
}
