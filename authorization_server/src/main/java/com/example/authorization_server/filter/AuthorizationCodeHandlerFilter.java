package com.example.authorization_server.filter;

import com.example.authorization_server.repository.ConsentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class AuthorizationCodeHandlerFilter extends GenericFilterBean {
    private static Logger logger = LoggerFactory.getLogger(AuthorizationCodeHandlerFilter.class);

    RequestMatcher requestMatcher = new AntPathRequestMatcher("/oauth2/authorize");

    static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        httpServletRequest.getHeaderNames();
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (this.requestMatcher.matches(httpServletRequest)) {
            try {
                Authentication principal = SecurityContextHolder.getContext().getAuthentication();
                MultiValueMap<String, String> parameters = getParameters(httpServletRequest);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (Exception e) {
                logger.error(e.getMessage());
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
