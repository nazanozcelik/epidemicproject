package com.example.linkconverter.service;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.service.page.PageRouter;
import com.example.linkconverter.util.LinkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class LinkBuilder {

    @Autowired
    PageRouter pageRouter;

    @Autowired
    public LinkBuilder(PageRouter pageRouter) {
        this.pageRouter = pageRouter;
    }

    public DeeplinkDto createDeeplink(String url) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        DeeplinkDto deeplinkDto = new DeeplinkDto();
        deeplinkDto.setDeeplink(pageRouter.routePage(url, LinkType.WEBURL_TO_DEEPLINK));
        return deeplinkDto;
    }
}



