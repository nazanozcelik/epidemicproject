package service;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.model.dto.DeeplinkDto;
import com.example.linkconverter.model.dto.WebUrlDto;
import com.example.linkconverter.service.LinkBuilder;
import com.example.linkconverter.service.page.PageRouter;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

import static org.mockito.Mockito.when;
import static util.TestConstants.*;

public class DeeplinkTests {


    @InjectMocks
    @Autowired
    private LinkBuilder linkbuilder;

    @Mock
    private PageRouter pageRouter;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MockMvc mockMvc;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateBlankHomePageDeeplinkWithGivenHostWebUrl() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink(HOME_BLANK)).thenReturn(createDeeplink(HOME_BLANK, DEEPLINK_HOME));

    }

    @Test
    public void shouldCreateValidSectionPageWithValidSectionWebUrl() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink(HOME_LIST_SECTION)).thenReturn(createDeeplink(HOME_LIST_SECTION, DEEPLINK_SECTION));

    }

    @Test
    public void shouldCreateValidSectionPageWithGivenTurkishCharacterValidSectionWebUrl() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink("https://www.trendyol.com/butik/liste/ÇOCUK")).thenReturn(createDeeplink(HOME_LIST_SECTION, DEEPLINK_SECTION));
    }

    @Test
    public void shouldCreateValidProductPageByGivenWebUrlWithContentId() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink(PRODUCT_CONTENT)).thenReturn(createDeeplink(PRODUCT_CONTENT, DEEPLINK_CONTENT));
    }

    @Test
    public void shouldCreateValidProductPageByGivenWebUrlWithContentIdAndBoutiqueId() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink(PRODUCT_BOUTIQUE)).thenReturn(createDeeplink(PRODUCT_BOUTIQUE, DEEPLINK_CAMPAIGN));
    }

    @Test
    public void shouldCreateValidProductPageByGivenWebUrlWithContentIdAndMerchantId() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink(PRODUCT_MERCHANT)).thenReturn(createDeeplink(PRODUCT_MERCHANT, DEEPLINK_MERCHANT));
    }

    @Test
    public void shouldCreateValidProductPageByGivenWebUrlWithContentIdAndBoutiqueIdAndMerchantId() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink(PRODUCT_BOUTIQUE_AND_MERCHANT)).thenReturn(createDeeplink(PRODUCT_BOUTIQUE_AND_MERCHANT, DEEPLINK_CAMPAIGN_AND_MERCHANT));
    }

    @Test
    public void shouldCreateValidSeacrhPageByGivenWebUrlWithValidQuery() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink(SEARCH_PAGE)).thenReturn(createDeeplink(SEARCH_PAGE, DEEPLINK_SEARCH));
    }

    @Test
    public void shouldCreateBlankHomePageWhenUrlIsNotDetermined() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        when(linkBuilder.createDeeplink(OTHER_PAGE)).thenReturn(createDeeplink(OTHER_PAGE, HOME_BLANK));
    }


    @Test
    public void shouldThrowResourceNotFoundExceptionWhenHostUrlIsMissing() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        expectedException.expect(ResourceNotFoundException.class);
        when(linkBuilder.createDeeplink("https://www.trenol.com")).thenThrow(new ResourceNotFoundException(""));

    }

    @Test
    public void shouldThrowSectionNotFoundExceptionWhenSectionIsNotValid() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        expectedException.expect(SectionNotFoundException.class);
        when(linkBuilder.createDeeplink("https://www.trendyol.com/butik/liste/erkekbolumu")).thenThrow(new SectionNotFoundException(""));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenSectionNameIsNotExist() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        expectedException.expect(SectionNotFoundException.class);
        when(linkBuilder.createDeeplink("https://www.trendyol.com/butik/liste")).thenThrow(new SectionNotFoundException(""));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenContentIdIsNotExist() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        expectedException.expect(SectionNotFoundException.class);
        when(linkBuilder.createDeeplink("https://www.trendyol.com/casio/erkek-kol-saaati-p-")).thenThrow(new SectionNotFoundException(""));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenSearchQueryIsNotExist() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException {
        LinkBuilder linkBuilder = Mockito.mock(LinkBuilder.class);
        expectedException.expect(SectionNotFoundException.class);
        when(linkBuilder.createDeeplink("https://www.trendyol.com/tum--urunler?q")).thenThrow(new SectionNotFoundException(""));
    }

    private DeeplinkDto createDeeplink(String webUrl, String deeplink) {
        WebUrlDto webUrlDto = new WebUrlDto();
        DeeplinkDto deeplinkDto = new DeeplinkDto();
        webUrlDto.setWebURL(webUrl);
        deeplinkDto.setDeeplink(deeplink);
        return deeplinkDto;
    }
}
