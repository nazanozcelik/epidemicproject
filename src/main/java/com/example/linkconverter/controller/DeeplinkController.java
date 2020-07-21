package com.example.linkconverter.controller;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;
import com.example.linkconverter.service.LinkConverter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class DeeplinkController {

    @Autowired
    private LinkConverter linkConverter;

    @ApiOperation("Creates deeplink from given webUrl.")
    @PostMapping(value = "/weburl-to-deeplink")
    @ResponseStatus(HttpStatus.CREATED)
    public DeeplinkDto createDeeplinkWithGivenWebUrl(
            @Validated @RequestBody WebUrlDto webUrlDto) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException {

        log.info("Creating deeplink from webUrl: {}", webUrlDto.getWebURL());
        return linkConverter.convertWebUrlToDeeplink(webUrlDto);

    }

}
