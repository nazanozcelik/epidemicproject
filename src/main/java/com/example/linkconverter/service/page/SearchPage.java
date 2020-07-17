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
    String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException {
        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return webUrlToDeeplink(uri);
        }
        return null;
    }

    private String webUrlToDeeplink(String uri) throws ResourceNotFoundException {
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
}
