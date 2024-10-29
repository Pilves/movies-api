package com.example.moviesapi.config;

import com.example.moviesapi.exception.ApiException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PaginationConfig implements WebMvcConfigurer {

    private final PaginationProperties properties;

    public PaginationConfig(PaginationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(properties.isOneIndexedParameters());
        resolver.setMaxPageSize(properties.getMaxPageSize());
        resolver.setPageParameterName(properties.getPageParameterName());
        resolver.setSizeParameterName(properties.getSizeParameterName());
        resolver.setFallbackPageable(PageRequest.of(0, properties.getDefaultPageSize()));

        resolvers.add(new CustomPageableResolver(resolver, properties));
    }

    @Configuration
    @ConfigurationProperties(prefix = "api.pagination")
    @Validated
    @Getter
    @Setter
    public static class PaginationProperties {
        @Min(1) @Max(1000)
        private int maxPageSize = 100;

        @Min(1) @Max(100)
        private int defaultPageSize = 20;

        @Min(0)
        private int minPageNumber = 0;

        private boolean oneIndexedParameters = false;
        private String pageParameterName = "page";
        private String sizeParameterName = "size";
    }

    public static class CustomPageableResolver extends PageableHandlerMethodArgumentResolver {
        private final PaginationProperties properties;

        public CustomPageableResolver(PageableHandlerMethodArgumentResolver delegate,
                                      PaginationProperties properties) {
            super();
            this.properties = properties;
        }

        @Override
        public Pageable resolveArgument(org.springframework.core.MethodParameter methodParameter,
                                        org.springframework.web.method.support.ModelAndViewContainer mavContainer,
                                        org.springframework.web.context.request.NativeWebRequest webRequest,
                                        org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {

            String pageParam = webRequest.getParameter(properties.getPageParameterName());
            String sizeParam = webRequest.getParameter(properties.getSizeParameterName());

            validatePaginationParameters(pageParam, sizeParam);

            Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
            validatePageable(pageable);

            return pageable;
        }

        private void validatePaginationParameters(String pageParam, String sizeParam) {
            if (pageParam != null) {
                try {
                    int page = Integer.parseInt(pageParam);
                    if (page < properties.getMinPageNumber()) {
                        throw ApiException.invalidRequest(
                                String.format("Page number must not be less than %d", properties.getMinPageNumber())
                        );
                    }
                } catch (NumberFormatException e) {
                    throw ApiException.invalidRequest("Invalid page number format");
                }
            }

            if (sizeParam != null) {
                try {
                    int size = Integer.parseInt(sizeParam);
                    if (size <= 0) {
                        throw ApiException.invalidRequest("Page size must be greater than 0");
                    }
                    if (size > properties.getMaxPageSize()) {
                        throw ApiException.invalidRequest(
                                String.format("Page size must not be greater than %d", properties.getMaxPageSize())
                        );
                    }
                } catch (NumberFormatException e) {
                    throw ApiException.invalidRequest("Invalid page size format");
                }
            }
        }

        private void validatePageable(Pageable pageable) {
            if (pageable.getPageSize() > properties.getMaxPageSize() ||
                    pageable.getPageSize() < 1 ||
                    pageable.getPageNumber() < properties.getMinPageNumber()) {
                throw ApiException.invalidRequest("Invalid pagination parameters");
            }
        }
    }
}