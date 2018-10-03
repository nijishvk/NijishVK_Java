package com.ubs.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ubs.AppConfig;
import com.ubs.vo.PositionsVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@TestPropertySource("classpath:application-test.properties")
public class CSVProcessorTest {

	@Value("${csvfileRead}")
	public String csvfileRead;

	List<PositionsVO> list;

	@Before
	public void setUp() throws Exception {
		CSVProcessor reader = new CSVProcessor();
		list = reader.processInputFile(csvfileRead);
	}

	@Test
	public void testProcessInputFileSize() {
		assertEquals(list.size(), 1);
	}

	@Test
	public void testProcessInputFileContent() {
		assertTrue(list.get(0).getAccount() == 101L);
	}

}
