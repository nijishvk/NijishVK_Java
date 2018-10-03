package com.ubs.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ubs.vo.PositionsVO;

@Service
public class CsvFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "Instrument,Account,AccountType,Quantity,Delta";

	public static void writeCsvFile(String fileName,List<PositionsVO> list) throws Exception {
		
			
		FileWriter fileWriter = null;
				
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			//Write a new object list to the CSV file
			for (PositionsVO vo : list) {
				fileWriter.append(vo.getInstrument());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(vo.getAccount()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(vo.getAccountType());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(vo.getQuantity()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(vo.getDelta()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			
			
			System.out.println("CSV file was created successfully @ "+fileName+" !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
			throw new Exception("Error in CsvFileWriter",e);
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
}
