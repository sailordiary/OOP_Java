package com.test;

import java.util.ArrayList;
import java.util.List;

import java.text.DecimalFormat;
import java.util.Scanner;

public class RedPacket {
	private static final float MIN_PACKET = 0.01f;
	private static final float MAX_PACKET = 200f;
	// 抢红包的额度设定在0.01元到(剩余平均值*coef)范围内，避免红包过大
	private static final double coef = 2.1;

	public List<Float> splitRedPackets(float money, int count) {
		if (!isValid(money, count))
			return null;

		List<Float> list = new ArrayList<Float>();
		float max = (float) (money * coef / count);

		max = max > MAX_PACKET ? MAX_PACKET : max;
		for (int i = 0; i < count; i++) {
			float rand_packet = randomRedPacket(money, MIN_PACKET, max, count - i);
			list.add(rand_packet);
			money -= rand_packet;
		}
		return list;
	}

	// 产生一个mins~maxs范围的红包
	private float randomRedPacket(float money, float mins, float maxs, int count) {
		if (count == 1)
			return (float) (Math.round(money * 100)) / 100;
		if (mins == maxs)
			return mins;

		float max = maxs > money ? money : maxs;
		float rand_packet = ((float) Math.random() * (max - mins) + mins);
		rand_packet = (float) (Math.round(rand_packet * 100)) / 100;
		// 产生随机红包后要判断剩余金额是否合法
		float remainingMoney = money - rand_packet;
		if (isValid(remainingMoney, count - 1))
			return rand_packet;
		else {
			float avg = remainingMoney / (count - 1);
			if (avg < MIN_PACKET)
				return randomRedPacket(money, mins, rand_packet, count);
			else if (avg > MAX_PACKET)
				return randomRedPacket(money, rand_packet, maxs, count);
		}
		return rand_packet;
	}

	// 判断一个金额是否合法
	private boolean isValid(float money, int count) {
		double avg = money / count;
		return (avg >= MIN_PACKET && avg <= MAX_PACKET);
	}

	public static void main(String[] args) {
		float money, cutoff;
		int people;

		Scanner input = new Scanner(System.in);
		DecimalFormat tn = new DecimalFormat("0.00"); // 两位小数
		while (true) {
			System.out.print("请输入总金额：");
			money = input.nextFloat();
			cutoff = Float.valueOf(tn.format(money)); // 精确到两位小数的值
			if (money == cutoff && money > 0 && money <= 200) // 精确到分，大于0且不超过200元
				break;
			else
				System.out.print("输入不合法！\n");
		}

		while (true) {
			System.out.print("请输入总人数：");
			people = input.nextInt(); // 输入人数
			if (money * 100 - people > 0.0 && people > 0) // 人数合法且每个人至少可得1分
				break;
			else
				System.out.print("输入不合法！\n");
		}
		input.close();

		RedPacket util = new RedPacket();
		System.out.println(util.splitRedPackets(money, people));
	}
}