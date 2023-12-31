package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author NXRUI
 * @version 1.0
 * @date 2023/6/6 20:14
 * @description:
 */
public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID批量删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据ID查询菜品以及口味数据
     * @return
     */
    DishVO selectByIdWithFlavor(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类查询
     * @param categoryId
     * @return
     */
    List<DishVO> selectList(Integer categoryId,String name);

    /**
     * 修改菜品状态
     * @param status
     * @param id
     */
    void statusOrStop(Integer status, Long id);


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
