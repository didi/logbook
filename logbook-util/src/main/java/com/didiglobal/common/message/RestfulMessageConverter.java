package com.didiglobal.common.message;

import com.didiglobal.common.response.ApiResponse;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;


public class RestfulMessageConverter extends MappingJackson2HttpMessageConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestfulMessageConverter.class);

    protected MessageManager messageMgr;

    public RestfulMessageConverter() {
        super();
        this.messageMgr = MessageManager.INSTANCE;
    }

    public RestfulMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
        this.messageMgr = MessageManager.INSTANCE;
    }

    public RestfulMessageConverter(ObjectMapper objectMapper,
                                   MessageManager messageMgr) {
        super(objectMapper);
        this.messageMgr = messageMgr;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        try {
            return super.readInternal(clazz, inputMessage);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Object read(Type type, Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        try {
            return super.read(type, clazz, inputMessage);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * override writeInternal
     *
     * @param object
     * @param type
     * @param outputMessage
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        if (!isApiResponse(object)) {
            super.writeInternal(object, type, outputMessage);
            return;
        }

        MediaType contentType = outputMessage.getHeaders().getContentType();
        JsonEncoding encoding = getJsonEncoding(contentType);
        JsonGenerator generator = defaultObjectMapper.getFactory()
                .createGenerator(outputMessage.getBody(), encoding);

        ApiResponse response = (ApiResponse) object;
        response.setErrmsg(messageMgr.getMessage(response.getErrmsg()));

        try {
            super.writePrefix(generator, object);
            this.writeValue(generator, object, type);
            super.writeSuffix(generator, object);
            generator.flush();
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(),
                    ex);
        }
    }

    protected void writeValue(JsonGenerator generator, Object object, Type type) throws IOException {
        Class<?> serializationView = null;
        FilterProvider filters = null;
        Object value = object;
        JavaType javaType = null;
        if (object instanceof MappingJacksonValue) {
            MappingJacksonValue container = (MappingJacksonValue) object;
            value = container.getValue();
            serializationView = container.getSerializationView();
            filters = container.getFilters();
        }
        if (type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
            javaType = getJavaType(type, null);
        }
        ObjectWriter objectWriter;
        if (serializationView != null) {
            objectWriter = this.defaultObjectMapper.writerWithView(serializationView);
        } else if (filters != null) {
            objectWriter = this.defaultObjectMapper.writer(filters);
        } else {
            objectWriter = this.defaultObjectMapper.writer();
        }
        if (javaType != null && javaType.isContainerType()) {
            objectWriter = objectWriter.forType(javaType);
        }
        objectWriter.writeValue(generator, value);
    }

    private boolean isApiResponse(final Object object) {
        return (object instanceof ApiResponse);
    }
}