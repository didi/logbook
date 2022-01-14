# 一、核心接口

## 1. 准入接口

- 请求路径：/logbook/api/event/permit

- 说明：该接口用于接收业务事件请求，进行准入校验（不会触发后续的翻译、分发等逻辑）

- 请求参数 header：

| header名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| Content-Type   | string   | 是  | 固定值：application/json   |
| x-caller   | string   | 是  | 调用方标识，如：upstream1   |
| x-authorization | string   | 是  | 将 caller 和 credential 拼接后，通过 md5 加密得到的 authorization 串。md5(${caller}.${credential})   |


- 请求参数 body：

| 参数名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| biz_event_type   | integer   | 是  | 业务事件类型 |
| biz_event_id   | string   | 是  | 业务事件id（唯一标识） |
| biz_event_info | json object   | 是  | 业务事件上下文   |
| biz_entity_info  | json object   | 是  | 业务事件实体信息 |
| biz_entity_info_before | json object   | 是  | 业务事件发生前的实体信息  |


- 返回结果：

| 参数名    | 参数类型 | 参数说明      |
| ------------ | -------- | ------------------|
| errno      | int      | 错误码，枚举见下方 |
| errmsg       | string   | 错误信息       |
| data | string   | 返回结果详情  |

返回结果解读：
> - 当且仅当 errno==0 && data.success==true 时，准入成功
> - errno!=0 时，准入失败，错误信息在 errmsg
> - error==0 && data.success==false 时，准入失败，错误信息在 data.result



## 2. 翻译接口

- 请求路径：/logbook/api/event/translate

- 说明：该接口用于接收业务事件请求，进行翻译和分发（不会执行准入，默认已经在请求当前接口前准入通过）

- 请求参数 header：

| header名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| Content-Type   | string   | 是  | 固定值：application/json   |
| x-caller   | string   | 是  | 调用方标识，如：upstream1   |
| x-authorization | string   | 是  | 将 caller 和 credential 拼接后，通过 md5 加密得到的 authorization 串。md5(${caller}.${credential})   |


- 请求参数 body：

| 参数名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| biz_event_type   | integer   | 是  | 业务事件类型 |
| biz_event_id   | string   | 是  | 业务事件id（唯一标识） |
| biz_event_info | json object   | 是  | 业务事件上下文   |
| biz_entity_info  | json object   | 是  | 业务事件实体信息 |
| biz_entity_info_before | json object   | 是  | 业务事件发生前的实体信息  |


- 返回结果：

| 参数名    | 参数类型 | 参数说明      |
| ------------ | -------- | ------------------|
| errno      | int      | 错误码，枚举见下方 |
| errmsg       | string   | 错误信息       |
| data | string   | 返回结果详情  |

返回结果解读：
> - 当且仅当 errno==0 && data.all_success==true 时，翻译成功，翻译结果在 data.translate_and_sync_results 里
> - errno!=0 时，翻译失败，错误信息在 errmsg
> - error==0 && data.all_success==false 时，翻译失败，翻译结果详情在 data.translate_and_sync_results



## 3. 上报接口

- 请求路径：/logbook/api/event/report

- 说明：该接口用于接收业务事件请求，进行准入、翻译和分发（是 permit 和 translate 接口逻辑的组合）
  
- 请求参数 header：

| header名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| Content-Type   | string   | 是  | 固定值：application/json   |
| x-caller   | string   | 是  | 调用方标识，如：upstream1   |
| x-authorization | string   | 是  | 将 caller 和 credential 拼接后，通过 md5 加密得到的 authorization 串。md5(${caller}.${credential})   |


- 请求参数 body：

| 参数名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| biz_event_type   | integer   | 是  | 业务事件类型 |
| biz_event_id   | string   | 是  | 业务事件id（唯一标识） |
| biz_event_info | json object   | 是  | 业务事件上下文   |
| biz_entity_info  | json object   | 是  | 业务事件实体信息 |
| biz_entity_info_before | json object   | 是  | 业务事件发生前的实体信息  |


- 返回结果：

| 参数名    | 参数类型 | 参数说明      |
| ------------ | -------- | ------------------|
| errno      | int      | 错误码，枚举见下方 |
| errmsg       | string   | 错误信息       |
| data | string   | 返回结果详情  |

返回结果解读：
> - 当且仅当 errno==0 && data.success==true 时，上报成功，翻译结果在 data.translate_and_sync_results 里
> - errno!=0 时，上报失败，错误信息在 errmsg
> - error==0 && data.success==false 时，上报失败，错误信息在 data.result，翻译结果详情在 data.translate_and_sync_results


# 二、非核心接口

## 1. 中断业务事件上报流程

- 请求路径：/logbook/api/abort/biz_event_progress
- 说明：将准入通过、翻译失败的业务事件做作废处理
- 请求参数 header：

| header名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| Content-Type   | string   | 是  | 固定值：application/json   |
| x-caller   | string   | 是  | 调用方标识，如：upstream1   |
| x-authorization | string   | 是  | 将 caller 和 credential 拼接后，通过 md5 加密得到的 authorization 串。md5(${caller}.${credential})   |


