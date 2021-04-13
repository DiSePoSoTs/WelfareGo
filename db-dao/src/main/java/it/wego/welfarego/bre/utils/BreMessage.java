package it.wego.welfarego.bre.utils;

/**
 *
 * @author aleph
 */
public class BreMessage {

    private String subject, message, level, code;
    private Object data;

    public BreMessage() {
    }

    public BreMessage(String message) {
        this("UNDEFINED", message);
    }

    public BreMessage(String level, String message) {
        this(null, level, message);
    }

    public BreMessage(String subject, String level, String message) {
        this(subject, level, message, null);
    }

    public BreMessage(String subject, String level, String message, Object data) {
        this.subject = subject;
        this.message = message;
        this.level = level;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.subject != null ? this.subject.hashCode() : 0);
        hash = 71 * hash + (this.message != null ? this.message.hashCode() : 0);
        hash = 71 * hash + (this.level != null ? this.level.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BreMessage other = (BreMessage) obj;
        if ((this.subject == null) ? (other.subject != null) : !this.subject.equals(other.subject)) {
            return false;
        }
        if ((this.message == null) ? (other.message != null) : !this.message.equals(other.message)) {
            return false;
        }
        if ((this.level == null) ? (other.level != null) : !this.level.equals(other.level)) {
            return false;
        }
        return true;
    }
}
