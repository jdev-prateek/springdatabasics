package com.example.springdatabasics.vo;

public class CategoryPostVO {
    private String name;
    private Long id;

    public CategoryPostVO(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CategoryPostVO{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
