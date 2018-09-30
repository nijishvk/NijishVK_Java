package com.ubs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	public void process() {

		List<PositionsVO> pos = csvprocess.processInputFile(csvfile);
		List<PositionsVO> deltaList = new ArrayList<PositionsVO>();
		List<TransactionVO> trans = jsonprocess.getTransaction(jsonfile);

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

		CsvFileWriter.writeCsvFile(output, deltaList);
		Collections.sort(deltaList);
		System.out.println("largest :" + deltaList.get(deltaList.size() - 1).getDelta());
		System.out.println("lowest :" + deltaList.get(0).getDelta());

	}

}
