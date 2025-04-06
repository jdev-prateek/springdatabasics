package com.example.springdatabasics.repository;

import com.example.springdatabasics.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where title = :title")
    Optional<Post> findByName(String name);

//    @Query("select p from Post p where price between :min and :max")
//    Page<Post> findPostsBetweenPrice(int min, int max, Pageable pageable);

//    @Modifying
//    @Query("""
//            update Post
//            set
//                title = :tile
//            where
//                id = :id
//            """)
//    void updatePostNameById(Long id, String name);
}
