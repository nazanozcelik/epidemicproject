package com.example.linkconverter.service.page;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.repository.LinkRepository;
import com.example.linkconverter.util.LinkType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.linkconverter.util.CharacterParser.parseCharacter;
import static com.example.linkconverter.util.Constants.*;

@Slf4j
@Service
public class SearchPage extends PageParser {
    @Autowired
    private LinkRepository linkRepository;

    @Override
    public String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException, LinkTypeNotFoundException {
        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return convertWebUrlToDeeplink(uri);
        } else if (linkType == LinkType.DEEPLINK_TO_WEBURL) {
            return convertDeeplinkToWebUrl(uri);
        } else {
            throw new LinkTypeNotFoundException("Given link type is not valid! " + linkType);
        }
    }

    private String convertWebUrlToDeeplink(String uri) throws ResourceNotFoundException {
        String deeplink;

        Pattern searchQuery = Pattern.compile(".*\\b(?:tum--urunler).*\\b(?:\\?q=)\\b(.+\\b)");
        Matcher matcher = searchQuery.matcher(uri);

        if (!matcher.find()) {
            throw new ResourceNotFoundException(uri + "search page query is not valid!");
        }

        deeplink = DEEP_LINK + DEEP_LINK_SEARCH + QUERY + parseCharacter(matcher.group(1));

        log.info("Search Page : Returns created deeplink: {}", deeplink);
        return deeplink;

    }


    private String convertDeeplinkToWebUrl(String uri) throws ResourceNotFoundException {

        String webUrl;

        Pattern searchQuery = Pattern.compile(".*\\b(?:Page=Search&Query=)\\b(.+)");
        Matcher matcher = searchQuery.matcher(uri);

        if (!matcher.find()) {
            throw new ResourceNotFoundException(uri + "search page query is not valid!");
        }

        webUrl = HOME_BLANK + SEARCH_QUERY + parseCharacter(matcher.group(1));
        log.info("Search Page : Returns created webUrl: {}", webUrl);
        return webUrl;
    }
}
