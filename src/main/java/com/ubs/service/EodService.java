package com.ubs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ubs.processor.CSVProcessor;
import com.ubs.processor.CsvFileWriter;
import com.ubs.processor.JsonProcessor;
import com.ubs.vo.PositionsVO;
import com.ubs.vo.TransactionVO;

@Service
public class EodService {

	@Autowired
	private CSVProcessor csvprocess;
	@Autowired
	private JsonProcessor jsonprocess;

	@Value("${csvfile}")
	private String csvfile;

	@Value("${jsonfile}")
	private String jsonfile;

	@Value("${output}")
	private String output;

	public void process() throws Exception {

		//read position
		List<PositionsVO> pos = csvprocess.processInputFile(csvfile);
		//read transaction
		List<TransactionVO> trans = jsonprocess.getTransaction(jsonfile);
		//get delta details
		List<PositionsVO> deltaList = findDeltaList(pos,trans);
		//write to csv
		writeToCSV(deltaList);
		//find lowest and largest
		getLargestAndLowest(deltaList);
	}

	public List<PositionsVO> findDeltaList(List<PositionsVO> pos, List<TransactionVO> trans) {
		List<PositionsVO> deltaList = new ArrayList<PositionsVO>();
		// loop through positions
		for (PositionsVO positionsVO : pos) {
			PositionsVO delta = new PositionsVO();
			delta.setAccount(positionsVO.getAccount());
			delta.setAccountType(positionsVO.getAccountType());
			delta.setInstrument(positionsVO.getInstrument());
			Long EQuantity = positionsVO.getQuantity();
			Long IQuantity = positionsVO.getQuantity();
			for (TransactionVO transactionVO : trans) {
				if (positionsVO.getInstrument().equals(transactionVO.getInstrument())
						&& positionsVO.getAccountType().equals("E")) {

					if (transactionVO.getTransactionType().equals("B")) {
						// Quantity = Quantity + TransactionQuantity
						EQuantity = EQuantity + transactionVO.getTransactionQuantity();
					}
					if (transactionVO.getTransactionType().equals("S")) {
						// Quantity = Quantity - TransactionQuantity
						EQuantity = EQuantity - transactionVO.getTransactionQuantity();
					}
				}
				if (positionsVO.getInstrument().equals(transactionVO.getInstrument())
						&& positionsVO.getAccountType().equals("I")) {
					if (transactionVO.getTransactionType().equals("B")) {
						// Quantity = Quantity - TransactionQuantity
						IQuantity = IQuantity - transactionVO.getTransactionQuantity();
					}
					if (transactionVO.getTransactionType().equals("S")) {
						// Quantity = Quantity + TransactionQuantity
						IQuantity = IQuantity + transactionVO.getTransactionQuantity();
					}
				}
			}
			if (positionsVO.getAccountType().equals("I")) {
				delta.setQuantity(IQuantity);
			} else {
				delta.setQuantity(EQuantity);
			}
			if (null != delta.getAccount()) {
				delta.setDelta(delta.getQuantity() - positionsVO.getQuantity());
				deltaList.add(delta);
			}
		}
		return deltaList;
	}

	public void writeToCSV(List<PositionsVO> list) throws Exception {
		CsvFileWriter.writeCsvFile(output, list);
	}

	public Map<String, Long> getLargestAndLowest(List<PositionsVO> list) throws Exception {

		try {
			if (null != list && list.size() > 0) {
				Collections.sort(list);
				Map<String, Long> returnValue = new HashMap<String, Long>();
				returnValue.put("largest", list.get(list.size() - 1).getDelta());
				returnValue.put("lowest", list.get(0).getDelta());
				return returnValue;
			} else {
				throw new Exception("Delta list is empty");
			}

		} catch (Exception e) {
			throw new Exception("error on getLargestAndLowest",e);
		}

	}

}
