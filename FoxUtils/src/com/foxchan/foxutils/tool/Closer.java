package com.foxchan.foxutils.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 关闭数据对象的工具类
 * @author foxchan@live.cn
 * @create 2013-4-27
 */
public class Closer {
	
	/**
	 * 关闭输入流
	 * @param in			等待关闭的输入流
	 * @throws IOException
	 */
	public static final void close(InputStream in) {
		if(in != null){
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭输出流
	 * @param os			等待关闭的输出流
	 * @throws IOException
	 */
	public static final void close(OutputStream os) {
		if(os != null){
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭输出流
	 * @param oswriter	等待关闭的输出流
	 */
	public static final void close(OutputStreamWriter oswriter){
		if(oswriter != null){
			try {
				oswriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭文件输入流
	 * @param fin	文件输入流
	 */
	public static final void close(FileInputStream fin){
		if(fin != null){
			try {
				fin.close();
				fin = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
