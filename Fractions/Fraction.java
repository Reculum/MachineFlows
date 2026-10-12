package Fractions;

public class Fraction{

		int n;
		int d;

		public Fraction(int num, int den){

			int m = ComputeMCD(num, den);

			this.n = num / m;
			this.d = den / m;
		}

		public int getNum(){return this.n;}
		public int getDen(){return this.d;}

		public Fraction add(Fraction f2){
			
			int newNum = this.n * f2.getDen() + f2.getNum() * this.d;
			int newDen = this.d * f2.getDen();

			return ( new Fraction(newNum, newDen) );
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

		return predictedPeriod;

	}
		
	
	}
