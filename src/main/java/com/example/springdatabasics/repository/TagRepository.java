package com.example.springdatabasics.repository;

import com.example.springdatabasics.entity.Post;
import com.example.springdatabasics.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select t from Tag t where name = :name")
    Optional<Post> findByName(String name);
}
