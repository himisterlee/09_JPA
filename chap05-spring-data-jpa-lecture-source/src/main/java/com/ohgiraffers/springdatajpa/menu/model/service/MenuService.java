package com.ohgiraffers.springdatajpa.menu.model.service;

import com.ohgiraffers.springdatajpa.menu.model.dto.CategoryDto;
import com.ohgiraffers.springdatajpa.menu.model.dto.MenuDto;
import com.ohgiraffers.springdatajpa.menu.model.entity.Category;
import com.ohgiraffers.springdatajpa.menu.model.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.model.repository.MenuRepository;
import com.ohgiraffers.springdatajpa.menu.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    public MenuService(MenuRepository menuRepository, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    // /** 입력 후 엔터 누르면 아래 양식 표시됨
    /**
     * 메뉴코드로 메뉴를 찾는 메소드
     * @param menuCode : 찾을 메뉴의 메뉴코드
     * @return
     */
    public MenuDto findMenuByCode(int menuCode) {

        // MenuDto -> 일반클래스
        // Menu -> Entity


        /*
         * findById 메소드는 이미 구현이 되어있다.
         * 반환값은 Optional 타입이고 Optional Type 은 NPE 방지하기 위해 다양한 기능을 제공
         * */
        Menu menu = menuRepository.findById(menuCode)
                .orElseThrow(IllegalArgumentException::new);

        log.info("menu ============ {}", menu);

        return modelMapper.map(menu, MenuDto.class);
    }

    public List<MenuDto> findMenuList() {

        List<Menu> menulist =
                menuRepository.findAll();
//                menuRepository.findAll(Sort.by("menuPrice").descending());

        return menulist.stream()
                .map(menu -> modelMapper.map(menu, MenuDto.class))
                .collect(Collectors.toList());
    }

    public Page<MenuDto> findAllMenus(Pageable pageable) {


        // page 파라미터가 Pageable의 number로 넘어옴
        // 조회했을때는 인덱스 기준이 되기 때문에 -1 처리
        pageable = PageRequest.of( // PageRequest.of -> Pageable 객체 조작
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("menuCode").descending());

        Page<Menu> menuList = menuRepository.findAll(pageable);

        return menuList.map(menu -> modelMapper.map(menu, MenuDto.class));
    }

    public List<MenuDto> findByMenuPrice(Integer menuPrice) {

        List<Menu> menuList = null;

//        if(menuPrice == 0) {
//            menuList = menuRepository.findAll();
//        } else if (menuPrice > 0) {
//            menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice,
//                    Sort.by("menuPrice").descending());
//        }
        if(menuPrice == 0) {
            menuList = menuRepository.findAll();
        } else if (menuPrice > 0) {
            menuList =
                    menuRepository.findByMenuPriceGreaterThanOrderByMenuPriceDesc(menuPrice);
        }

        return menuList.stream()
                .map(menu -> modelMapper.map(menu, MenuDto.class))
                .collect(Collectors.toList());
    }

    public List<CategoryDto> findAllCategory() {

//        List<Category> categoryList = categoryRepository.findAll();

        //JPQL
//        List<Category> categoryList = categoryRepository.findAllCategoryByJPQL();

        // NativeQuery
        List<Category> categoryList = categoryRepository.findAllCategoryByNativeJPQL();

        return categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void registNewMenu(MenuDto newMenu) {

        Menu menu = modelMapper.map(newMenu, Menu.class);

        // Builder 적용
//        Menu menu = new Menu().builder()
//                .menuName(newMenu.getMenuName())
//                .menuPrice(newMenu.getMenuPrice())
//                .categoryCode(newMenu.getCategoryCode())
//                .orderableStatus(newMenu.getOrderableStatus())
//                .build();

        menuRepository.save(menu);
    }

    @Transactional
    public void modifyMenu(MenuDto modifyMenu) {

        // modifyMenu -> 비영속
        // 영속
        log.info("modifyMenu ===========> {}", modifyMenu);
        Menu foundMenu = menuRepository.findById(modifyMenu.getMenuCode())
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        foundMenu.setMenuName(modifyMenu.getMenuName());
//        foundMenu.toBuilder()
//                .menuName(modifyMenu.getMenuName())
//                .build();

        log.info("foundMenu ===========> {}", foundMenu);
    }

    @Transactional
    public void deleteMenu(Integer menuCode) {

        menuRepository.deleteById(menuCode);
    }
}