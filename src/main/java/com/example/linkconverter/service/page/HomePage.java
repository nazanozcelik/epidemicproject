package com.example.linkconverter.service.page;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.entity.Links;
import com.example.linkconverter.repository.LinkRepository;
import com.example.linkconverter.util.LinkType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.linkconverter.util.CharacterParser.parseCharacter;
import static com.example.linkconverter.util.Constants.*;

@Slf4j
@Service
public class HomePage extends PageParser {

    @Autowired
    private LinkRepository linkRepository;

    Map<String, String> sectionMap = new HashMap<String, String>() {{
        put("1", SECTION_WOMAN);
        put("2", SECTION_MAN);
        put("3", SECTION_SUPERMARKET);
        put("4", SECTION_KID);
    }};

    @Override
    public String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException, SectionNotFoundException {

        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return convertWebUrlToDeeplink(uri);
        } else if (linkType == LinkType.DEEPLINK_TO_WEBURL) {
            return convertDeeplinkToWebUrl(uri);
        } else {
            return "shortlink";
        }
    }


    private String convertWebUrlToDeeplink(String uri) throws ResourceNotFoundException, SectionNotFoundException {

        String deeplink;

        if (uri.matches("^https:\\/\\/\\www.trendyol.com")) {
            return deeplink = DEEP_LINK + DEEP_LINK_HOME;
        }


        if (uri.matches("^https:\\/\\/\\www.trendyol.com\\/\\b(butik)\\/\\b(liste)$")) {
            throw new ResourceNotFoundException(uri + "path doesn't have section name!");
        } else {
            Links links = new Links();

            Optional<Links> foundDeeplink = Optional.ofNullable(linkRepository.findOneByWebUrl(uri));
            if (foundDeeplink.isPresent()) {
                links.setDeeplink(foundDeeplink.get().getDeeplink());

                log.info("Home Page: Returns existing deeplink: {}", foundDeeplink);
                return links.getDeeplink();
            }

            String sectionName = uri.substring(uri.indexOf("/butik/liste/") + "/butik/liste/".length());
            String sectionId = sectionMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equalsIgnoreCase(parseCharacter(sectionName)))
                    .findFirst().map(Map.Entry::getKey)
                    .orElseThrow(() -> new SectionNotFoundException("Section " + sectionName + " is not a valid name!"));

            deeplink = DEEP_LINK + DEEP_LINK_HOME + SECTION_ID + sectionId;
            links.setWebUrl(uri);
            links.setDeeplink(deeplink);
            log.info("Home Page: Returns created deeplink: {}", links.getDeeplink());
            linkRepository.save(links);
            return deeplink;
        }

    }

    private String convertDeeplinkToWebUrl(String uri) throws ResourceNotFoundException, SectionNotFoundException {

        String webUrl;

        if (uri.matches("^\\b(ty:\\/\\/\\?)\\b(Page=)\\b(Home)$")) {
            throw new ResourceNotFoundException(uri + "path doesn't have section id!");
        } else {
            Links links = new Links();

            Optional<Links> foundWebUrl = Optional.ofNullable(linkRepository.findOneByDeeplink(uri));
            if (foundWebUrl.isPresent()) {
                links.setWebUrl(foundWebUrl.get().getWebUrl());

                log.info("Home Page: Returns existing webUrl: {}", foundWebUrl);
                return links.getWebUrl();
            }

            String sectionId = uri.substring(uri.indexOf("ty://?Page=Home&SectionId=") + "ty://?Page=Home&SectionId=".length());
            String sectionName = sectionMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(parseCharacter(sectionId)))
                    .findFirst().map(Map.Entry::getValue)
                    .orElseThrow(() -> new SectionNotFoundException("Section has not a valid id!"));

            webUrl = HOME_BLANK + HOME_LIST + "/" + sectionName;
            links.setDeeplink(uri);
            links.setWebUrl(webUrl);
            log.info("Home Page: Returns created webUrl: {}", links.getWebUrl());
            linkRepository.save(links);
            return webUrl;
        }

    }
}

