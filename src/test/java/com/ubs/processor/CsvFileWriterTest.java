package com.ubs.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
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
@ContextConfiguration(classes = {AppConfig.class})
@TestPropertySource("classpath:application-test.properties")
public class CsvFileWriterTest {

	@Value("${csvfileWrite}")
    public String csvfileWrite;
	
	@Value("${header}")
    public String headerExpected;
	
	public PositionsVO data ;
	public List<PositionsVO> dataList;
	
	@Before
	public void setUp() throws Exception {
		data=createVO();
		dataList = new ArrayList<PositionsVO>();
		dataList.add(data);
	}
	
	
	@Test
	public void testFileCreated() {
		assertEquals("E:/Projects/workspace/Test/Expected_EndOfDay_Positions.txt", csvfileWrite);
		try {
			CsvFileWriter.writeCsvFile(csvfileWrite, Collections.emptyList());
			File file = new File(csvfileWrite);
			assertTrue(file.exists());
		} catch (Exception e) {
			fail("failed");
			System.out.println("failed" +e);
		}
		
	}
	
	@Test
	public void testFileContent() {
		try {
			CsvFileWriter.writeCsvFile(csvfileWrite, dataList);
			CSVProcessor reader = new CSVProcessor();
			List<PositionsVO> list= reader.processInputFile(csvfileWrite);
			assertEquals(list.get(0).getAccount(), data.getAccount());
		} catch (Exception e) {
			fail("failed");
			System.out.println("failed" +e);
		}
		
	}
	
	@Test
	public void testFileHeader() {
		try {
			CsvFileWriter.writeCsvFile(csvfileWrite, Collections.emptyList());
			assertTrue(header(csvfileWrite).equals(headerExpected));
		} catch (Exception e) {
			System.out.println("failed" +e);
			fail("failed");
			
		}
		
	}
	
	private PositionsVO createVO() {
		//String instrument, Long account, String accountType, Long quantity, Long delta
		PositionsVO pos= new PositionsVO("IBM",123L,"E",100L,null);
		return pos;
		
	}
	
	private String header(String inputFilePath) throws Exception {
		try {
			File inputF = new File(inputFilePath);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			String line = br.readLine();
			br.close();
			return line;
		} catch (IOException e) {
			throw new Exception("error while reading csv file",e);
		}
		
	}

}
