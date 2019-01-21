
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

public class Ice {
	
	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.println("Please enter atleast one valid argument\t + <flag>");
				return;
		}
		int flag = Integer.parseInt(args[0]);
		Map<Integer, Integer> iceRecord = new HashMap<Integer, Integer>() {
			{
				 put(1855, 118); put(1856, 151); put(1857, 121); put(1858, 96); put(1859, 110); put(1860, 117); put(1861, 132); put(1862, 104); put(1863, 125); put(1864, 118); put(1865, 125); put(1866, 123); put(1867, 110); put(1868, 127); put(1869, 131); put(1870, 99); put(1871, 126); put(1872, 144); put(1873, 136); put(1874, 126); put(1875, 91); put(1876, 130); put(1877, 62); put(1878, 112); put(1879, 99); put(1880, 161); put(1881, 78); put(1882, 124); put(1883, 119); put(1884, 124); put(1885, 128); put(1886, 131); put(1887, 113); put(1888, 88); put(1889, 75); put(1890, 111); put(1891, 97); put(1892, 112); put(1893, 101); put(1894, 101); put(1895, 91); put(1896, 110); put(1897, 100); put(1898, 130); put(1899, 111); put(1900, 107); put(1901, 105); put(1902, 89); put(1903, 126); put(1904, 108); put(1905, 97); put(1906, 94); put(1907, 83); put(1908, 106); put(1909, 98); put(1910, 101); put(1911, 108); put(1912, 99); put(1913, 88); put(1914, 115); put(1915, 102); put(1916, 116); put(1917, 115); put(1918, 82); put(1919, 110); put(1920, 81); put(1921, 96); put(1922, 125); put(1923, 104); put(1924, 105); put(1925, 124); put(1926, 103); put(1927, 106); put(1928, 96); put(1929, 107); put(1930, 98); put(1931, 65); put(1932, 115); put(1933, 91); put(1934, 94); put(1935, 101); put(1936, 121); put(1937, 105); put(1938, 97); put(1939, 105); put(1940, 96); put(1941, 82); put(1942, 116); put(1943, 114); put(1944, 92); put(1945, 98); put(1946, 101); put(1947, 104); put(1948, 96); put(1949, 109); put(1950, 122); put(1951, 114); put(1952, 81); put(1953, 85); put(1954, 92); put(1955, 114); put(1956, 111); put(1957, 95); put(1958, 126); put(1959, 105); put(1960, 108); put(1961, 117); put(1962, 112); put(1963, 113); put(1964, 120); put(1965, 65); put(1966, 98); put(1967, 91); put(1968, 108); put(1969, 113); put(1970, 110); put(1971, 105); put(1972, 97); put(1973, 105); put(1974, 107); put(1975, 88); put(1976, 115); put(1977, 123); put(1978, 118); put(1979, 99); put(1980, 93); put(1981, 96); put(1982, 54); put(1983, 111); put(1984, 85); put(1985, 107); put(1986, 89); put(1987, 87); put(1988, 97); put(1989, 93); put(1990, 88); put(1991, 99); put(1992, 108); put(1993, 94); put(1994, 74); put(1995, 119); put(1996, 102); put(1997, 47); put(1998, 82); put(1999, 53); put(2000, 115); put(2001, 21); put(2002, 89); put(2003, 80); put(2004, 101); put(2005, 95); put(2006, 66); put(2007, 106); put(2008, 97); put(2009, 87); put(2010, 109); put(2011, 57); put(2012, 87); put(2013, 117); put(2014, 91); put(2015, 62); put(2016, 65); put(2017, 94);
			}
		};
		int noOfYears = iceRecord.size();
		if (flag == 100) {
			for (Entry<Integer, Integer> iceInfo : iceRecord.entrySet()) {
				System.out.println(iceInfo.getKey() + " " + iceInfo.getValue());
			}
		} else if (flag == 200) {
			int coldDays = iceRecord.values().stream().reduce(0, Integer::sum);
			double mean = (double)coldDays/noOfYears;
			double sumOfMeanSquares = iceRecord.values().stream().map(v -> Math.pow(v-mean, 2)).collect(Collectors.reducing(0.0, Double::sum));
			double sd = Math.sqrt((1.0/(noOfYears-1)) * sumOfMeanSquares) ;
			System.out.println( iceRecord.size() + "\n"+ String.format("%.2f", mean) +  "\n" + String.format("%.2f", sd));
		} else if (flag == 300) {
			if(args.length != 3) {
				System.out.println("Please enter 3 valid arguments:\n" +
					"<flag> <Beta_0> <Beta_1>");
				return;
			}
			double beta0 = Double.parseDouble(args[1]);
			double beta1 = Double.parseDouble(args[2]);
			double normMeanSquareError = (1.0/noOfYears) * iceRecord.entrySet().stream().map(e -> Math.pow((beta0 + beta1 * e.getKey() - e.getValue()), 2)).collect(Collectors.reducing(0.0, Double::sum));
			System.out.println(String.format("%.2f", normMeanSquareError));	
		} else if (flag == 400) {
			if(args.length != 3) {
				System.out.println("Please enter 3 valid arguments:\n" +
					"<flag> <Beta_0> <Beta_1>");
				return;
			}
			double normmeanSquareErrorForBeta0 = getGradientDescentForBeta0(iceRecord, Math.round(Double.parseDouble(args[1])*100.0)/100.0, Math.round(Double.parseDouble(args[2])*100.0)/100.0);
			double normmeanSquareErrorForBeta1 = getGradientDescentForBeta1(iceRecord, Math.round(Double.parseDouble(args[1])*100.0)/100.0, Math.round(Double.parseDouble(args[2])*100.0)/100.0);
			System.out.println(String.format("%.2f", normmeanSquareErrorForBeta0) + "\n" + String.format("%.2f", normmeanSquareErrorForBeta1));	
		
		} else if (flag == 500) {
			if(args.length != 3) {
				System.out.println("Please enter 3 valid arguments:\n" +
					"<flag> <Beta_0> <Beta_1>");
				return;
			}
			double eta = Double.parseDouble(args[1]);
			int times = Integer.parseInt(args[2]);
			double preNormMeanSquareErrorForBeta0 = 0.0;
			double preNormMeanSquareErrorForBeta1 = 0.0;
			for (int i=0; i< times; i++) {
				double normMeanSquareErrorForBeta0 = preNormMeanSquareErrorForBeta0 - eta * getGradientDescentForBeta0(iceRecord, preNormMeanSquareErrorForBeta0, preNormMeanSquareErrorForBeta1 );
				double normMeanSquareErrorForBeta1 = preNormMeanSquareErrorForBeta1 - eta * getGradientDescentForBeta1(iceRecord, preNormMeanSquareErrorForBeta0, preNormMeanSquareErrorForBeta1 );
				double normMeanSquareError = getMeanSquareError(iceRecord, normMeanSquareErrorForBeta0, normMeanSquareErrorForBeta1);
				System.out.println(i+1 + " " + String.format("%.2f", normMeanSquareErrorForBeta0) + " " + String.format("%.2f", normMeanSquareErrorForBeta1) + " " + String.format("%.2f", normMeanSquareError));
				preNormMeanSquareErrorForBeta0 = normMeanSquareErrorForBeta0;
				preNormMeanSquareErrorForBeta1 = normMeanSquareErrorForBeta1;
			}
		} else if (flag == 600) {
			double xMean = (1.0/noOfYears) * iceRecord.keySet().stream().reduce(0, Integer::sum);
			double yMean = (1.0/noOfYears) * iceRecord.values().stream().reduce(0, Integer::sum);
			double sumOfXYMeanError = iceRecord.entrySet().stream().map(e -> (e.getKey()-xMean)*(e.getValue()-yMean)).collect(Collectors.reducing(0.0, Double::sum));
			double XMeanSquareError = iceRecord.keySet().stream().map(k -> Math.pow(k-xMean, 2)).collect(Collectors.reducing(0.0, Double::sum));
			double beta1 = sumOfXYMeanError / XMeanSquareError;
			double beta0 = yMean - beta1*xMean;
			double normMeanSquareError = getMeanSquareError(iceRecord, beta0, beta1);
			System.out.println(String.format("%.2f", beta0) + " " + String.format("%.2f", beta1) + " " + String.format("%.2f", normMeanSquareError));
		} else if (flag == 700) {
			if(args.length != 2) {
				System.out.println("Please enter 3 valid arguments:\n" +
					"<flag> <Year to Predict>");
				return;
			}
			int year = Integer.parseInt(args[1]);
			double xMean = (1.0/noOfYears) * iceRecord.keySet().stream().reduce(0, Integer::sum);
			double yMean = (1.0/noOfYears) * iceRecord.values().stream().reduce(0, Integer::sum);
			double sumOfXYMeanError = iceRecord.entrySet().stream().map(e -> (e.getKey()-xMean)*(e.getValue()-yMean)).collect(Collectors.reducing(0.0, Double::sum));
			double XMeanSquareError = iceRecord.keySet().stream().map(k -> Math.pow(k-xMean, 2)).collect(Collectors.reducing(0.0, Double::sum));
			double beta1 = sumOfXYMeanError / XMeanSquareError;
			double beta0 = yMean - beta1*xMean;
			double coldDaysPredicted = beta0 + beta1 * year;
			System.out.println(String.format("%.2f", coldDaysPredicted));		
		} else if (flag == 800) {
			if(args.length != 3) {
				System.out.println("Please enter 3 valid arguments:\n" +
					"<flag> <eta : A small number> <Time>");
				return;
			}
			double eta = Double.parseDouble(args[1]);
			int times = Integer.parseInt(args[2]);
			double xMean = (1.0/noOfYears) * iceRecord.keySet().stream().reduce(0, Integer::sum);
			double sumOfMeanSquares = iceRecord.keySet().stream().map(k -> Math.pow(k-xMean, 2)).collect(Collectors.reducing(0.0, Double::sum));
			double sd = Math.sqrt((1.0/(noOfYears-1)) * sumOfMeanSquares) ;
			
			Map<Double, Integer> iceRecordsNorm = new HashMap<Double, Integer>();
			for (Entry<Integer, Integer> e : iceRecord.entrySet()) {
				double key = ((double) e.getKey() - xMean) / sd;
				iceRecordsNorm.put(key, e.getValue());
			}
			double preNormMeanSquareErrorForBeta0 = 0.0;
			double preNormMeanSquareErrorForBeta1 = 0.0;
			for (int i=0; i< times; i++) {
				double normMeanSquareErrorForBeta0 = preNormMeanSquareErrorForBeta0 - eta * getGradientDescentOnNormXForBeta0(iceRecordsNorm, preNormMeanSquareErrorForBeta0, preNormMeanSquareErrorForBeta1 );
				double normMeanSquareErrorForBeta1 = preNormMeanSquareErrorForBeta1 - eta * getGradientDescentOnNormXForBeta1(iceRecordsNorm, preNormMeanSquareErrorForBeta0, preNormMeanSquareErrorForBeta1 );
				double normMeanSquareError = getMeanSquareErrorNorm(iceRecordsNorm, normMeanSquareErrorForBeta0, normMeanSquareErrorForBeta1);
				System.out.println(i+1 + " " + String.format("%.2f", normMeanSquareErrorForBeta0) + " " + String.format("%.2f", normMeanSquareErrorForBeta1) + " " + String.format("%.2f", normMeanSquareError));
				preNormMeanSquareErrorForBeta0 = normMeanSquareErrorForBeta0;
				preNormMeanSquareErrorForBeta1 = normMeanSquareErrorForBeta1;
			}
		} else if (flag == 900) {
			if(args.length != 3) {
				System.out.println("Please enter 3 valid arguments:\n" +
					"<flag> <eta : A small number>  <times>");
				return;
			}
			double eta = Double.parseDouble(args[1]);
			int times = Integer.parseInt(args[2]);
			double xMean = (1.0/noOfYears) * iceRecord.keySet().stream().reduce(0, Integer::sum);
			double sumOfMeanSquares = iceRecord.keySet().stream().map(k -> Math.pow(k-xMean, 2)).collect(Collectors.reducing(0.0, Double::sum));
			double sd = Math.sqrt((1.0/(noOfYears-1)) * sumOfMeanSquares) ;
			
			Map<Double, Integer> iceRecordsNorm = new HashMap<Double, Integer>();
			for (Entry<Integer, Integer> e : iceRecord.entrySet()) {
				double key = ((double) e.getKey() - xMean) / sd;
				iceRecordsNorm.put(key, e.getValue());
			}
			double preNormMeanSquareErrorForBeta0 = 0.0;
			double preNormMeanSquareErrorForBeta1 = 0.0;
			for (int i=0; i< times; i++) {
				Object[] years = iceRecordsNorm.keySet().toArray();
				double key = (double)(years[new Random().nextInt(years.length)]);
				double value = iceRecordsNorm.get(key);
				double normMeanSquareErrorForBeta0 = preNormMeanSquareErrorForBeta0 - (eta * (2 * (preNormMeanSquareErrorForBeta0 + (preNormMeanSquareErrorForBeta1 * key) - value)));
				double normMeanSquareErrorForBeta1 = preNormMeanSquareErrorForBeta1 - (eta * (2 * (preNormMeanSquareErrorForBeta0 + (preNormMeanSquareErrorForBeta1 * key) - value) * key));
				double normMeanSquareError = getMeanSquareErrorNorm(iceRecordsNorm, normMeanSquareErrorForBeta0, normMeanSquareErrorForBeta1);
				System.out.println(i+1 + " " + String.format("%.2f", normMeanSquareErrorForBeta0) + " " + String.format("%.2f", normMeanSquareErrorForBeta1) + " " + String.format("%.2f", normMeanSquareError));
				preNormMeanSquareErrorForBeta0 = normMeanSquareErrorForBeta0;
				preNormMeanSquareErrorForBeta1 = normMeanSquareErrorForBeta1;
			}
		} else {
			System.out.println("Please enter valid flag value <100, 200, ..... 900>\n");
			return;
		}
	}
	
	public static double getMeanSquareError(Map<Integer, Integer> iceRecord, double beta0, double beta1) {
		double normMeanSquareError = (1.0/iceRecord.size()) * iceRecord.entrySet().stream().map(e -> Math.pow((beta0 + beta1 * e.getKey() - e.getValue()), 2)).collect(Collectors.reducing(0.0, Double::sum));
		return normMeanSquareError;
	}
	
	public static double getMeanSquareErrorNorm(Map<Double, Integer> iceRecord, double beta0, double beta1) {
		double normMeanSquareError = (1.0/iceRecord.size()) * iceRecord.entrySet().stream().map(e -> Math.pow((beta0 + beta1 * e.getKey() - e.getValue()), 2)).collect(Collectors.reducing(0.0, Double::sum));
		return normMeanSquareError;
	}
	
	public static double getGradientDescentForBeta0(Map<Integer, Integer> iceRecord, double beta0, double beta1) {
		double normmeanSquareErrorForBeta0 = (2.0/iceRecord.size()) * iceRecord.entrySet().stream().map(e -> (beta0 + beta1 * e.getKey() - e.getValue())).collect(Collectors.reducing(0.0, Double::sum));
		return normmeanSquareErrorForBeta0;
	}
	
	public static double getGradientDescentForBeta1(Map<Integer, Integer> iceRecord, double beta0, double beta1) {
		double normmeanSquareErrorForBeta1 = (2.0/iceRecord.size()) * iceRecord.entrySet().stream().map(e -> (beta0 + beta1 * e.getKey() - e.getValue()) * e.getKey()).collect(Collectors.reducing(0.0, Double::sum));
		return normmeanSquareErrorForBeta1;
	}
	
	public static double getGradientDescentOnNormXForBeta0(Map<Double, Integer> iceRecord, double beta0, double beta1) {
		double normmeanSquareErrorForBeta0 = (2.0/iceRecord.size()) * iceRecord.entrySet().stream().map(e -> (beta0 + beta1 * e.getKey() - e.getValue())).collect(Collectors.reducing(0.0, Double::sum));
		return normmeanSquareErrorForBeta0;
	}
	
	public static double getGradientDescentOnNormXForBeta1(Map<Double, Integer> iceRecord, double beta0, double beta1) {
		double normmeanSquareErrorForBeta1 = (2.0/iceRecord.size()) * iceRecord.entrySet().stream().map(e -> (beta0 + beta1 * e.getKey() - e.getValue()) * e.getKey()).collect(Collectors.reducing(0.0, Double::sum));
		return normmeanSquareErrorForBeta1;
	}
		
}

