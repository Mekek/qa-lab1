package software_testing.function;

public class Arccos {

    public static final int TERMS = 500;

    public static double arccos(double x) {

        if (Double.isNaN(x)) return Double.NaN;
        if (x < -1.0 || x > 1.0) return Double.NaN;

        if (x == 1.0) return 0.0;
        if (x == -1.0) return Math.PI;

        // если близко к -1 — используем симметрию
        if (x < -0.95) {
            return Math.PI - arccos(-x);
        }

        // если близко к +1 — используем устойчивую формулу
        if (x > 0.95) {
            return 2.0 * arcsinSeries(Math.sqrt((1 - x) / 2.0));
        }

        return Math.PI / 2.0 - arcsinSeries(x);
    }

    private static double arcsinSeries(double x) {
        double sum = 0.0;

        for (int n = 0; n < TERMS; n++) {
            double coef = binomial(2 * n, n) / Math.pow(4.0, n);
            double term = coef * Math.pow(x, 2 * n + 1) / (2 * n + 1);
            sum += term;

            if (Math.abs(term) < 1e-15) break;
        }

        return sum;
    }

    private static double binomial(int n, int k) {
        k = Math.min(k, n - k);
        double result = 1.0;

        for (int i = 1; i <= k; i++) {
            result *= (double)(n - k + i) / i;
        }

        return result;
    }
}