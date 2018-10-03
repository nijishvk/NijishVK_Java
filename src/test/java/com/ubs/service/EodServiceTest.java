/**
 * 
 */
package com.ubs.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ubs.AppConfig;
import com.ubs.vo.PositionsVO;
import com.ubs.vo.TransactionVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@TestPropertySource("classpath:application-test.properties")
public class EodServiceTest {

	public PositionsVO data ;
	public TransactionVO trns ;
	public List<PositionsVO> dataList;
	public List<TransactionVO> transDataList;
	List <PositionsVO> delta;
	
	@Before
	public void setUp() throws Exception {
		data=createVO();
		dataList = new ArrayList<PositionsVO>();
		dataList.add(data);
		
		trns=createTransVO();
		transDataList = new ArrayList<TransactionVO>();
		transDataList.add(trns);
		
		EodService eod = new EodService();
		delta = eod.findDeltaList(dataList, transDataList);
	}

	@Test
	public void testFindDeltaListSize() {
		assertEquals(delta.size(), 1);
	}
	
	@Test
	public void testFindDeltaListContent() {
		assertTrue(delta.get(0).getAccount()== 123L);
		assertEquals(delta.get(0).getInstrument(), "IBM");
		assertTrue(delta.get(0).getDelta()== -200);
	}


	@Test
	public void testGetLargestAndLowest() {
		EodService eod = new EodService();
		try {
			PositionsVO pos2= new PositionsVO("APPLE",123L,"E",100L,null);
			TransactionVO trns2= new TransactionVO(1L, "APPLE", "B", 200L);
			transDataList.add(trns2);
			dataList.add(pos2);
			delta = eod.findDeltaList(dataList, transDataList);
			Map<String, Long> data = eod.getLargestAndLowest(delta);
			assertTrue(data.get("largest")==200);
			assertTrue(data.get("lowest")==-200);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("failed");
		}
	}
	
	private PositionsVO createVO() {
		//String instrument, Long account, String accountType, Long quantity, Long delta
		PositionsVO pos= new PositionsVO("IBM",123L,"E",100L,null);
		return pos;
		
	}
	
	private TransactionVO createTransVO() {
		//(Long transactionId, String instrument, String transactionType, Long transactionQuantity)
		TransactionVO trns= new TransactionVO(1L, "IBM", "S", 200L);
		return trns;
		
	}


}
