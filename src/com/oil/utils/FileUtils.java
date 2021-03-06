package com.oil.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.os.AsyncTask;

/**
 * 
 * @author gfx
 *
 */
public class FileUtils {

	public static InputStream openFile(String path) {
		File file = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return fis;
	}

	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		}
	}

	public void savaData(final String path, InputStream is) {

		// TODO Auto-generated method stub

		FileWriteThread fs = new FileWriteThread();
		fs.setFilePath(path);
		fs.setIs(is);
		fs.execute();

	}

	public void savaData(final String path, InputStream is,
			FileSaveListener fsListener) {

		// TODO Auto-generated method stub

		FileWriteThread fs = new FileWriteThread();
		this.fsListener = fsListener;
		fs.setFilePath(path);
		fs.setIs(is);
		fs.execute();

	}

	class FileWriteThread extends AsyncTask<Void, Void, Boolean> {
		String filePath;
		InputStream is;

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public InputStream getIs() {
			return is;
		}

		public void setIs(InputStream is) {
			this.is = is;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// TODO Auto-generated method stub
			File file = new File(filePath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream fos = null;
			BufferedInputStream bis = null;
			int BUFFER_SIZE = 1024;
			byte[] buf = new byte[BUFFER_SIZE];
			int size = 0;

			bis = new BufferedInputStream(is);

			try {
				fos = new FileOutputStream(file);
				while ((size = bis.read(buf)) != -1) {
					fos.write(buf, 0, size);
				}

				//
				fos.close();
				bis.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result) {
				if (null != fsListener) {
					fsListener.onSuccess();
				}
			} else {
				if (null != fsListener) {
					fsListener.onFailed();
				}
			}

			super.onPostExecute(result);
		}
	};

	FileSaveListener fsListener;

	public interface FileSaveListener {
		public void onSuccess();

		public void onFailed();
	}
}
