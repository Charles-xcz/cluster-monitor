package com.ustc.sa.mapper;

import com.ustc.sa.pojo.MachineInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MachineInfoMapper {

    boolean add(MachineInfo machineInfo);

    List<MachineInfo> queryAll();
}
