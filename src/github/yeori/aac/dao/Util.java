package github.yeori.aac.dao;

public class Util {

	public static boolean stringsOf(Object[] data, String value) {
		for (int i = 0; i < data.length; i++) {
			if (data[i].getClass() != String.class) {
				return false;
			}
			String s = (String) data[i];
			if(!s.equals(value) ) {
				return false;
			}
		}
		return true;
	}

	public static int toInt(Object o) {
		if ( o instanceof Double) {
			return ((Double)o).intValue();
		} else if ( o instanceof String) {
			return Integer.parseInt(((String)o));
		} else if ( o instanceof Integer) {
			return (Integer)o;
		}
		throw new RuntimeException("not a String nor Double: type is " + o.getClass().getName());
	}
	/**
	 * o를 String으로 반환함(String 타입이어야함)
	 * @param o
	 * @return
	 */
	public static String strictStr(Object o) {
		return toStr(o, true);
	}
	public static String toStr(Object o, boolean strict) {
		if ( o instanceof String) {
			return (String) o;
		} else {
			if ( strict ) {
				throw new RuntimeException("not a type of String : real type is " + o.getClass().getName());
			} else {
				return o.toString();
			}
		}
	}
}
