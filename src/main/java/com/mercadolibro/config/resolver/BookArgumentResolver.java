package com.mercadolibro.config.resolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookCategoryReqDTO;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.annotation.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class BookArgumentResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper mapper;

    @Autowired
    public BookArgumentResolver(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(BookRequest.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws JsonProcessingException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        Map<String, String[]> attributes = httpServletRequest.getParameterMap();

        Map<String, Object> simpleAttributes = extractSimpleAttributes(attributes);

        BookReqDTO bookReqDTO = mapper.convertValue(simpleAttributes, BookReqDTO.class);
        handleMultipartData(httpServletRequest, bookReqDTO);

        return bookReqDTO;
    }

    private Map<String, Object> extractSimpleAttributes(Map<String, String[]> attributes) throws JsonProcessingException {
        Map<String, Object> simpleAttributes = new HashMap<>();
        for (Map.Entry<String, String[]> entry : attributes.entrySet()) {
            String[] values = entry.getValue();
            if (values.length > 0) {
                if ("categories[]".equals(entry.getKey())) {
                    List<BookCategoryReqDTO> bookCategoryReqDTOS = mapper.readValue(Arrays.toString(values), mapper.getTypeFactory().constructCollectionType(List.class, BookCategoryReqDTO.class));
                    simpleAttributes.put("categories", bookCategoryReqDTOS);
                } else {
                    simpleAttributes.put(entry.getKey(), values[0]);
                }
            }
        }
        return simpleAttributes;
    }

    private void handleMultipartData(HttpServletRequest httpServletRequest, BookReqDTO bookReqDTO) {
        if (httpServletRequest instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) httpServletRequest;
            List<MultipartFile> files = multipartRequest.getMultiFileMap().get("images[]");
            bookReqDTO.setImages(files);
        }
    }
}

