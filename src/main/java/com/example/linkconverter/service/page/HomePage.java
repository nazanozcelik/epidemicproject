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
        put("1", "Kadin");
        put("2", "Erkek");
        put("3", "Supermarket");
        put("4", "Cocuk");
    }};

    @Override
    public String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException, SectionNotFoundException {

        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return this.webUrlToDeeplink(uri);
        } else if (linkType == LinkType.DEEPLINK_TO_WEBURL) {
            return "Deeplink!";
        } else {
            return "shortlink";
        }
    }


    private String webUrlToDeeplink(String uri) throws ResourceNotFoundException, SectionNotFoundException {

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

}




