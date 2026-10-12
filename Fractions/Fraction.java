package Fractions;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Fraction{

		long n;
		long d;

		public Fraction(long num, long den){

			this.n = num;
			this.d = den;			
		}

		public long getNum(){return this.n;}
		public long getDen(){return this.d;}

		public Fraction add(Fraction f2){
			
			long newNum = this.n * f2.getDen() + f2.getNum() * this.d;
			long newDen = this.d * f2.getDen();

			return ( new Fraction(newNum, newDen) );
		}

		public void reduce(){
			long num = getNum();
			long den = getDen();

			long m = ComputeMCD(num, den);

			this.n = num / m;
			this.d = den / m;
		}

		private static long ComputeMCD(long a, long b){

			long x;
			long y;
			long m;

			if (b > a){x = a; y = b;}
			else{x = b; y = a;}

			m = y % x;

			if (y % x == 0){return  x;}
			else{
				return ComputeMCD(x, m);
			}

		}

		private static char[] FindStringPeriod(String s){

			int stringLength = s.length();
			char[] stringChars = new char[stringLength];

			s.getChars(0, stringLength, stringChars, 0);

			char[] predictedPeriod = new char[stringLength];
			char[] cachedPeriod = new char[stringLength];
			
			predictedPeriod[0] = stringChars[0];

			int predictedIdx = 0;
			int cachedIdx = 0;

			int predictedLength = 1;
			int cachedLength = 0;

			for (int k = 1; k < stringLength; k++){

				cachedPeriod[cachedIdx] = stringChars[k];
				cachedLength += 1;

				if (stringChars[k] == predictedPeriod[predictedIdx]){

					predictedIdx = (predictedIdx + 1) % predictedLength;				
					cachedIdx += 1;
				}
				else{
					
					for (int j = 0; j < cachedLength; j++){
						predictedPeriod[ predictedLength + j] = cachedPeriod[j];
					}

					predictedLength += cachedLength;
					cachedLength = 0;

					predictedIdx = 0;
					cachedIdx = 0;
				}

			}

			char[] adaptedPredictedPeriod = new char[predictedLength];

			for (int j = 0; j < predictedLength; j++){
				adaptedPredictedPeriod[j] = predictedPeriod[j];
			}


			return adaptedPredictedPeriod;

		}
		

		public static Fraction doubleToFraction(double x){

			//once again, we need to introdce another cutoff for precision. In this case, we assume that anything beyond
			//the precision digit is just floting point garbage

			BigDecimal xBD = BigDecimal.valueOf(x);
			xBD = xBD.setScale(15, RoundingMode.DOWN);

			x = xBD.doubleValue();

			//first, convert into string
			String xString = Double.toString(x);

			

			//then split the decimals
			String[] periodSplit = xString.split("\\.");

			//first, we process the integral part
			String intString = periodSplit[0];
			long integralPart = Long.parseLong(intString);
			
			Fraction integralFraction = new Fraction(integralPart, 1);

			//then the tricky decimal one
			String decString = periodSplit[1];

			//this is the cutoff point for our periodical digits hypothesis

			if (decString.length() <= 12 && !xString.contains("E")){

				long retNum = Long.parseLong(decString);

				String retDenStr = "1";

				for (int k = 0; k < decString.length(); k++){
					retDenStr += "0";
				}

				long retDen = Long.parseLong(retDenStr);
				
				Fraction returnFraction = new Fraction(retNum, retDen);
				returnFraction.reduce();
				returnFraction = returnFraction.add(integralFraction);
				returnFraction.reduce();

				return returnFraction;
			}

			//if we didn't return yet, we can now compute the approximating fraction (given periodic decimals)

			char[] periodChars = FindStringPeriod(decString);
			int periodLength = periodChars.length;

			String suffNines = "";

			for (int k = 0; k < periodLength; k++){
				suffNines += "9";
			}

			long ninesDenom = Long.parseLong(suffNines);
			long iterNum = 1;
			long periodNumerator = 0;		

			for (int k = periodLength - 1; k >= 0; k--){

				char valueChar = periodChars[k];
				int intValue = valueChar - '0';

				periodNumerator += iterNum * intValue;
				iterNum *= 10;				

			}

			Fraction periodFraction = new Fraction(periodNumerator, ninesDenom);
			//avoid overflowing errors
			periodFraction.reduce();
			Fraction retFraction = integralFraction.add(periodFraction);
			retFraction.reduce();

			return retFraction;

		}

		@Override 
		public String toString(){
			return Long.toString(n) + " / " + Long.toString(d);
		}

	}
