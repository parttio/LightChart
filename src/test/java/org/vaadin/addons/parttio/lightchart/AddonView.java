package org.vaadin.addons.parttio.lightchart;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.Series;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import org.apache.commons.io.FileUtils;

@Route("")
public class AddonView extends Div {

    Chart chart;

    public AddonView() {
        RadioButtonGroup<Class<?>> group = new RadioButtonGroup<>();
        group.setItemLabelGenerator(c -> c.getSimpleName());
        group.setItems(LightChart.class, Chart.class);
        group.addValueChangeListener(e -> {
            Chart c;
            if (e.getValue() == LightChart.class) {
                c = new LightChart(ChartType.LINE);
            } else {
                c = new Chart();
            }
            c.getConfiguration().addSeries(createLargeSeries(c));
            c.getConfiguration().addSeries(createLargeSeries(c));
            c.getConfiguration().addSeries(createLargeSeries(c));
            c.getConfiguration().addSeries(createLargeSeries(c));
            add(c);

            if (chart != null) {
                remove(chart);
            }

            chart = c;
        });

        add(group);

        Button button = new Button("Calculate UI size");
        button.addClickListener(e -> {
            String msg = serializeAndCalculateUISize();
            Notification.show(msg).setDuration(10000);
        });
        add(button);
        add(new Paragraph("Serialized UI size is not exactly what it consumes in JVM memory, because it is complicated. In reality it is more, ~ 2X, depending on data. But it is easy to calculated and it is probably well aligned relativile (when comparing LightChart & Chart)"));

        group.setValue(LightChart.class);

    }

    private String serializeAndCalculateUISize() {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bout);
            objectOutputStream.writeObject(UI.getCurrent());
            objectOutputStream.flush();
            String msg = "Serialized UI size with " + chart.getClass().getSimpleName() + ":" + FileUtils.byteCountToDisplaySize(bout.size());
            return msg;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Series createLargeSeries(Chart c) {
        Random r = new Random(0);
        DataSeries dataSeries = new DataSeries();
        dataSeries.setName("Series " + c.getConfiguration().getSeries().size());
        for (int i = 0; i < 100000; i++) {
            dataSeries.add(new DataSeriesItem(i, r.nextInt(1000)));
        }
        return dataSeries;
    }
}
