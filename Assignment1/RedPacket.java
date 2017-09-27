package com.test;

import java.util.ArrayList;
import java.util.List;

import java.text.DecimalFormat;
import java.util.Scanner;

public class RedPacket {
	private static final float MIN_PACKET = 0.01f;
	private static final float MAX_PACKET = 200f;
	// ������Ķ���趨��0.01Ԫ��(ʣ��ƽ��ֵ*coef)��Χ�ڣ�����������
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

	// ����һ��mins~maxs��Χ�ĺ��
	private float randomRedPacket(float money, float mins, float maxs, int count) {
		if (count == 1)
			return (float) (Math.round(money * 100)) / 100;
		if (mins == maxs)
			return mins;

		float max = maxs > money ? money : maxs;
		float rand_packet = ((float) Math.random() * (max - mins) + mins);
		rand_packet = (float) (Math.round(rand_packet * 100)) / 100;
		// ������������Ҫ�ж�ʣ�����Ƿ�Ϸ�
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

	// �ж�һ������Ƿ�Ϸ�
	private boolean isValid(float money, int count) {
		double avg = money / count;
		return (avg >= MIN_PACKET && avg <= MAX_PACKET);
	}

	public static void main(String[] args) {
		float money, cutoff;
		int people;

		Scanner input = new Scanner(System.in);
		DecimalFormat tn = new DecimalFormat("0.00"); // ��λС��
		while (true) {
			System.out.print("�������ܽ�");
			money = input.nextFloat();
			cutoff = Float.valueOf(tn.format(money)); // ��ȷ����λС����ֵ
			if (money == cutoff && money > 0 && money <= 200) // ��ȷ���֣�����0�Ҳ�����200Ԫ
				break;
			else
				System.out.print("���벻�Ϸ���\n");
		}

		while (true) {
			System.out.print("��������������");
			people = input.nextInt(); // ��������
			if (money * 100 - people > 0.0 && people > 0) // �����Ϸ���ÿ�������ٿɵ�1��
				break;
			else
				System.out.print("���벻�Ϸ���\n");
		}
		input.close();

		RedPacket util = new RedPacket();
		System.out.println(util.splitRedPackets(money, people));
	}
}