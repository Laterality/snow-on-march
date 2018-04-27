package latera.kr.snowonmarch.util;

public interface OnSynchronizationFinishedListener {
	public static final int SYNCH_CONTACT = 1;
	public static final int SYNCH_SMS = 2;

	public void onFinish(int syncID);
}
