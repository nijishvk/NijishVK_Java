package com.ubs.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ubs.processor.CSVProcessor;
import com.ubs.processor.CsvFileWriter;
import com.ubs.processor.JsonProcessor;
import com.ubs.vo.PositionsVO;
import com.ubs.vo.TransactionVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;

@SpringBootApplication
public class PositionCalculationProcess implements CommandLineRunner {

	// @Autowired
	private CSVProcessor csvprocess = new CSVProcessor();

	// @Autowired
	private JsonProcessor jsonprocess = new JsonProcessor();

	@Value("${csvfile}")
	private String csvfile;

	@Value("${jsonfile}")
	private String jsonfile;

	@Value("${output}")
	private String output;

	public static void main(String args[]) {
		SpringApplication app = new SpringApplication(PositionCalculationProcess.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	public void run(String... arg0) throws Exception {
		List<PositionsVO> pos = csvprocess.processInputFile(csvfile);
		List<PositionsVO> deltaList = new ArrayList<PositionsVO>();
		List<TransactionVO> trans = jsonprocess.getTransaction(jsonfile);

		// loop through positins
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
						// delta.setQuantity(positionsVO.getQuantity()+transactionVO.getTransactionQuantity());
						// Quantity = Quantity + TransactionQuantity
						EQuantity = EQuantity + transactionVO.getTransactionQuantity();
					}
					if (transactionVO.getTransactionType().equals("S")) {
						// delta.setQuantity(positionsVO.getQuantity()-transactionVO.getTransactionQuantity());
						// Quantity = Quantity - TransactionQuantity
						EQuantity = EQuantity - transactionVO.getTransactionQuantity();
					}

				}

				if (positionsVO.getInstrument().equals(transactionVO.getInstrument())
						&& positionsVO.getAccountType().equals("I")) {

					if (transactionVO.getTransactionType().equals("B")) {
						// delta.setQuantity(positionsVO.getQuantity()-transactionVO.getTransactionQuantity());
						// Quantity = Quantity - TransactionQuantity
						IQuantity = IQuantity - transactionVO.getTransactionQuantity();
					}
					if (transactionVO.getTransactionType().equals("S")) {
						delta.setQuantity(positionsVO.getQuantity() + transactionVO.getTransactionQuantity());
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
