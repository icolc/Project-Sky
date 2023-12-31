package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author NXRUI
 * @version 1.0
 * @date 2023/6/6 20:13
 * @description:
 */
@Slf4j
@Api(tags = "菜品接口")
@RequestMapping("/admin/dish")
@RestController
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result<?> save(@RequestBody DishDTO dishDTO) {
        log.info("save() called with parameters => 【dishDTO = {}】", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        //构建key
        String key = "dish_" + dishDTO.getCategoryId();
        //清理缓存数据
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("page() called with parameters => 【dishPageQueryDTO = {}】", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     */
    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result<?> deleteByIds(@RequestParam List<Long> ids) {
        log.info("deleteByIds() called with parameters => 【ids = {}】", ids);
        //开始删除
        dishService.deleteByIds(ids);
        //将所有菜品数据从缓存中删
        cleanCache();
        return Result.success();
    }

    /**
     * 根据ID查询菜品
     *
     * @param id
     * @return
     */
    @ApiOperation("查询回现")
    @GetMapping("/{id}")
    public Result<DishVO> selectById(@PathVariable Long id) {
        log.info("查询回显中:{}", id);
        DishVO dishVO = dishService.selectByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品")
    @PutMapping
    public Result<?> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品中:{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        //将所有菜品数据从缓存中删
        cleanCache();
        return Result.success();
    }

    /**
     * 根据id查
     */
    @ApiOperation("根据ID查询菜品")
    @GetMapping("/list")
    public Result<List<DishVO>> selectList(Integer categoryId, String name) {
        log.info("selectList() called with parameters => 【categoryId = {}】", categoryId);
        List<DishVO> dishVOs = dishService.selectList(categoryId, name);
        return Result.success(dishVOs);
    }

    /**
     * 修改状态
     */
    @ApiOperation("起售或停售")
    @PostMapping("/status/{status}")
    public Result<?> statusOrStop(@PathVariable Integer status, Long id) {
        log.info("StatusOrStop() called with parameters => 【status = {}】, 【id = {}】", status, id);
        dishService.statusOrStop(status, id);
        cleanCache();
        return Result.success();
    }

    private void cleanCache() {
        //将所有菜品数据从缓存中删
        Set dish = redisTemplate.keys("dish_*");
        if (dish != null) {
            redisTemplate.delete(dish);
        }
    }
}
