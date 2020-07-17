package com.example.linkconverter.service.page;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.model.entity.Links;
import com.example.linkconverter.repository.LinkRepository;
import com.example.linkconverter.util.LinkType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.linkconverter.util.Constants.*;

@Slf4j
@Service
public class ProductPage extends PageParser {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException, URISyntaxException {
        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return this.webUrlToDeeplink(uri);
        } else {
            return null;
        }
    }

    private String webUrlToDeeplink(String uri) throws URISyntaxException, ResourceNotFoundException {
        String deeplink;
        Links links = new Links();

        Pattern contentId = Pattern.compile(".*\\b(-p-)\\b(\\d+)");
        Matcher matcher = contentId.matcher(uri);

        if (!matcher.find()) {
            throw new ResourceNotFoundException(uri + " Doesn't have content id!");
        }

        Optional<Links> foundDeeplink = Optional.ofNullable(linkRepository.findOneByWebUrl(uri));
        if (foundDeeplink.isPresent()) {
            links.setDeeplink(foundDeeplink.get().getDeeplink());
            log.info("Product Page: Returns existing deeplink: {}", foundDeeplink);
            return links.getDeeplink();
        }

        deeplink = DEEP_LINK + DEEP_LINK_PRODUCT + CONTENT_ID + matcher.group(2);
        List<NameValuePair> params = URLEncodedUtils.parse(new URI(uri), Charset.forName("UTF-8"));

        if (!params.isEmpty()) {
            Optional<String> boutiqueId;
            Optional<String> merchantId;
            for (NameValuePair param : params) {
                boutiqueId = param.getName().contains("boutiqueId") ? Optional.of(param.getValue()) : Optional.empty();
                merchantId = param.getName().contains("merchantId") ? Optional.of(param.getValue()) : Optional.empty();
                deeplink += (boutiqueId.isPresent() ? CAMPAIGN_ID + boutiqueId.get() : "") + (merchantId.isPresent() ? MERCHANT_ID + merchantId.get() : "");
            }
        }

        links.setWebUrl(uri);
        links.setDeeplink(deeplink);
        log.info("Product Page : Returns created deeplink: {}", links.getDeeplink());
        linkRepository.save(links);
        return deeplink;

    }
}
