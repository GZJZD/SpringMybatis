package com.web.service;

import com.web.pojo.Tactics;

/**
 * 策略
 * Created by may on 2018/5/20.
 */
public interface ITacticsService {
    void save(Tactics tactics);
    void updateTactics(Tactics tactics);
    Tactics getTacticsByTacticsId(Long id);
}
