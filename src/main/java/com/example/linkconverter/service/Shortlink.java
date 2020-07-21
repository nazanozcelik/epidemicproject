package com.example.linkconverter.service;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.exception.ShortlinkNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.LinksDto;
import com.example.linkconverter.model.dto.ShortlinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;

import java.net.URISyntaxException;

public interface Shortlink {

    String convertWebUrlToShortlink(WebUrlDto url) throws ResourceNotFoundException, SectionNotFoundException, LinkTypeNotFoundException, URISyntaxException;

    String convertDeeplinkToShortlink(DeeplinkDto url) throws ResourceNotFoundException, SectionNotFoundException, LinkTypeNotFoundException, URISyntaxException;

    LinksDto getLinksWithGivenShortlink(ShortlinkDto shortlinkDto) throws ShortlinkNotFoundException;
}

