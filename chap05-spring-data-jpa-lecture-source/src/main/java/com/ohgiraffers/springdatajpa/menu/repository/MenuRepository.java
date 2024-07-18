package com.ohgiraffers.springdatajpa.menu.model.repository;

import com.ohgiraffers.springdatajpa.menu.model.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// EntityMangerFactory, EntityManager, EntityTransaction -> 자동 구현
// JpaRepository<엔티티명, PK타입>
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    // 쿼리 메소드
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice, Sort menuPrice1);
    List<Menu> findByMenuPriceGreaterThanOrderByMenuPriceDesc(Integer menuPrice);

    // 금액이 같은 메뉴들
    List<Menu> findByMenuPriceEquals(Integer menuPrice);

    // 금액이 크거나 같은 메뉴들
    List<Menu> findByMenuPriceGreaterThanEqual(Integer menuPrice);


    // menuPrice랑 같은 금액의 메뉴목록 조회
}