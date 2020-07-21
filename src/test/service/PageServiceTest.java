package service;

import com.example.linkconverter.exception.LinkTypeNotFoundException;
import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.service.page.PageService;
import com.example.linkconverter.util.LinkType;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

import static util.TestConstants.*;

public class PageServiceTest {

    @InjectMocks
    private PageService underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateBlankHomePageWithGivenWebUrlWhenUrlIsNotDetermined() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String deeplink = underTest.routePage(OTHER_PAGE, LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_HOME, deeplink);
    }

    @Test
    public void shouldCreateBlankHomePageWithGivenDeeplinkWhenUrlIsNotDetermined() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String webUrl = underTest.routePage(DEEPLINK_OTHER, LinkType.DEEPLINK_TO_WEBURL);
        Assert.assertEquals(HOME_BLANK, webUrl);
    }

    @Test
    public void shouldCreateHostHomePageWebUrlWithGivenHomePageDeeplink() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        String webUrl = underTest.routePage(DEEPLINK_HOME, LinkType.DEEPLINK_TO_WEBURL);
        Assert.assertEquals(HOME_BLANK, webUrl);

    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWhenHostUrlIsMissing() throws ResourceNotFoundException, SectionNotFoundException, URISyntaxException, LinkTypeNotFoundException {
        underTest.routePage("https://www.trenol.com", LinkType.WEBURL_TO_DEEPLINK);

    }
}
