package com.example.sales.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// CustomerItem用のJdbcClientを特定するカスタムアノテーション
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("customerItemJdbcClient")
public @interface CustomerItemJdbcClient {
}
