package com.example.linkconverter.service;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;
import com.example.linkconverter.service.page.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class LinkRouter implements LinkConverter {

    @Autowired
    private PageService pageService;

    @Override
    public DeeplinkDto deeplinkConverter(WebUrlDto url) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {

        LinkBuilder linkBuilder = new LinkBuilder(pageService);
        return linkBuilder.createDeeplink(url.getWebURL());
    }

    @Override
    public WebUrlDto webUrlConverter(DeeplinkDto url) {
        return null;
    }

}
