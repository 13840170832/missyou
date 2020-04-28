package com.lin.missyou.repository;

import com.lin.missyou.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {

    List<Tag> findAllByType(Integer type);
}
