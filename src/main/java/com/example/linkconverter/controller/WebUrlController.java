package com.example.linkconverter.controller;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;
import com.example.linkconverter.service.LinkRouter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

/**
 * WebUrl Controller
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class WebUrlController {

    @Autowired
    private LinkRouter linkRouter;

    @ApiOperation("Creates webUrl from given deeplink.")
    @PostMapping(value = "/deeplink-to-weburl")
    @ResponseStatus(HttpStatus.OK)
    public WebUrlDto createWebUrlWithGivenDeeplink(
            @Validated @RequestBody DeeplinkDto deeplinkDto) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {

        log.info("Creating webUrl from deeplink: {}", deeplinkDto.getDeeplink());
        return linkRouter.convertDeeplinkToWebUrl(deeplinkDto);

    }
}
