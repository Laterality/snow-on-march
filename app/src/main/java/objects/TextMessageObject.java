package objects;

/**
 * Created by Jinwoo Shin on 2018-04-20.
 */

public class TextMessageObject {

    public final String content;
    public final String senderName;
    public final String senderNumber;

    public TextMessageObject(String content, String name, String number) {
        this.content = content;
        this.senderName = name;
        this.senderNumber = number;
    }
}