- 请求参数 body：

| 参数名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| global_biz_event_id   | string   | 是  | 全局业务事件id（由logbook生成） |


- 返回结果：

| 参数名    | 参数类型 | 参数说明      |
| ------------ | -------- | ------------------|
| errno      | int      | 错误码，枚举见下方 |
| errmsg       | string   | 错误信息       |
| data | string   | 返回结果详情  |

返回结果解读：
> - 当且仅当 errno==0 && data.success==true 时，请求成功
> - errno!=0 时，请求失败，错误信息在 errmsg
> - error==0 && data.success==false 时，请求失败，错误信息在 data.result


## 2. 中断实体事件分发流程

- 请求路径：/logbook/api/abort/entity_event_progress
- 说明：将分发到数据使用方的实体事件失败的请求，做作废处理
- 请求参数 header：

| header名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| Content-Type   | string   | 是  | 固定值：application/json   |
| x-caller   | string   | 是  | 调用方标识，如：upstream1   |
| x-authorization | string   | 是  | 将 caller 和 credential 拼接后，通过 md5 加密得到的 authorization 串。md5(${caller}.${credential})   |


- 请求参数 body：

| 参数名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| consume_global_event_id   | string   | 是  | 全局实体事件id（由logbook生成） |


- 返回结果：

| 参数名    | 参数类型 | 参数说明      |
| ------------ | -------- | ------------------|
| errno      | int      | 错误码，枚举见下方 |
| errmsg       | string   | 错误信息       |
| data | string   | 返回结果详情  |

返回结果解读：
> - 当且仅当 errno==0 && data.success==true 时，请求成功
> - errno!=0 时，请求失败，错误信息在 errmsg
> - error==0 && data.success==false 时，请求失败，错误信息在 data.result



## 3. 业务事件上报流程重试

- 请求路径：/logbook/api/retry/report
- 说明：对准入通过、翻译失败的业务事件的请求，进行重试
- 请求参数 header：

| header名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| Content-Type   | string   | 是  | 固定值：application/json   |
| x-caller   | string   | 是  | 调用方标识，如：upstream1   |
| x-authorization | string   | 是  | 将 caller 和 credential 拼接后，通过 md5 加密得到的 authorization 串。md5(${caller}.${credential})   |


- 请求参数 body：

| 参数名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| global_biz_event_id   | string   | 是  | 全局业务事件id（由logbook生成） |


- 返回结果：

| 参数名    | 参数类型 | 参数说明      |
| ------------ | -------- | ------------------|
| errno      | int      | 错误码，枚举见下方 |
| errmsg       | string   | 错误信息       |
| data | string   | 返回结果详情  |

返回结果解读：
> - 当且仅当 errno==0 && data.success==true 时，请求成功
> - errno!=0 时，请求失败，错误信息在 errmsg
> - error==0 && data.success==false 时，上报失败，错误信息在 data.result，翻译结果详情在 data.translate_and_sync_results

## 4. 实体事件分发流程重试

- 请求路径：/logbook/api/retry/consume
- 说明：对分发到数据使用方的实体事件失败的请求，进行重试
- 请求参数 header：

| header名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| Content-Type   | string   | 是  | 固定值：application/json   |
| x-caller   | string   | 是  | 调用方标识，如：upstream1   |
| x-authorization | string   | 是  | 将 caller 和 credential 拼接后，通过 md5 加密得到的 authorization 串。md5(${caller}.${credential})   |


- 请求参数 body：

| 参数名 | 参数类型 | 必填 | 参数说明 |
| -------- | -------- | ---- | ------------ |
| consume_global_event_id   | string   | 是  | 全局实体事件id（由logbook生成） |


- 返回结果：

| 参数名    | 参数类型 | 参数说明      |
| ------------ | -------- | ------------------|
| errno      | int      | 错误码，枚举见下方 |
| errmsg       | string   | 错误信息       |
| data | string   | 返回结果详情  |

返回结果解读：
> - 当且仅当 errno==0 && data.success==true 时，请求成功
> - errno!=0 时，请求失败，错误信息在 errmsg
> - error==0 && data.success==false 时，请求失败，错误信息在 data.result
> - 重试失败时，结果中的 data.need_retry 用来描述是否需要再次重试




# 二、错误码

- 定义错误码 id-enName 的文件：
    - ```logbook-util/src/main/java/com/didiglobal/common/error/AuthError.java```
    - ```logbook-util/src/main/java/com/didiglobal/common/error/CommonError.java```
    - ```logbook-server/src/main/java/com/didiglobal/common/LogbookError.java```

- 定义错误码 enName-cnName 的文件：
    - ```logbook-server/src/main/resources/message-zh_CN.properties```

- 重要错误码说明：

| 错误码 | 错误码信息 | 中文描述 |
| ------ | ------------ | -------- |
| 0   | SUCCESS | 请求成功 |
| 1   | SUCCEED       | 已经请求成功 |
| 100000   | PARAM_INVALID       | 参数错误 |
| 100001   | UNEXPECTED_ERROR       | 未知异常 |
