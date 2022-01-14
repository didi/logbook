import com.didiglobal.common.translate.TranslateOutput;
TranslateOutput output = new TranslateOutput();
output.setEventType(40000);
output.setEventInfo(translateInput.getBizEventInfo());
Long eventTime = translateInput.getBizEventInfo().getLong("event_time");
output.setEventTime(eventTime==null? System.currentTimeMillis() : eventTime);
output.setEntityId(String.valueOf(translateInput.getBizEntityInfo().getLong("worksheet_id")));
output.setEntityInfoBeforeEvent(translateInput.getBizEntityInfoBefore());
output.setEntityInfoAfterEvent(translateInput.getBizEntityInfo());
return output;

