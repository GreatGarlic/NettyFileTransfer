/**
 * 
 */
package org.server.test;

import java.io.File;
import java.io.RandomAccessFile;

import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class FileTest {

	
	@Test
	public void test1() throws Exception{
		File file =new File("E://DSC_4623.JPG");
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		System.out.println(randomAccessFile.length());
		randomAccessFile.seek(15);
//		randomAccessFile.skipBytes(15);
		System.out.println(randomAccessFile.getFilePointer());
		
		System.out.println(randomAccessFile.length()-randomAccessFile.getFilePointer());
		
		System.out.println(randomAccessFile.length());
		
		randomAccessFile.close();
	
		
		
	
		
		
		
		
		
	}
}
