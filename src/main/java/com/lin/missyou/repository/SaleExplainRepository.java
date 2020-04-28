package com.lin.missyou.repository;

import com.lin.missyou.model.SaleExplain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleExplainRepository extends JpaRepository<SaleExplain,Long> {

    List<SaleExplain> findAll();
}
