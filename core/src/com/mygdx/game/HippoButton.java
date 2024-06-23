package com.mygdx.game;

import static com.mygdx.game.MyHippoGame.*;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class HippoButton {
    String text;
    BitmapFont font;
    float x, y;
    float width, height;

    public HippoButton(String text, BitmapFont font, float x, float y ){
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
        GlyphLayout layout = new GlyphLayout(font, text);
        width = layout.width;
        height = layout.height;
    }

    public HippoButton(String text, BitmapFont font, float y) {
        this.text = text;
        this.font = font;
        GlyphLayout layout = new GlyphLayout(font, text);
        width = layout.width;
        height = layout.height;
        x = SCR_WIDTH/2 - width/2;
        this.y = y;
    }

    boolean hit(float tx, float ty){
        return x < tx & tx < x+width & y-height < ty & ty < y;
    }

    public void setText(String text) {
        this.text = text;
        GlyphLayout layout = new GlyphLayout(font, text);
        width = layout.width;
    }
}
