package com.example.moviesapi.config;

import com.example.moviesapi.exception.InvalidPaginationException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.context.request.NativeWebRequest;

public class CustomPageableResolver implements HandlerMethodArgumentResolver {
    private final PageableHandlerMethodArgumentResolver delegate;

    public CustomPageableResolver(PageableHandlerMethodArgumentResolver delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return delegate.supportsParameter(parameter);
    }

    @Override
    public Pageable resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String pageParam = webRequest.getParameter("page");
        String sizeParam = webRequest.getParameter("size");

        if (pageParam != null && Integer.parseInt(pageParam) < 0) {
            throw new InvalidPaginationException("Invalid page number");
        }

        if (sizeParam != null) {
            int size = Integer.parseInt(sizeParam);
            if (size <= 0 || size > 100) {
                throw new InvalidPaginationException("Invalid page size");
            }
        }

        return delegate.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }
}