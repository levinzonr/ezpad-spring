package com.levinzonr.ezpad.security.resource

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class ResourceServerConfiguration : ResourceServerConfigurerAdapter() {

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.resourceId(ResourceServerSettings.RESOURCE_ID)
    }

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()
                ?.antMatchers(HttpMethod.POST, "/oauth/**")?.permitAll()
                ?.antMatchers("/auth/**")?.permitAll()
                ?.antMatchers(HttpMethod.OPTIONS, "/api/**")?.permitAll()
                ?.and()
                ?.antMatcher("/api/**")?.authorizeRequests()
                ?.antMatchers(HttpMethod.POST, "/api/users")?.permitAll()
                ?.anyRequest()?.authenticated()
    }

}