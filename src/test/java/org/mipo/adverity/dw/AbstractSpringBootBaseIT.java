package org.mipo.adverity.dw;

import org.mipo.adverity.dw.services.HttpDownloader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AbstractSpringBootBaseIT {

	@MockBean
	HttpDownloader httpDownloader;
}
