package org.useless.gui.template.container;

import jdk.jfr.Experimental;
import org.useless.gui.data.Location;
import org.useless.gui.data.Size;

@Experimental
public class Root extends Window {

    public Root() {
        this(null);
    }

    public Root(String title) {
        retitle(title);
    }

    public Root retitle(String title) {
        super.setTitle(title);
        return this;
    }

    public Root relocation(Location location) {
        super.setLocation(location);
        return this;
    }

    public Root relocation(int x,int y) {
        return relocation(new Location(x, y));
    }

    public Root recenter() {
        return relocation(null);
    }

    public Root resize(Size size) {
        super.setSize(size);
        return this;
    }

    public Root resize(int width,int height) {
        return resize(new Size(width, height));
    }

    public void show() {
        super.setVisible(true);
        UTS.submit(this::initWindow);
    }

}
