package com.searchify.suggestion.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class DeepaiService {

	public String generateText() {

		String command = "curl  -F text=we are the biggest online book store -H api-key:86b6c948-72b3-4090-826b-387177ebccde https://api.deepai.org/api/text-generator";
		try {
			Process process = Runtime.getRuntime().exec(command);
			InputStream ins = process.getInputStream();
			// creating a buffered reader
			BufferedReader read = new BufferedReader(new InputStreamReader(ins));
			StringBuilder sb = new StringBuilder();
			read
			.lines()
			.forEach(line -> {
			     sb.append(line);
			});
			// close the buffered reader
			read.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* get the inputstream from the process which would get printed on  
		 * the console / terminal
		 */
		
		return null;
	}

}
