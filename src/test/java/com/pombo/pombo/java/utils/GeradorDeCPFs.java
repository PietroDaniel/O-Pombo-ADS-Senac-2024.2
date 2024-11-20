package com.pombo.pombo.java.utils;

public class GeradorDeCPFs {

    public static String generateValidCPF() {
        int[] cpf = new int[9];
        for (int i = 0; i < 9; i++) {
            cpf[i] = (int) (Math.random() * 10);
        }

        int d1 = calculateDigit(cpf, 10);

        int[] cpfWithD1 = new int[10];
        System.arraycopy(cpf, 0, cpfWithD1, 0, 9);
        cpfWithD1[9] = d1;

        int d2 = calculateDigit(cpfWithD1, 11);

        return String.format("%d%d%d%d%d%d%d%d%d%d%d",
                cpf[0], cpf[1], cpf[2],
                cpf[3], cpf[4], cpf[5],
                cpf[6], cpf[7], cpf[8],
                d1, d2);
    }

    private static int calculateDigit(int[] cpf, int factor) {
        int sum = 0;
        for (int i = 0; i < cpf.length; i++) {
            sum += cpf[i] * factor--;
        }
        int result = 11 - (sum % 11);
        return (result >= 10) ? 0 : result;
    }

}
