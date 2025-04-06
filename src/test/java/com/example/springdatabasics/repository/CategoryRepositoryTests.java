package com.example.springdatabasics.repository;

import com.example.springdatabasics.entity.Category;
import com.example.springdatabasics.entity.Post;
import com.example.springdatabasics.entity.Tag;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeAll
    void setUp() {
        Tag t1 = new Tag();
        t1.setName("java");

        Tag t2 = new Tag();
        t2.setName("spring");

        Category c1 = new Category();
        c1.setName("cat1");

        Post p1 = new Post();
        p1.setTitle("title1");
        p1.addTag(t1);
        p1.addTag(t2);

        Post p2 = new Post();
        p2.setTitle("title2");

        c1.addPost(p1);
        c1.addPost(p2);

        categoryRepository.save(c1);
    }

    @AfterAll
    void tearDown() {
        categoryRepository.deleteAll();
    }


    @Test
    @Transactional
    public void testGetCategory(){
        Optional<Category> optionalCategory = categoryRepository.findByName("cat1");

        Assertions.assertTrue(optionalCategory.isPresent());
        Category category = optionalCategory.get();
        Assertions.assertEquals(2, category.getPostSet().size());

        category.getPostSet().forEach(post -> {
            if(post.getTitle().equals("title1")){
                Assertions.assertEquals(2, post.getTagSet().size());
            }

            if(post.getTitle().equals("title2")){
                Assertions.assertEquals(0, post.getTagSet().size());
            }
        });
    }

}
