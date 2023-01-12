package org.vaadin.addons.parttio.lightchart;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class AddonView extends Div {

    public AddonView() {
        LightChart theAddon = new LightChart();
        theAddon.setId("theAddon");
        add(theAddon);
    }
}
