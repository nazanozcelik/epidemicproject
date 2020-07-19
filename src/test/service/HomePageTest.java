package service;

import com.example.linkconverter.exception.ResourceNotFoundException;
import com.example.linkconverter.exception.SectionNotFoundException;
import com.example.linkconverter.repository.LinkRepository;
import com.example.linkconverter.service.page.HomePage;
import com.example.linkconverter.util.LinkType;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static util.TestConstants.*;

public class HomePageTest {


    @InjectMocks
    private HomePage underTest;

    @Mock
    private LinkRepository linkRepository;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateBlankHomePageDeeplinkWithGivenHostWebUrl() throws ResourceNotFoundException, SectionNotFoundException {
        String deeplink = underTest.parseURI(HOME_BLANK, LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_HOME, deeplink);
    }


    @Test
    public void shouldCreateValidSectionPageWithValidSectionWebUrl() throws ResourceNotFoundException, SectionNotFoundException {
        String deeplink = underTest.parseURI(HOME_LIST_SECTION, LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_SECTION, deeplink);
    }

    @Test
    public void shouldCreateValidSectionPageWithGivenTurkishCharacterValidSectionWebUrl() throws ResourceNotFoundException, SectionNotFoundException {
        String deeplink = underTest.parseURI("https://www.trendyol.com/butik/liste/ÇOCUK", LinkType.WEBURL_TO_DEEPLINK);
        Assert.assertEquals(DEEPLINK_SECTION, deeplink);
    }

    @Test
    public void shouldCreateValidSectionPageWithGivenExistingDeeplinkSectionId() throws ResourceNotFoundException, SectionNotFoundException {
        String webUrl = underTest.parseURI(DEEPLINK_SECTION, LinkType.DEEPLINK_TO_WEBURL);
        Assert.assertEquals(HOME_LIST_SECTION, webUrl);
    }


    @Test(expectedExceptions = SectionNotFoundException.class)
    public void shouldThrowSectionNotFoundExceptionWhenSectionIsNotValid() throws ResourceNotFoundException, SectionNotFoundException {
        underTest.parseURI("https://www.trendyol.com/butik/liste/erkekbolumu", LinkType.WEBURL_TO_DEEPLINK);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void shouldThrowResourceNotFoundExceptionWhenSectionNameIsNotExist() throws ResourceNotFoundException, SectionNotFoundException {
        underTest.parseURI("https://www.trendyol.com/butik/liste", LinkType.WEBURL_TO_DEEPLINK);
    }


    @Test(expectedExceptions = SectionNotFoundException.class)
    public void shouldThrowSectionNotFoundExceptionWhenSectionIdNotValid() throws ResourceNotFoundException, SectionNotFoundException {
        underTest.parseURI("ty://?Page=Home&SectionId=9", LinkType.DEEPLINK_TO_WEBURL);
    }

}
