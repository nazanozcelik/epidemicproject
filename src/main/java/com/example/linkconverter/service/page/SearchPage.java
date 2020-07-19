package com.example.linkconverter.service.page;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.model.entity.Links;
import com.example.linkconverter.repository.LinkRepository;
import com.example.linkconverter.util.LinkType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
    public String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException {
        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return convertWebUrlToDeeplink(uri);
        } else if (linkType == LinkType.DEEPLINK_TO_WEBURL) {
            return convertDeeplinkToWebUrl(uri);
        } else {
            return "shortlink";
        }
    }

    private String convertWebUrlToDeeplink(String uri) throws ResourceNotFoundException {
        String deeplink;
        Links links = new Links();

        Pattern searchQuery = Pattern.compile(".*\\b(?:tum--urunler).*\\b(?:\\?q=)\\b(.+\\b)");
        Matcher matcher = searchQuery.matcher(uri);

        if (!matcher.find()) {
            throw new ResourceNotFoundException(uri + "search page query is not valid!");
        }

        Optional<Links> foundDeeplink = Optional.ofNullable(linkRepository.findOneByWebUrl(uri));
        if (foundDeeplink.isPresent()) {
            links.setDeeplink(foundDeeplink.get().getDeeplink());
            log.info("Search Page: Returns existing deeplink: {}", foundDeeplink);
            return links.getDeeplink();
        }

        deeplink = DEEP_LINK + DEEP_LINK_SEARCH + QUERY + parseCharacter(matcher.group(1));

        links.setWebUrl(uri);
        links.setDeeplink(deeplink);
        log.info("Search Page : Returns created deeplink: {}", links.getDeeplink());
        linkRepository.save(links);
        return deeplink;

    }


    private String convertDeeplinkToWebUrl(String uri) throws ResourceNotFoundException {
        String webUrl;
        Links links = new Links();

        Pattern searchQuery = Pattern.compile(".*\\b(?:Page=Search&Query=)\\b(.+)");
        Matcher matcher = searchQuery.matcher(uri);

        if (!matcher.find()) {
            throw new ResourceNotFoundException(uri + "search page query is not valid!");
        }

        Optional<Links> fourndWebUrl = Optional.ofNullable(linkRepository.findOneByDeeplink(uri));
        if (fourndWebUrl.isPresent()) {
            links.setDeeplink(fourndWebUrl.get().getWebUrl());
            log.info("Search Page: Returns existing webUrl: {}", fourndWebUrl);
            return links.getDeeplink();
        }

        webUrl = HOME_BLANK + SEARCH_QUERY + parseCharacter(matcher.group(1));

        links.setDeeplink(uri);
        links.setWebUrl(webUrl);
        log.info("Search Page : Returns created webUrl: {}", links.getWebUrl());
        linkRepository.save(links);
        return webUrl;
    }
}
