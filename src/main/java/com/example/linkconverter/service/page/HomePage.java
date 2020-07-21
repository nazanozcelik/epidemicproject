package com.example.linkconverter.service.page;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.repository.LinkRepository;
import com.example.linkconverter.util.LinkType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    public String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException, SectionNotFoundException, LinkTypeNotFoundException {

        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return convertWebUrlToDeeplink(uri);
        } else if (linkType == LinkType.DEEPLINK_TO_WEBURL) {
            return convertDeeplinkToWebUrl(uri);
        } else {
            throw new LinkTypeNotFoundException("Given link type is not valid! " + linkType);
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
            String sectionName = uri.substring(uri.indexOf("/butik/liste/") + "/butik/liste/".length());
            String sectionId = sectionMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equalsIgnoreCase(parseCharacter(sectionName)))
                    .findFirst().map(Map.Entry::getKey)
                    .orElseThrow(() -> new SectionNotFoundException("Section " + sectionName + " is not a valid name!"));

            deeplink = DEEP_LINK + DEEP_LINK_HOME + SECTION_ID + sectionId;
            log.info("Home Page: Returns created deeplink: {}", deeplink);
            return deeplink;
        }

    }

    private String convertDeeplinkToWebUrl(String uri) throws ResourceNotFoundException, SectionNotFoundException {

        String webUrl;

        if (uri.matches("^\\b(ty:\\/\\/\\?)\\b(Page=)\\b(Home)$")) {
            throw new ResourceNotFoundException(uri + "path doesn't have section id!");
        } else {

            String sectionId = uri.substring(uri.indexOf("ty://?Page=Home&SectionId=") + "ty://?Page=Home&SectionId=".length());
            String sectionName = sectionMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(parseCharacter(sectionId)))
                    .findFirst().map(Map.Entry::getValue)
                    .orElseThrow(() -> new SectionNotFoundException("Section has not a valid id!"));

            webUrl = HOME_BLANK + HOME_LIST + "/" + sectionName;
            log.info("Home Page: Returns created webUrl: {}", webUrl);
            return webUrl;
        }

    }
}

