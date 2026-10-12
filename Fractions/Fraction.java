package Fractions;

public class Fraction{

		int n;
		int d;

		public Fraction(int num, int den){

			this.n = num;
			this.d = den;			
		}

		public int getNum(){return this.n;}
		public int getDen(){return this.d;}

		public Fraction add(Fraction f2){
			
			int newNum = this.n * f2.getDen() + f2.getNum() * this.d;
			int newDen = this.d * f2.getDen();

			return ( new Fraction(newNum, newDen) );
		}

		public void reduce(){
			int num = getNum();
			int den = getDen();

			int m = ComputeMCD(num, den);

			this.n = num / m;
			this.d = den / m;
		}

		private static int ComputeMCD(int a, int b){

			int x;
			int y;
			int m;

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

			//first, convert into string

			String xString = Double.toString(x);

			//then split the decimals

			String[] periodSplit = xString.split("\\.");

			//first, we process the integral part
			String intString = periodSplit[0];
			int integralPart = Integer.parseInt(intString);

			//then the tricky decimal one
			String decString = periodSplit[1];

			//remove the last char to avoid it messing up the period calculation
			decString = decString.substring(0, decString.length() - 1);

			char[] periodChars = FindStringPeriod(decString);
			int periodLength = periodChars.length;

			String suffNines = "";

			for (int k = 0; k < periodLength; k++){
				suffNines += "9";
			}

			int ninesDenom = Integer.parseInt(suffNines);
			int iterNum = 1;
			int periodNumerator = 0;		

			for (int k = periodLength - 1; k >= 0; k--){

				char valueChar = periodChars[k];
				int intValue = valueChar - '0';

				periodNumerator += iterNum * intValue;
				iterNum *= 10;				

			}

			Fraction periodFraction = new Fraction(periodNumerator, ninesDenom);
			//avoid overflowing errors
			periodFraction.reduce();
			
			Fraction integralFraction = new Fraction(integralPart, 1);

			Fraction retFraction = integralFraction.add(periodFraction);
			retFraction.reduce();

			return retFraction;

		}

		@Override 
		public String toString(){
			return Integer.toString(n) + " / " + Integer.toString(d);
		}

	}
