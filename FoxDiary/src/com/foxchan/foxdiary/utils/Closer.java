package com.foxchan.foxdiary.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * 该类负责关闭输入输出流等对象
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-5
 */
public class Closer {
	
	public static void close(FileInputStream fis){
		try {
			if(fis != null){
				fis.close();
				fis = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close(FileOutputStream fos) {
		if(fos != null){
			try {
				fos.close();
				fos = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(ObjectOutputStream oos) {
		if(oos != null){
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(ObjectInputStream ois) {
		if(ois != null){
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(PrintWriter pw) {
		if(pw != null){
			pw.close();
		}
	}

	public static void close(FileWriter fw) {
		if(fw != null){
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(ByteArrayOutputStream bos) {
		if(bos != null){
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
