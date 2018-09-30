package com.ubs.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ubs.vo.PositionsVO;

@Service
public class CSVProcessor {

	public List<PositionsVO> processInputFile(String inputFilePath) {
		List<PositionsVO> inputList = new ArrayList<PositionsVO>();
		try {
			File inputF = new File(inputFilePath);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			// skip the header of the csv
			inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
			br.close();
		} catch (IOException e) {

		}
		return inputList;
	}

	private Function<String, PositionsVO> mapToItem = (line) -> {
		String[] p = line.split(",");// a CSV has comma separated lines
		PositionsVO item = new PositionsVO();
		item.setInstrument(p[0]);// <-- this is the first column in the csv file
		item.setAccount(Long.parseLong(p[1]));
		item.setAccountType(p[2]);
		item.setQuantity(Long.parseLong(p[3]));
		return item;
	};

}
