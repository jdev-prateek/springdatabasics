package com.example.springdatabasics.repository;

import com.example.springdatabasics.entity.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeAll
    void setUp() {
        Post p1 = new Post();
        p1.setTitle("aa");

        Post p2 = new Post();
        p2.setTitle("bb");

        Post p3 = new Post();
        p3.setTitle("cc");

        Post p4 = new Post();
        p4.setTitle("dd");

        postRepository.saveAll(List.of(p1, p2, p3, p4));
    }

    @AfterAll
    void tearDown() {
        postRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testProductFindByName(){
        Optional<Post> optionalProduct = postRepository.findByName("aa");
        Assertions.assertTrue(optionalProduct.isPresent());
    }

    @Test
    @Transactional
    public void testFindProductByName_whenNonExistentProductProvided(){
        Optional<Post> optionalProduct = postRepository.findByName("zz");
        Assertions.assertTrue(optionalProduct.isEmpty());
    }


    @Test
    @Transactional
    public void testProductCreation(){
        Post post = new Post();
        post.setTitle("ee");
        Post saved = postRepository.save(post);
        Assertions.assertNotNull(saved);
    }

    @Test
    @Transactional
    public void testProductDeletion(){
        Post post = new Post();
        post.setTitle("aa");
        Post saved = postRepository.save(post);
        postRepository.deleteById(saved.getId());
        entityManager.flush();

        Optional<Post> byId = postRepository.findById(saved.getId());
        Assertions.assertTrue(byId.isEmpty());
    }

//    @Test
//    @Transactional
//    public void testFindProductsBetweenPrice(){
//        Page<Post> pageOneProducts = postRepository.findPostsBetweenPrice(
//                0,
//                50,
//                PageRequest.of(0, 2, Sort.by("price").descending()));
//
//        System.out.println(pageOneProducts.getTotalPages());
//        System.out.println(pageOneProducts.getContent());
//
//        Page<Post> pageTwoProducts = postRepository.findPostsBetweenPrice(
//                0,
//                50,
//                PageRequest.of(1, 2, Sort.by("price").descending()));
//
//        System.out.println(pageTwoProducts.getTotalPages());
//        System.out.println(pageTwoProducts.getContent());
//    }
//
//    @Test
//    @Transactional
//    public void testProductUpdate(){
//        String newName = "aa-update";
//
//        Optional<Post> optionalProduct1 = postRepository.findByName("aa");
//        Assertions.assertTrue(optionalProduct1.isPresent());
//
//        postRepository.updatePostNameById(optionalProduct1.get().getId(), newName);
//
//        entityManager.flush();
//        entityManager.refresh(optionalProduct1.get());
//
//        Optional<Post> optionalProduct2 = postRepository.findByName(newName);
//        Assertions.assertTrue(optionalProduct2.isPresent());
//        Assertions.assertEquals(newName, optionalProduct2.get().getTitle());
//    }

}
