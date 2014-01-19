/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.search;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * �Ż���AStar�㷨��������E���Դ���
 * 
 * @author dewitt
 * @date 2009-11-25 create
 */
public class OptimizeAStar implements Searcher {

	private int width;
	private int height;
	private byte[] maskdata;

	public void init(int width, int height, byte[] maskdata) {
		this.width = width;
		this.height = height;
		this.maskdata = convertData(maskdata);
	}

	public boolean pass(int x, int y) {
		return maskdata[x + y * width] == 0;
	}

	/**
	 * ����ϵ�任
	 * 
	 * @param maskdata
	 * @return
	 */
	private byte[] convertData(byte[] maskdata) {
		byte[] data = new byte[maskdata.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				data[x + y * width] = maskdata[x + (height - 1 - y) * width];
			}
		}
		return data;
	}

	public List<Point> findPath(int x1, int y1, int x2, int y2) {
		List<Point> openlist = new ArrayList<Point>();
		List<Point> closelist = new ArrayList<Point>();
		// ���
		List<Point> path = new ArrayList<Point>();
		// �����б�_ֵ
		byte[][] openlistV = new byte[width][height];
		// �����б�_������_x
		int[][] parentX = new int[width][height];
		// �����б�_������_y
		int[][] parentY = new int[width][height];
		// �����б�_Gֵ
		int[][] openlistG = new int[width][height];
		// �����б�_Hֵ
		int[][] openlistH = new int[width][height];
		// ����б�_ֵ
		byte[][] closelistV = new byte[width][height];
		// Fֵ��ʷ
		int oldF = -1;
		// F��ǰֵ
		int currF;
		// ��ʱ����x,y
		Point tmpCoords = new Point(x1, y1);
		int tmp = 0;
		int currX = 0;
		int currY = 0;
		int tmpX = 0;
		int tmpY = 0;
		int[] xdirs = new int[] { -1, 1, -1, 1, 0, -1, 1, 0 };
		int[] ydirs = new int[] { -1, -1, 1, 1, -1, 0, 0, 1 };

		openlist.add(tmpCoords);

		while (openlistG[x2][y2] == 0) {
			if (openlist.isEmpty()) {
				// ���ִ���
				System.err.println("���ִ��󣬿����б�Ϊ��");
				return null;
			}
			for (int i = 0; i < openlist.size(); i++) {
				Point a = openlist.get(i);
				if (i == 0) {
					oldF = openlistG[a.x][a.y] + openlistH[a.x][a.y];
					tmp = i;
					currX = a.x;
					currY = a.y;
				} else {
					currF = openlistG[a.x][a.y] + openlistH[a.x][a.y];
					if (oldF >= currF) {
						oldF = currF;
						tmp = i;
						currX = a.x;
						currY = a.y;
					}
				}
			}
			System.out.println("select: " + tmp);
			openlist.remove(tmp);
			openlistV[currX][currY] = 0;
			tmpCoords = new Point(currX, currY);
			closelist.add(tmpCoords);
			closelistV[currX][currY] = 1;

			for (int d = 0; d < 8; d++) {
				tmpX = currX + xdirs[d];
				tmpY = currY + ydirs[d];

				if (tmpX < 1 || tmpY < 1) {

				} else if (maskdata[tmpX + tmpY * width] == 0) {

				} else if (closelistV[tmpX][tmpY] == 1) {

				} else {
					if (openlistV[tmpX][tmpY] == 1) {
						tmp = openlistG[currX][currY] + d > 3 ? 10 : 14;
						if (openlistG[tmpX][tmpY] > tmp) {
							openlistG[tmpX][tmpY] = openlistG[currX][currY] + d > 3 ? 10 : 14;
							parentX[tmpX][tmpY] = currX;
							parentY[tmpX][tmpY] = currY;
						}
					} else {
						openlistV[tmpX][tmpY] = 1;
						tmpCoords = new Point(tmpX, tmpY);
						openlist.add(tmpCoords);

						parentX[tmpX][tmpY] = currX;
						parentY[tmpX][tmpY] = currY;
						openlistG[tmpX][tmpY] = openlistG[currX][currY] + d > 3 ? 10 : 14;
						openlistH[tmpX][tmpY] = (Math.abs(x2 - tmpX) + Math.abs(y2 - tmpY)) * 10;

						if (currX > 0) {

						}
					}
				}
			}
		}

		tmpCoords = new Point(x2, y2);
		path.add(tmpCoords);

		tmpCoords = new Point(parentX[x2][y2], parentY[x2][y2]);
		while (!(tmpCoords.x == 0 && tmpCoords.y == 0)) {
			path.add(tmpCoords);
			if (tmpCoords.x > 0 && tmpCoords.y > 0) {
				tmpX = parentX[tmpCoords.x][tmpCoords.y];
				tmpY = parentY[tmpCoords.x][tmpCoords.y];
				tmpCoords = new Point(tmpX, tmpY);
			} else {
				System.out.println("���ɵ���㣺" + tmpCoords.x + "," + tmpCoords.y);
			}
		}

		Collections.reverse(path);
		return path;
	}

}