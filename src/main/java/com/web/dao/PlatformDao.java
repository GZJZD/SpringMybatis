package com.web.dao;

import com.web.pojo.Platform;

import java.util.List;

public interface PlatformDao {
    Platform getPlatformById(Long platformId);
    List<Platform> getListPlatform();
}
