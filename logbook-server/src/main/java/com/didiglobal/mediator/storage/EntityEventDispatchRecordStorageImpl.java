package com.didiglobal.mediator.storage;

import com.didiglobal.mybatis.entity.EntityEventDispatchRecord;
import com.didiglobal.mybatis.mapper.EntityEventDispatchRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author liyanling
 * @date 2021/11/22 3:09 下午
 */
@Component
@Slf4j
public class EntityEventDispatchRecordStorageImpl implements EntityEventDispatchRecordStorage{

    @Autowired
    EntityEventDispatchRecordMapper mapper;


    @Override
    public int insert(EntityEventDispatchRecord record) {
        int effectLine = 0;
        try {
            effectLine = mapper.insert(record);
        } catch(DuplicateKeyException de) {
            log.info("EntityEventDispatchRecordStorage insert duplicateKeyException,de=",de);
        } catch (Exception e) {
            effectLine = mapper.insert(record);
            log.error("EntityEventDispatchRecordStorage insert caught exception, record={}, effectLine={}, e=",
                    record, effectLine, e);
        }

        return effectLine;    }

    @Override
    public int update(EntityEventDispatchRecord record,int oldStatus) {
        int effectLine = 0;
        try {
            effectLine = mapper.update(record,oldStatus);
        } catch (Exception e) {
            log.error("EntityEventDispatchRecordStorage update caught exception,record={},effectLine={}, e=",
                    record, effectLine, e);
        }

        return effectLine;
    }

    @Override
    public EntityEventDispatchRecord queryByConsumeGlobalEventId(String consumeGlobalEventId) {
        try{
            return mapper.queryByConsumeGlobalEventId(consumeGlobalEventId);
        }catch (Exception e){
            log.warn("EntityEventDispatchRecordStorage queryByConsumeGlobalEventId failed,consumeGlobalEventId={},e=",
                    consumeGlobalEventId,e);
            return null;
        }
    }
}
