package com.fintech.wallet.transfer.system.config.filter;

import com.fintech.wallet.transfer.system.adapter.constant.AdapterConstant;
import com.fintech.wallet.transfer.system.adapter.out.cache.RedisIdempotencyKeyAdapter;
import com.fintech.wallet.transfer.system.application.constant.ApplicationError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class IdempotencyKeyFilter extends OncePerRequestFilter {

    private final RedisIdempotencyKeyAdapter redisIdempotencyKeyAdapter;
    private final ObjectMapper objectMapper;

    private static final String MONEY_TRANSFER_ENDPOINT = "/api/v1/transactions";
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    public IdempotencyKeyFilter(RedisIdempotencyKeyAdapter redisIdempotencyKeyAdapter, ObjectMapper objectMapper) {
        this.redisIdempotencyKeyAdapter = redisIdempotencyKeyAdapter;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!request.getRequestURI().startsWith(MONEY_TRANSFER_ENDPOINT)) {
            filterChain.doFilter(request, response);
        }

        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);

        String idempotencyKey = request.getHeader(AdapterConstant.HEADER_IDEMPOTENCY_KEY);
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("code", HttpStatus.BAD_REQUEST.value());
            errorBody.put("message", ApplicationError.MISSING_IDEMPOTENCY_KEY.getErrorMessage());
            errorBody.put("timestamp", Instant.now().toString());

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(errorBody));
            response.getWriter().flush();
            return;
        }

        Optional<String> cached = redisIdempotencyKeyAdapter.getResult(idempotencyKey);
        if (cached.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(cached.get());
            response.getWriter().flush();
            log.info("Cache hit!! Returning result for [{}={}].", AdapterConstant.HEADER_IDEMPOTENCY_KEY, idempotencyKey);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
