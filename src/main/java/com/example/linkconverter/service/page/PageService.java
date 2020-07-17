package com.example.linkconverter.service.page;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.util.LinkType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

import static com.example.linkconverter.util.Constants.DEEP_LINK;
import static com.example.linkconverter.util.Constants.DEEP_LINK_HOME;
import static com.example.linkconverter.util.LinkType.DEEPLINK_TO_WEBURL;
import static com.example.linkconverter.util.LinkType.WEBURL_TO_DEEPLINK;

@Slf4j
@Service
public class PageService implements PageRouter {

    @Autowired
    private HomePage homePage;

    @Autowired
    private ProductPage productPage;

    @Autowired
    private SearchPage searchPage;

    @Override
    public String routePage(String uri, LinkType linkType) throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {

        if (!uri.matches("^(https:\\/\\/\\www.trendyol.com)\\b(.+)")) {
            throw new ResourceNotFoundException("WebUrl host name doesn't contain wwww.trendyol.com! " + uri);
        }

        log.info("Routing given link to related pages with given link type: {}, {}", uri, linkType);

        if (linkType == WEBURL_TO_DEEPLINK) {

            if (uri.matches("^.*\\/\\b(butik)\\/\\b(liste).*")) {
                return homePage.parseURI(uri, WEBURL_TO_DEEPLINK);
            } else if (uri.matches(".*\\b(-p-).*")) {
                return productPage.parseURI(uri, WEBURL_TO_DEEPLINK);
            } else if (uri.matches(".*\\b(tum--urunler).*")) {
                return searchPage.parseURI(uri, WEBURL_TO_DEEPLINK);
            } else {
                return DEEP_LINK + DEEP_LINK_HOME;
            }

        } else if (linkType == DEEPLINK_TO_WEBURL) {
            return "webUrl";
        } else {
            return "shortlink";
        }
    }

}
