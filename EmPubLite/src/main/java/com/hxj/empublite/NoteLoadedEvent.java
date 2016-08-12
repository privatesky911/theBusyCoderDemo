package com.hxj.empublite;

/**
 * Created by huxj-win7 on 2016/8/12.
 */
public class NoteLoadedEvent {
    private int position;
    private String prose;

    public NoteLoadedEvent(int position, String prose) {
        this.prose = prose;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getProse() {
        return prose;
    }
}
