/**
 * Rohan Krishna Ramkhumar
 * Util for download and lookup help
 */
package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Util {

	/**
	 * Method listFilesForFolder
	 * @param folder: Folder to be checcked during automatic registry of files
	 * @return all files (including folders
	 */
	public static ArrayList<String> listFilesForFolder(final File folder){

		ArrayList<String> fileName = new ArrayList<String>();

		for (final File fileEntry : folder.listFiles())
			fileName.add(fileEntry.getName());

		return fileName;
	}

	/**
	 * Method copy
	 * @param in: File data input
	 * @param out: File Data output
	 * @throws IOException: Standard Exception
	 * Used to help download file
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int count = 0;
		long start = System.currentTimeMillis();
		while ((count = in.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
		System.out.println("Download time: " + (System.currentTimeMillis() - start) + " ms.");
	}

	/**
	 * Method calculateAverage
	 * @param times
	 * @return average download/ connection times
	 */
	public static double calculateAverage(ArrayList<Long> times) {
		Long sum = 0L;
		if(!times.isEmpty()) {
			for (Long time : times) {
				sum += time;
			}
			return sum.doubleValue() / times.size();
		}
		return sum;
	}
}
