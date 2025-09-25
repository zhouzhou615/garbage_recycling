// src/main/java/com/stu/mapper/AddressMapper.java
package com.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stu.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {

    List<Address> selectByUserId(@Param("userId") Long userId);

    Address selectDefaultByUserId(@Param("userId") Long userId);
}