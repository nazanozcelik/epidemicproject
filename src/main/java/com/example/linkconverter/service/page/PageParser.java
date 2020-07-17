package com.example.linkconverter.service.page;

import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.util.LinkType;

import java.net.URISyntaxException;

abstract public class PageParser {
    abstract String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException;
}
