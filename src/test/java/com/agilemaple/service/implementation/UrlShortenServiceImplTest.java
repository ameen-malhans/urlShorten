package com.agilemaple.service.implementation;

import com.agilemaple.model.UrlShortner;
import com.agilemaple.service.UrlShortnerService;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/servlet-context.xml"})
public class UrlShortenServiceImplTest {

	@Autowired	
	private UrlShortnerService urlShortenService;
	
	@org.junit.Before
	public void createUrlShortner(){
		  UrlShortner urlShorten = new UrlShortner();
		  urlShorten.setOriginalUrl("https://www.youtube.com/watch?v=diQrKCLvd3I");
		  urlShorten.setCreatedOn(urlShorten.getCreatedOn());
		  Long id =urlShortenService.saveUrl(urlShorten);
	      Assert.assertNotNull(id);
	}
	
	// 1 Get from Database
	@Test
	public void testFindByIdAndName() {
		Optional<UrlShortner> optional = urlShortenService.findByOriginalUrl("https://www.youtube.com/watch?v=diQrKCLvd3I");
		Assert.assertNotNull(optional.isPresent());
		if(optional.isPresent()){
			Optional<UrlShortner> findeById = urlShortenService.findeById(optional.get().getId());
			Assert.assertNotNull(findeById);
		}
	}

	@Test
	public void testShortenUrlByDb() {
		String longUrlExistsInDb = "https://www.youtube.com/watch?v=diQrKCLvd3I";
		String shortUrl = urlShortenService.shortenUrlByDb(longUrlExistsInDb);
		Assert.assertNotNull(shortUrl);
		String longUrl = "http://tdkhiem.com/2017/06/22/creating-a-url-shortener-with-spring-restful-and-postgres/";
		shortUrl = urlShortenService.shortenUrlByDb(longUrl);
		Assert.assertNotNull(shortUrl);

		String  newLongUrl = urlShortenService.getUrlByDb(shortUrl);
		Assert.assertEquals(longUrl,newLongUrl);

	}

}
