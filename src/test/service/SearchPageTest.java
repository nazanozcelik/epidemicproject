package service;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.repository.LinkRepository;
import com.example.linkconverter.service.page.SearchPage;
import com.example.linkconverter.util.LinkType;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static util.TestConstants.DEEPLINK_SEARCH;
import static util.TestConstants.SEARCH_PAGE;

public class SearchPageTest {


    @InjectMocks
    private SearchPage underTest;

    @Mock
    private LinkRepository linkRepository;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateValidSearchPageByGivenWebUrlWithValidQuery() throws ResourceNotFoundException, LinkTypeNotFoundException {
        String deeplink = underTest.parseURI(SEARCH_PAGE, LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_SEARCH, deeplink);
    }

    @Test
    public void shouldCreateValidSearchPageByGivenDeeplikWithValidQuery() throws ResourceNotFoundException, LinkTypeNotFoundException {
        String webUrl = underTest.parseURI(DEEPLINK_SEARCH, LinkType.DEEPLINK_TO_WEBURL);
        Assert.assertEquals(SEARCH_PAGE, webUrl);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWithGivenWebUrWhenSearchQueryIsNotExist() throws ResourceNotFoundException, LinkTypeNotFoundException {
        underTest.parseURI("\"https://www.trendyol.com/tum--urunler?q", LinkType.WEBURL_TO_DEEPLINK);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWithGivenDeeplinkWhenSearchQueryIsNotExist() throws ResourceNotFoundException, LinkTypeNotFoundException {
        underTest.parseURI("ty://?Page=Search&Query=", LinkType.DEEPLINK_TO_WEBURL);
    }

}
