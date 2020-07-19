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
    public String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException, URISyntaxException {
        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return convertWebUrlToDeeplink(uri);
        } else if (linkType == LinkType.DEEPLINK_TO_WEBURL) {
            return convertDeeplinkToWebUrl(uri);
        } else {
            return "shortlink";
        }
    }

    private String convertWebUrlToDeeplink(String uri) throws URISyntaxException, ResourceNotFoundException {
        String deeplink;
        Links links = new Links();

        Pattern contentId = Pattern.compile(".*\\b(?:-p-)\\b(\\d+)");
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

        deeplink = DEEP_LINK + DEEP_LINK_PRODUCT + CONTENT_ID + matcher.group(1);

        List<NameValuePair> params = URLEncodedUtils.parse(new URI(uri), Charset.forName("UTF-8"));
        if (!params.isEmpty()) {
            for (NameValuePair param : params) {
                Optional<String> boutiqueId;
                Optional<String> merchantId;
                boutiqueId = param.getName().equalsIgnoreCase("boutiqueId") ? Optional.of(param.getValue()) : Optional.empty();
                merchantId = param.getName().equalsIgnoreCase("merchantId") ? Optional.of(param.getValue()) : Optional.empty();

                if (uri.matches(".*\\b\\?(boutiqueId)\\=\\d+\\&(merchantId)\\=\\d+")) {
                    deeplink += (boutiqueId.isPresent() ? "&CampaignId=" + boutiqueId.get() : "") + (merchantId.isPresent() ? "&MerchantId=" + merchantId.get() : "");

                } else if (uri.matches(".*\\b\\?(boutiqueId)\\=\\d+$")) {
                    deeplink += (boutiqueId.isPresent() ? "&CampaignId=" + boutiqueId.get() : "");

                } else if (uri.matches(".*\\b\\?(merchantId)\\=\\d+$")) {
                    deeplink += (merchantId.isPresent() ? "&MerchantId=" + merchantId.get() : "");

                } else {
                    throw new ResourceNotFoundException("Given web URL is not valid, please spell check of parameters!  " + uri);
                }
            }
        }

        links.setWebUrl(uri);
        links.setDeeplink(deeplink);
        log.info("Product Page : Returns created deeplink: {}", links.getDeeplink());
        linkRepository.save(links);
        return deeplink;

    }

    private String convertDeeplinkToWebUrl(String uri) throws URISyntaxException, ResourceNotFoundException {

        String webUrl;
        Links links = new Links();

        Pattern contentId = Pattern.compile(".*\\b(?:Page=Product&ContentId=)\\b(\\d+)");
        Matcher matcher = contentId.matcher(uri);

        if (!matcher.find()) {
            throw new ResourceNotFoundException(uri + " Doesn't have content id!");
        }

        Optional<Links> foundWebUrl = Optional.ofNullable(linkRepository.findOneByDeeplink(uri));
        if (foundWebUrl.isPresent()) {
            links.setWebUrl(foundWebUrl.get().getWebUrl());
            log.info("Product Page: Returns existing webUrl: {}", foundWebUrl);
            return links.getWebUrl();
        }

        webUrl = HOME_BLANK + PRODUCT_BRAND + matcher.group(1);

        List<NameValuePair> params = URLEncodedUtils.parse(new URI(uri), Charset.forName("UTF-8"));
        if (!params.isEmpty()) {
            for (NameValuePair param : params) {
                Optional<String> campaignId;
                Optional<String> merchantId;
                campaignId = param.getName().equalsIgnoreCase("CampaignId") ? Optional.of(param.getValue()) : Optional.empty();
                merchantId = param.getName().equalsIgnoreCase("MerchantId") ? Optional.of(param.getValue()) : Optional.empty();

                if (uri.matches(".*\\b(ContentId)\\=\\d+$")) {
                    return webUrl;
                }

                if (uri.matches(".*\\b(CampaignId)\\=\\d+\\&(MerchantId)\\=\\d+")) {
                    webUrl += (campaignId.isPresent() ? "?boutiqueId=" + campaignId.get() : "") + (merchantId.isPresent() ? ((webUrl.contains("?") ? "&" : "?") + "merchantId=" + merchantId.get()) : "");

                } else if (uri.matches(".*\\b(CampaignId)\\=\\d+$")) {
                    webUrl += (campaignId.isPresent() ? "?boutiqueId=" + campaignId.get() : "");

                } else if (uri.matches(".*\\b(MerchantId)\\=\\d+$")) {
                    webUrl += (merchantId.isPresent() ? ((webUrl.contains("?") ? "&" : "?") + "merchantId=" + merchantId.get()) : "");

                } else {
                    throw new ResourceNotFoundException("Given web URL is not valid, please spell check of parameters!  " + uri);
                }
            }
        }

        links.setDeeplink(uri);
        links.setWebUrl(webUrl);
        log.info("Product Page : Returns created webUrl: {}", links.getWebUrl());
        linkRepository.save(links);
        return webUrl;
    }
}
