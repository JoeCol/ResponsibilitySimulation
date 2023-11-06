import Responsibility.Responsibility;

public class Message {
    String sender;
    String receiver;
    String content;
    Responsibility attachedRes;
    
    public Message(String sender, String receiver, String content, Responsibility attachedRes) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.attachedRes = attachedRes;
    }

    public String getReceiver() 
    {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public Responsibility getAttachedRes() {
        return attachedRes;
    }
}
