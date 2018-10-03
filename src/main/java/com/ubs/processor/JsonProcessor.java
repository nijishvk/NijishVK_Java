package com.ubs.processor;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.ubs.vo.TransactionVO;

@Service
public class JsonProcessor {

	@SuppressWarnings("unchecked")
	public List<TransactionVO> getTransaction(String fileName) throws Exception {

		List<TransactionVO> tVoList = new ArrayList<TransactionVO>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(fileName));
			JSONArray jsonArray = (JSONArray) obj;
			jsonArray.forEach(trans -> {
				tVoList.add(parseTransaction((JSONObject) trans));
			});
			/*
			 * tVoList = jsonArray.forEach( trans -> parseTransaction( (JSONObject) trans )
			 * ); List<TransactionVO> transList = Arrays.stream(jsonArray).map(thing ->
			 * parseTransaction(jsonArray)).collect(Collectors.toList());
			 * 
			 * tVoList.add(trans); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error while reading transaction json",e);
		}
		return tVoList;
	}

	private static TransactionVO parseTransaction(JSONObject jsonObject) {

		//(Long transactionId, String instrument, String transactionType, Long transactionQuantity)
		TransactionVO trans = new TransactionVO((Long) jsonObject.get("TransactionId"), (String) jsonObject.get("Instrument"), (String) jsonObject.get("TransactionType"), (Long) jsonObject.get("TransactionQuantity"));
		return trans;

	}

	/*
	 * try (JsonReader jsonReader = new JsonReader(new InputStreamReader(new
	 * FileInputStream(fileName), "UTF-8"))) { Gson gson = new
	 * GsonBuilder().create(); jsonReader.beginArray(); while (jsonReader.hasNext())
	 * { TransactionVO trasaction = gson.fromJson(jsonReader, TransactionVO.class);
	 * System.out.println("$$$$$$$"+trasaction.getInstrument());
	 * tVoList.add(trasaction); }
	 * 
	 * } catch (UnsupportedEncodingException e) { e.printStackTrace(); } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) {
	 * e.printStackTrace(); }
	 */

}
