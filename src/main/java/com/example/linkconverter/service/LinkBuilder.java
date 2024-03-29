package com.example.linkconverter.service;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;
import com.example.linkconverter.service.page.PageRouter;
import com.example.linkconverter.util.LinkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

/**
 * Service that creates links from given link types
 */
@Service
public class LinkBuilder {

    @Autowired
    public PageRouter pageRouter;

    @Autowired
    public LinkBuilder(PageRouter pageRouter) {
        this.pageRouter = pageRouter;
    }

    public DeeplinkDto createDeeplink(String url) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        DeeplinkDto deeplinkDto = new DeeplinkDto();
        deeplinkDto.setDeeplink(pageRouter.routePage(url, LinkType.WEBURL_TO_DEEPLINK));
        return deeplinkDto;
    }

    public WebUrlDto createWebUrl(String url) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        WebUrlDto webUrlDto = new WebUrlDto();
        webUrlDto.setWebURL(pageRouter.routePage(url, LinkType.DEEPLINK_TO_WEBURL));
        return webUrlDto;
    }
}



