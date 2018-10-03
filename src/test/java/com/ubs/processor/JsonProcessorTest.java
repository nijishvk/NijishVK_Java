/**
 * 
 */
package com.ubs.processor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ubs.AppConfig;
import com.ubs.vo.TransactionVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@TestPropertySource("classpath:application-test.properties")
public class JsonProcessorTest {

	@Value("${jsonfileRead}")
	public String jsonfileRead;

	List<TransactionVO> list;

	@Before
	public void setUp() throws Exception {
		JsonProcessor json = new JsonProcessor();
		list = json.getTransaction(jsonfileRead);
	}

	@Test
	public void testProcessInputFileSize() {
		assertEquals(list.size(), 2);
	}

	@Test
	public void testProcessInputFileContent() {
		assertEquals(list.get(0).getInstrument(), "IBM");
	}

}
