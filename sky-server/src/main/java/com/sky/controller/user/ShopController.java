package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NXRUI
 * @version 1.0
 * @date 2023/6/10 15:05
 * @description:
 */
@Api(tags = "店铺相关接口")
@RestController("userShopController")
@RequestMapping("user/shop")
@Slf4j
public class ShopController {

    private final RedisTemplate redisTemplate;

    public static final String KEY="SHOP_STATUS";

    public ShopController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 查询店铺状态
     */
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("设置店铺的营业状态为:{}", shopStatus == 1 ? "营业中":"打烊中");
        return Result.success(shopStatus);
    }
}

