package com.web.imp;


import com.web.dao.DocumentaryDetailMapper;
import com.web.pojo.DocumentaryDetail;
import com.web.service.IDocumentaryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by may on 2018/5/9.
 */
@Service
@Transactional
public class DocumentaryDetailServiceImpl implements IDocumentaryDetailService {
    @Autowired
    private DocumentaryDetailMapper documentaryDetailMapper;
    @Override
    public void insert(DocumentaryDetail record) {
        documentaryDetailMapper.insert(record);
    }

    @Override
    public List<DocumentaryDetail> selectAll() {

        return documentaryDetailMapper.selectAll();
    }

    @Override
    public List<DocumentaryDetail> selectDataByASC() {
        return documentaryDetailMapper.selectDataByASC();
    }

    @Override
    public void updateByPrimaryKey(DocumentaryDetail record) {
        documentaryDetailMapper.updateByPrimaryKey(record);
    }
}
