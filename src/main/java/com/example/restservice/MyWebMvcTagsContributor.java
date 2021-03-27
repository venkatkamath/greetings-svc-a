package com.example.restservice;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsContributor;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class MyWebMvcTagsContributor implements WebMvcTagsContributor {

    @Override
    public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response, Object handler, Throwable exception) {
        return Tags.of("requestor","browser");
    }

    @Override
    public Iterable<Tag> getLongRequestTags(HttpServletRequest request, Object handler) {
        return null;
    }
}
