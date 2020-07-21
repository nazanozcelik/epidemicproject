package com.example.linkconverter.service;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.exception.ShortlinkNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.LinksDto;
import com.example.linkconverter.model.dto.ShortlinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;
import com.example.linkconverter.model.entity.Links;
import com.example.linkconverter.repository.LinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Optional;

import static com.example.linkconverter.util.Constants.SHORTLINK_PREFIX;

/**
 * Service that creates and serves shortlinks of given deeplink and web URL
 */
@Slf4j
@Service
public class ShortlinkService implements Shortlink {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    public LinkRouter linkRouter;

    @Autowired
    public ShortlinkService(LinkRouter linkRouter) {
        this.linkRouter = linkRouter;
    }

    @Retryable(maxAttempts = 2, value = {RuntimeException.class}, backoff = @Backoff(delay = 2000, multiplier = 2))
    @Override
    public String convertWebUrlToShortlink(WebUrlDto url) throws ResourceNotFoundException, SectionNotFoundException, LinkTypeNotFoundException, URISyntaxException {

        if (url.getWebURL().matches("https://www.trendyol.com")) {
            createShortlinkDirectsHomePage();
            return "https://ty.gl/shortlink";
        }

        Links links = new Links();

        Optional<Links> foundLink = Optional.ofNullable(linkRepository.findOneByWebUrl(url.getWebURL()));
        if (!foundLink.isPresent()) {

            DeeplinkDto deeplinkDto = linkRouter.convertWebUrlToDeeplink(url);
            links.setDeeplink(deeplinkDto.getDeeplink());
            links.setWebUrl(url.getWebURL());
            links.setShortlink(generateShortlink());
            linkRepository.save(links);
            log.info("Shortlink: Generating shortlink: {}", generateShortlink());
            return links.getShortlink();
        }

        log.info("Shortlink: Returns shortlink: {}", foundLink.get().getShortlink());
        return foundLink.get().getShortlink();
    }

    @Recover
    public String convertWebUrlToShortlink(RuntimeException e) {
        System.out.println(e.getMessage());
        return e.getMessage() + " Recovery shortlink for home page: " + createShortlinkDirectsHomePage();
    }

    @Retryable(maxAttempts = 2, value = {RuntimeException.class}, backoff = @Backoff(delay = 2000, multiplier = 2))
    @Override
    public String convertDeeplinkToShortlink(DeeplinkDto url) throws ResourceNotFoundException, SectionNotFoundException, LinkTypeNotFoundException, URISyntaxException {

        if (url.getDeeplink().matches("ty://?Page=Home")) {
            createShortlinkDirectsHomePage();
            return "https://ty.gl/shortlink";
        }

        Links links = new Links();

        Optional<Links> foundLink = Optional.ofNullable(linkRepository.findOneByDeeplink(url.getDeeplink()));
        if (!foundLink.isPresent()) {

            WebUrlDto webUrlDto = linkRouter.convertDeeplinkToWebUrl(url);

            links.setDeeplink(url.getDeeplink());
            links.setWebUrl(webUrlDto.getWebURL());
            links.setShortlink(generateShortlink());

            linkRepository.save(links);
            log.info("Shortlink: Generating shortlink: {}", generateShortlink());
            return links.getShortlink();
        }
        return foundLink.get().getShortlink();
    }

    @Recover
    public String convertDeeplinkToShortlink(RuntimeException e) {
        System.out.println(e.getMessage());
        return e.getMessage() + " Recovery shortlink for home page: " + createShortlinkDirectsHomePage();
    }

    @Override
    public LinksDto getLinksWithGivenShortlink(ShortlinkDto shortlinkDto) throws ShortlinkNotFoundException {
        LinksDto linksDto = new LinksDto();

        LinksDto foundLinks = Optional.ofNullable(linkRepository.findOneByShortlink(shortlinkDto.getShortlink()))
                .map(links -> {
                    linksDto.setWebURL(links.getWebUrl());
                    linksDto.setDeeplink(links.getDeeplink());
                    return linksDto;
                }).orElseThrow(() -> new ShortlinkNotFoundException("Given shortlink is not exist! " + shortlinkDto.getShortlink()));

        log.info("Shortlink: Returns existing webUrl and deeplink: {}, {}", foundLinks.getWebURL(), foundLinks.getDeeplink());
        return linksDto;
    }

    private String generateShortlink() {

        String generatedString = RandomStringUtils.randomAlphabetic(10);
        return SHORTLINK_PREFIX + generatedString;
    }

    private String createShortlinkDirectsHomePage() {
        Links links = new Links();
        links.setDeeplink("ty://?Page=Home");
        links.setWebUrl("https://www.trendyol.com");
        links.setShortlink("https://ty.gl/shortlink");
        linkRepository.save(links);
        return links.getShortlink();
    }

}
