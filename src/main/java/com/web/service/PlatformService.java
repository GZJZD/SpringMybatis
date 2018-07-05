package com.web.service;

import com.web.pojo.Platform;

import java.util.List;

public interface PlatformService {
    Platform getPlatformById(Long platformId);
    List<Platform> getListPlatform();
}
