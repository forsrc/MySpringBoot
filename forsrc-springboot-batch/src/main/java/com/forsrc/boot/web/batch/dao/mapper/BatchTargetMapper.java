package com.forsrc.boot.web.batch.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.forsrc.boot.batch.pojo.BatchTarget;

@Mapper
public interface BatchTargetMapper {

    public void create();

    public void delete();

    public int count();

    public void insert(@Param("bean") BatchTarget bean);
}
