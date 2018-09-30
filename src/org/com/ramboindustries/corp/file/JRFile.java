package org.com.ramboindustries.corp.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JRFile {

	public void createFile(String content, String fileName) throws IOException {
		File file = new File(fileName);
		FileWriter writer = new FileWriter(file);
		BufferedWriter buffer = new BufferedWriter(writer);
		buffer.write(content);
		buffer.flush();
		buffer.close();
	}
	
}
