package com.example.linkconverter.service.page;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
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
    public String parseURI(String uri, LinkType linkType) throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        if (linkType == LinkType.WEBURL_TO_DEEPLINK) {
            return convertWebUrlToDeeplink(uri);
        } else if (linkType == LinkType.DEEPLINK_TO_WEBURL) {
            return convertDeeplinkToWebUrl(uri);
        } else {
            throw new LinkTypeNotFoundException("Given link type is not valid! " + linkType);
        }
    }

    private String convertWebUrlToDeeplink(String uri) throws URISyntaxException, ResourceNotFoundException {
        String deeplink;

        Pattern contentId = Pattern.compile(".*\\b(?:-p-)\\b(\\d+)");
        Matcher matcher = contentId.matcher(uri);

        if (!matcher.find()) {
            throw new ResourceNotFoundException(uri + " Doesn't have content id!");
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

        log.info("Product Page : Returns created deeplink: {}", deeplink);
        return deeplink;

    }

    private String convertDeeplinkToWebUrl(String uri) throws URISyntaxException, ResourceNotFoundException {

        String webUrl;

        Pattern contentId = Pattern.compile(".*\\b(?:Page=Product&ContentId=)\\b(\\d+)");
        Matcher matcher = contentId.matcher(uri);

        if (!matcher.find()) {
            throw new ResourceNotFoundException(uri + " Doesn't have content id!");
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

        log.info("Product Page : Returns created webUrl: {}", webUrl);
        return webUrl;
    }
}
