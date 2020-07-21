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
public class WebUrlController {


    @Autowired
    private LinkConverter linkConverter;

    @ApiOperation("Creates webUrl from given deeplink.")
    @PostMapping(value = "/deeplink-to-weburl")
    @ResponseStatus(HttpStatus.CREATED)
    public WebUrlDto createWebUrlWithGivenDeeplink(
            @Validated @RequestBody DeeplinkDto deeplinkDto) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException {

        log.info("Creating webUrl from deeplink: {}", deeplinkDto.getDeeplink());
        return linkConverter.convertDeeplinkToWebUrl(deeplinkDto);

    }
}
