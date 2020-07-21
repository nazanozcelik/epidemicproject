package service;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.repository.LinkRepository;
import com.example.linkconverter.service.page.ProductPage;
import com.example.linkconverter.util.LinkType;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

import static util.TestConstants.*;

public class ProductPageTest {

    @InjectMocks
    private ProductPage underTest;

    @Mock
    private LinkRepository linkRepository;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldCreateValidProductPageByGivenWebUrlWithContentId() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String deeplink = underTest.parseURI(PRODUCT_CONTENT, LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_CONTENT, deeplink);
    }

    @Test
    public void shouldCreateValidProductPageByGivenWebUrlWithContentIdAndBoutiqueId() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String deeplink = underTest.parseURI(PRODUCT_BOUTIQUE, LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_CAMPAIGN, deeplink);
    }

    @Test
    public void shouldCreateValidProductPageByGivenWebUrlWithContentIdAndMerchantId() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String deeplink = underTest.parseURI(PRODUCT_MERCHANT, LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_MERCHANT, deeplink);
    }

    @Test
    public void shouldCreateValidProductPageByGivenWebUrlWithContentIdAndBoutiqueIdAndMerchantId() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String deeplink = underTest.parseURI(PRODUCT_BOUTIQUE_AND_MERCHANT, LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_CAMPAIGN_AND_MERCHANT, deeplink);
    }


    @Test
    public void shouldCreateValidProductPageByGivenDeeplinkWithContentId() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String webUrl = underTest.parseURI(DEEPLINK_CONTENT, LinkType.DEEPLINK_TO_WEBURL);
        Assert.assertEquals(PRODUCT_CONTENT, webUrl);
    }

    @Test
    public void shouldCreateValidProductPageByGivenDeeplinkWithContentIdAndCampaigId() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String webUrl = underTest.parseURI(DEEPLINK_CAMPAIGN, LinkType.DEEPLINK_TO_WEBURL);
        Assert.assertEquals(PRODUCT_BOUTIQUE, webUrl);
    }

    @Test
    public void shouldCreateValidProductPageByGivenDeeplinkWithContentIdAndMerchantId() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String webUrl = underTest.parseURI(DEEPLINK_MERCHANT, LinkType.DEEPLINK_TO_WEBURL);
        Assert.assertEquals(PRODUCT_MERCHANT, webUrl);
    }

    @Test
    public void shouldCreateValidProductPageByGivenDeeplinkWithContentIdAndBoutiqueIdAndMerchantId() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String webUrl = underTest.parseURI(DEEPLINK_CAMPAIGN_AND_MERCHANT, LinkType.DEEPLINK_TO_WEBURL);
        Assert.assertEquals(PRODUCT_BOUTIQUE_AND_MERCHANT, webUrl);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWithGivenWebUrlWhenContentIdIsNotExist() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        underTest.parseURI("https://www.trendyol.com/casio/erkek-kol-saaati-p-", LinkType.WEBURL_TO_DEEPLINK);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWhenGivenProductPageWebUrlParametersAreNotValid() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        underTest.parseURI("https://www.trendyol.com/casio/erkek-kol-saaati-p-125?bouId=9876&metId=78999", LinkType.WEBURL_TO_DEEPLINK);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWithGivenDeeplikWhenContentIdIsNotExist() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        underTest.parseURI("ty://?Page=Product&ContentId=", LinkType.DEEPLINK_TO_WEBURL);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWhenContentIdIsNotExist() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        underTest.parseURI("ty://?Page=Product&ContentId=", LinkType.DEEPLINK_TO_WEBURL);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWhenGivenDeeplinkProductPageParametersAreNotValid() throws ResourceNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        underTest.parseURI("ty://?Page=Product&ContentId=125&Campd=9876&MerId=78999", LinkType.DEEPLINK_TO_WEBURL);
    }

}
