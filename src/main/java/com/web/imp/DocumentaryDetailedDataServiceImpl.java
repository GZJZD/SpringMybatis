package com.web.imp;


import com.web.dao.DocumentaryDetailedDataMapper;
import com.web.pojo.DocumentaryDetailedData;
import com.web.service.IDocumentaryDetailedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by may on 2018/5/9.
 */
@Service
@Transactional
public class DocumentaryDetailedDataServiceImpl implements IDocumentaryDetailedDataService {
    @Autowired
    private DocumentaryDetailedDataMapper documentaryDetailedDataMapper;
    @Override
    public void insert(DocumentaryDetailedData record) {
        documentaryDetailedDataMapper.insert(record);
    }

    @Override
    public List<DocumentaryDetailedData> selectAll() {

        return documentaryDetailedDataMapper.selectAll();
    }

    @Override
    public List<DocumentaryDetailedData> selectDataByASC() {
        return documentaryDetailedDataMapper.selectDataByASC();
    }

    @Override
    public void updateByPrimaryKey(DocumentaryDetailedData record) {
        documentaryDetailedDataMapper.updateByPrimaryKey(record);
    }
}
