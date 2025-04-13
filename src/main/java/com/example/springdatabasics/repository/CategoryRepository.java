package com.example.springdatabasics.repository;

import com.example.springdatabasics.entity.Category;
import com.example.springdatabasics.entity.Post;
import com.example.springdatabasics.vo.CategoryPostVO;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>{
//    @Transactional
    @Query("select c from Category c where name = :name")
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    Optional<Category> findByName(String name);

    // this method works because Category class has a field named PostSet
    List<Category> findByPostSetTitle(String title);

    @Query("""
            select
                c.name,
                p.id
            from
                Category c
            inner join Post p
            on
                p.category = c
            where p.title = :title
           """)
    List<Object[]> findByPostSetTitle1(String title, Pageable pageable);

    @Query(value = """
            select
                c.name,
                p.id
            from
                Category c
            inner join Post p
            on
                p.cat_id = c.id
            where p.title = :title
            limit 1
           """, nativeQuery = true)
    Optional<Object> findByPostSetTitle2(String title);

    @Query(value = """
            select
                c.name,
                p.id
            from
                Category c
            inner join Post p
            on
                p.cat_id = c.id
            where p.title = :title
            limit 1
           """, nativeQuery = true)
    Optional<CategoryPostVO> findByPostSetTitle3(String title);
}
