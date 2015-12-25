package com.app.finder.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片处理工具类
 */
public class ThumbnailsUtils {

	/**
	 * 等比例缩放图片的大小
	 * 
	 * @param width
	 *            缩小后的宽度
	 * @param height
	 *            缩小后的高度
	 * @param picture
	 *            上传图片的字节
	 * @param out
	 *            压缩后的字节
	 */
	public static void resetPicture(int width, int height, byte[] picture,
			ByteArrayOutputStream out) {
		try {
			Thumbnails.of(new ByteArrayInputStream(picture)).size(width, height)
					.toOutputStream(out);
		} catch (IOException e) {
		}
	}
}
