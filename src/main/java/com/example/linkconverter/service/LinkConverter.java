package com.example.linkconverter.service;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;

import java.net.URISyntaxException;

public interface LinkConverter {

    DeeplinkDto convertWebUrlToDeeplink(WebUrlDto webUrlDto) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException;

    WebUrlDto convertDeeplinkToWebUrl(DeeplinkDto deeplinkDto) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException;

}
