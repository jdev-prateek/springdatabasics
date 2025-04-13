package com.example.springdatabasics.repository;

import com.example.springdatabasics.entity.Category;
import com.example.springdatabasics.entity.Post;
import com.example.springdatabasics.entity.Tag;
import com.example.springdatabasics.vo.CategoryPostVO;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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

    @Test
    @Transactional
    public void testCaching(){
        /*
         * hibernate 1st-level by default caches by ID
         * so the second call to findByName still hits the db
         * but once it fetches the records it knows both objects are same
         * so the return value of the second call come from the cache
         * i.e it returns the same object reference from the persistence context
         *
         * the above stmt is true only when 2nd level cache is not enabled
         */

        Optional<Category> optionalCategory1 = categoryRepository.findByName("cat1");
        Optional<Category> optionalCategory2 = categoryRepository.findByName("cat1");
        Assertions.assertTrue(optionalCategory1.isPresent());
        Assertions.assertTrue(optionalCategory2.isPresent());
        Assertions.assertSame(optionalCategory1.get(), optionalCategory2.get());
//        System.out.println(optionalCategory1.get().getPostSet());
    }

    @Test
    @Transactional
    public void testCaching2(){

        /*
         * here findById call hits the db, following two calls fetches the object from
         * persistent context only.
         * Here if we remove @Transactional then 3 persistent context will be created
         * for each call
         *
         * the above stmt is true when 2nd level caching is not enabled
         *
         * if 2LC is enabled then the following happens:
         *
         * * Hibernate's second-level cache stores entities outside of the persistence context
         * (in a cache like Ehcache, Caffeine, etc.).
         * * When you load an entity and it’s not in the first-level cache, Hibernate:
         *      * Checks the second-level cache
         *      * If found, it deserializes or reconstructs a new entity instance
         *      * Then attaches it to the current (new) persistence context
         *
         * Even though the entity is fetched from second-level cache, it is:
         *  1. Detached from any previous context
         *  2. Rehydrated into a new object for each new persistence context
         *
         * Each entityManager.find(Category.class, 1) call creates a new instance of
         * Category — even if the underlying data comes from the second-level cache.
         */

        Optional<Category> optionalCategory = categoryRepository.findById(1L);
        Category category1 = entityManager.find(Category.class, 1L);
        Category category2 = entityManager.find(Category.class, 1L);
        Assertions.assertSame(category1, category2);
    }

    @Test
    @Transactional
    public void testCaching3(){
        /*
            if entity is 2nd level cache is enabled for entity
            then this test case will fire only 1 query to db, otherwise, it will fire 2
         */

        Session session = entityManager.unwrap(Session.class);
        Optional<Category> optionalCategory = categoryRepository.findById(1L);
        Category category1 = entityManager.find(Category.class, 1);
        session.evict(category1); // remove from pc
        Category category2 = entityManager.find(Category.class, 1);
        Assertions.assertNotSame(category1, category2);
    }

    @Test
    @Transactional
    public void testAssocation1(){
        List<Object[]> categoryList = categoryRepository.findByPostSetTitle1("title1", PageRequest.of(0, 1));

        for (Object[] objects : categoryList) {
            String cat = (String) objects[0];
            Long i = (Long) objects[1];
            System.out.println(cat + ":" + i);
        }
    }

    @Test
    @Transactional
    public void testAssocation2(){
        Optional<Object> category = categoryRepository.findByPostSetTitle2("title1");
        Assertions.assertTrue(category.isPresent());
        Object[] row = (Object[]) category.get();
        System.out.println(row[0]);
        System.out.println(row[1]);
    }

    @Test
    @Transactional
    public void testAssocation3(){
        Optional<CategoryPostVO> categoryPostVO = categoryRepository.findByPostSetTitle3("title1");
        Assertions.assertTrue(categoryPostVO.isPresent());
        System.out.println(categoryPostVO.get());
    }

}
