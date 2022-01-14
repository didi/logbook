package com.didiglobal.mediator.storage;

import com.didiglobal.mybatis.entity.BizEventProcessRecord;
import com.didiglobal.mybatis.mapper.BizEventProcessRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author mayingdong
 * @date 2020/11/30
 */
@Component
@Slf4j
public class BizEventRecordStorageImpl implements BizEventRecordStorage {

    @Autowired
    BizEventProcessRecordMapper bizEventProcessRecordMapper;

    @Override
    public int insert(BizEventProcessRecord record) {
        int effectLine = 0;
        try {
            effectLine = bizEventProcessRecordMapper.insert(record);
        } catch(DuplicateKeyException de) {
            log.info("BizEventRecordStorage insert duplicateKeyException,de=",de);
        } catch (Exception e) {
            effectLine = bizEventProcessRecordMapper.insert(record);
            log.error("BizEventRecordStorage insert caught exception, record={}, " +
                    "effectLine={}, e=", record, effectLine, e);
        }

        return effectLine;
    }

    @Override
    public int update(BizEventProcessRecord record,int oldStatus) {
        int effectLine = 0;
        try {
            effectLine = bizEventProcessRecordMapper.update(record,oldStatus);
        } catch (Exception e) {
            log.error("BizEventRecordStorage update caught exception,bizEventId={}, upgradeInfo={}, " +
                    "effectLine={}, e=", record.getGlobalBizEventId(), record, effectLine, e);
        }

        return effectLine;
    }


    @Override
    public BizEventProcessRecord queryByBizEventId(String bizEventId) {
        try {
            return bizEventProcessRecordMapper.queryByBizEventId(bizEventId);
        }catch (Exception e) {
            log.warn("BizEventRecordStorage query failed,bizEventId={},e=",bizEventId,e);
            return null;
        }
    }


}
