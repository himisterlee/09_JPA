package com.ohgiraffers.springdatajpa.menu.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CategoryDto {

    private int categoryCode;
    private String categoryName;
    private Integer refCategoryCode;

}