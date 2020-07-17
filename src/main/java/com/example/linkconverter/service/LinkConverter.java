package com.example.linkconverter.service;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;

import java.net.URISyntaxException;

public interface LinkConverter {

    DeeplinkDto deeplinkConverter(WebUrlDto webUrlDto) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException;

    WebUrlDto webUrlConverter(DeeplinkDto deeplinkDto);

}
