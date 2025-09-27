package com.stu.service;

import com.stu.entity.DormitoryOrderDTO;
import com.stu.entity.TextbookOrderDTO;
import com.stu.vo.Result;

/**
 * 校园订单服务接口
 * 提供教材回收订单、宿舍回收订单的创建与用户校验功能。
 */
public interface CampusOrderService {

    /**
     * 创建教材回收订单
     *
     * @param orderDTO 教材订单数据传输对象，包含教材类型、数量、取件地点等信息
     * @param userId   下单用户的ID
     * @return 订单创建结果（成功/失败及提示信息）
     */
    Result createTextbookOrder(TextbookOrderDTO orderDTO, Long userId);

    /**
     * 创建宿舍回收订单
     *
     * @param orderDTO 宿舍订单数据传输对象，包含宿舍楼、房间范围、回收品类等信息
     * @param userId   下单用户的ID
     * @return 订单创建结果（成功/失败及提示信息）
     */
    Result createDormitoryOrder(DormitoryOrderDTO orderDTO, Long userId);

    /**
     * 校验用户是否为校园用户
     *
     * @param userId 用户ID
     * @return true 表示用户有效且属于校园用户，false 表示无效或不属于
     */
    boolean validateCampusUser(Long userId);

}

