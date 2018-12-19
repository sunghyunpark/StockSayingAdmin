package model;

import java.io.Serializable;

public class SayingModel implements Serializable {
    private int no;
    private String contents;
    private String authorName;
    private int textSize;
    private int gravityHorizontal;
    private int gravityVertical;
    private String createdAt;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getGravityHorizontal() {
        return gravityHorizontal;
    }

    public void setGravityHorizontal(int gravityHorizontal) {
        this.gravityHorizontal = gravityHorizontal;
    }

    public int getGravityVertical() {
        return gravityVertical;
    }

    public void setGravityVertical(int gravityVertical) {
        this.gravityVertical = gravityVertical;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
