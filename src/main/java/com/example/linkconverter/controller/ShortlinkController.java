package com.example.linkconverter.controller;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.exception.ShortlinkNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.LinksDto;
import com.example.linkconverter.model.dto.ShortlinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;
import com.example.linkconverter.service.Shortlink;
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
public class ShortlinkController {

    @Autowired
    private Shortlink shortlink;

    @ApiOperation("Creates shortlink from given webUrl.")
    @PostMapping(value = "/weburl-to-shortlink")
    @ResponseStatus(HttpStatus.CREATED)
    public ShortlinkDto createShortlinkWithGivenWebUrl(
            @Validated @RequestBody WebUrlDto webUrlDto) throws ResourceNotFoundException, SectionNotFoundException, LinkTypeNotFoundException, URISyntaxException {

        ShortlinkDto shortlinkDto = new ShortlinkDto();
        log.info("Creating shortlink from webUrl: {}", webUrlDto.getWebURL());
        shortlinkDto.setShortlink(shortlink.convertWebUrlToShortlink(webUrlDto));

        return shortlinkDto;
    }


    @ApiOperation("Creates shortlink from given deeplink.")
    @PostMapping(value = "/deeplink-to-shortlink")
    @ResponseStatus(HttpStatus.CREATED)
    public ShortlinkDto createShortlinkWithGivenDeeplink(
            @Validated @RequestBody DeeplinkDto deeplinkDto) throws ResourceNotFoundException, SectionNotFoundException, LinkTypeNotFoundException, URISyntaxException {

        ShortlinkDto shortlinkDto = new ShortlinkDto();

        log.info("Creating shortlink from deeplink: {}", deeplinkDto.getDeeplink());
        shortlinkDto.setShortlink(shortlink.convertDeeplinkToShortlink(deeplinkDto));
        return shortlinkDto;

    }


    @ApiOperation("Gets deeplink and webUrl from given shortlink.")
    @GetMapping(value = "/links")
    @ResponseStatus(HttpStatus.OK)
    public LinksDto getAllLiks(
            @Validated @RequestBody ShortlinkDto shortlinkDto) throws ShortlinkNotFoundException {

        log.info("Getting webUrl and deeplink from given shortlink: {}", shortlinkDto.getShortlink());
        return shortlink.getLinksWithGivenShortlink(shortlinkDto);

    }
}

