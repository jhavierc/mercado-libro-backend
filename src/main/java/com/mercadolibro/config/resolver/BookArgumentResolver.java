package com.mercadolibro.config.resolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.dto.annotation.BookRequest;
import com.mercadolibro.exception.UnsupportedParameterTypeException;
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
    private final ObjectMapper patchMapper;
    public static final String ARGUMENT_RESOLVER_UNSUPPORTED_PARAMETER_TYPE_ERROR = "The parameter class is not supported. Please use BookReqDTO or BookReqPatchDTO.";

    @Autowired
    public BookArgumentResolver(ObjectMapper mapper, ObjectMapper patchMapper) {
        this.mapper = mapper;
        this.patchMapper = patchMapper;
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

        if (parameter.getParameterType().equals(BookReqDTO.class)) {
            BookReqDTO bookReqDTO = mapper.convertValue(simpleAttributes, BookReqDTO.class);
            handleMultipartData(httpServletRequest, bookReqDTO);

            return bookReqDTO;
        } else if (parameter.getParameterType().equals(BookReqPatchDTO.class)) {
            patchMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            BookReqPatchDTO bookReqPatchDTO = patchMapper.convertValue(simpleAttributes, BookReqPatchDTO.class);
            handleMultipartData(httpServletRequest, bookReqPatchDTO);

            return bookReqPatchDTO;
        }

        throw new UnsupportedParameterTypeException(ARGUMENT_RESOLVER_UNSUPPORTED_PARAMETER_TYPE_ERROR);
    }

    private Map<String, Object> extractSimpleAttributes(Map<String, String[]> attributes) throws JsonProcessingException {
        Map<String, Object> simpleAttributes = new HashMap<>();
        for (Map.Entry<String, String[]> entry : attributes.entrySet()) {
            String[] values = entry.getValue();
            if (values.length > 0) {
                switch (entry.getKey()){
                    case "categories[]":
                        List<BookCategoryReqDTO> bookCategoryReqDTOS = mapper.readValue(Arrays.toString(values), mapper.getTypeFactory().constructCollectionType(List.class, BookCategoryReqDTO.class));
                        simpleAttributes.put("categories", bookCategoryReqDTOS);
                        break;
                    case "remove_images_ids[]":
                        List<Long> imagesToDelete = mapper.readValue(Arrays.toString(values), mapper.getTypeFactory().constructCollectionType(List.class, Long.class));
                        simpleAttributes.put("remove_images_ids", imagesToDelete);
                        break;
                    case "authors[]":
                        List<AuthorDTO> authors = mapper.readValue(Arrays.toString(values), mapper.getTypeFactory().constructCollectionType(List.class, AuthorDTO.class));
                        simpleAttributes.put("authors", authors);
                        break;
                    default:
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

    private void handleMultipartData(HttpServletRequest httpServletRequest, BookReqPatchDTO bookReqPatchDTO) {
        if (httpServletRequest instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) httpServletRequest;

            List<MultipartFile> files = multipartRequest.getMultiFileMap().get("images[]");
            if (files != null) {
                bookReqPatchDTO.getImages().addAll(files);
            }
        }
    }
}

