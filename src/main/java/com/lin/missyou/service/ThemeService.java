package com.lin.missyou.service;

import com.lin.missyou.model.Theme;
import com.lin.missyou.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    public List<Theme> findByNames(List<String> names){
        return themeRepository.findByNames(names);
    }

    public Optional<Theme> findByname(String name){
        return themeRepository.findByName(name);
    }

}
